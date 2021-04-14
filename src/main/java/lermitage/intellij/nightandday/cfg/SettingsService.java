// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@SuppressWarnings({"WeakerAccess", "unused"})
@State(
    name = "NightanddaySettings",
    storages = @Storage("lermitage-nightandday.xml")
)
public class SettingsService implements PersistentStateComponent<SettingsService> {

    private final Logger LOG = Logger.getInstance(getClass().getName());
    public static final String AWAKE_SEPARATOR = ":";

    // which time left
    public StatusDurationEndType statusDurationEndType;

    // status bar appearance
    public StatusUIType statusUIType;
    public Integer statusWidth;
    public Integer fontSize;

    // status bar: text
    public StatusTextType statusTextType;
    public String txtDateRegexPattern;

    public String prefixTxt;
    public String suffixTxt;
    public Boolean awakeModeEnabled;
    public String awakeStart;
    public String awakeEnd;
    public String customStartDatetime;
    public String customEndDatetime;

    @Override
    public SettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // config keys getters/setters


    public StatusDurationEndType getStatusDurationEndType() {
        return statusDurationEndType == null ? Defaults.DEFAULT_STATUS_DURATION_END_TYPE : statusDurationEndType;
    }

    public void setStatusDurationEndType(StatusDurationEndType statusDurationEndType) {
        this.statusDurationEndType = statusDurationEndType;
    }

    public StatusUIType getStatusUIType() {
        return statusUIType == null ? Defaults.DEFAULT_STATUS_UI_TYPE : statusUIType;
    }

    public void setStatusUIType(StatusUIType statusUIType) {
        this.statusUIType = statusUIType;
    }

    public Integer getStatusWidth() {
        return statusWidth == null ? Defaults.DEFAULT_WIDGET_WIDTH : statusWidth;
    }

    public void setStatusWidth(Integer statusWidth) {
        this.statusWidth = statusWidth;
    }

    public Integer getFontSize() {
        return fontSize == null ? Defaults.DEFAULT_FONT_SIZE : fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public StatusTextType getStatusTextType() {
        return statusTextType == null ? Defaults.DEFAULT_STATUS_TEXT_TYPE : statusTextType;
    }

    public void setStatusTextType(StatusTextType statusTextType) {
        this.statusTextType = statusTextType;
    }

    public String getTxtDateRegexPattern() {
        return txtDateRegexPattern == null ? Defaults.DEFAULT_TXT_DATE_REGEX_PATTERN : txtDateRegexPattern;
    }

    public void setTxtDateRegexPattern(String txtDateRegexPattern) {
        this.txtDateRegexPattern = txtDateRegexPattern;
    }

    public String getPrefixTxt() {
        return prefixTxt == null ? Defaults.DEFAULT_PREFIX_TXT : prefixTxt;
    }

    public void setPrefixTxt(String prefixTxt) {
        this.prefixTxt = prefixTxt;
    }

    public String getSuffixTxt() {
        return suffixTxt == null ? Defaults.DEFAULT_SUFFIX_TXT : suffixTxt;
    }

    public void setSuffixTxt(String suffixTxt) {
        this.suffixTxt = suffixTxt;
    }

    public Boolean getAwakeModeEnabled() {
        return awakeModeEnabled == null ? Defaults.DEFAULT_AWAKE_MODE_ENABLED : awakeModeEnabled;
    }

    public void setAwakeModeEnabled(Boolean awakeModeEnabled) {
        this.awakeModeEnabled = awakeModeEnabled;
    }

    public String getAwakeStart() {
        return awakeStart == null ? Defaults.DEFAULT_AWAKE_START : awakeStart;
    }

    public void setAwakeStart(int h, int m) {
        this.awakeStart = StringUtils.leftPad(Integer.toString(h), 2, "0")
            + AWAKE_SEPARATOR
            + StringUtils.leftPad(Integer.toString(m), 2, "0");
    }

    public void setAwakeStart(String awakeStart) {
        this.awakeStart = awakeStart;
    }

    public String getAwakeEnd() {
        return awakeEnd == null ? Defaults.DEFAULT_AWAKE_END : awakeEnd;
    }

    public void setAwakeEnd(int h, int m) {
        this.awakeEnd = StringUtils.leftPad(Integer.toString(h), 2, "0")
            + AWAKE_SEPARATOR
            + StringUtils.leftPad(Integer.toString(m), 2, "0");
    }

    public void setAwakeEnd(String awakeEnd) {
        this.awakeEnd = awakeEnd;
    }

    public String getCustomStartDatetime() {
        return this.customStartDatetime == null || this.customStartDatetime.isEmpty() ? Defaults.DEFAULT_CUSTOM_START_DATETIME : customStartDatetime;
    }

    public void setCustomStartDatetime(String customStartDatetime) {
        this.customStartDatetime = customStartDatetime;
    }

    public String getCustomEndDatetime() {
        return customEndDatetime == null || customEndDatetime.isEmpty() ? Defaults.DEFAULT_CUSTOM_END_DATETIME : customEndDatetime;
    }

    public void setCustomEndDatetime(String customEndDatetime) {
        this.customEndDatetime = customEndDatetime;
    }
}
