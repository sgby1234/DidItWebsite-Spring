package hello;

import org.springframework.web.bind.annotation.RestController;

import Objects.CompleteUser;
import Objects.LoginForm;
import Objects.SignupForm;
import database.MultipleRecordsReturnedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@RestController
public class Controller {

	@Autowired
	Dao dao;
	private static final ConcurrentMap<String, CompleteUser> sessions = new ConcurrentHashMap<>();

	@RequestMapping("/")
	public String retrieveCards() {
		return "Shifra!";
	}

	@RequestMapping("/signup")
	public String index(@PathVariable(name = "id") String id) {
		return "Greetings from Spring Boot! In a. Your id is " + id;
	}

	@RequestMapping("/b")
	public String index() {
		return "Greetings from Spring Boot! In B";
	}

	/**
	 * Login method will return the usersInformatino
	 * 
	 * @throws MultipleRecordsReturnedException
	 */

	@RequestMapping(value = "/processLogin", method = RequestMethod.POST)
	public ResponseEntity<Boolean> processLogin(@RequestBody LoginForm loginForm) {
		String psw = loginForm.getPassword();
		String email = loginForm.getEmail();
		//try to retrieve a user by querying the db, user will == null if the email and password were not found
		CompleteUser user = dao.Login(email, psw);

		UUID sessionId = UUID.randomUUID();
		HttpCookie cookie;

		if (user != null) {
			// add them to the session
			sessions.put(sessionId.toString(), user);
			// create their cookie
			cookie = ResponseCookie.from("sessionID", sessionId.toString()).path("/").build();
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
			// headers.set("HeaderName", "Headervalue"); - this we had in class. But since
			// we have cookies we should need it
			return new ResponseEntity<>(true, headers, HttpStatus.ACCEPTED);

		}

		else {
			// create cookie
			cookie = ResponseCookie.from("loggedIn", "false").path("/").build();
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
			// we have cookies we should need it
			/// return false
			return new ResponseEntity<>(false, headers, HttpStatus.ACCEPTED);
		}

	}

	/**
	 * 
	 * @param form
	 * @return
	 */
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

	/**
	 * Second attempt for logging in, really just an extra to play around with.  I played around with a bunch of different things in this method.  
	 * @param loginForm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/processLogin2")
	public void processLogin2(@RequestBody LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String psw = loginForm.getPassword();
		String email = loginForm.getEmail();

		System.out.println("Attempting to login email " + email +" and password" + psw);
		CompleteUser user = dao.Login(email, psw);

		UUID sessionId = UUID.randomUUID();

		Cookie cookie;

		if (user != null) {
			// add them to the session
			System.out.println("The user is not null, adding a session id for them");
			sessions.put(sessionId.toString(), user);
			// create their cookie
			cookie = new Cookie("sessionID", sessionId.toString());
			System.out.println("The cookies persistence is " + cookie.getMaxAge());
			cookie.setPath("/");
			System.out.println("The cookies path is " + cookie.getPath());
			// headers.set("HeaderName", "Headervalue"); - this we had in class. But since
			// we have cookies we should need it
			response.addCookie(cookie);
			response.getWriter().write("true");

		}

		else {
			// create cookie
			cookie = new Cookie("loggedIn", "false");
			
			response.addCookie(cookie);
			response.getWriter().write("false");
		}

	}
	@PostMapping("/processLogout")
	public void processLogout(@CookieValue(value = "sessionID", defaultValue = "missingCookie") String sessionID) {
		
		if (sessionID.equals("missingCookie")) {
			System.out.println("The Required Cookie was not detected");
		} 
		
		else {
			if (sessions.remove(sessionID) != null) {
				System.out.println("removed it!");
			}
		}
	}
	
	
}