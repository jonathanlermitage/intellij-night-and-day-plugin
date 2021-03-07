package lermitage.intellij.nightandday.cfg;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@SuppressWarnings({"WeakerAccess", "unused"})
@State(
    name = "NightanddaySettings",
    storages = @Storage("lermitage-nightandday.xml")
)
public class SettingsService implements PersistentStateComponent<SettingsService> {

    private final Logger LOG = Logger.getInstance(getClass().getName());

    // status bar appearance
    public StatusUIType statusUIType;

    // common config

    // status bar: text
    public StatusTextType statusTextType;
    public String txtPrefixTxt;
    public String txtSuffixTxt;
    public String txtDateRegexPattern;

    // status bar: percentage
    public String percentagePrefixTxt;
    public String percentageSuffixTxt;

    @Override
    public SettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // config keys getters/setters

    public StatusUIType getStatusUIType() {
        return statusUIType == null ? Defaults.DEFAULT_STATUS_UI_TYPE : statusUIType;
    }

    public void setStatusUIType(StatusUIType statusUIType) {
        this.statusUIType = statusUIType;
    }

    public StatusTextType getStatusTextType() {
        return statusTextType == null ? Defaults.DEFAULT_STATUS_TEXT_TYPE : statusTextType;
    }

    public void setStatusTextType(StatusTextType statusTextType) {
        this.statusTextType = statusTextType;
    }

    public String getTxtPrefixTxt() {
        return txtPrefixTxt == null ? Defaults.DEFAULT_TXT_PREFIX_TXT : txtPrefixTxt;
    }

    public void setTxtPrefixTxt(String txtPrefixTxt) {
        this.txtPrefixTxt = txtPrefixTxt;
    }

    public String getTxtSuffixTxt() {
        return txtSuffixTxt == null ? Defaults.DEFAULT_TXT_SUFFIX_TXT : txtSuffixTxt;
    }

    public void setTxtSuffixTxt(String txtSuffixTxt) {
        this.txtSuffixTxt = txtSuffixTxt;
    }

    public String getTxtDateRegexPattern() {
        return txtDateRegexPattern == null ? Defaults.DEFAULT_TXT_DATE_REGEX_PATTERN : txtDateRegexPattern;
    }

    public void setTxtDateRegexPattern(String txtDateRegexPattern) {
        this.txtDateRegexPattern = txtDateRegexPattern;
    }

    public String getPercentagePrefixTxt() {
        return percentagePrefixTxt == null ? Defaults.DEFAULT_PERCENTAGE_TXT_PREFIX_TXT : percentagePrefixTxt;
    }

    public void setPercentagePrefixTxt(String percentagePrefixTxt) {
        this.percentagePrefixTxt = percentagePrefixTxt;
    }

    public String getPercentageSuffixTxt() {
        return percentageSuffixTxt == null ? Defaults.DEFAULT_PERCENTAGE_TXT_SUFFIX_TXT : percentageSuffixTxt;
    }

    public void setPercentageSuffixTxt(String percentageSuffixTxt) {
        this.percentageSuffixTxt = percentageSuffixTxt;
    }
}
