package hello;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import Objects.CompleteUser;
import Objects.Goal;
import Objects.GoalHeadings;
import Objects.UserFriend;
import database.MultipleRecordsReturnedException;

@Component
public class Dao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return User Complete object, returns null if that email and password were not found in database
	 */
	public CompleteUser Login(String email, String password) {		
		CompleteUser user;
		try {
			user = queryForOneOrZero("SELECT * FROM USER WHERE UserEmail = ? and UserPassword = ?", (rs, rn) -> new CompleteUser (rs.getInt("UserID"), rs.getString("UserFirstName"), rs.getString("UserLastName"), rs.getString("UserName"),
					rs.getString("UserEmail"), rs.getString("UserPassword")) ,email, password);
			System.out.println("In the login method: The user returned is " + user);
			return user;
		} 
		catch (MultipleRecordsReturnedException e) {
			System.out.println("More than one record retuned");
		}
			return null;
				
	}
	
	public boolean Signup(String firstName, String lastName, String userName, String password, String email) {
		int rowsEffected;
		
			System.out.println("FirstName : " + firstName);
			System.out.println("LastName : " + lastName);
			

			String sql = "INSERT INTO `user` (`UserFirstName`, `UserLastName`, `UserEmail`, `UserName`, `UserPassword`) VALUES ( ?, ?, ?, ?, ?)";
			rowsEffected = jdbcTemplate.update(sql, firstName, lastName, email, userName, password);
			if(rowsEffected == 1)
			{
				System.out.println("It updated correctly");
				return true;
			}
			else {return false;}
		
	}
	
	public List<GoalHeadings> getUsersGoals(int userID)
	{
		List<GoalHeadings> goalHeadings = new ArrayList<GoalHeadings>();
		goalHeadings.addAll(jdbcTemplate.query("SELECT `GoalId`, `GoalName`  FROM `goal` WHERE `UserId` = ?",
				(rs, rn) -> new GoalHeadings(rs.getInt("GoalId"), rs.getString("GoalName")), userID ));
		return goalHeadings;
				
		
	}
	
	public Goal getGoal(int userId, int goalId)
	{
		String sql = "SELECT * FROM GOAL WHERE GoalId = ? AND UserId = ?";
		
		try {
			Goal goal = queryForOneOrZero(sql,
					(rs, rn) -> { 
				Date sqlDate= rs.getDate("GoalBeginDate");
				System.out.println("Will attempt to map the data to a localdate");
				
				LocalDate date = sqlDate.toLocalDate();
				return new Goal(rs.getInt("GoalId"), rs.getInt("UserId"), rs.getInt("GoalLength"),
					rs.getInt("GoalAmount"), rs.getBoolean("GoalPublic"), date, rs.getString("GoalName"));}, goalId, userId);
			
			if(goal == null) {
				System.out.println("The Goal is null");
			}
			return goal;
		} catch (MultipleRecordsReturnedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Wrapper method for JdbcTemplate query, will return one object or null if no records were returned
	 * @param sql
	 * @param rowMapper
	 * @param arg
	 * @return
	 * @throws MultipleRecordsReturnedException If more than one row was returned, throws exception
	 */
	public <T> T queryForOneOrZero(String sql, RowMapper<T> rowMapper,  Object... arg) throws MultipleRecordsReturnedException{
		List<T> list = jdbcTemplate.query(sql, arg, rowMapper);
		if(list.size() > 1){
			throw new MultipleRecordsReturnedException("More than one row has been returned");
		}
		else if(list.size() == 0){
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	public String getFirstName(String email) throws MultipleRecordsReturnedException
	{
		String name = queryForOneOrZero("SELECT UserFirstName FROM user WHERE UserEmail = ?", (rs, rn) -> new String(rs.getString("UserFirstName")), email);
		return name;
	}

	public Boolean updateAmountAccomplished(int userID, String goalID, int newAmount) {
		// TODO Auto-generated method stub
		int rowsEffected = jdbcTemplate.update("UPDATE GOAL SET GOALAMOUNT = ? WHERE USERID = ? AND GOALID = ?", newAmount, userID, goalID );
		System.out.printf("Ran update: %d rows returned\n", rowsEffected);
		if(rowsEffected == 1) {
			return true;
		}
		
		return false;
	}
	
	
	public Boolean addGoal(int userID, String goalName, int goalLength, boolean isPublic, LocalDate localDate) {

		Boolean response = false;
	
		String sql = "INSERT INTO `goal` (`GoalId`, `UserId`, `GoalName`, `GoalLength`, `GoalBeginDate`, `GoalPublic`, `GoalAmount`) VALUES (NULL, ?, ?, ?, ?, ?, ?)";
		int rowsEffected = jdbcTemplate.update(sql, userID, goalName, goalLength, java.sql.Date.valueOf( localDate ), isPublic, 0);
	    response = rowsEffected == 1 ?  true: false;
					
		return response;
	
	}
	
	public ArrayList<UserFriend> getPossibleFriends(int userID)
	{
		String sql = "SELECT UserID, UserName FROM `USER` WHERE UserId NOT IN (SELECT FRIENDTWO FROM FRIENDS WHERE FRIENDONE = ? ) AND USERID != ?";
		ArrayList<UserFriend> possibleFriends = new ArrayList<UserFriend>();
		possibleFriends.addAll(jdbcTemplate.query(sql, 
				(rs, rn) -> new UserFriend(rs.getInt("userID"), rs.getString("UserName")),
				userID, userID));
		
		return possibleFriends;
	}
	
	public boolean addFriendship(int friendOneID, int friendTwoID)
	{
		System.out.println("Will try to insert friendship with values: " +  friendOneID + ", " + friendTwoID);
		Boolean response  = false;
		String sql = "INSERT INTO `friends`(`friendOne`, `friendTwo`) VALUES (?, ?)";
		int rowsEffected = jdbcTemplate.update(sql, friendOneID, friendTwoID);
		response = rowsEffected == 1 ?  true: false;
		
		return response;
	}
}
	
