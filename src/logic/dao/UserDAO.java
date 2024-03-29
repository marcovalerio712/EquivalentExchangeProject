package logic.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.query.UserQuery;
import logic.support.database.MyConnection;
import logic.support.exception.AlreadyRegisteredUserException;
import logic.support.exception.WrongLoginCredentialsException;
import logic.entity.User;

public class UserDAO {

	MyConnection connection = MyConnection.getInstance();
	UserQuery userQ = new UserQuery();

	public List<User> getModerators(){
		List<User> moderators = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {

			Connection con = connection.getConnection();
			stmt = con.createStatement();
			String query = userQ.selectModerators(true);
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				User mod = new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("passwd"), rs.getInt("credit"));
				mod.setModerator(true);
				moderators.add(mod);
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null)
					stmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return moderators;
	}
	
	public User selectUser(String username){
		User user = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			Connection con = connection.getConnection();
			stmt = con.createStatement();
			String query = userQ.selectUser(username);
			rs = stmt.executeQuery(query);

			if (!rs.next()) {
				return null;
			}

			user = new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("passwd"), rs.getInt("credit"));
			user.setGender(rs.getString("gender"));
			user.setBirthDate(rs.getDate("birthDate"));
			
			if(rs.getInt("isModerator") == 1) {
				user.setModerator(true);
			}
			rs.close();
			
			
		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			try {
				if(stmt!=null)
					stmt.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;

	}
	
	public User selectLoginUser(String username) throws WrongLoginCredentialsException{
		User user = selectUser(username);
		if(user == null) {
			throw new WrongLoginCredentialsException(1);
		}
		else {
			return user;
		}
	}

	public void insertUser(User user) throws AlreadyRegisteredUserException {
		String query = selectQuery(user, "insert");
		
		if(selectUser(user.getUsername()) != null) {
			throw new AlreadyRegisteredUserException(2);
		}
		
		try {
			writeOnUser(query);
		} catch (SQLException e) {
			throw new AlreadyRegisteredUserException(1);
		}
	}
	
	public void updateUser(User user) {
		String query = selectQuery(user, "update");
		try {
			writeOnUser(query);
		}catch(SQLException e) {
			//DO nothing
		}
	}
	
	private void writeOnUser(String query) throws SQLException {
		Statement stmt = null;
		try {

			Connection con = connection.getConnection();
			stmt = con.createStatement();
			
			stmt.executeUpdate(query);

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String selectQuery(User user, String queryType ) {
		String username = user.getUsername();
		String password = user.getPassword();
		String name = user.getName();
		String lastName = user.getSurname();
		String email = user.getEmail();
		
		String gender = "";
		if(user.getGender() != null)
			gender = user.getGender().toString().substring(0,1);
		Date birthDate = user.getBirthDate();
		Integer credit = user.getWallet().getCurrentCredit();
		
		String query;
		if(queryType.equals("insert")) {
			query = userQ.insertUser(username, password, name, lastName, email, gender, birthDate, credit);
		}
		else{
			Boolean isModerator = user.isModerator();
			query = userQ.updateUser(username, password, name, lastName, email, gender, birthDate, credit, isModerator);
		}
		return query;
		
	}
	public void deleteUser(String username) {
		Statement stmt = null;
		try {

			Connection con = connection.getConnection();
			stmt = con.createStatement();
			String query = userQ.deleteUser(username);
			stmt.executeUpdate(query);

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}
