package it.homeworkheroes.pollbot.handlers;


public interface PollHandler {
    void handle(String channelId, String content);
}
