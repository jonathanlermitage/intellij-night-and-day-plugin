// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.JBColor;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.ui.JBUI;
import lermitage.intellij.nightandday.cfg.Defaults;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.Globals;
import lermitage.intellij.nightandday.core.IJUtils;
import lermitage.intellij.nightandday.core.TimeLeft;
import lermitage.intellij.nightandday.core.UIUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static lermitage.intellij.nightandday.cfg.StatusUIType.PROGRESS_BAR;

@SuppressWarnings("WeakerAccess")
public class ProgressbarStatusWidget extends JButton implements CustomStatusBarWidget {

    private final Logger LOGGER = Logger.getInstance(getClass().getName());

    private final SettingsService settingsService = IJUtils.getSettingsService();
    private ScheduledFuture<?> timer;

    ProgressbarStatusWidget() {
        setBorder(JBUI.CurrentTheme.StatusBar.Widget.border()); // TODO add an option to remove the call to 'setBorder' in order to remove margins
        setOpaque(false);
        setFocusable(false);
        setBorderPainted(false);
    }

    @NotNull
    @Override
    public String ID() {
        return Globals.PROGRESSBAR_STATUS_WIDGET_ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(this::startIfNeeded);
        setVisible(true);
    }

    public void reload() {
        dispose();
        startIfNeeded();
    }

    private void startIfNeeded() {
        if (settingsService.getStatusUIType() == StatusUIType.PROGRESS_BAR) {
            try {
                timer = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(
                    this::repaint,
                    0, 30, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
    }

    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel(true);
            timer = null;
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (settingsService.getStatusUIType() != PROGRESS_BAR) {
            setVisible(false);
            return;
        }
        LocalTime timerStart = LocalTime.now();
        setVisible(true);

        final Dimension size = getSize();
        final Insets insets = getInsets();

        final int totalBarLength = size.width - insets.left - insets.right;
        final int barHeight = Math.max(size.height, getFont().getSize() + 2);
        final int yOffset = (size.height - barHeight) / 2;
        final int xOffset = insets.left;

        TimeLeft timeLeft = DateUtils.computeStatusWidgetText();

        if (settingsService.getStatusUIType() == PROGRESS_BAR) {
            if (settingsService.getCustomPbgarColorsEnabled()) {
                if (timeLeft.getPercentage() < settingsService.getPgbarRedLevel()) {
                    g.setColor(UIUtils.rgbaStrToColor(settingsService.getRgbaRedColor()));
                } else if (timeLeft.getPercentage() < settingsService.getPgbarYellowLevel()) {
                    g.setColor(UIUtils.rgbaStrToColor(settingsService.getRgbaYellowColor()));
                } else {
                    g.setColor(UIUtils.rgbaStrToColor(settingsService.getRgbaGreenColor()));
                }
            } else {
                if (timeLeft.getPercentage() < settingsService.getPgbarRedLevel()) {
                    g.setColor(Defaults.Colors.DEFAULT_RED_JBCOLOR);
                } else if (timeLeft.getPercentage() < settingsService.getPgbarYellowLevel()) {
                    g.setColor(Defaults.Colors.DEFAULT_YELLOW_JBCOLOR);
                } else {
                    g.setColor(Defaults.Colors.DEFAULT_GREEN_JBCOLOR);
                }
            }
            g.fillRect(0, insets.bottom - 30, (int) (getPreferredSize().width * timeLeft.getPercentage() / 100.0), 130);
        }
        UISettings.setupAntialiasing(g);

        long executionDuration = Duration.between(timerStart, LocalTime.now()).toMillis();
        String txt = timeLeft.getLabel();
        setToolTipText("<html>" + timeLeft.getTooltip() + "</html>");

        Font widgetFont = JBUI.Fonts.label(settingsService.getFontSize());
        g.setFont(widgetFont);
        g.setColor(JBColor.foreground());
//        if (txt.contains(DateUtils.TIMER_ENDED_MSG)) {
//            g.setColor(JBColor.YELLOW);
//        } else {
//            g.setColor(JBColor.BLACK);
//        }

        final FontMetrics fontMetrics = g.getFontMetrics();
        final int infoWidth = fontMetrics.charsWidth(txt.toCharArray(), 0, txt.length());
        final int infoHeight = fontMetrics.getAscent();
        g.drawString(txt, xOffset + (totalBarLength - infoWidth) / 2, yOffset + infoHeight + (barHeight - infoHeight) / 2 - 1);

        if (executionDuration > 30) {
            LOGGER.warn("Status updated in " + executionDuration + " ms, it should be faster once IDE or project is fully loaded");
        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Font widgetFont = JBUI.Fonts.label(settingsService.getFontSize());
        final FontMetrics fontMetrics = getFontMetrics(widgetFont);
        final Insets insets = getInsets();
        int height = fontMetrics.getHeight() + insets.top + insets.bottom + JBUI.scale(2) - 2;
        return new Dimension(settingsService.getStatusWidth(), height);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
