package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.core.Globals;
import lermitage.intellij.nightandday.core.UIUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;

class StatusPresentation implements StatusBarWidget.MultipleTextValuesPresentation, StatusBarWidget.Multiframe {

    public StatusPresentation(StatusBar statusBar, Project project, Disposable widget) {
        this.statusBar = statusBar;
        this.project = project;
        this.widget = widget;
    }

    private final StatusBar statusBar;
    private final Project project;
    private final Disposable widget;
    private SettingsService settingsService;
    private String statusText = getSelectedValue();

    @Override
    public String getTooltipText() {
        return statusText;
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        // FIXME getClickConsumer() is never called since migration to MultipleTextValuesPresentation + Multiframe
        return mouseEvent -> statusBar.updateWidget(Globals.PLUGIN_ID);
    }

    @Override
    public @Nullable("null means the widget is unable to show the popup") ListPopup getPopupStep() {
        return null;
    }

    @Override
    public @Nullable String getSelectedValue() {
        if (settingsService == null) {
            settingsService = ServiceManager.getService(SettingsService.class);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().withHour(21).withMinute(0).withSecond(0);
        if (now.isAfter(end)) { // reached end of period
            end = end.plusDays(1);
        }
        Duration diff = Duration.between(now, end);
        statusText = DurationFormatUtils
            .formatDurationWords(diff.abs().toMillis(), true, true)
            .replaceAll("[0-9]{1,2} second(s)?", "")
            .replaceAll(" hours", "hrs")
            .replaceAll(" hour", "hr")
            .replaceAll(" minute(s)?", "min")
            .trim();
        //statusText = DurationFormatUtils.formatDuration(diff.toMillis(), "H:mm") + " to 21h";

        return statusText + " ⨠ 21h";
    }

    @Override
    public @Nullable Icon getIcon() {
        if (settingsService == null) {
            settingsService = ServiceManager.getService(SettingsService.class);
        }
        return UIUtils.getStatusIcon(null);
    }

    @Override
    public StatusBarWidget copy() {
        return new StatusWidget(project);
    }

    @Override
    public @NotNull String ID() {
        return Globals.PLUGIN_ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public void dispose() {
        Disposer.dispose(widget);
    }
}
