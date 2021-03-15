// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import java.util.Arrays;

public enum StatusUIType {
    PROGRESS_BAR("text and a progress bar in background", 0),
    TEXT("text only", 1);

    String label;
    int idx;

    StatusUIType(String label, int idx) {
        this.label = label;
        this.idx = idx;
    }

    public String getLabel() {
        return label;
    }

    public int getIdx() {
        return idx;
    }

    public static StatusUIType getByLabel(String label) {
        return Arrays.stream(StatusUIType.values())
            .filter(elt -> elt.getLabel().equalsIgnoreCase(label))
            .findFirst().orElse(Defaults.DEFAULT_STATUS_UI_TYPE);
    }
}
