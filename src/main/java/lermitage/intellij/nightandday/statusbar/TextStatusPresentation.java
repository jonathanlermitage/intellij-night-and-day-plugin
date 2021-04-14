// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusDurationEndType;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.Globals;
import lermitage.intellij.nightandday.core.TimeLeft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalTime;

class TextStatusPresentation implements StatusBarWidget.MultipleTextValuesPresentation, StatusBarWidget.Multiframe {

    public TextStatusPresentation(StatusBar statusBar, Project project, Disposable widget) {
        this.statusBar = statusBar;
        this.project = project;
        this.widget = widget;
    }

    private final Logger LOG = Logger.getInstance(getClass().getName());
    private final StatusBar statusBar;
    private final Project project;
    private final Disposable widget;
    private SettingsService settingsService;
    private String statusTooltip = "";

    @Override
    public String getTooltipText() {
        return "<html>" + statusTooltip + "</html>";
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        // FIXME getClickConsumer() is never called since migration to MultipleTextValuesPresentation + Multiframe
        return mouseEvent -> statusBar.updateWidget(Globals.TEXT_STATUS_WIDGET_ID);
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
        if (settingsService.getStatusUIType() != StatusUIType.TEXT) {
            return "";
        }
        LocalTime timerStart = LocalTime.now();
        TimeLeft timeLeft = DateUtils.computeStatusWidgetText();
        String statusText = timeLeft.getLabel();
        statusTooltip = timeLeft.getTooltip();
        long executionDuration = Duration.between(timerStart, LocalTime.now()).toMillis();
        if (executionDuration > 30) {
            LOG.warn("Status updated in " + executionDuration + " ms, it should be faster once IDE or project is fully loaded");
        }
        return statusText;
    }

    @Override
    public @Nullable Icon getIcon() {
        return null;//UIUtils.getStatusIcon(null);
    }

    @Override
    public StatusBarWidget copy() {
        return new TextStatusWidget(project);
    }

    @Override
    public @NotNull String ID() {
        return Globals.TEXT_STATUS_WIDGET_ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public void dispose() {
        Disposer.dispose(widget);
    }
}
