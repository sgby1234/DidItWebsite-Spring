package Objects;

import java.time.LocalDate;

public class Message {

	private Long messageId;
	private String messageText;
	private LocalDate messageDate;
	
	public Message() {
	}
	
	public Message(Long messageId, String messageText, LocalDate messageDate) {
		this.messageId = messageId;
		this.messageText = messageText;
		this.messageDate = messageDate;
	}

	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public LocalDate getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(LocalDate messageDate) {
		this.messageDate = messageDate;
	}
	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", messageText=" + messageText
				+ ", messageDate=" + messageDate + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (messageDate == null) {
			if (other.messageDate != null)
				return false;
		} else if (!messageDate.equals(other.messageDate))
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (messageText == null) {
			if (other.messageText != null)
				return false;
		} else if (!messageText.equals(other.messageText))
			return false;
		
		return true;
	}
	

}
