// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.cfg;

import java.util.Arrays;

public enum StatusDurationEndType {
    END_OF_DAY("end of day", 0),
    END_OF_WEEK("end of week", 1),
    END_OF_MONTH("end of month", 2),
    END_OF_YEAR("end of year", 3),
    CUSTOM_DATE("custom date", 4);

    String label;
    int idx;

    StatusDurationEndType(String label, int idx) {
        this.label = label;
        this.idx = idx;
    }

    public String getLabel() {
        return label;
    }

    public int getIdx() {
        return idx;
    }

    public static StatusDurationEndType getByLabel(String label) {
        return Arrays.stream(StatusDurationEndType.values())
            .filter(elt -> elt.getLabel().equalsIgnoreCase(label))
            .findFirst().orElse(Defaults.DEFAULT_STATUS_DURATION_END_TYPE);
    }
}
