package it.homeworkheroes.pollbot.classes;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Poll {
    private String text, channelId, messageId;
    /**Unique ID for poll, there is a 1:1 association from existing polls and ID*/
    private long pollId;
    /**List of {@link Option} */
    private ArrayList<Option> optionList;
    // private boolean closed;
    private long votes_sum;

    private final static Character PROGRESS_BAR_FULL = '▰';
    private final static Character PROGRESS_BAR_EMPTY = '▱';
    private final static String BLUE_CIRCLE = ":blue_circle:";
    private final static String ORANGE_DIAMOND = ":large_orange_diamond:";


    public Poll(String text, String channelId){
        this.text = text;
        this.optionList = new ArrayList<>();
        this.channelId = channelId;
        this.pollId = PollBotAPP.getNewId();
    }

    public String getText() {
        return text;
    }

    public String getChannelId(){
        return channelId;
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

    public void setOptionList(ArrayList<Option> optionList) {
        this.optionList = optionList;
    }

    synchronized public boolean addOption(Option o) {
        if(optionList.size() >= PollBotAPP.MAX_ENTRY)
            return false;
        this.optionList.add(o);
        return true;
    }

    synchronized public void removeOption(int index) {
        this.optionList.remove(index);
    }

    synchronized public void removeOption(Option o) {
        this.optionList.remove(o);
    }

    private String buildBar(int votes) {
        final int LINE_NUMBERS = 10;
        StringBuilder stringBuilder = new StringBuilder();
        long full = votes_sum > 0 ? LINE_NUMBERS * votes / votes_sum : 0;
        for (int i = 0; i < LINE_NUMBERS; i++) {
            stringBuilder.append(i < full ? PROGRESS_BAR_FULL : PROGRESS_BAR_EMPTY);
        }
        return stringBuilder.toString();
    }

    private MessageEmbed buildMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for(int i= 0; i < 4; i++) stringBuilder.append(ORANGE_DIAMOND + " " + BLUE_CIRCLE + " ");
        // add the text of the poll
        embedBuilder.addField(stringBuilder.toString(), "**" + text + "**", true).addBlankField(false);

        for(int i = 0; i < Math.min(optionList.size(), PollBotAPP.MAX_ENTRY); i++){
            Option o = optionList.get(i);
            embedBuilder.addField(EMONUMBER.values()[i].toString(), o.toString(), false);
            embedBuilder.addField("Votes: " + Integer.toString(o.getVotes()), "", true);
            embedBuilder.addField("", buildBar(o.getVotes()), true);

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
        });

    }

    public void update() {
        TextChannel textChannel = PollBotAPP.getJDA().getTextChannelById(this.channelId);

        textChannel.editMessageById(messageId, buildMessage()).queue();
    }


    public void close(List<String> winner) {
        TextChannel textChannel = PollBotAPP.getJDA().getTextChannelById(this.channelId);
        MessageEmbed message = buildMessage();
        EmbedBuilder eb = new EmbedBuilder(message);
        eb.addBlankField(false).addField("VOTATION CLOSED", (winner.size() == 1 ? "Winner: " : "Winners: ") + winner.stream().collect(Collectors.joining(" ")), false);
        textChannel.editMessageById(messageId, eb.build()).queue();

        PollBotAPP.removePoll(this);
    }

    public void close(){
        PollBotAPP.removePoll(this);
        PollBotAPP.getJDA().getTextChannelById(channelId).deleteMessageById(messageId).queue();
    }


    synchronized public boolean addVote(String preference){
        try // prova a vedere se la stringa in input è valida per il voto
        {
            int value = EMONUMBER.getValue(preference);
            if(value < getOptionList().size() && value >= 0) { // se il valore è nel range della lista allora aggiorna il valore
                getOptionList().get(value).increment();
                this.votes_sum++;
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
                this.votes_sum--;
                update();
            }
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
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

    /**
     * Option that can be added to {@link Poll}
     * */
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
            if(votes > 0)
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
                return Integer.compare(getVotes(), op.getVotes());
            }
            return 0;
        }
    }
}
