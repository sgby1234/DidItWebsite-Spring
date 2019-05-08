package hello;

import org.springframework.web.bind.annotation.RestController;

import Objects.CompleteUser;
import Objects.LoginForm;
import Objects.SignupForm;
import database.MultipleRecordsReturnedException;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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


    /**
     * Login method will return the usersInformatino
     * @throws MultipleRecordsReturnedException 
     */
   
    @RequestMapping(value="/processLogin", method=RequestMethod.POST)
    public ResponseEntity<Boolean> processLogin(@RequestBody LoginForm loginForm){
    	String psw = loginForm.getPassword();
    	String email = loginForm.getEmail();
    	
    	CompleteUser user = dao.Login(email, psw);
    	
    	UUID sessionId = UUID.randomUUID();
    	
    	HttpCookie cookie;
    	
    	if(user != null)
    	{
    		sessions.put(sessionId.toString(), user);
    		cookie = ResponseCookie.from("sessionID", sessionId.toString())
        	        .path("/")
        	        .build();
    	     	   
    	}
    	
    	else {
    		cookie = ResponseCookie.from("loggedIn", "false")
        	        .path("/")
        	        .build();
    	}
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.set(HttpHeaders.SET_COOKIE, cookie.toString());
    	//headers.set("HeaderName", "Headervalue"); - this we had in class. But since we have cookies we should need it
    	return new ResponseEntity<>(true, headers, HttpStatus.ACCEPTED);
    	
    	
    }
    
    
    @PostMapping("/processSignup")
   public boolean processSignup(@RequestBody SignupForm form)
   {
	   System.out.println("process signup is running... Will attempt to sign them up");
	
	   boolean signedUp  = dao.Signup(form.getFirstName(), form.getLastName(), form.getUserName(), form.getPassword(), form.getEmail() );
	   
	   System.out.println("did we successfully sign up? Answer: " +  signedUp);
	   /*if(signedUp)
	   {
		   //send them an email :)
		   
	   }*/
	   return signedUp;
   }

  
}