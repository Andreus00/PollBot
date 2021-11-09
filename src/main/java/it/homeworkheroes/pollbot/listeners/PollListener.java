package it.homeworkheroes.pollbot.listeners;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;
import it.homeworkheroes.pollbot.handlers.CommandsHandler;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

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
        super.onMessageReactionAdd(event);
//        System.out.println(event.getReaction().getMessageId());
        System.out.println(event.getMessageId());
        Poll poll = PollBotAPP.getPollFromMessageId(event.getMessageId());
        System.out.println(event.getReactionEmote().getName().getBytes(StandardCharsets.UTF_8));

    }
}
