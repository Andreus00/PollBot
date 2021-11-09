package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;

public class Create extends PollCommand {

    public Create(String channelId, String messageId, String content) {
        super(channelId, messageId, content);
    }

    @Override
    public void run() {
        Poll p = new Poll(super.messageId, content, channelId);

        PollBotAPP.addPoll(p);

        p.send();
    }

    @Override
    public String getName() {
        return "Create";
    }
}
