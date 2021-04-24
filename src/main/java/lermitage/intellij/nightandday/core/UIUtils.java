// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

import com.intellij.openapi.util.IconLoader;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.Color;

public class UIUtils {

    public static final String COLOR_MASK = "###,###,###,###";

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

    public static @NotNull Icon getStatusIcon(@Nullable String state) {
        String path = "/icons/nightandday/status";
        if (state != null && !state.isEmpty()) {
            path += "-" + state;
        }
        path += ".svg";
        return IconLoader.getIcon(path, UIUtils.class);
    }
}
