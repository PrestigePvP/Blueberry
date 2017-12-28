package me.missionary.blueberry.scoreboard;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Missionary on on 6/4/2017.
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class Options {

    //Do we want the score to countdown from 15 -> 1 or do we want the scoreboard to countup from 1 -> 15. Default option is false.
    private boolean countUp;

}
