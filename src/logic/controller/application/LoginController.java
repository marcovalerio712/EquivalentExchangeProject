package logic.controller.application;
import logic.bean.LoginBean;

import logic.bean.RegistrationBean;
import logic.bean.UserBean;
import logic.dao.UserDAO;
import logic.dao.UserProfileDAO;
import logic.entity.User;
import logic.entity.UserProfile;
import logic.support.connection.ConnectionData;
import logic.support.connection.ConnectionServer;
import logic.support.connection.SessionHandler;
import logic.support.exception.AlreadyRegisteredUserException;
import logic.support.exception.WrongLoginCredentialsException;
import logic.support.other.MailBox;


public class LoginController {
	
	public void register(RegistrationBean newUserData) throws AlreadyRegisteredUserException {
		
		//check if exists another user with the same username or email
		
		UserDAO userDB = new UserDAO();
		User newUser = new User();
		newUser.setUsername(newUserData.getUsername());
		newUser.setName(newUserData.getName());
		newUser.setSurname(newUserData.getLastName());
		newUser.setEmail(newUserData.getEmail());
		newUser.setPassword(newUserData.getPassword());
		newUser.setBirthDate(newUserData.getBirthDate());
		newUser.getWallet().setCurrentCredit(500);

		
		userDB.insertUser(newUser);
		
	}

	public void login(LoginBean userData) throws WrongLoginCredentialsException {
		
		UserDAO userDB = new UserDAO();
		User loggedUser = userDB.selectLoginUser(userData.getUserID());
		

		if(!loggedUser.getPassword().equals(userData.getPassword())) {
			throw new WrongLoginCredentialsException(2);
		}
			
	}
	
	
	public void logout(UserBean loggedUser) {
		SessionHandler session = new SessionHandler();
		ConnectionServer serverInstance = ConnectionServer.getInstance();
		String ip = serverInstance.getConnectionData().getIP();
		Integer port = serverInstance.getConnectionData().getPort();
		session.endSession(loggedUser.getUserID(), ip, port);
		serverInstance.stopServer();
		ConnectionServer.resetInstance();
	}
	
	
	public UserBean getUserByLoginData(LoginBean loginData) {
		
		var bean = new UserBean();
		UserDAO userDAO = new UserDAO();
		UserProfileDAO profileDAO = new UserProfileDAO();
		
		User loggedUser = userDAO.selectUser(loginData.getUserID());
		UserProfile profileData = profileDAO.selectProfileByUsername(loginData.getUserID(), true);
		
		
		bean.setEmail(loggedUser.getEmail());
		bean.setName(loggedUser.getName());
		bean.setLastName(loggedUser.getSurname());
		bean.setUserID(loggedUser.getUsername());
		bean.setModerator(loggedUser.isModerator());
		bean.setProfilePicPath(profileData.getProfilePicturePath());
		
		return bean;
	}

	public MailBox connect(UserBean userData){

		var mailBox = new MailBox();
		ConnectionServer server = ConnectionServer.getInstance();
		server.setMailBox(mailBox);
		ConnectionData myServerData = server.getConnectionData();
		SessionHandler session = new SessionHandler();
		
		if(session.startSession(userData.getUserID(), myServerData.getIP() , myServerData.getPort()))
			server.startServer();
			
		return mailBox;
	}
	
	
	
}
