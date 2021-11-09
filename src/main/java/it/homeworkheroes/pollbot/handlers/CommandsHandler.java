package it.homeworkheroes.pollbot.handlers;

import it.homeworkheroes.pollbot.commands.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.StringTokenizer;

public class CommandsHandler implements PollHandler {

    /**
     * Gets the content of the command message and the room controller in order to perform the command.
     * It's important to have the same name in the command and in the class!!!
     */
    @Override
    public void handle(String channelId, String messageId, String content) {
        if (content.length() < 1)
            return;

        String commandName = Character.toUpperCase(content.charAt(0)) + content.substring(1, content.indexOf(' ')).toLowerCase(Locale.ROOT);
        try {
            ((Command) Class.forName("it.homeworkheroes.pomobot.commands." + commandName).getConstructor(String.class).newInstance(channelId, messageId, content.substring(commandName.length()))).run();
        } catch (ClassNotFoundException e) {
            System.err.println("Command for " + content + " not found. Calculated command name = " + commandName);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor for " + content + "not found. Calculated command name = " + commandName + ". Remember not to make the constructor private");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
