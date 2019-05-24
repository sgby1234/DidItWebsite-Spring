package Objects;

public class UserID {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserID(int id) {
		this.id = id;
	}
	public UserID()
	{
		
	}

	@Override
	public String toString() {
		return "UserID [id=" + id + "]";
	}
	

}
