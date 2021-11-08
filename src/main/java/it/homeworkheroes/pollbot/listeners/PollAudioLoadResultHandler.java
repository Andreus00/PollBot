package it.homeworkheroes.pollbot.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PollAudioLoadResultHandler implements AudioLoadResultHandler {

    private TrackScheduler trackScheduler;

    public PollAudioLoadResultHandler(TrackScheduler tS){
        this.trackScheduler = tS;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        trackScheduler.queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            trackScheduler.queue(track);
        }
    }

    @Override
    public void noMatches() {
        // Notify the user that we've got nothing
        System.out.println("No Matches");
    }

    @Override
    public void loadFailed(FriendlyException throwable) {
        // Notify the user that everything exploded
        System.out.println("Load Failed");
    }
}
