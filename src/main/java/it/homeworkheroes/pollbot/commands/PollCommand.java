package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.classes.Poll;

public abstract class PollCommand implements Command {
    protected String channelId, content;
    PollCommand(String channelId, String content) {
        this.channelId = channelId;
        this.content = content;
    }

    protected boolean checkChannel(Poll p) {
        return p != null  && p.getChannelId().equals(channelId);
    }

}
