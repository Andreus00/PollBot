package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;

import java.util.StringTokenizer;

public class Remove extends PollCommand{
    public Remove(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        StringTokenizer tokenizer = new StringTokenizer(content);
        String pollId = tokenizer.nextToken();
        Poll p = PollBotAPP.getPollList().get(Long.valueOf(pollId));

        if (!checkChannel(p)) return;

        String optionIndex = tokenizer.nextToken();
        p.removeOption(Math.max(Math.min(Integer.parseInt(optionIndex), optionIndex.length() - 1), 0));
        p.update();
    }

    @Override
    public String getName() {
        return "Remove";
    }
}
