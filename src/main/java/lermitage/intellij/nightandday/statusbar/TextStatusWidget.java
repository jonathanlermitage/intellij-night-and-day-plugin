// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import lermitage.intellij.nightandday.core.Globals;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("WeakerAccess")
public class TextStatusWidget implements StatusBarWidget {

    private final StatusBar statusBar;
    private final Project project;
    private Timer timer;

    @Contract(pure = true)
    public TextStatusWidget(Project project) {
        this.statusBar = WindowManager.getInstance().getStatusBar(project);
        this.project = project;
    }

    @NotNull
    @Override
    public String ID() {
        return Globals.TEXT_STATUS_WIDGET_ID;
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return new TextStatusPresentation(statusBar, project, this);
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> continuousStatusWidgetUpdate(statusBar));
    }

    private void continuousStatusWidgetUpdate(StatusBar statusBar) {
        try {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    statusBar.updateWidget(Globals.TEXT_STATUS_WIDGET_ID);
                }
            }, 0, 30_000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
