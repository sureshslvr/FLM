package main;

import pojo.Song;
import service.MusicPlayer;
import java.util.Scanner;
public class MainTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MusicPlayer musicPlayer = new MusicPlayer();
        while(true) {
            System.out.println("\n========= MUSIC PLAYER MENU =========");
            System.out.println("1. Add New Song, 2. Update Existing Song, 3. Delete Song,  4. Display All Songs,\n 5. Create New Playlist,6. Add Song to Playlist,7. Display All Playlists, 8. Display Songs in Playlist,\n 9. Play Song in Playlist, 10. Pause Song in Playlist, 11. Stop Song in Playlist, 12. Exit" );
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            if(choice == 12) {
                System.out.println("Exiting Music Player. Goodbye!");
                break;
            }
            switch(choice) {
                case 1: // Add New Song
                    System.out.print("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter artist name: ");
                    String artist = scanner.nextLine();
                    System.out.print("Enter duration (in minutes): ");
                    Double duration = null;
                    try {
                        duration = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid duration input.");
                        break;
                    }
                    Song newSong = new Song(title, artist, duration);
                    musicPlayer.addSong(newSong);
                    break;
                case 2: // Update Existing Song
                    System.out.print("Enter title of the song to update: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new song title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new artist name: ");
                    String newArtist = scanner.nextLine();
                    System.out.print("Enter new duration: ");
                    Double newDuration = null;
                    try {
                        newDuration = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid duration input.");
                        break;
                    }
                    Song updatedSong = new Song(newTitle, newArtist, newDuration);
                    musicPlayer.updateSong(oldTitle, updatedSong);
                    break;
                case 3: // Delete Song
                    System.out.print("Enter title of the song to delete: ");
                    String delTitle = scanner.nextLine();
                    musicPlayer.deleteSong(delTitle);
                    break;
                case 4: // Display All Songs
                    musicPlayer.displayAllSongs();
                    break;
                case 5: // Create New Playlist
                    System.out.print("Enter playlist name: ");
                    String playlistName = scanner.nextLine();
                    musicPlayer.createPlaylist(playlistName);
                    break;
                case 6: // Add Song to Playlist
                    System.out.print("Enter playlist name: ");
                    String plNameForAdd = scanner.nextLine();
                    System.out.print("Enter title of the song to add: ");
                    String songTitleToAdd = scanner.nextLine();
                    try {
                        Song songToAdd = musicPlayer.getAllSongs().stream()
                                .filter(s -> s.getTitle().equals(songTitleToAdd))
                                .findFirst()
                                .orElse(null);
                        if (songToAdd != null) {
                            musicPlayer.addSongToPlaylist(plNameForAdd, songToAdd);
                        } else {
                            System.out.println("Song not found in library.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error adding song to playlist.");
                    }
                    break;
                case 7: // Display All Playlists
                    musicPlayer.displayAllPlaylists();
                    break;
                case 8: // Display Songs in Playlist
                    System.out.print("Enter playlist name: ");
                    String plNameForDisplay = scanner.nextLine();
                    musicPlayer.getPlaylist(plNameForDisplay);
                    break;
                case 9: // Play Song in Playlist
                    System.out.print("Enter playlist name: ");
                    String plNameForPlay = scanner.nextLine();
                    musicPlayer.playPlaylist(plNameForPlay);
                    break;
                case 10: // Pause Song in Playlist
                    System.out.print("Enter playlist name: ");
                    String plNameForPause = scanner.nextLine();
                    System.out.print("Enter song title to pause: ");
                    String songTitleToPause = scanner.nextLine();
                    try {
                        service.Playlist plPause = musicPlayer.getPlaylists().stream()
                                .filter(p -> p.getPlaylistName().equals(plNameForPause))
                                .findFirst()
                                .orElse(null);
                        if(plPause != null) {
                            plPause.pause(songTitleToPause);
                        } else {
                            System.out.println("Playlist not found.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error pausing song.");
                    }
                    break;
                case 11: // Stop Song in Playlist
                    System.out.print("Enter playlist name: ");
                    String plNameForStop = scanner.nextLine();
                    System.out.print("Enter song title to stop: ");
                    String songTitleToStop = scanner.nextLine();
                    try {
                        service.Playlist plStop = musicPlayer.getPlaylists().stream()
                                .filter(p -> p.getPlaylistName().equals(plNameForStop))
                                .findFirst()
                                .orElse(null);
                        if(plStop != null) {
                            plStop.stop(songTitleToStop);
                        } else {
                            System.out.println("Playlist not found.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error stopping song.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 12.");
            }
        }
        scanner.close();
    }
}
 
 
