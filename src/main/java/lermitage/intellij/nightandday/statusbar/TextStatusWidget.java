// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.concurrency.AppExecutorUtil;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.core.Globals;
import lermitage.intellij.nightandday.core.IJUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class TextStatusWidget implements StatusBarWidget {

    private final Logger LOGGER = Logger.getInstance(getClass().getName());

    private final StatusBar statusBar;
    private final SettingsService settingsService = IJUtils.getSettingsService();
    private ScheduledFuture<?> timer;

    @Contract(pure = true)
    public TextStatusWidget(Project project) {
        this.statusBar = WindowManager.getInstance().getStatusBar(project);
    }

    @NotNull
    @Override
    public String ID() {
        return Globals.TEXT_STATUS_WIDGET_ID;
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return new TextStatusPresentation();
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> startIfNeeded(statusBar));
    }

    public void reload() {
        dispose();
        startIfNeeded(statusBar);
    }

    private void startIfNeeded(StatusBar statusBar) {
        if (settingsService.getStatusUIType() == StatusUIType.TEXT) {
            try {
                timer = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(() -> {
                    statusBar.updateWidget(Globals.TEXT_STATUS_WIDGET_ID);
                }, 0, 30, TimeUnit.SECONDS);
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
}
