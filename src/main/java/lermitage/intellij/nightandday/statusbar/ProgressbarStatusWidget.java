// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.Globals;
import lermitage.intellij.nightandday.core.TimeLeft;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.time.Duration;
import java.time.LocalTime;

import static lermitage.intellij.nightandday.cfg.StatusUIType.PROGRESS_BAR;

@SuppressWarnings("WeakerAccess")
public class ProgressbarStatusWidget extends JButton implements CustomStatusBarWidget {

    private final Logger LOG = Logger.getInstance(getClass().getName());
    private boolean forceExit = false;
    private Thread updateThread = null;
    private SettingsService settingsService;
    private static final JBColor PROGRESS_BAR_GREEN = new JBColor(
        new Color(28, 152, 19, 80),
        new Color(56, 113, 41, 80));
    private static final Color PROGRESS_BAR_RED = new JBColor(
        new Color(239, 42, 59, 100),
        new Color(239, 42, 59, 100));
    private static final Color PROGRESS_BAR_YELLOW = new JBColor(
        new Color(200, 164, 23, 100),
        new Color(170, 124, 36, 100));

    ProgressbarStatusWidget() {
        setBorder(StatusBarWidget.WidgetBorder.INSTANCE);
        setOpaque(false);
        setFocusable(false);
    }

    @NotNull
    @Override
    public String ID() {
        return Globals.PROGRESSBAR_STATUS_WIDGET_ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(this::continuousStatusWidgetUpdate);
        setVisible(true);
    }

    private void continuousStatusWidgetUpdate() {
        try {
            updateThread = Thread.currentThread();
            LOG.info("Registered updateThread " + updateThread.getId());
            while (!forceExit) {
                repaint();
                //noinspection BusyWait
                Thread.sleep(30_000);
            }
        } catch (InterruptedException e) {
            LOG.info("App disposed, forced updateThread interruption.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        forceExit = true;
        if (updateThread != null && !updateThread.isInterrupted()) {
            LOG.info("Interrupting updateThread " + updateThread.getId());
            updateThread.interrupt();
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (settingsService == null) {
            settingsService = ServiceManager.getService(SettingsService.class);
        }

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
            if (timeLeft.getPercentage() > 40) {
                g.setColor(PROGRESS_BAR_GREEN);
            } else if (timeLeft.getPercentage() > 20) {
                g.setColor(PROGRESS_BAR_YELLOW);
            } else {
                g.setColor(PROGRESS_BAR_RED);
            }
            g.fillRect(0, insets.bottom, (int) (getPreferredSize().width * timeLeft.getPercentage() / 100.0), barHeight);
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
            LOG.warn("Status updated in " + executionDuration + " ms, it should be faster once IDE or project is fully loaded");
        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        if (settingsService == null) {
            settingsService = ServiceManager.getService(SettingsService.class);
        }

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