package Objects;

public class UserId {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserId(int id) {
		this.id = id;
	}
	public UserId()
	{
		
	}

	@Override
	public String toString() {
		return "UserID [id=" + id + "]";
	}
	

}
