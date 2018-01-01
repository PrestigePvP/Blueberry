package me.missionary.blueberry.scoreboard.timer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.missionary.blueberry.scoreboard.board.Board;

/**
 * Created by Missionary (missionarymc@gmail.com) on 6/11/2017.
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

    public void setNewEnd(long duration) {
        setEnd(System.currentTimeMillis() + (duration * 1000));
    }

    public void removeTimer() {
        board.getTimers().remove(this);
    }
}
