package logic.controller.application;
import logic.bean.LoginBean;
import logic.bean.RegistrationBean;
import logic.bean.UserBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import logic.DAO.UserDAO;
import logic.entity.User;
import logic.support.connection.ConnectionData;
import logic.support.connection.ConnectionServer;
import logic.support.connection.SessionHandler;
import logic.support.other.MailBox;


public class LoginController {
	
	public Boolean register(RegistrationBean newUserData) {
		
		//check if exists another user with the same username or email
		
		if(false)
			return false;
		
		UserDAO userDB = new UserDAO();
		User newUser = new User();
		newUser.setUsername(newUserData.getUsername());
		newUser.setName(newUserData.getName());
		newUser.setSurname(newUserData.getLastName());
		newUser.setEmail(newUserData.getEmail());
		newUser.setPassword(newUserData.getPassword());
		
		//userDB.registerUser(newUser);
		userDB.insertUser(newUser);
		
		return true;
		
	}

	public Boolean login(LoginBean userData) {
		
		UserDAO userDB = new UserDAO();
		User loggedUser = userDB.selectUser(userData.getUserID(), userData.getPassword());
		
		if(loggedUser == null) {
			return false;
		}
		return true;
	}
	
	
	public UserBean getUserByLoginData(LoginBean loginData) {
		
		//TODO Considerare se cambiare il parametro in un tipo LoginBean
		var bean = new UserBean();
		UserDAO userDAO = new UserDAO();
		
		User loggedUser = userDAO.selectUser(loginData.getUserID(), loginData.getPassword());
		
		bean.setEmail(loggedUser.getEmail());
		bean.setName(loggedUser.getName());
		bean.setLastName(loggedUser.getSurname());
		
		bean.setUserID(loggedUser.getUsername());
		
		return bean;
	}

	public MailBox connect(LoginBean userData){

		var mailBox = new MailBox();
		ConnectionData myServerData = new ConnectionServer(mailBox).getConnectionData();
		SessionHandler session = new SessionHandler();
		
		if(session.startSession(userData.getUserID(), myServerData.getIP() , myServerData.getPort()))
			return mailBox;
		return null;
	}
	
	
	
}
