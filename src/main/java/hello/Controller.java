package hello;

import org.springframework.web.bind.annotation.RestController;

import Objects.CompleteUser;
import Objects.LoginForm;
import Objects.SignupForm;
import Objects.updateGoalAmount;
import Objects.Goal;
import Objects.GoalHeadings;
import database.MultipleRecordsReturnedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//this is for java cookie - if its not used we can delte it
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
public class Controller {

	private final String userIDDefault ="38400000-8cf0-11bd-b23e-10b96e4ef00d" ;; //TO REMOVE
	@Autowired
	Dao dao;
	private static final ConcurrentMap<String, CompleteUser> sessions = new ConcurrentHashMap<>();

	/**
	 * Login method will return the usersInformatino
	 * 
	 * @throws MultipleRecordsReturnedException
	 */

	@RequestMapping(value = "/processLogin", method = RequestMethod.POST)
	public ResponseEntity<Boolean> processLogin(@RequestBody LoginForm loginForm) {
		String psw = loginForm.getPassword();
		String email = loginForm.getEmail();

		CompleteUser user = dao.Login(email, psw);

		//UUID sessionId = UUID.randomUUID();
		UUID sessionId = UUID.fromString(userIDDefault); //TO REMOVE
		
		
		
		HttpCookie cookie;

		if (user != null) {
			System.out.println("The user is :" + user);//TO REMOVE
			sessions.put(sessionId.toString(), user);
			cookie = ResponseCookie.from("sessionID", sessionId.toString()).path("/").build();

			System.out.println("Cookie created: Max age= " + ((ResponseCookie) cookie).getMaxAge());
		}

		else {
			cookie = ResponseCookie.from("loggedIn", "false").path("/").build();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
		// headers.set("HeaderName", "Headervalue"); - this we had in class. But since
		// we have cookies we should need it
		return new ResponseEntity<>(true, headers, HttpStatus.ACCEPTED);

	}
	
	
	@PostMapping("/addGoal")
	public Boolean addGoal( @CookieValue(value = "sessionID", defaultValue = userIDDefault) String sessionID, @RequestBody Goal goal) {	 //TO REMOVE - CHANGE THE DEFAULT)
		//check for cookie
		if(sessionID.equals("NoCookie")) {
			System.out.println("No Cookie detected in /getGoal");
			return false;
		}
		//call DAO to perform insert
		CompleteUser user = sessions.get(sessionID);
		boolean response = dao.addGoal(user.getUserID(), goal.getDescription(), goal.getDuration(), goal.isPublic(), goal.getDateCreated());
		//return
		return response;
		
	}

	@GetMapping("/getGoal")
	public ResponseEntity<Goal> getGoal(@RequestParam("goalID") String goalID,
			@CookieValue(value = "sessionID", defaultValue = userIDDefault) String sessionID) { //TO REMOVE - CHANGE THE DEFAULT
		if (sessionID.equals("NoCookie")) {
			System.out.println("No Cookie detected in /getGoal");
		}

		int userId = sessions.get(sessionID).getUserID();
		Goal g = dao.getGoal(userId, Integer.valueOf(goalID));
		System.out.println("Sending back goal of " + g + " the name is " + g.getDescription());
		return new ResponseEntity(g, HttpStatus.OK);

	}
	
	@PostMapping("/updateAmountAccomplished")
	public Boolean updateAmountAccomplished(@RequestBody updateGoalAmount newInfo, @CookieValue(value = "sessionID", defaultValue = userIDDefault) String sessionID) { //TO REMOVE - CHANGE THE DEFAULT)
	
		if(sessionID.equals("NoCookie")) {
			return false;
		}
		CompleteUser user = sessions.get(sessionID);
		if(user == null) {
			return false;
		}
		
		System.out.println("The Goal Id is " + newInfo.getGoalId() +" The amount is "+ newInfo.getNewAmount());
		Boolean success =  dao.updateAmountAccomplished(user.getUserID(), newInfo.getGoalId(), newInfo.getNewAmount());
		return success;
	}

	@GetMapping("/getAllGoals")
	public ResponseEntity<List<GoalHeadings>> getAllGoals(
			@CookieValue(value = "sessionID", defaultValue = userIDDefault) String sessionID) { //TO REMOVE - CHANGE DEFAULT TO NOCOOKIE
		
		if (sessionID.equals("NoCookie")) {
			System.out.println("No cookie found.");
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		} else {
			// get all goals from database
			List<GoalHeadings> goals = dao.getUsersGoals(sessions.get(sessionID).getUserID());
			for(GoalHeadings g: goals)
			{
				System.out.println(g);
			}
			return new ResponseEntity<>(goals, HttpStatus.OK);
		}

	}
	
	

	@PostMapping("/processSignup")
	public boolean processSignup(@RequestBody SignupForm form) {
		System.out.println("process signup is running... Will attempt to sign them up");

		boolean signedUp = dao.Signup(form.getFirstName(), form.getLastName(), form.getUserName(), form.getPassword(),
				form.getEmail());

		System.out.println("did we successfully sign up? Answer: " + signedUp);
		/*
		 * if(signedUp) { //send them an email :)
		 * 
		 * }
		 */
		return signedUp;
	}

	@PostMapping("/processLogout")
	public void processLogout(@CookieValue(value = "sessionID", defaultValue = "NoCookie") String sessionID) {
		if (sessionID.equals("NoCookie")) {
			System.out.println("No cookie detected");

		} else {
			System.out.println("Cookie detected: removing session id from session map");
			sessions.remove(sessionID);
		}

	}

	

}