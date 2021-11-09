package it.homeworkheroes.pollbot.commands;

public abstract class PollCommand implements Command {
    protected String channelId, content;
    PollCommand(String channelId, String content) {
        this.channelId = channelId;
        this.content = content;
    }
}
