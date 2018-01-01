package me.missionary.blueberry.scoreboard.timer;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateTimeFormats {

    public final TimeZone SERVER_TIME_ZONE;
    public final ZoneId SERVER_ZONE_ID;
    public final FastDateFormat MTH_DAY_YEAR_TIME_AMPM;
    public final FastDateFormat DAY_MTH_HR_MIN_SECS;
    public final FastDateFormat DAY_MTH_YR_HR_MIN_AMPM;
    public final FastDateFormat DAY_MTH_HR_MIN_AMPM;
    public final FastDateFormat HR_MIN_AMPM;
    public final FastDateFormat HR_MIN_AMPM_TIMEZONE;
    public final FastDateFormat HR_MIN;
    public final FastDateFormat MIN_SECS;
    public final FastDateFormat KOTH_FORMAT;
    public final FastDateFormat MTH_DAY_HR_MIN_AMPM;
    public final ThreadLocal<DecimalFormat> REMAINING_SECONDS;
    public final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING;

    static {
        SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
        SERVER_ZONE_ID = DateTimeFormats.SERVER_TIME_ZONE.toZoneId();
        MTH_DAY_YEAR_TIME_AMPM = FastDateFormat.getInstance("MM/dd/yy h:mm a", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        DAY_MTH_HR_MIN_SECS = FastDateFormat.getInstance("dd/MM HH:mm:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        DAY_MTH_YR_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM/yy hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        DAY_MTH_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        HR_MIN_AMPM = FastDateFormat.getInstance("hh:mma", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        HR_MIN_AMPM_TIMEZONE = FastDateFormat.getInstance("hh:mma z", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        HR_MIN = FastDateFormat.getInstance("hh:mm", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        MIN_SECS = FastDateFormat.getInstance("mm:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        KOTH_FORMAT = FastDateFormat.getInstance("m:ss", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        MTH_DAY_HR_MIN_AMPM = FastDateFormat.getInstance("MM/dd h:mm a", DateTimeFormats.SERVER_TIME_ZONE, Locale.ENGLISH);
        REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
        REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));
    }


    public String getFormattedTimeBasedOnTimeZone(TimeZone timeZone) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(MTH_DAY_HR_MIN_AMPM.getPattern(), timeZone, Locale.ENGLISH);
        return dateFormat.format(System.currentTimeMillis());
    }

    public static String getRemaining(final long duration, final boolean milliseconds, final boolean trail) {
        return milliseconds && duration < TimeUnit.MINUTES.toMillis(1L) ? (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001) + 's' : DurationFormatUtils.formatDuration(duration, ((duration >= TimeUnit.HOURS.toMillis(1L)) ? "HH:" : "") + "mm:ss");
    }
}
