package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;

/**
 * Create an empty poll
 */
public class Create extends PollCommand {

    public Create(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        Poll p = new Poll(content, channelId);

        PollBotAPP.addPoll(p);

        p.send();
        PollBotAPP.getPba().getLogger().info("Poll created in channel " + p.getChannelId() + ". Poll ID: " + p.getId() + " poll message id: " + p.getMessageId());
    }

    @Override
    public String getName() {
        return "Create";
    }
}
