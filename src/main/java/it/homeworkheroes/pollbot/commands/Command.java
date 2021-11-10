package it.homeworkheroes.pollbot.commands;

/**
 * A command has to implement a run() method
 */
public interface Command {
    void run();
    String getName();
    String getUsage();
}
