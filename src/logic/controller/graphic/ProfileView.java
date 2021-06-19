package logic.controller.graphic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.bean.ItemInSaleBean;
import logic.bean.UserBean;
import logic.bean.UserProfileBean;
import logic.controller.application.ProfileController;
import logic.support.other.Bundle;
import logic.support.other.SceneManageable;

public class ProfileView extends SceneManageable{

	private UserBean loggedUser;
	private UserProfileBean userData;
	private Boolean changed = false;
	
	@FXML
	private Label nameLabel;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label genderLabel;
	@FXML
	private Label ageLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label descriptionLabel;
	@FXML
	private Circle profileImage;
	@FXML
	private ImageView coverImage;
	@FXML
	private HBox productList;
	@FXML
	private HBox guideList;
	
	@FXML
	private Button saveButton;
	
	private ProfileController controller = new ProfileController();
	
	@FXML
	public void goToSellerPanel() {
		goToScene("sellerpanel");
	}
	
	@FXML
	public void goToReviewerPanel() {
		goToScene("reviewerpanel");
	}
	@FXML
	public void changeProfilePic() {
		String profilePicPath = getPicture();
		if(profilePicPath != null) {
			try {
				profileImage.setFill(new ImagePattern(new Image(new FileInputStream(profilePicPath))));
				userData.setProfilePicPath(profilePicPath);
				changed = true;
			} catch (FileNotFoundException e) {
				
			}
		}
		saveButton.setVisible(changed);
	}
	@FXML
	public void changeCoverPic() {
		String coverPicPath = getPicture();
		if(coverPicPath != null) {
			try {
				coverImage.setImage(new Image(new FileInputStream(coverPicPath)));
				userData.setCoverPicPath(coverPicPath);
				changed = true;
			} catch (FileNotFoundException e) {
				
			}
		}
		saveButton.setVisible(changed);
	}
	@FXML
	public void saveProfile() {
		controller.updateUserProfileData(userData);
		changed = false;
		saveButton.setVisible(changed);
	}
	
	@Override
	public void onLoad(Bundle bundle) {
		super.onLoad(bundle);
		
		loggedUser = (UserBean) getBundle().getBean("loggedUser");
		
		if(loggedUser == null) {
			goToScene("login");
			return;
		}
		
		userData = controller.getUserProfileData(loggedUser);
		changed = false;
		saveButton.setVisible(changed);
		nameLabel.setText(userData.getName() + " " + userData.getLastName());
		usernameLabel.setText(userData.getUserID());
		genderLabel.setText(userData.getGender());
		ageLabel.setText("I'm " + userData.getAge().toString() + " years old");
		cityLabel.setText(userData.getCity() + ", " + userData.getCountry());
		
		if(userData.getDescription() != null)
			descriptionLabel.setText(userData.getDescription());
		else 
			descriptionLabel.setText("There is no biography for this user");
		
		profileImage.setFill(new ImagePattern(new Image(userData.getProfilePicPath())));
		
		loadProducts(4);
	}
	
	private void loadProducts(Integer numberOfProducts) {
		
		
		productList.getChildren().clear();
		List<ItemInSaleBean> products = controller.getProductsByUser(loggedUser, numberOfProducts);
		for(ItemInSaleBean product : products) {
			ProductCase productCase = new ProductCase(product);
			productCase.getProductName().setOnMouseClicked(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {
		        	Bundle bundle = getBundle();
		        	bundle.addBean("selectedItem", product);
		            goToScene("itemDetails");
		        }
		    });
			productList.getChildren().add(productCase.getBody());
			
			
		}
		
	}
	
	private String getPicture() {
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png", "*.jpg"));
		 File selectedFile = fileChooser.showOpenDialog(null);
		 if(selectedFile != null) {
			 return selectedFile.getAbsolutePath();
		 }
		 return null;
	}
	
	
}
