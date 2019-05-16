package Objects;

import java.time.LocalDate;

public class Goal {
	private int goalID;
	private int userID;
	private int duration; 
	private int accomplishedDays;
	private boolean isPublic;
	private String description;
	private LocalDate dateCreated;

	
	public Goal() {
		
	}
	/**
	 * 
	 * @param goalID The Goals id
	 * @param userID Id of the user with this goal
	 * @param duration number of days/checkboxes for goal
	 * @param accomplishedDays
	 * @param isPublic
	 * @param dateCreated
	 * @param description
	 */
	public Goal(int goalID, int userID, int duration, int accomplishedDays, boolean isPublic, LocalDate dateCreated, String description )
	{
		this.goalID = goalID;
		this.userID = userID;
		this.duration = duration;
		this.accomplishedDays = accomplishedDays;
		this.isPublic = isPublic;
		this.dateCreated = dateCreated;
		this.description = description;
	}
	@Override
	public String toString() {
		return "Goal [goalID=" + goalID + ", userID=" + userID + ", duration=" + duration + ", accomplishedDays="
				+ accomplishedDays + ", isPublic=" + isPublic + ", description=" + description + ", dateCreated="
				+ dateCreated + "]";
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAccomplishedDays() {
		return accomplishedDays;
	}
	public void setAccomplishedDays(int accomplishedDays) {
		this.accomplishedDays = accomplishedDays;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	

}
