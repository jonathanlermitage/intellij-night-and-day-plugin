// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

public class TimeLeft {

    private final String label;
    private final int percentage;
    private final String tooltip;

    public TimeLeft(String label, int percentage, String tooltip) {
        this.label = label;
        this.percentage = percentage;
        this.tooltip = tooltip;
    }

    public String getLabel() {
        return label;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getTooltip() {
        return tooltip;
    }
}
