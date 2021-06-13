package logic.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.entity.ChatMessage;
import logic.query.MessageQuery;
import logic.support.database.MyConnection;

public class MessageDAO {

	MyConnection connection = MyConnection.getInstance();
	MessageQuery mQuery = new MessageQuery();
	
	public List<ChatMessage> getMessagesByUsers(String myUsername, String otherUsername){
		ArrayList<ChatMessage> messages = new ArrayList<>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection con = connection.getConnection();
			stmt = con.createStatement();
			String query = mQuery.getMessagesByUsers(myUsername, otherUsername);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				ChatMessage message = new ChatMessage();
				message.setSender(rs.getString("senderID"));
				Date messageDate = null;
				try {
					messageDate = format.parse(rs.getString("sDateTime"));
				} catch (ParseException e) {
					messageDate = new Date();
				}
				message.setDate(messageDate);
				message.setText(rs.getString("body"));
				messages.add(message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return messages;
	}
	
	public void addMessageforUser(ChatMessage message, String receiverID) {
		String senderID = message.getSender();
		Date messageDate = message.getDate();
		String text = message.getText();
		Statement stmt = null;
		try {
			Connection con = connection.getConnection();
			stmt = con.createStatement();
			String query = mQuery.addMessage(senderID, receiverID, text, messageDate);
			System.out.println(query);
			stmt.executeUpdate(query);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}