package it.homeworkheroes.pollbot.listeners;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.EMONUMBER;
import it.homeworkheroes.pollbot.classes.Poll;
import it.homeworkheroes.pollbot.handlers.CommandsHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmoteEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.requests.Route;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

public class PollListener extends ListenerAdapter {

    private PollBotAPP pba;
    private CommandsHandler commandsHandler;
    private final static String KEYWORD = "!poll";

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        pba = PollBotAPP.getPba(); //This method gets the already initialized pba instance (since it uses the singleton pattern).
        pba.setJDA(e.getJDA());
        commandsHandler = new CommandsHandler();

        System.out.println("BOT is ready!");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay();
        String channelId = event.getChannel().getId();

        if(content.startsWith(KEYWORD)) {
            String messageId = event.getMessageId();
            event.getChannel().deleteMessageById(messageId);
            String pollText = content.substring(KEYWORD.length() + 1);

            commandsHandler.handle(channelId, pollText);
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(!PollBotAPP.getPollFromMessageId(event.getMessageId())
                     .addVote(event.getReactionEmote().getName())){
            String msg = String.format(Locale.ROOT, "<@%s> you must use a valid reaction", event.getUserId());
            event.getChannel().sendMessage(msg).queue();
            // TODO remove emoji
//           event.getChannel().removeReactionById().queue();
            event.getTextChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji()).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        PollBotAPP.getPollFromMessageId(event.getMessageId()).removeVote(event.getReactionEmote().getName());
    }

}
