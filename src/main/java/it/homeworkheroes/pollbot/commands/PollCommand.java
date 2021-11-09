package it.homeworkheroes.pollbot.commands;

public abstract class PollCommand implements Command {
    protected String channelId, messageId, content;
    PollCommand(String channelId, String messageId, String content) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.content = content;
    }
}
