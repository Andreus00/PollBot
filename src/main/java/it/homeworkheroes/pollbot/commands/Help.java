package it.homeworkheroes.pollbot.commands;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Help extends PollCommand{

    public Help(String channelId, String content) {
        super(channelId, content);
    }

    @Override
    public void run() {
        EmbedBuilder helpMsg = new EmbedBuilder();
        helpMsg.setColor(Color.RED);
        helpMsg .setTitle("**POLL HELP**")
                .addField("Create", "Usage: !poll create <text of the poll>", false)
                .addField("Add", "Usage: !poll add <poll id> <option text>", false)
                .addField("Remove", "Usage: !poll remove <poll id> <option index>", false)
                .addField("Close", "Usage: !poll Close <poll id>", false);
        PollBotAPP.getJDA().getTextChannelById(channelId).sendMessage(helpMsg.build()).queue();

    }

    @Override
    public String getName() {
        return "Help";
    }
}
