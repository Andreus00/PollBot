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
     * Initializes the structures of the PollBotAPP instance and reads the command line arguments.
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

    /**
     * Set the command line arguments
     * @param args arguments
     */
    public static void setArgs(String[] args) {
        PollBotAPP.args = args;
    }

    /**
     * Get a new Id for a poll
     * @return
     */
    public static synchronized long getNewId() {
        return poll_number++;
    }

    /**
     * Add a poll to the poll list
     * @param p a poll
     */
    public static synchronized void addPoll(Poll p) {
        pollList.put(p.getId(), p);
    }

    /**
     * Adds an entry to the map that maps the MessageId to the PollId
     * @param messageId a MessageId (the key of the map)
     * @param id a pollId (the value of the map)
     */
    public static synchronized void addMessageIdPollId(String messageId, long id) {
        getMessageIdPollId().put(messageId, id);
    }

    /**
     * remove a poll from the pollList
     * @param id the id of the poll
     */
    public static synchronized void removePoll(long id) {
        messageIdPollId.remove(pollList.get(id).getMessageId());
        pollList.remove(id);
    }

    /**
     * Remove a poll from the pollList
     * @param p the instance of the poll that has to be removed.
     */
    public static synchronized void removePoll(Poll p) {
        removePoll(p.getId());
    }

    /**
     * get the pollList
     * @return
     */
    public static HashMap<Long, Poll> getPollList() {
        return pollList;
    }

    /**
     * get the logger
     * @return the logger used by te bot
     */
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
     * Method which firsts initializes and then builds the JDA bot with its status and listeners
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

    /**
     * get the instance of JDA
     * @return
     */
    public static JDA getJDA() {
        return PollBotAPP.jda;
    }

    /**
     * Get a poll's id from a message's id
     * @param messageId the id of the message
     * @return the poll associated to the messageId
     */
    public static Poll getPollFromMessageId(String messageId) {
        //return pollList.entrySet().parallelStream().filter(x -> x.getValue().getMessageId().equals(messageId)).findFirst().orElse(null).getValue();
        return pollList.get(messageIdPollId.getOrDefault(messageId, -1l));
    }

    /**
     * get the map that associates the messageId's and the pollId's
     * @return
     */
    public static HashMap<String, Long> getMessageIdPollId() {
        return messageIdPollId;
    }

    /**
     * set the JDA
     * @param jda the JDA
     */
    public static void setJDA(JDA jda) {
        PollBotAPP.jda = jda;
    }
}
