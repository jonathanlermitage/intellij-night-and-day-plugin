package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import lermitage.intellij.nightandday.core.Globals;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("WeakerAccess")
public class StatusWidget implements StatusBarWidget {

    private final Logger LOG = Logger.getInstance(getClass().getName());
    private final StatusBar statusBar;
    private final Project project;
    private boolean forceExit = false;
    private Thread updateThread = null;

    @Contract(pure = true)
    public StatusWidget(Project project) {
        this.statusBar = WindowManager.getInstance().getStatusBar(project);
        this.project = project;
    }

    @NotNull
    @Override
    public String ID() {
        return Globals.PLUGIN_ID;
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return new StatusPresentation(statusBar, project, this);
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> continuousStatusWidgetUpdate(statusBar));
    }

    private void continuousStatusWidgetUpdate(StatusBar statusBar) {
        try {
            updateThread = Thread.currentThread();
            LOG.info("Registered updateThread " + updateThread.getId());
            while (!forceExit) {
                statusBar.updateWidget(Globals.PLUGIN_ID);
                //noinspection BusyWait
                Thread.sleep(60_000);
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
}
