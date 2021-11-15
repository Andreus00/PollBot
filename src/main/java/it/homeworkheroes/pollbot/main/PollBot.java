package it.homeworkheroes.pollbot.main;

import it.homeworkheroes.pollbot.apps.PollBotAPP;

import javax.security.auth.login.LoginException;

public class PollBot {

    public static void main(String[] args) throws LoginException {
        PollBotAPP.setArgs(args);
        PollBotAPP.getPba().run();
    }
}
