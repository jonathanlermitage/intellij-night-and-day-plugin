// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg.gui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import lermitage.intellij.nightandday.cfg.Defaults;
import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusDurationEndType;
import lermitage.intellij.nightandday.cfg.StatusTextType;
import lermitage.intellij.nightandday.cfg.StatusUIType;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.IJUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class SettingsForm implements Configurable {

    private JPanel mainPane;
    private JButton resetDefaultsBtn;
    private JComboBox<String> statusUITypeSelector;
    private JLabel statusUITypeLabel;
    private JComboBox<String> statusTextTypeSelector;
    private JLabel statusTextTypeLabel;
    private JComboBox<String> statusDurationEndTypeSelector;
    private JLabel statusDurationEndTypeLabel;
    private JFormattedTextField awakeStartTextField;
    private JLabel awakeLabel;
    private JFormattedTextField awakeEndTextField;
    private JPanel awakePanel;
    private JCheckBox awakeModeEnabledCheckBox;
    private JTextField prefixTextField;
    private JTextField suffixTextField;
    private JLabel prefixLabel;
    private JLabel suffixLabel;
    private JPanel customDatesPanel;
    private JFormattedTextField customDatesStartTextField;
    private JFormattedTextField customDatesEndTextField;
    private JLabel customDatesLabel;
    private JButton customDatesStartButton;
    private JLabel fontSizeLabel;
    private JSpinner fontSizeField;
    private JLabel widgetWidthLabel;
    private JSpinner widgetWidthField;

    private final Logger LOG = Logger.getInstance(getClass().getName());
    private final SettingsService settingsService;

    private boolean modified = false;

    public SettingsForm() {
        this.settingsService = ServiceManager.getService(SettingsService.class);

        Arrays.stream(StatusUIType.values()).forEach(type -> statusUITypeSelector.addItem(type.getLabel()));
        Arrays.stream(StatusTextType.values()).forEach(type -> statusTextTypeSelector.addItem(type.getLabel()));
        Arrays.stream(StatusDurationEndType.values()).forEach(type -> statusDurationEndTypeSelector.addItem(type.getLabel()));

        resetDefaultsBtn.addActionListener(e -> {
            statusUITypeSelector.setSelectedIndex(Defaults.DEFAULT_STATUS_UI_TYPE.getIdx());
            statusTextTypeSelector.setSelectedIndex(Defaults.DEFAULT_STATUS_TEXT_TYPE.getIdx());
            statusDurationEndTypeSelector.setSelectedIndex(Defaults.DEFAULT_STATUS_DURATION_END_TYPE.getIdx());
            awakeModeEnabledCheckBox.setSelected(Defaults.DEFAULT_AWAKE_MODE_ENABLED);
            awakeStartTextField.setText(Defaults.DEFAULT_AWAKE_START);
            awakeEndTextField.setText(Defaults.DEFAULT_AWAKE_END);
            prefixTextField.setText(Defaults.DEFAULT_PREFIX_TXT);
            suffixTextField.setText(Defaults.DEFAULT_SUFFIX_TXT);
            customDatesStartTextField.setText(Defaults.DEFAULT_CUSTOM_START_DATETIME);
            customDatesEndTextField.setText(Defaults.DEFAULT_CUSTOM_END_DATETIME);
            fontSizeField.setValue(Defaults.DEFAULT_FONT_SIZE);
            widgetWidthField.setValue(Defaults.DEFAULT_WIDGET_WIDTH);
            modified = true;
        });
        customDatesStartButton.addActionListener(e -> {
            customDatesStartTextField.setText(DateUtils.DATE_TIME_FORMATTER.format(LocalDateTime.now()));
            modified = true;
        });
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Night and Day";
    }

    private void updateComponentsVisibility() {
        boolean progressbarSelected = Objects.equals(statusUITypeSelector.getSelectedItem(), StatusUIType.PROGRESS_BAR.getLabel());
        fontSizeLabel.setVisible(progressbarSelected);
        fontSizeField.setVisible(progressbarSelected);
        widgetWidthLabel.setVisible(progressbarSelected);
        widgetWidthField.setVisible(progressbarSelected);

        boolean awakeModeSelected = awakeModeEnabledCheckBox.isSelected();
        awakeModeEnabledCheckBox.setSelected(awakeModeSelected);
        awakePanel.setVisible(awakeModeSelected);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        statusUITypeLabel.setText("Appearance:");
        statusTextTypeLabel.setText("Text format:");
        statusDurationEndTypeLabel.setText("Time left:");
        prefixLabel.setText("Text prefix:");
        suffixLabel.setText("Text suffix:");
        resetDefaultsBtn.setText("Reset to defaults");
        awakeModeEnabledCheckBox.setText("Enable awake time");
        awakeLabel.setText("Awake time:");
        customDatesLabel.setText("Custom date:");
        customDatesStartButton.setText("Now");
        fontSizeLabel.setText("Font size:");
        widgetWidthLabel.setText("Status Bar widget width:");

        awakeStartTextField.setToolTipText("<html><b>HH:mm</b> (24-hours time format)</html>");
        awakeEndTextField.setToolTipText("<html><b>HH:mm</b> (24-hours time format)</html>");
        customDatesStartTextField.setToolTipText("<html><b>yyyy-MM-dd HH:mm</b> (ISO date, 24-hours time format)</html>");
        customDatesEndTextField.setToolTipText("<html><b>yyyy-MM-dd HH:mm</b> (ISO date, 24-hours time format)</html>");

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
        ChangeListener changeListener = e -> modified = true;

        statusDurationEndTypeSelector.addItemListener(item -> {
            customDatesPanel.setVisible(item.getItem().equals(StatusDurationEndType.CUSTOM_DATE.getLabel()));
            modified = true;
        });
        statusUITypeSelector.addComponentListener(componentListener);
        statusUITypeSelector.addItemListener(item -> {
            updateComponentsVisibility();
            modified = true;
        });
        statusTextTypeSelector.addComponentListener(componentListener);
        awakeModeEnabledCheckBox.addComponentListener(componentListener);
        awakeStartTextField.getDocument().addDocumentListener(docListener);
        awakeEndTextField.getDocument().addDocumentListener(docListener);
        awakeModeEnabledCheckBox.addItemListener(item -> {
            updateComponentsVisibility();
            modified = true;
        });
        prefixTextField.getDocument().addDocumentListener(docListener);
        suffixTextField.getDocument().addDocumentListener(docListener);
        fontSizeField.addChangeListener(changeListener);
        widgetWidthField.addChangeListener(changeListener);

        updateComponentsVisibility();

        return mainPane;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void apply() {
        settingsService.setStatusUIType(StatusUIType.getByLabel((String) statusUITypeSelector.getSelectedItem()));
        settingsService.setStatusTextType(StatusTextType.getByLabel((String) statusTextTypeSelector.getSelectedItem()));
        settingsService.setStatusDurationEndType(StatusDurationEndType.getByLabel((String) statusDurationEndTypeSelector.getSelectedItem()));
        settingsService.setAwakeModeEnabled(awakeModeEnabledCheckBox.isSelected());
        settingsService.setAwakeStart(awakeStartTextField.getText());
        settingsService.setAwakeEnd(awakeEndTextField.getText());
        settingsService.setPrefixTxt(prefixTextField.getText());
        settingsService.setSuffixTxt(suffixTextField.getText());
        settingsService.setCustomStartDatetime(customDatesStartTextField.getText());
        settingsService.setCustomEndDatetime(customDatesEndTextField.getText());
        settingsService.setFontSize((Integer) fontSizeField.getValue());
        settingsService.setStatusWidth((Integer) widgetWidthField.getValue());
        IJUtils.refreshOpenedProjects(settingsService.getStatusUIType());
    }

    @Override
    public void reset() {
        settingsService.setStatusUIType(settingsService.getStatusUIType());
        settingsService.setStatusTextType(settingsService.getStatusTextType());
        settingsService.setStatusDurationEndType(settingsService.getStatusDurationEndType());
        settingsService.setAwakeModeEnabled(settingsService.getAwakeModeEnabled());
        settingsService.setAwakeStart(settingsService.getAwakeStart());
        settingsService.setAwakeEnd(settingsService.getAwakeEnd());
        settingsService.setPrefixTxt(settingsService.getPrefixTxt());
        settingsService.setSuffixTxt(settingsService.getSuffixTxt());
        settingsService.setCustomStartDatetime(settingsService.getCustomStartDatetime());
        settingsService.setCustomEndDatetime(settingsService.getCustomEndDatetime());
        settingsService.setFontSize(settingsService.getFontSize());
        settingsService.setStatusWidth(settingsService.getStatusWidth());
        loadConfig();
        modified = false;
    }

    private void loadConfig() {
        statusUITypeSelector.setSelectedIndex(settingsService.getStatusUIType().getIdx());
        statusTextTypeSelector.setSelectedIndex(settingsService.getStatusTextType().getIdx());
        statusDurationEndTypeSelector.setSelectedIndex(settingsService.getStatusDurationEndType().getIdx());
        awakePanel.setVisible(settingsService.getAwakeModeEnabled());
        awakeModeEnabledCheckBox.setSelected(settingsService.getAwakeModeEnabled());
        awakeStartTextField.setText(settingsService.getAwakeStart());
        awakeEndTextField.setText(settingsService.getAwakeEnd());
        prefixTextField.setText(settingsService.getPrefixTxt());
        suffixTextField.setText(settingsService.getSuffixTxt());
        customDatesPanel.setVisible(settingsService.getStatusDurationEndType() == StatusDurationEndType.CUSTOM_DATE);
        customDatesStartTextField.setText(settingsService.getCustomStartDatetime());
        customDatesEndTextField.setText(settingsService.getCustomEndDatetime());
        fontSizeField.setValue(settingsService.getFontSize());
        widgetWidthField.setValue(settingsService.getStatusWidth());
    }

    private void createUIComponents() {
        // place custom component creation code here
        try {
            awakeStartTextField = new JFormattedTextField(new MaskFormatter(DateUtils.TIME_MASK));
            awakeEndTextField = new JFormattedTextField(new MaskFormatter(DateUtils.TIME_MASK));
            customDatesStartTextField = new JFormattedTextField(new MaskFormatter(DateUtils.DATE_TIME_MASK));
            customDatesEndTextField = new JFormattedTextField(new MaskFormatter(DateUtils.DATE_TIME_MASK));
        } catch (ParseException e) {
            LOG.error("Init error", e);
        }
    }
}
