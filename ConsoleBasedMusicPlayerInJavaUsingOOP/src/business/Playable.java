package business;

public interface Playable {

	void play(String title);
	void play(int songId) ;
	void pause(String title) ;
	 void pause(int songId) ;
	void stop(String title) ;
	 void stop(int songId);
}
