package lermitage.intellij.nightandday.cfg.gui;

import com.intellij.openapi.options.Configurable;
import lermitage.intellij.nightandday.core.Globals;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class SettingsForm implements Configurable {

    private JPanel mainPane;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return Globals.PLUGIN_NATURAL_NAME;
    }

    @Override
    public @Nullable JComponent createComponent() {
        return mainPane;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {
    }

    @Override
    public void reset() {
    }
}
