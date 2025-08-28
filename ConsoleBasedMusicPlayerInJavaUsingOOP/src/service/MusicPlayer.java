package service;

import pojo.Song;
import java.util.*;
public class MusicPlayer {
    private List<Song> allSongs;
    private List<Playlist> playlists;
    public MusicPlayer(List<Song> allSongs) {
        this.allSongs = allSongs;
        this.playlists=new ArrayList<>();
    }
    public MusicPlayer() {
        this.allSongs = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public List<Song> getAllSongs() {
        return allSongs;
    }
    public List<Playlist> getPlaylists() {
        return playlists;
    }
    public void addSong(Song s){
        if(allSongs.stream().anyMatch(cp->cp.getTitle().equals(s.getTitle()))){
            System.out.println(s.getTitle()+ " song already available");
        }else {
            allSongs.add(s);
            System.out.println(s.getTitle() + " is added to allSongs");
        }
    }
    public void updateSong(String title, Song updatedSong){
        for (int i = 0; i < allSongs.size(); i++) {
            Song currentSong = allSongs.get(i);
            if (currentSong.getTitle().equals(title)) {
                System.out.println("Before update: " + currentSong);
                // Keep the same songId; update other fields
                currentSong.setTitle(updatedSong.getTitle());
                currentSong.setArtist(updatedSong.getArtist());
                currentSong.setDuration(updatedSong.getDuration());
                System.out.println("After update: " + currentSong);
                return;
            }
        }
        songNotAvailableWithTitle(title);
    }
    public void deleteSong(String title){
        try{
            Song o = getSongBySongTitle(title);
            allSongs.remove(o);
            System.out.println(o.getTitle() + " is removed");
        }catch (NoSuchElementException e){
            songNotAvailableWithTitle(title);
        }
    }
    public void displayAllSongs(){
        if(allSongs.isEmpty()){
            System.out.println("song list is empty, pleas add songs first");
        }
        else {
            allSongs.stream().forEach(s-> System.out.println(s));
        }
    }
    public void createPlaylist(String plName){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            System.out.println(plName+" already available try diff name");
        }else {
            playlists.add(new Playlist(plName));
        }
    }
    public void createPlaylist(String plName,List<Song> sL){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            System.out.println(plName+" already available try diff name");
        }else {
            playlists.add(new Playlist(plName,sL));
        }
    }
    public void addSongToPlaylist(String plName, Song s){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            Playlist pl=playlists.stream().filter(i->i.getPlaylistName().equals(plName)).findFirst().get();
            pl.addSong(s);
            System.out.println(s.getTitle()+" song added to "+ pl.getPlaylistName());
        }else {
            System.out.println(plName+" not available");
        }
    }
    public void deletePlaylist(String plName){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            Playlist pl=playlists.stream().filter(i->i.getPlaylistName().equals(plName)).findFirst().get();
            playlists.remove(pl);
            System.out.println(plName+" is deleted");
        }else {
            System.out.println(plName+" not available");
        }
    }
    public void playPlaylist(String plName){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            Playlist pl=playlists.stream().filter(i->i.getPlaylistName().equals(plName)).findFirst().get();
            Song song = pl.getSongList().get(0);
            pl.play(song.getTitle());
            System.out.println(song.getTitle()+" is playing from "+pl.getPlaylistName());
        }else {
            System.out.println(plName+" not available");
        }
    }
    public  void getPlaylist(String plName){
        if(playlists.stream().anyMatch(p -> p.getPlaylistName().equals(plName))){
            Playlist pl=playlists.stream().filter(i->i.getPlaylistName().equals(plName)).findFirst().get();
            System.out.println(pl+" having the below songs");
            for (Song s:pl.getSongList()) {
                System.out.println(s);
            }
        }else {
            System.out.println(plName+" not available");
        }
    }
   public void displayAllPlaylists(){
       if(playlists.isEmpty()){
           System.out.println("no play list available , pleas create one");
       }
       else {
           playlists.stream().forEach(s-> System.out.println(s));
       }
   }

    private Song getSongBySongTitle(String title) {
        return allSongs.stream().filter(song -> song.getTitle().equals(title)).findFirst().get();
    }
    private void songNotAvailableWithTitle(String title) {
        System.out.println(title +" song not available");
    }
}
 
 