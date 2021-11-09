package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.StringTokenizer;

public class Close extends PollCommand{

    public Close(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        StringTokenizer tokenizer = new StringTokenizer(content);
        int i = 0;
        String pollId = "";
        while (tokenizer.hasMoreTokens() && i++ < 1) {
            pollId = tokenizer.nextToken();
        }
        Poll p = PollBotAPP.getPollList().getOrDefault(Long.valueOf(pollId), null);
        if(p != null){
            p.closeVotation();
            p.addOption(new Poll.Option("VOTATION CLOSED"));
            p.addOption(new Poll.Option("WON: " + p.getOptionList().stream().max(Poll.Option::compareTo).get().getVotes()));

            p.update();
        }
    }

    @Override
    public String getName() {
        return "Close";
    }
}
