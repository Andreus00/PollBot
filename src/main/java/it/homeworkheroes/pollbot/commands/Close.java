package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.ArrayList;
import java.util.List;
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

        if(o == null) p.close();

        int max_votes = o.getVotes();

        List<Poll.Option> winners = p.getOptionList().stream().filter(x -> x.getVotes() == max_votes).collect(Collectors.toList());

        p.close(winners.stream().map(x -> EMONUMBER.values()[p.getOptionList().indexOf(x)].getEmoji()).collect(Collectors.toList())); //EMONUMBER.values()[p.getOptionList().indexOf(o)].getEmoji()

    }

    @Override
    public String getName() {
        return "Close";
    }
}
