package logic.controller.graphic;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import logic.bean.OrderBean;
import logic.bean.UserBean;
import logic.controller.application.BuyController;
import logic.controller.application.WalletController;
import logic.support.other.Bundle;
import logic.support.other.SceneManageable;

public class WalletView extends SceneManageable{
	
	 @FXML
	 private VBox orderBox;
	 @FXML
	 private Label creditLabel;
	 
	 private WalletController walletController = new WalletController();
	 private BuyController bController = new BuyController();
	 UserBean loggedUser = null;
	 
	@Override
	public void onLoad(Bundle bundle) {
		super.onLoad(bundle);
		
		loggedUser = (UserBean)bundle.getBean("loggedUser");
		if(loggedUser == null) {
			goToScene("login");
			return;
		}
		
		creditLabel.setText(walletController.getCredit(loggedUser).toString() + "coins");
		List<OrderBean> prevOrders = walletController.getOrderList(loggedUser);
		for(OrderBean prevOrder: prevOrders) {
			bController.checkRemainingTime(prevOrder);
		}
		
		
		List<OrderBean> orders = walletController.getOrderList(loggedUser);
		orderBox.getChildren().clear();
		for(OrderBean order: orders) {
			OrderCase orderCase = new OrderCase(order, loggedUser);
			orderCase.getUserLabel().setOnMouseClicked((MouseEvent e) -> {    	
		        	if(!orderCase.getUserData().getUserID().equals(loggedUser.getUserID())) {
		        		getBundle().addBean("selectedUser", orderCase.getUserData());
		        	}else {
		        		getBundle().addBean("selectedUser", loggedUser);
		        	}
		        	
		            goToScene("profile");
		        }
		    );
			
			orderCase.getItemLabel().setOnMouseClicked((MouseEvent e) -> {
		        
		        	getBundle().addBean("selectedItem", order.getInvolvedItem());
		            goToScene("itemDetails");
		        
		    });	
			orderCase.getOrderLabel().setOnMouseClicked((MouseEvent e) -> {
		        
		        	getBundle().addBean("selectedOrder", order);
		            goToScene("ordersummary");
		        
		    });	
			orderBox.getChildren().add(orderCase.getBody());
		}	
	}
}
