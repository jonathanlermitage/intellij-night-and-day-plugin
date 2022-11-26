// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.IJUtils;
import lermitage.intellij.nightandday.core.TimeLeft;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;

class TextStatusPresentation implements StatusBarWidget.MultipleTextValuesPresentation {

    private final Logger LOG = Logger.getInstance(getClass().getName());
    private SettingsService settingsService;
    private String statusTooltip = "";

    @Override
    public String getTooltipText() {
        return "<html>" + statusTooltip + "</html>";
    }

    // removed @Override as MultipleTextValuesPresentation.getClickConsumer is scheduled for removal in a future release
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return null;
    }

    // removed @Override as MultipleTextValuesPresentation.getPopupStep is scheduled for removal in a future release
    public @Nullable("null means the widget is unable to show the popup") ListPopup getPopupStep() {
        return null;
    }

    @Override
    public @Nullable String getSelectedValue() {
        if (settingsService == null) {
            settingsService = IJUtils.getSettingsService();
        }
        if (settingsService.getStatusUIType() != StatusUIType.TEXT) {
            return null;
        }
        LocalDateTime timerStart = LocalDateTime.now();
        TimeLeft timeLeft = DateUtils.computeStatusWidgetText();
        String statusText = timeLeft.getLabel();
        statusTooltip = timeLeft.getTooltip();
        long executionDuration = Duration.between(timerStart, LocalDateTime.now()).toMillis();
        if (executionDuration > 30) {
            LOG.warn("Status updated in " + executionDuration + " ms, it should be faster once IDE or project is fully loaded");
        }
        return statusText;
    }

    @Override
    public @Nullable Icon getIcon() {
        return null;//UIUtils.getStatusIcon(null);
    }
}
