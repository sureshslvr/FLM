package service;

import business.*;
import pojo.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Playlist implements Playable {
    private static int counter = 0;
    private int playlistId=0;
    private String playlistName;
    private List<Song> songList;
    public Playlist(String playlistName, List<Song> songList) {
        this.playlistId = ++counter;
        this.playlistName = playlistName;
        this.songList = songList;
    }
    public Playlist(String playlistName) {
        this.playlistId = ++counter;
        this.playlistName = playlistName;
        this.songList=new ArrayList<>();
    }
    public int getPlaylistId() {
        return playlistId;
    }
    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
    public String getPlaylistName() {
        return playlistName;
    }
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
    public List<Song> getSongList() {
        return songList;
    }
    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
    public void addSong(Song s){
        songList.add(s);
        System.out.println(s.getTitle()+" is added to "+ playlistName);
    }
    public void removeSong(String title) {
        try{
            Song o = getSongBySongTitle(title);
            songList.remove(o);
            System.out.println(o.getTitle() + " is removed from " + playlistName);
        }catch (NoSuchElementException e){
            songNotAvailableWithTitle(title);
        }
    }

    public void removeSong(int id){
        try{
           Song s = getSongBySongID(id);
            songList.remove(s);
            System.out.println(s.getTitle() + " is removed from " + playlistName);
        }catch (NoSuchElementException e){
            songNotAvailableWithID(id);
        }
    }

    public void displaySongs(){
        if(songList.isEmpty()){
            System.out.println(playlistName+" is empty, pleas add song to "+ playlistName+" first");
        }
        else {
            songList.stream().forEach(s-> System.out.println(s));
        }
    }
    @Override
    public void play(String title) {
        try{
            if(songList.contains(getSongBySongTitle(title)))
                System.out.println(title+ "song is playing");
        }
        catch (NoSuchElementException e){
            songNotAvailableWithTitle(title);
        }
    }
    @Override
    public void play(int songId) {
        try {
            Song s = getSongBySongID(songId);
            if (songList.contains(s)) {
                System.out.println(s.getTitle() + "song is playing");
            }
        } catch (NoSuchElementException e){
            songNotAvailableWithID(songId);
        }
    }
    @Override
    public void pause(String title) {
        try {
            if (songList.contains(getSongBySongTitle(title))) {
                System.out.println(title + "song is paused");
            }
        }
        catch (NoSuchElementException e){
            songNotAvailableWithTitle(title);
        }
    }
    @Override
    public void pause(int songId) {
        try {
            Song s = getSongBySongID(songId);
            if (songList.contains(s)) {
                System.out.println(s.getTitle() + "song is paused");
            }
        } catch (NoSuchElementException e){
            songNotAvailableWithID(songId);
        }
    }
    @Override
    public void stop(String title) {
        try{
            if(songList.contains(getSongBySongTitle(title))){
                System.out.println(title+ "song is stopped");
            }
        } catch (NoSuchElementException e){
            songNotAvailableWithTitle(title);
        }
    }

    @Override
    public void stop(int songId) {
        try {
            Song s = getSongBySongID(songId);
            if (songList.contains(s)) {
                System.out.println(s.getTitle() + "song is stopped");
            }
        } catch (NoSuchElementException e){
            songNotAvailableWithID(songId);
        }
    }

    private Song getSongBySongTitle(String title) {
        return songList.stream().filter(song -> song.getTitle().equals(title)).findFirst().get();
    }
    private Song getSongBySongID(int id) {
        return songList.stream().filter(i -> i.getSongId() == id).findFirst().get();
    }
    private void songNotAvailableWithTitle(String title) {
        System.out.println(title +" song not available in "+playlistName);
    }
    private void songNotAvailableWithID(int songId) {
        System.out.println("song with id: "+ songId +" , not available in "+playlistName);
    }
    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", playlistName='" + playlistName + '\'' +
                ", songList=" + songList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return playlistId == playlist.playlistId;
    }
    @Override
    public int hashCode() {
        return playlistId;
    }
}
 
 