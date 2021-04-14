// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.statusbar.ProgressbarStatusWidget;
import org.jetbrains.annotations.Nullable;

public class IJUtils {

    /**
     * Refresh project status bar.
     */
    public static void refresh(Project project, StatusUIType statusUIType) {
        if (IJUtils.isAlive(project)) {
            ProjectView view = ProjectView.getInstance(project);
            if (view != null) {
                StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                if (statusBar != null) {
                    statusBar.updateWidget(Globals.TEXT_STATUS_WIDGET_ID);
                    statusBar.updateWidget(Globals.PROGRESSBAR_STATUS_WIDGET_ID);
                    ProgressbarStatusWidget widget = (ProgressbarStatusWidget) statusBar.getWidget(Globals.PROGRESSBAR_STATUS_WIDGET_ID);
                    if (widget != null) {
                        widget.setVisible(false); // trick to force UI update if widget size changed
                        if (statusUIType == StatusUIType.PROGRESS_BAR) {
                            widget.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Refresh all opened project status bar.
     */
    public static void refreshOpenedProjects(StatusUIType statusUIType) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            refresh(project, statusUIType);
        }
    }

    /**
     * Return true if the project can be manipulated. Project is not null, not disposed, etc.
     * Developed to fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39">issue #39</a>.
     */
    public static boolean isAlive(@Nullable Project project) {
        return project != null && !project.isDisposed();
    }
}
