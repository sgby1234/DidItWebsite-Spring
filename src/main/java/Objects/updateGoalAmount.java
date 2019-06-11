package Objects;

public class updateGoalAmount {

	private String goalId;
	private int newAmount;
	
	public updateGoalAmount() {
		
	}

	public updateGoalAmount(String goalId, int newAmount) {
		this.goalId = goalId;
		this.newAmount = newAmount;
	}

	@Override
	public String toString() {
		return "updateGoalAmount [goalId=" + goalId + ", newAmount=" + newAmount + "]";
	}

	public String getGoalId() {
		return goalId;
	}

	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}

	public int getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(int newAmount) {
		this.newAmount = newAmount;
	}

	
}
