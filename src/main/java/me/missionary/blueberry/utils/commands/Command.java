package me.missionary.blueberry.utils.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command Framework - Command <br>
 * The command annotation used to designate methods as commands. All methods
 * should have a single CommandArgs commands
 *
 * @author minnymin3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * The name of the command. If it is a sub command then its values would be
     * separated by periods. ie. a command that would be a subcommand of test
     * would be 'test.subcommandname'
     *
     * @return name
     */
    String name();

    /**
     * Gets the required permission of the command
     *
     * @return permission
     */
    String permission() default "";

    /**
     * The message sent to the player when they do not have permission to
     * execute it
     *
     * @return
     */
    String noPerm() default "You do not have permission to perform that action";

    /**
     * A list of alternate names that the command is executed under. See
     * name() for details on how names work
     *
     * @return aliases
     */
    String[] aliases() default {};

    /**
     * The description that will appear in /help of the command
     *
     * @return description
     */
    String description() default "";

    /**
     * The usage that will appear in /help (commandname)
     *
     * @return usage
     */
    String usage() default "";

    /**
     * Whether or not the command is available to players only
     *
     * @return is in game command only
     */
    boolean inGameOnly() default false;

    /**
     * Weather or not the command is executable by the console only
     *
     * @return boolean value of the state of the command, default false
     */
    boolean isConsoleOnly() default false;
}
