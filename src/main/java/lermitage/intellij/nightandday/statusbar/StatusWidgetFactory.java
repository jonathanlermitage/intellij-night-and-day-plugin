package lermitage.intellij.nightandday.statusbar;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import lermitage.intellij.nightandday.core.Globals;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class StatusWidgetFactory implements StatusBarWidgetFactory {

    @NotNull
    @Override
    public String getId() {
        return Globals.PLUGIN_ID;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return Globals.PLUGIN_NATURAL_NAME;
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @NotNull
    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new StatusWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
