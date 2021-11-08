package it.homeworkheroes.pollbot.classes;

import it.homeworkheroes.pollbot.apps.PollBotAPP;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;


public class Poll {
    private long pollId;
    private String text;
    private String messageId;
    private ArrayList<Option> optionList;
    private String channelId;


    public Poll(String messageId, String text, String channelId){
        this.pollId = PollBotAPP.getNewId();
        this.messageId = messageId;
        this.text = text;
        this.optionList = new ArrayList<>();
        this.channelId = channelId;
    }

    public long getId() {
        return pollId;
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

    public void setOptionList(ArrayList<Option> optionList) {
        this.optionList = optionList;
    }

    synchronized public void addOption(Option o) {
        this.optionList.add(o);
    }

    synchronized public void removeOption(Option o) {
        this.optionList.remove(o);
    }

    public void send(TextChannel tc) {
        MessageBuilder mb = new MessageBuilder();
        // TODO: mockup del messaggio
        mb.append(text);
        for(Option o : optionList){
            mb.append(o);
        }

        tc.sendMessage(mb.build());
    }

    static public class Option {

        private int id, votes;
        private String optionText;

        public Option(int id, String optionText) {
            this.id = id;
            this.optionText = optionText;
        }

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
    }
}
