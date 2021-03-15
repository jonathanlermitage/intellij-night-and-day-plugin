// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class UIUtils {

    public static @NotNull Icon getStatusIcon(@Nullable String state) {
        String path = "/icons/nightandday/status";
        if (state != null && !state.isEmpty()) {
            path += "-" + state;
        }
        path += ".svg";
        return IconLoader.getIcon(path, UIUtils.class);
    }
}
