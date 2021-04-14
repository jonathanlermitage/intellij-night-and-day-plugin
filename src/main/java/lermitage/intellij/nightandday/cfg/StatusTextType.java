// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import java.util.Arrays;

public enum StatusTextType {
    PREDEFINED_DURATION_FORMAT("time left", 0),
    PERCENTAGE("percentage left", 1),
    PREDEFINED_DURATION_FORMAT_PLUS_PERCENTAGE("time left + percentage left", 2);

    String label;
    int idx;

    StatusTextType(String label, int idx) {
        this.label = label;
        this.idx = idx;
    }

    public String getLabel() {
        return label;
    }

    public int getIdx() {
        return idx;
    }

    public static StatusTextType getByLabel(String label) {
        return Arrays.stream(StatusTextType.values())
            .filter(elt -> elt.getLabel().equalsIgnoreCase(label))
            .findFirst().orElse(Defaults.DEFAULT_STATUS_TEXT_TYPE);
    }
}
