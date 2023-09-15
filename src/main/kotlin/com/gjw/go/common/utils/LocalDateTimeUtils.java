package com.gjw.go.common.utils;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeUtils extends cn.hutool.core.date.LocalDateTimeUtil {
    /**
     * 计算两个日期之间的天数, 包含开始日期, 不包含结束日期, 计算年月日
     */
    public static long betweenPeriodDays(@NotNull LocalDate startDateInclude, @NotNull LocalDate endDateExclude) {
        return ChronoUnit.DAYS.between(startDateInclude, endDateExclude);
    }

    /**
     * 计算第二个日期的所在周是第一个日期的的所在周之后的第几周
     * 日期在对应周中第几天对结果无影响
     * 如果第二个日期的所在周和第一个日期的的所在周是同一周, 返回 0
     */
    public static long getWeeksAfter(@NotNull LocalDate startDateInclude, @NotNull LocalDate endDateInclude) {
        return ChronoUnit.WEEKS.between(startDateInclude.with(DayOfWeek.MONDAY), endDateInclude.with(DayOfWeek.MONDAY));
    }

    /**
     * 计算第二个日期的所在月是第一个日期的的所在月之后的第几月
     * 日期在对应月中第几天对结果无影响
     * 如果第二个日期的所在月和第一个日期的的所在月是同一月, 返回 0
     */
    public static long getMonthsAfter(@NotNull LocalDate startDateInclude, @NotNull LocalDate endDateInclude) {
        return ChronoUnit.MONTHS.between(YearMonth.from(startDateInclude), YearMonth.from(endDateInclude));
    }

    public static int getDayOfWeek(@NotNull LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public static int getDayOfMonth(@NotNull LocalDate date) {
        return date.getDayOfMonth();
    }

    public static boolean isMonthEnd(@NotNull LocalDate date) {
        return date.getDayOfMonth() == date.lengthOfMonth();
    }
}
