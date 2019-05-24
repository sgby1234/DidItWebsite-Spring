package Objects;

public class UserFriend {

	private int userID;
	private String userName;


	public UserFriend(int userID, String userName) {
		super();
		this.userID = userID;
		this.userName = userName;
	}
	
	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	@Override
	public String toString() {
		return "UserFriend [userID=" + userID + ", userName=" + userName + "]";
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public UserFriend() {
		// TODO Auto-generated constructor stub
	}

}
