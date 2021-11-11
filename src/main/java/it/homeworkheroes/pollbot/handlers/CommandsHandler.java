package it.homeworkheroes.pollbot.handlers;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import it.homeworkheroes.pollbot.commands.Command;
import net.dv8tion.jda.api.entities.TextChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class CommandsHandler implements PollHandler {

    private Logger logger;

    public CommandsHandler(){
        logger = PollBotAPP.getPba().getLogger();
    }

    /**
     * Gets the content of the command message and the room controller in order to perform the command.
     * It's important to have the same name in the command and in the class!!!
     */
    @Override
    public void handle(String channelId, String sender, String content) {
        if (content.length() < 1)
            return;
        String commandName = "";
        StringTokenizer tokenizer = new StringTokenizer(content);
        TextChannel textChannel = PollBotAPP.getJDA().getTextChannelById(channelId);
        try {
            commandName = tokenizer.nextToken();
            commandName = Character.toUpperCase(commandName.charAt(0)) + commandName.toLowerCase(Locale.ROOT).substring(1);
            ((Command) Class.forName("it.homeworkheroes.pollbot.commands." + commandName).getConstructor(String.class, String.class).newInstance(channelId, content.substring(commandName.length()))).run();
            logger.info("Command executed: <" + commandName + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (ClassNotFoundException e) {
            logger.warning("Command not found: <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (NoSuchMethodException e) {
            logger.warning("Command constructor not found for <" + content + ">. Message sent in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (IllegalAccessException e) {
            logger.warning("Illegal access <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (InstantiationException e) {
            logger.warning("Instantiation exceptio <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (InvocationTargetException e) {
            logger.warning("Invocation target exception <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (IndexOutOfBoundsException e) {
            logger.warning("Out of bounds <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (NumberFormatException e) {
            logger.warning("Number format is invalid <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (NoSuchElementException e) {
            logger.warning("Arguments are incorrect <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        } catch (Exception e) {
            logger.warning("Not specific error raised by command <" + content + "> in guild <" + textChannel.getGuild().getName() + "> in channel <" + textChannel.getName() + "> by <" + sender + ">\n");
        }
    }
}
