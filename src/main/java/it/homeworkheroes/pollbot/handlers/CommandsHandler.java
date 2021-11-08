package it.homeworkheroes.pollbot.handlers;

import it.homeworkheroes.pollbot.commands.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class CommandsHandler implements PollHandler {

    /**
     * Gets the content of the command message and the room controller in order to perform the command.
     * It's important to have the same name in the command and in the class!!!
     * @param content content of the message es. "pd!start" or "pd!clear"
     * @param roomController Room controller associated to the TextChannel where the command was send
     */
    @Override
    public void handle(String content) {
        if (content.length() < 5)
            return;
        String commandName = Character.toUpperCase(content.charAt(3)) + content.substring(4).toLowerCase(Locale.ROOT) + "Command";
        try {
            ((Command) Class.forName("it.homeworkheroes.pomobot.commands." + commandName).getConstructor().newInstance()).run();
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
        }
    }
}
