package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.StringTokenizer;

public class Close extends PollCommand{

    public Close(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        StringTokenizer tokenizer = new StringTokenizer(content);
        String pollId = tokenizer.nextToken();
        Poll p = PollBotAPP.getPollList().getOrDefault(Long.valueOf(pollId), null);

        if (!checkChannel(p)) return;
        Poll.Option o = p.getOptionList().stream().max(Poll.Option::compareTo).orElse(null);

        if (o != null) { // poll is being closed
            p.close(EMONUMBER.values()[p.getOptionList().indexOf(o)].getEmoji());
        }
        else { // poll is being REMOVED because it hasn't any added option
            p.close();
        }
    }

    @Override
    public String getName() {
        return "Close";
    }
}
