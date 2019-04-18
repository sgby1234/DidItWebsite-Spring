package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import Objects.CompleteUser;
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
}
