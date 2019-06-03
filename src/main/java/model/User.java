package model;
import java.util.List;
public class User {
	private long chatID;
	private String name;
	private List<Note> schedule;
	public User(long chatID, String name, List<Note> schedule) {
		this.chatID = chatID;
		this.name = name;
		this.schedule = schedule;
	}
	
	public long getChatID() {
		return chatID;
	}
	public String getName() {
		return name;
	}
	public List<Note> getSchedule() {
		return schedule;
	}
}

