package it.homeworkheroes.pollbot.handlers;


public interface PollHandler {
    public void handle(String channelId, String mId, String content);
}
