package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.StringTokenizer;

public class Delete extends Close{

    public Delete(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        StringTokenizer tokenizer = new StringTokenizer(content);
        String pollId = tokenizer.nextToken();
        Poll p = PollBotAPP.getPollList().getOrDefault(Long.valueOf(pollId), null);
        p.getOptionList().clear();
        super.run();
    }

    @Override
    public String getName() {
        return "delete";
    }
}
