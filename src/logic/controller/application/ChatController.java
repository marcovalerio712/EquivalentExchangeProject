package logic.controller.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.bean.ChatBean;
import logic.bean.OrderBean;
import logic.bean.UserBean;
import logic.dao.MessageDAO;
import logic.dao.OrderDAO;
import logic.dao.UserProfileDAO;
import logic.entity.ChatMessage;
import logic.entity.Order;
import logic.entity.User;
import logic.entity.UserProfile;
import logic.enumeration.NotificationType;
import logic.support.connection.MessageSender;
import logic.support.other.MailBox;

public class ChatController{
	
	public void sendMessage(ChatBean messageData){
		
		MessageDAO chatDAO = new MessageDAO();
		
		String senderID = messageData.getSender();
		String text = messageData.getMessageText();
		Date messageDate = new Date();
		String receiverID = messageData.getReceiver();
	
		ChatMessage message = new ChatMessage(senderID, messageDate, text);
		
		MessageSender sender = new MessageSender();
		sender.sendChatMessage(receiverID, message);
		sender.sendChatMessage(message.getSender(), message);
		chatDAO.addMessageforUser(message, receiverID);
    }
	
	public Map<String, ChatBean> getLastMessagesSaved(UserBean loggedUser){
		HashMap<String, ChatBean> lastMessages = new HashMap<>();
		MessageDAO chatDAO = new MessageDAO();
		for(UserBean user : getActiveChats(loggedUser)) {
			ChatMessage message = chatDAO.getLastMessageSentByUsers(loggedUser.getUserID(), user.getUserID());
			ChatBean messageBean = new ChatBean();
			messageBean.setSender(message.getSender());
			messageBean.setMessageText(message.getText());
			messageBean.setDate(message.getDate());
			
			if(messageBean.getSender().equals(loggedUser.getUserID())) {
				messageBean.setReceiver(user.getUserID());
			}
			else {
				messageBean.setReceiver(loggedUser.getUserID());
			}
			lastMessages.put(user.getUserID(), messageBean);
		}
		return lastMessages;
	}
	
	public List<UserBean> getActiveChats(UserBean user){
		ArrayList<UserBean> activeUsers = new ArrayList<>();
		
		OrderDAO orderDAO = new OrderDAO();
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		
		List<Order> userOrders = orderDAO.selectAllOrders(user.getUserID());
		
		for(Order order : userOrders) {
			User otherUser;
			if(user.getUserID().equals(order.getBuyer().getUsername())) {
				otherUser = order.getInvolvedItem().getSeller();
			}else {
				otherUser = order.getBuyer();
			}
			
			UserProfile profileData = userProfileDAO.selectProfileByUsername(otherUser.getUsername(), true);
			UserBean otherUserBean = new UserBean();
			otherUserBean.setName(otherUser.getName());
			otherUserBean.setLastName(otherUser.getSurname());
			otherUserBean.setUserID(otherUser.getUsername());
			otherUserBean.setEmail(otherUser.getEmail());
			otherUserBean.setProfilePicPath(profileData.getProfilePicturePath());
			
			Boolean present = false;
			for(UserBean userBean : activeUsers) {
				if(userBean.getUserID().equals(otherUserBean.getUserID())) {
					present = true;
				}
			}
			if(Boolean.FALSE.equals(present))
				activeUsers.add(otherUserBean);
		}
		
		return activeUsers;
	}
	
	public List<ChatBean> getMessagesByUser(UserBean loggedUser, UserBean otherUser){
		
		MessageDAO chatDAO = new MessageDAO();
		List<ChatMessage> messages = chatDAO.getMessagesByUsers(loggedUser.getUserID(), otherUser.getUserID());
		ArrayList<ChatBean> messageBeans = new ArrayList<>();
		
		for(ChatMessage message : messages) {
			ChatBean messageBean = new ChatBean();
			messageBean.setSender(message.getSender());
			if(message.getSender().equals(otherUser.getUserID())){	
				messageBean.setReceiver(loggedUser.getUserID());
			}
			else {
				messageBean.setReceiver(otherUser.getUserID());
			}
			messageBean.setMessageText(message.getText());
			messageBean.setDate(message.getDate());
			messageBeans.add(messageBean);
		}
		
		return messageBeans;
		
	}
	
	
	public ChatBean getLastMessageSent(MailBox box) {
		ArrayList<ChatMessage> messages = (ArrayList<ChatMessage>) box.getMessages();
		if(messages.isEmpty())
			return null;
		ChatMessage message = messages.get(messages.size()-1);
		messages.remove(message);
		ChatBean messageBean = new ChatBean();
		messageBean.setMessageText(message.getText());
		messageBean.setSender(message.getSender());
		messageBean.setDate(message.getDate());
		return messageBean;
	}
	
	public OrderBean getActiveOrderByUsers(UserBean loggedUser, UserBean otherUser, Boolean flag) {
		WalletController walletController = new WalletController();
		List<OrderBean> orders = walletController.getOrderList(loggedUser);
		
		
		
		if(Boolean.TRUE.equals(flag) && !(orders.isEmpty()) ) {
			return orders.get(0);
		}
		for(OrderBean order : orders) {
			if(order.getBuyer().getUserID().equals(loggedUser.getUserID()) && order.getInvolvedItem().getSeller().getUserID().equals(otherUser.getUserID())) {
				if(Boolean.FALSE.equals(order.getBuyerStatus())) {
					return order;
				}
			}else if(order.getBuyer().getUserID().equals(otherUser.getUserID()) && Boolean.FALSE.equals(order.getSellerStatus())){
				return order;
			}
		}
		return null;
	}
	
	public Boolean getChatNotifications(MailBox box) {
		return !box.getNotifications(NotificationType.CHAT).isEmpty();
	}
	
}