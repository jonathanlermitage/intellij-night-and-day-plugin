// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import lermitage.intellij.nightandday.core.DateUtils;
import lermitage.intellij.nightandday.core.UIUtils;

import java.awt.Color;
import java.time.LocalDateTime;

@SuppressWarnings("UseJBColor")
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
    public static final Boolean DEFAULT_CUSTOM_PGBAR_COLORS_ENABLED = false;
    public static final Integer DEFAULT_PGBAR_RED_LEVEL = 20;
    public static final Integer DEFAULT_PGBAR_YELLOW_LEVEL = 40;

    public static class Colors {
        public static final Color DEFAULT_GREEN_COLOR = new Color(28, 152, 19, 80);
        public static final Color DEFAULT_YELLOW_COLOR = new Color(200, 164, 23, 100);
        public static final Color DEFAULT_RED_COLOR = new Color(239, 42, 59, 100);

        public static final Color DEFAULT_DARK_GREEN_COLOR = new Color(56, 113, 41, 80);
        public static final Color DEFAULT_DARK_YELLOW_COLOR = new Color(170, 124, 36, 100);
        public static final Color DEFAULT_DARK_RED_COLOR = new Color(239, 42, 59, 100);

        public static final String DEFAULT_GREEN_COLOR_STR = UIUtils.colorToRgbaStr(DEFAULT_GREEN_COLOR);
        public static final String DEFAULT_YELLOW_COLOR_STR = UIUtils.colorToRgbaStr(DEFAULT_YELLOW_COLOR);
        public static final String DEFAULT_RED_COLOR_STR = UIUtils.colorToRgbaStr(DEFAULT_RED_COLOR);

        public static final JBColor DEFAULT_GREEN_JBCOLOR = new JBColor(DEFAULT_GREEN_COLOR, DEFAULT_DARK_GREEN_COLOR);
        public static final Color DEFAULT_YELLOW_JBCOLOR = new JBColor(DEFAULT_YELLOW_COLOR, DEFAULT_DARK_YELLOW_COLOR);
        public static final Color DEFAULT_RED_JBCOLOR = new JBColor(DEFAULT_RED_COLOR, DEFAULT_DARK_RED_COLOR);
    }
}
