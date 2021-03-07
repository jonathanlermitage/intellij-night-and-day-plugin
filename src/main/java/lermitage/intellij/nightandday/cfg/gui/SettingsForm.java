package lermitage.intellij.nightandday.cfg.gui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import lermitage.intellij.nightandday.cfg.Defaults;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusTextType;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;

public class SettingsForm implements Configurable {

    private JPanel mainPane;
    private JButton resetDefaultsBtn;
    private JComboBox<String> statusUITypeSelector;
    private JLabel statusUITypeLabel;
    private JComboBox<String> statusTextTypeSelector;
    private JLabel statusTextTypeLabel;
    private JTextField textField1;

    private final SettingsService settingsService;

    private boolean modified = false;

    public SettingsForm() {
        this.settingsService = ServiceManager.getService(SettingsService.class);

        Arrays.stream(StatusUIType.values()).forEach(statusUIType -> statusUITypeSelector.addItem(statusUIType.getLabel()));
        Arrays.stream(StatusTextType.values()).forEach(statusTextType -> statusTextTypeSelector.addItem(statusTextType.getLabel()));

        resetDefaultsBtn.addActionListener(e -> {
            statusUITypeSelector.setSelectedIndex(Defaults.DEFAULT_STATUS_UI_TYPE.getIdx());
            statusTextTypeSelector.setSelectedIndex(Defaults.DEFAULT_STATUS_TEXT_TYPE.getIdx());
            modified = true;
        });
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Night and Day";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        statusUITypeLabel.setText("Time left to display:");
        statusTextTypeLabel.setText("How to display time left:");
        resetDefaultsBtn.setText("Reset to defaults");

        loadConfig();

        DocumentListener docListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                modified = true;
            }

            public void removeUpdate(DocumentEvent e) {
                modified = true;
            }

            public void insertUpdate(DocumentEvent e) {
                modified = true;
            }
        };
        ComponentListener componentListener = new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                modified = true;
            }

            public void componentMoved(ComponentEvent e) {
                modified = true;
            }

            public void componentShown(ComponentEvent e) {
                modified = true;
            }

            public void componentHidden(ComponentEvent e) {
                modified = true;
            }
        };

        textField1.getDocument().addDocumentListener(docListener);
        statusUITypeSelector.addComponentListener(componentListener);
        statusTextTypeSelector.addComponentListener(componentListener);

        return mainPane;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void apply() {
        settingsService.setStatusUIType(StatusUIType.getByIdx(statusUITypeSelector.getSelectedIndex()));
        settingsService.setStatusTextType(StatusTextType.getByIdx(statusTextTypeSelector.getSelectedIndex()));
    }

    @Override
    public void reset() {
        settingsService.setStatusUIType(settingsService.getStatusUIType());
        settingsService.setStatusTextType(settingsService.getStatusTextType());
        loadConfig();
        modified = false;
    }

    private void loadConfig() {
        statusUITypeSelector.setSelectedIndex(settingsService.getStatusUIType().getIdx());
        statusTextTypeSelector.setSelectedIndex(settingsService.getStatusTextType().getIdx());
    }
}
