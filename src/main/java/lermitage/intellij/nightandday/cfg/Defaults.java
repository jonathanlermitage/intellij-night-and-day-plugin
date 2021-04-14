// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import com.intellij.util.ui.JBUI;
import lermitage.intellij.nightandday.core.DateUtils;

import java.time.LocalDateTime;

public class Defaults {
    public static final StatusUIType DEFAULT_STATUS_UI_TYPE = StatusUIType.PROGRESS_BAR;
    public static final StatusTextType DEFAULT_STATUS_TEXT_TYPE = StatusTextType.PREDEFINED_DURATION_FORMAT;
    public static final StatusDurationEndType DEFAULT_STATUS_DURATION_END_TYPE = StatusDurationEndType.END_OF_DAY;
    public static final String DEFAULT_PREFIX_TXT = "⏰ ";
    public static final String DEFAULT_SUFFIX_TXT = "";
    public static final String DEFAULT_TXT_DATE_REGEX_PATTERN = "H:mm";
    public static final Boolean DEFAULT_AWAKE_MODE_ENABLED = false;
    public static final String DEFAULT_AWAKE_START = "09:00";
    public static final String DEFAULT_AWAKE_END = "17:00";
    public static final String DEFAULT_CUSTOM_START_DATETIME = LocalDateTime.now().withHour(0).withMinute(0).format(DateUtils.DATE_TIME_FORMATTER);
    public static final String DEFAULT_CUSTOM_END_DATETIME = LocalDateTime.now().withHour(23).withMinute(59).format(DateUtils.DATE_TIME_FORMATTER);
    public static final Integer DEFAULT_WIDGET_WIDTH = 100;
    public static final Integer DEFAULT_FONT_SIZE = JBUI.Fonts.toolbarFont().getSize();
}
