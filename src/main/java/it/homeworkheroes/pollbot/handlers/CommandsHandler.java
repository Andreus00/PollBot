package it.homeworkheroes.pollbot.handlers;

import it.homeworkheroes.pollbot.commands.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class CommandsHandler implements PollHandler {

    /**
     * Gets the content of the command message and the room controller in order to perform the command.
     * It's important to have the same name in the command and in the class!!!
     */
    @Override
    public void handle(String channelId, String content) {
        if (content.length() < 1)
            return;
        String commandName = "";
        StringTokenizer tokenizer = new StringTokenizer(content);
        try {
            commandName = tokenizer.nextToken();
            commandName = Character.toUpperCase(commandName.charAt(0)) + commandName.toLowerCase(Locale.ROOT).substring(1);
            ((Command) Class.forName("it.homeworkheroes.pollbot.commands." + commandName).getConstructor(String.class, String.class).newInstance(channelId, content.substring(commandName.length()))).run();
        } catch (ClassNotFoundException e) {
            System.err.println("Command for " + content + " not found. Calculated command name = " + commandName);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor for " + content + " not found. Calculated command name = " + commandName + ". Remember to make the constructor public");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Someone tried to create a poll without a text argument.");
        } catch (NumberFormatException e) {
            System.err.println("Numeric arguments are incorrect.");
        } catch (NoSuchElementException e) {
            System.err.println("Arguments are incorrect.");
        }
    }
}
