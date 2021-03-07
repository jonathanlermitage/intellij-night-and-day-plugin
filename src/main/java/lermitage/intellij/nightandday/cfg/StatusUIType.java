package lermitage.intellij.nightandday.cfg;

import java.util.Arrays;

public enum StatusUIType {
    TEXT("text only", 0),
    PROGRESS_BAR("text and a progress bar in background", 1);

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

    public static StatusUIType getByIdx(int idx) {
        return Arrays.stream(StatusUIType.values())
            .filter(elt -> elt.getIdx() == idx)
            .findFirst().orElse(Defaults.DEFAULT_STATUS_UI_TYPE);
    }
}
