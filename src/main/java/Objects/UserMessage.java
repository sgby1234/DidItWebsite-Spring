package Objects;

import java.time.LocalDate;

public class UserMessage extends Message {

	private Long userId;
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public UserMessage() {
		super();
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public UserMessage(Long messageId, Long userId, String userName, String messageText, LocalDate messageDate) {
		super(messageId, messageText, messageDate);
		this.userId = userId;
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "UserMessage [userId=" + userId + ", userName=" + userName + "]" + super.toString();
	}

}
