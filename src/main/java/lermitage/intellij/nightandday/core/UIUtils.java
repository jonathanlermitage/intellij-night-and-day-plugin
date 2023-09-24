// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

import org.apache.commons.lang.StringUtils;

import java.awt.*;

public class UIUtils {

    public static String colorToRgbaStr(Color color) {
        return StringUtils.leftPad(Integer.toString(color.getRed()), 3, "0")
            + "," + StringUtils.leftPad(Integer.toString(color.getGreen()), 3, "0")
            + "," + StringUtils.leftPad(Integer.toString(color.getBlue()), 3, "0")
            + "," + StringUtils.leftPad(Integer.toString(color.getAlpha()), 3, "0");
    }

    @SuppressWarnings("UseJBColor")
    public static Color rgbaStrToColor(String str) {
        String[] split = str.split(",");
        return new Color(Integer.parseInt(split[0]),
            Integer.parseInt(split[1]),
            Integer.parseInt(split[2]),
            Integer.parseInt(split[3]));
    }
}
