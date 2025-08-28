package pojo;
public class Song {
    private static int counter = 0;
   private int songId=0;
    private String title, artist;
    private Double duration;
    public Song(String title, String artist, Double duration) {
        this.songId = ++counter;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }
    public int getSongId() {
        return songId;
    }
    public void setSongId(int songId) {
        this.songId = songId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Song other = (Song) obj;
        return this.songId == other.songId;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(songId);
    }
}
 
 