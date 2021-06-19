// SPDX-License-Identifier: MIT

package lermitage.intellij.nightandday.core;

import lermitage.intellij.nightandday.cfg.SettingsService;
import lermitage.intellij.nightandday.cfg.StatusDurationEndType;
import lermitage.intellij.nightandday.cfg.StatusTextType;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static lermitage.intellij.nightandday.cfg.StatusDurationEndType.CUSTOM_DATE;
import static lermitage.intellij.nightandday.cfg.StatusTextType.PREDEFINED_DURATION_FORMAT_PLUS_PERCENTAGE;

public class DateUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String DATE_TIME_MASK = "####-##-## ##:##";
    public static final String TIME_MASK = "##:##";
    public static final String TIMER_ENDED_MSG = "timer ended";

    public static TimeLeft computeStatusWidgetText() {
        try {
            String statusText;
            String tooltip;
            SettingsService settingsService = IJUtils.getSettingsService();
            String prefix = settingsService.getPrefixTxt();
            String suffix = settingsService.getSuffixTxt();
            String awakeStart = settingsService.getAwakeStart();
            String awakeEnd = settingsService.getAwakeEnd();
            StatusDurationEndType statusDurationEndType = settingsService.getStatusDurationEndType();

            tooltip = "type: ";
            if (statusDurationEndType == StatusDurationEndType.CUSTOM_DATE) {
                tooltip += " custom, " + settingsService.getCustomStartDatetime() + " ≻ " + settingsService.getCustomEndDatetime();
            } else {
                tooltip += statusDurationEndType.getLabel();
            }
            if (settingsService.getAwakeModeEnabled()) {
                tooltip += "<br/>awake time: " + awakeStart + " ≻ " + awakeEnd;
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = DateUtils.endOf(statusDurationEndType, settingsService.getCustomEndDatetime());

            int percentLeft = timeLeftPercentage(awakeStart, awakeEnd);

            if (percentLeft == 0) {
                statusText = TIMER_ENDED_MSG;
                statusText = prefix + statusText + suffix;
                return new TimeLeft(statusText, percentLeft, tooltip);
            }

            StatusTextType statusTextType = settingsService.getStatusTextType();

            switch (statusTextType) {

                case PREDEFINED_DURATION_FORMAT:
                case PREDEFINED_DURATION_FORMAT_PLUS_PERCENTAGE:
                    Duration timeLeft;
                    if (now.isAfter(end)) {
                        if (statusDurationEndType == CUSTOM_DATE) {
                            return new TimeLeft(prefix + TIMER_ENDED_MSG + suffix, 0, tooltip);
                        } else {
                            end = end.plusDays(1);
                        }
                    }
                    if (settingsService.getAwakeModeEnabled()) {
                        timeLeft = DateUtils.between(now, end, awakeStart, awakeEnd);
                    } else {
                        timeLeft = Duration.between(now, end);
                    }
                    statusText = DateUtils.toText(timeLeft);
                    if (statusTextType == PREDEFINED_DURATION_FORMAT_PLUS_PERCENTAGE) {
                        statusText += " (" + percentLeft + "%)";
                    }
                    break;

                case PERCENTAGE:
                    statusText = percentLeft + "%";
                    break;

                default:
                    throw new IllegalStateException("No StatusTextType");
            }

            statusText = prefix + statusText + suffix;
            return new TimeLeft(statusText, percentLeft, tooltip);
        } catch (Exception e) {
            return new TimeLeft("Error: " + e.getMessage(), 0, "");
        }
    }

    private static int timeLeftPercentage(String awakeStart, String awakeEnd) {
        SettingsService settingsService = IJUtils.getSettingsService();
        LocalDateTime start = DateUtils.startOf(settingsService.getStatusDurationEndType(), settingsService.getCustomStartDatetime());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = DateUtils.endOf(settingsService.getStatusDurationEndType(), settingsService.getCustomEndDatetime());
        long timeTotal;
        if (now.isAfter(end)) {
            if (settingsService.getStatusDurationEndType() == CUSTOM_DATE) {
                return 0;
            } else {
                end = end.plusDays(1);
            }
        }
        if (settingsService.getAwakeModeEnabled()) {
            timeTotal = DateUtils.between(start, end, awakeStart, awakeEnd).toMillis();
            long timeRemaining = DateUtils.between(now, end, awakeStart, awakeEnd).toMillis();
            return (int) (timeRemaining * 100 / timeTotal);
        } else {
            timeTotal = Duration.between(start, end).toMillis();
            long timeDone = Duration.between(start, now).toMillis();
            return (int) (100 - (timeDone * 100 / timeTotal));
        }
    }

    @NotNull
    private static LocalDateTime startOf(StatusDurationEndType statusDurationEndType, @NotNull String awakeStart) {
        LocalDateTime start;
        switch (statusDurationEndType) {
            case END_OF_DAY:
                start = DateUtils.startOfCurrentDay();
                break;
            case END_OF_WEEK:
                start = DateUtils.startOfCurrentWeek();
                break;
            case END_OF_MONTH:
                start = DateUtils.startOfCurrentMonth();
                break;
            case END_OF_YEAR:
                start = DateUtils.startOfCurrentYear();
                break;
            case CUSTOM_DATE:
                start = LocalDateTime.parse(awakeStart, DateUtils.DATE_TIME_FORMATTER);
                break;
            default:
                throw new IllegalStateException("No StatusDurationEndType");
        }
        return start;
    }

    @NotNull
    private static LocalDateTime endOf(StatusDurationEndType statusDurationEndType, @NotNull String awakeEnd) {
        LocalDateTime end;
        switch (statusDurationEndType) {
            case END_OF_DAY:
                end = DateUtils.endOfDay(LocalDateTime.now());
                break;
            case END_OF_WEEK:
                end = DateUtils.endOfCurrentWeek();
                break;
            case END_OF_MONTH:
                end = DateUtils.endOfCurrentMonth();
                break;
            case END_OF_YEAR:
                end = DateUtils.endOfCurrentYear();
                break;
            case CUSTOM_DATE:
                end = LocalDateTime.parse(awakeEnd, DateUtils.DATE_TIME_FORMATTER);
                break;
            default:
                throw new IllegalStateException("No StatusDurationEndType");
        }
        return end;
    }

    @NotNull
    private static String toText(@NotNull Duration duration) {
        return DurationFormatUtils
            .formatDurationWords(duration.abs().toMillis(), true, true)
            .replaceAll("[0-9]{1,2} second(s)?", "")
            .replaceAll(" hour(s)?", "h")
            .replaceAll(" minute(s)?", "m")
            .replaceAll(" day(s)?", "d")
            .trim();
    }

    @NotNull
    private static Duration between(@NotNull LocalDateTime start, @NotNull LocalDateTime end,
                                    @NotNull String awakeStart, @NotNull String awakeEnd) {
        int awakeStartHour = Integer.parseInt(awakeStart.substring(0, 2));
        int awakeStartMin = Integer.parseInt(awakeStart.substring(3, 5));
        int awakeEndHour = Integer.parseInt(awakeEnd.substring(0, 2));
        int awakeEndMin = Integer.parseInt(awakeEnd.substring(3, 5));

        // duration of awake time for a day
        LocalDateTime startAwakeStartTime = start
            .withHour(awakeStartHour)
            .withMinute(awakeStartMin)
            .withSecond(0)
            .withNano(0);
        LocalDateTime startAwakeEndTime = start
            .withHour(awakeEndHour)
            .withMinute(awakeEndMin)
            .withSecond(0)
            .withNano(0);

        // duration of awake time for first day
        Duration firstDayDuration;
        if (start.isBefore(startAwakeEndTime)) {
            firstDayDuration = Duration.between(start, startAwakeEndTime);
            if (start.isBefore(startAwakeStartTime)) {
                firstDayDuration = firstDayDuration.minus(Duration.between(start, startAwakeStartTime));
            }
        } else {
            firstDayDuration = Duration.ZERO;
        }

        // duration of awake time between tomorrow and last day
        Duration tomorrowToEndDuration;
        LocalDateTime tomorrowMorning = endOfDay(start);
        LocalDateTime lastMorning = atZeroHHmm(end.withHour(awakeEndHour));
        if (tomorrowMorning.isBefore(lastMorning)) {
            Duration oneDayAwakeTime = Duration.between(startAwakeStartTime, startAwakeEndTime);
            long nbDays = Duration.between(tomorrowMorning, atZeroHHmm(end).plusDays(1)).toDays();
            tomorrowToEndDuration = oneDayAwakeTime.multipliedBy(nbDays);

            LocalDateTime lastDayAwakeStart = end
                .withHour(awakeStartHour)
                .withMinute(awakeStartMin)
                .withSecond(0)
                .withNano(0);
            LocalDateTime lastDayAwakeEnd = end
                .withHour(awakeEndHour)
                .withMinute(awakeEndMin)
                .withSecond(0)
                .withNano(0);
            if (end.isBefore(lastDayAwakeEnd)) {
                if (end.isBefore(lastDayAwakeStart)) {
                    tomorrowToEndDuration = tomorrowToEndDuration.minus(Duration.between(startAwakeStartTime, startAwakeEndTime));
                } else {
                    tomorrowToEndDuration = tomorrowToEndDuration.minus(Duration.between(end, lastDayAwakeEnd));
                }
            }
        } else {
            tomorrowToEndDuration = Duration.ZERO;
        }

        return firstDayDuration.plus(tomorrowToEndDuration);
    }

    @NotNull
    private static LocalDateTime atZeroHHmm(@NotNull LocalDateTime dateTime) {
        return dateTime
            .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @NotNull
    private static LocalDateTime endOfDay(LocalDateTime day) {
        return atZeroHHmm(day)
            .plusDays(1)
            .minusNanos(1);
    }

    @NotNull
    private static LocalDateTime endOfCurrentWeek() {
        return atZeroHHmm(LocalDateTime.now()
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)))
            .plusDays(1)
            .minusNanos(1);
    }

    @NotNull
    private static LocalDateTime endOfCurrentMonth() {
        return atZeroHHmm(LocalDateTime.now()
            .withDayOfMonth(1))
            .plusMonths(1)
            .minusNanos(1);
    }

    @NotNull
    private static LocalDateTime endOfCurrentYear() {
        return atZeroHHmm(LocalDateTime.now()
            .withDayOfYear(1))
            .plusYears(1)
            .minusNanos(1);
    }

    @NotNull
    private static LocalDateTime startOfCurrentDay() {
        return atZeroHHmm(LocalDateTime.now());
    }

    @NotNull
    private static LocalDateTime startOfCurrentWeek() {
        return atZeroHHmm(LocalDateTime.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
    }

    @NotNull
    private static LocalDateTime startOfCurrentMonth() {
        return atZeroHHmm(LocalDateTime.now()
            .withDayOfMonth(1));
    }

    @NotNull
    private static LocalDateTime startOfCurrentYear() {
        return atZeroHHmm(LocalDateTime.now()
            .withDayOfYear(1));
    }
}
