// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

public class TimeLeft {

    private final String label;
    private final int percentage;

    public TimeLeft(String label, int percentage) {
        this.label = label;
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public int getPercentage() {
        return percentage;
    }
}
