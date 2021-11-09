package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.StringTokenizer;

public class Add extends PollCommand {

    public Add(String channelId, String content) {
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
        Poll p = PollBotAPP.getPollList().get(Long.valueOf(pollId));

        if(p == null) return;

        if(!p.addOption(new Poll.Option(content))) {
            PollBotAPP.getJDA().getTextChannelById(channelId).sendMessage(new EmbedBuilder().addField("Warning", "Numero massimo di entry raggiunto", true).build()).queue();
        }
        p.update();
    }

    @Override
    public String getName() {
        return "Add";
    }
}
