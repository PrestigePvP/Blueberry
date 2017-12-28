package me.missionary.blueberry.scoreboard.timer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.missionary.blueberry.scoreboard.board.Board;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;

/**
 * Created by Missionary (missionarymc@gmail.com)0 on 6/11/2017.
 */
public class Timer {

    @Getter
    private final Board board;
    @Getter
    private final String name;
    @Getter
    private final double duration;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private long end;

    public Timer(Board board, String name, double duration) {
        this.board = board;
        this.name = name;
        this.duration = duration;
        this.end = (long) (System.currentTimeMillis() + (duration * 1000));
        board.getTimers().add(this);
    }

    public String getFormattedString() {
        return DateTimeFormats.MIN_SECS.format(end - System.currentTimeMillis());
    }

    public void setNewEnd(long duration) {
        setEnd(System.currentTimeMillis() + (duration * 1000));
    }

    public void cancel() {
        board.getTimers().remove(this);
    }
}
