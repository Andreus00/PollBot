package it.homeworkheroes.pollbot.classes;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;


public class Poll {
    private String text, channelId, messageId;
    private long pollId;
    private ArrayList<Option> optionList;
    private boolean closed;

    private final static Character PROGRESS_BAR_FULL = '▰';
    private final static Character PROGRESS_BAR_EMPTY = '▱';
    private final static String BLUE_CIRCLE = ":blue_circle:";
    private final static String ORANGE_DIAMOND = ":large_orange_diamond:";


    public Poll(String text, String channelId){
        this.text = text;
        this.optionList = new ArrayList<>();
        this.channelId = channelId;
        this.pollId = PollBotAPP.getNewId();
        this.closed = false;
    }

    public String getText() {
        return text;
    }

    public String getMessageId() {
        return messageId;
    }

    public ArrayList<Option> getOptionList() {
        return optionList;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getId() {
        return pollId;
    }

    public boolean isClosed(){
        return closed;
    }

    public void setOptionList(ArrayList<Option> optionList) {
        this.optionList = optionList;
    }

    synchronized public void addOption(Option o) {
        this.optionList.add(o);
    }

    synchronized public void removeOption(Option o) {
        this.optionList.remove(o);
    }

    private MessageEmbed buildMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for(int i= 0; i < 4; i++) stringBuilder.append(ORANGE_DIAMOND + " " + BLUE_CIRCLE + " ");
        // add the text of the poll
        embedBuilder.addField(stringBuilder.toString(), "**" + text + "**", true).addBlankField(false);

        for(int i = 0; i < Math.min(optionList.size(), 10); i++){
            Option o = optionList.get(i);
            embedBuilder.addField(EMONUMBER.values()[i].toString(), o.toString(), false);
            embedBuilder.addField("Votes: " + Integer.toString(o.getVotes()), "", true);
        }

        embedBuilder.setFooter("poll id " + pollId);
        embedBuilder.setColor(Color.ORANGE);

        return embedBuilder.build();
    }

    public void send() {
        TextChannel textChannel = PollBotAPP.getJDA().getTextChannelById(this.channelId);

        MessageEmbed m = buildMessage();

        textChannel.sendMessage(m).queue(e -> {
            messageId = e.getId();
            PollBotAPP.addMessageIdPollId(getMessageId(), getId());
            System.out.println("Questo è il message id: " + this.messageId);
        });

    }

    public void update() {
        TextChannel textChannel = PollBotAPP.getJDA().getTextChannelById(this.channelId);

        textChannel.editMessageById(messageId, buildMessage()).queue();
    }

    synchronized public boolean addVote(String preference){
        try // prova a vedere se la stringa in input è valida per il voto
        {
            int value = EMONUMBER.getValue(preference);
            if(value < getOptionList().size() && value >= 0) { // se il valore è nel range della lista allora aggiorna il valore
                Option option = getOptionList().get(value);
                option.increment();
                update();
                return true;
            }
        }
        catch (IllegalArgumentException e) { //se lancia eccezione allora è stata passata una stringa obsoleta
            e.printStackTrace();
        }
        return false; //oki vai
    }

    synchronized public void removeVote(String preference){
        try
        {
            int value = EMONUMBER.getValue(preference);
            if(value < getOptionList().size() && value >= 0) { // se il valore è nel range della lista allora aggiorna il valore
                Option option = getOptionList().get(value);
                option.decrement();
                update();
            }
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void closeVotation(){
        closed = true;
    }

    public void openVotation(){
        closed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return pollId == poll.pollId;
    }

    @Override
    public int hashCode() {
        return (int) (pollId ^ (pollId >>> 32));
    }




    static public class Option implements Comparable{

        private static int ID_COUNT = 0;
        private int id, votes;
        private String optionText;

        public Option(String optionText) {
            this.id = getNewId();
            this.optionText = optionText;
        }

        synchronized int getNewId() { return ID_COUNT++;}

        public int getId() {
            return id;
        }

        public int getVotes() {
            return votes;
        }

        public String getOptionText() {
            return optionText;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setVotes(int votes) {
            this.votes = votes;
        }

        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }

        public void increment() {
            this.votes++;
        }

        public void decrement() {
            this.votes--;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Option option = (Option) o;

            return id == option.id;
        }

        @Override
        public int hashCode() {
            return id;
        }



        @Override
        public String toString() {
            return getOptionText();
        }

        @Override
        public int compareTo(@NotNull Object o) {
            if(o instanceof Option){
                Option op = (Option) o;
                return Integer.compare(op.getVotes(), getVotes());
            }
            return 0;
        }
    }
}
