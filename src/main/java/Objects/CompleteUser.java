package Objects;

public class CompleteUser {

	private int userID;
	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private String password;
	
	public CompleteUser(int userID, String firstName, String lastName, String userName, String email, String password) {
		super();
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserComplete [userID=" + userID + ", firstName=" + firstName + ", lastName=" + lastName + ", userName="
				+ userName + ", email=" + email + ", password=" + password + "]";
	}
	
}