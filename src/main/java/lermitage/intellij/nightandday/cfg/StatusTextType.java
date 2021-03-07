package lermitage.intellij.nightandday.cfg;

import java.util.Arrays;

public enum StatusTextType {
    PREDEFINED_DURATION_FORMAT("predefined duration format", 0),
    CUSTOM_DURATION_FORMAT("custom duration format", 1),
    PERCENTAGE("percentage", 2);

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

    public static StatusTextType getByIdx(int idx) {
        return Arrays.stream(StatusTextType.values())
            .filter(elt -> elt.getIdx() == idx)
            .findFirst().orElse(Defaults.DEFAULT_STATUS_TEXT_TYPE);
    }
}
