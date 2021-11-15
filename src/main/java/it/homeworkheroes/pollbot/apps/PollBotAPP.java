package it.homeworkheroes.pollbot.apps;

import it.homeworkheroes.pollbot.classes.Poll;
import it.homeworkheroes.pollbot.listeners.PollListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * main pomobot class
 */
public class PollBotAPP {

    private static String[] args = null;

    private static PollBotAPP pba;

    private static JDA jda;

    public final static int MAX_ENTRY = 8;

    private static HashMap<Long, Poll> pollList;

    private static HashMap<String, Long> messageIdPollId;

    private static long poll_number;

    private Logger logger;


    /**
     * Private constructor needed to make use of the singleton pattern.
     */
    private PollBotAPP() {
        pollList = new HashMap<>();
        messageIdPollId = new HashMap<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--file")){
                logger = Logger.getLogger("it.homeworkheroes.pollbot.log");
                try {
                    FileHandler fileHandler = new FileHandler("log/pollog.txt", true);

                    fileHandler.setFormatter(new SimpleFormatter());
                    logger.addHandler(fileHandler);
                    logger.log(Level.INFO, "-------------------------------------------------------------------------------------\nStarting...\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(logger == null)
            logger = Logger.getLogger("it.homeworkheroes.pollbot.log");
    }

    public static void setArgs(String[] args) {
        PollBotAPP.args = args;
    }

    public static synchronized long getNewId() {
        return poll_number++;
    }

    public static synchronized void addPoll(Poll p) {
        pollList.put(p.getId(), p);
    }
    public static synchronized void addMessageIdPollId(String messageId, long id) {
        getMessageIdPollId().put(messageId, id);
    }
    public static synchronized void removePoll(long id) {
        messageIdPollId.remove(pollList.get(id).getMessageId());
        pollList.remove(id);
    }
    public static synchronized void removePoll(Poll p) {
        removePoll(p.getId());
    }

    public static HashMap<Long, Poll> getPollList() {
        return pollList;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Method which initializes a PBA instance by following the singleton pattern.
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
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.competing("Poll Poll Poll"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.addEventListeners(new PollListener());
        builder.build();
    }

    public static JDA getJDA() {
        return PollBotAPP.jda;
    }

    public static Poll getPollFromMessageId(String messageId) {
        //return pollList.entrySet().parallelStream().filter(x -> x.getValue().getMessageId().equals(messageId)).findFirst().orElse(null).getValue();
        return pollList.get(messageIdPollId.getOrDefault(messageId, -1l));
    }
    public static HashMap<String, Long> getMessageIdPollId() {
        return messageIdPollId;
    }
    public static void setJDA(JDA jda) {
        PollBotAPP.jda = jda;
    }
}
