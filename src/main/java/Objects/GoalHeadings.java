package Objects;

import java.time.LocalDate;

public class GoalHeadings {
	private int goalID;
	private String description;
	public GoalHeadings(int goalID, String description) {
		this.goalID = goalID;
		this.description = description;
	}
	public int getGoalID() {
		return goalID;
	}
	public void setGoalID(int goalID) {
		this.goalID = goalID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
