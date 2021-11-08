package it.homeworkheroes.pollbot.apps;

import it.homeworkheroes.pollbot.classes.Poll;
import it.homeworkheroes.pollbot.listeners.PollListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * main pomobot class
 */
public class PollBotAPP {

    private static PollBotAPP pba;

    private static JDA jda;

    private static HashMap<Long, Poll> pollList;

    private static long poll_number;

    /**
     * Private constructor needed to make use of the singleton pattern.
     */
    private PollBotAPP() {
        this.pollList = new HashMap<>();
    }

    public static synchronized long getNewId() {
        return poll_number++;
    }

    public static synchronized void addPoll(Poll p) {
        pollList.put(p.getId(), p);
    }
    public static synchronized void removePoll(long id) {
        pollList.remove(id);
    }
    public static synchronized void removePoll(Poll p) {
        removePoll(p.getId());
    }

    /**
     * Method which initializes a TBA instance by following the singleton pattern.
     * It saves the created instance into a field, and then returns it.
     * @return The created or pre-existing instance of tba.
     */
    public static PollBotAPP getPba() {
        if (pba == null)
            pba = new PollBotAPP();
        return pba;
    }

    /**
     * Method which firsts initializes and then builds the JBA bot with its status and listeners, and then calls a method
     * @throws LoginException
     */
    public void run() throws LoginException {
        String token = "";
        File file=new File("token/token");
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            token = br.readLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        JDABuilder builder = JDABuilder.createDefault(token);  // TODO: token
        builder.setActivity(Activity.watching("Poll Poll Poll"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.addEventListeners(new PollListener());
        builder.build();
    }

    public static JDA getJDA() {
        return PollBotAPP.jda;
    }

    public static void setJDA(JDA jda) {
        PollBotAPP.jda = jda;
    }
}
