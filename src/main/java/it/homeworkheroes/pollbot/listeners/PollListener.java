package it.homeworkheroes.pollbot.listeners;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.classes.Poll;
import it.homeworkheroes.pollbot.handlers.CommandsHandler;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

        PollBotAPP.getPba().getLogger().info("Bot is ready");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay();
        String channelId = event.getChannel().getId();

        if(content.startsWith(KEYWORD)) {
            String messageId = event.getMessageId();
            event.getChannel().deleteMessageById(messageId).queue();
            String pollText = content.substring(KEYWORD.length()).strip();
            PollBotAPP.getPba().getLogger().info("Command received in channel " + event.getChannel().getName() + "(" + channelId + ") by " + event.getAuthor().getName() + "(" + event.getAuthor().getId() + ") with content " + content);

            commandsHandler.handle(channelId, event.getAuthor().getName(), pollText);
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Poll p = PollBotAPP.getPollFromMessageId(event.getMessageId());
        if (p != null && !
                p.addVote(event.getReactionEmote().getName())) {
            String msg = String.format(Locale.ROOT, "<@%s> you must use a valid reaction", event.getUserId());
            event.getChannel().sendMessage(msg).queue();
            // TODO remove emoji
            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().isEmote() ?
                    event.getReactionEmote().getEmote().toString() :
                    event.getReactionEmote().getEmoji()).queue();

            PollBotAPP.getPba().getLogger().warning("Invalid emoji added in " + event.getTextChannel().getName() + "(" + event.getTextChannel().getId() + ") by " + event.getUser().getName() + "(" + event.getUserId() + ") in poll " + event.getMessageId() + "\n");
        }
        else {
            PollBotAPP.getPba().getLogger().info("Emoji added in " + event.getTextChannel().getName() + "(" + event.getTextChannel().getId() + ") by " + event.getUser().getName() + "(" + event.getUserId() + ") in poll " + event.getMessageId() + "\n");
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        Poll p = PollBotAPP.getPollFromMessageId(event.getMessageId());
        if (p != null){
            p.removeVote(event.getReactionEmote().getName());
            PollBotAPP.getPba().getLogger().info("Emoji removed in " + event.getTextChannel().getName() + "(" + event.getTextChannel().getId() + ") by " + event.getUser().getName() + "(" + event.getUserId() + ") in poll " + event.getMessageId() + "\n");
        }
    }

}
