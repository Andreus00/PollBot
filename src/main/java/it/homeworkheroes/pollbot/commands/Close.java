package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.StringTokenizer;
import java.util.stream.Collectors;

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
        if (o == null) return;
        p.close(EMONUMBER.values()[p.getOptionList().indexOf(o)].getEmoji());
    }

    @Override
    public String getName() {
        return "Close";
    }
}
