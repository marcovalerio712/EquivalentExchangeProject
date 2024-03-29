package logic.controller.graphic;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import logic.bean.ItemInSaleBean;
import logic.bean.UserBean;
import logic.controller.application.CatalogueController;
import logic.support.other.Bundle;
import logic.support.other.SceneManageable;

public class CatalogueView extends SceneManageable {
	
	private CatalogueController controller = new CatalogueController();
	private List<ItemInSaleBean> itemInSaleBeanList;
	private ArrayList<String> genres = new ArrayList<>();
	private ArrayList<String> consoles = new ArrayList<>();
	private HashMap<String, String> filters = new HashMap<>();
	private int maxItem = 9;
	private UserBean loggedUser;
	private Integer pageNumber = 0;
	private String genreStr = "genre";
	private String consoleStr = "console";
	
	@FXML
	private TextField searchBar;
	@FXML
	private Button searchBtn;
	@FXML
	private RadioButton book;
	@FXML
	private RadioButton movie;
	@FXML
	private RadioButton videogame;
	@FXML
	private RadioButton all;
	@FXML
	private ComboBox<String> genre;
	@FXML
	private ComboBox<String> orderBy;
	@FXML 
	private ToggleGroup type;
	@FXML
	private FlowPane flowPane;
	@FXML
	private Button nextPage;
	@FXML
	private Button prevPage;
	@FXML
	private Label page;
	@FXML 
	private Label genreLabel;
	@FXML 
	private Label consoleLabel;
	@FXML
	private ComboBox<String> consoleList;
	
	
	


	@Override
	public void onLoad(Bundle bundle) {
		super.onLoad(bundle);


		loggedUser = (UserBean) bundle.getBean("loggedUser");
		genre.setVisible(false);
		genreLabel.setVisible(false);
		consoleList.setVisible(false);
		consoleLabel.setVisible(false);
		all.setSelected(true);
		
		setOrderByList();
		
		doSearch();
	}
	
	@Override
	public void onExit() {
		genres = new ArrayList<>();
		consoles = new ArrayList<>();
		filters.clear();
		if (searchBar.getText()!=null) {
			searchBar.setText("");
		}	
		
	}
	
	public void doSearch() {
		String username = null;
		if (loggedUser != null) {
			username = loggedUser.getUserID();
		}
		itemInSaleBeanList = controller.getListItemInSaleBeanFiltered(username, filters);	
		setPageBtn();
		fillCatalogue();
	}
	
	public void fillCatalogue() {
		flowPane.getChildren().clear();
		for (int i = 0; i < maxItem; i++) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/logic/view/ItemCatalogue.fxml"));
			try {
				
				flowPane.getChildren().add(loader.load());
				ItemCatalogueView itemController = loader.getController();	
				
				itemController.setView(this, itemInSaleBeanList.get(i+(maxItem*pageNumber)));
			} catch (IndexOutOfBoundsException | IOException e) {
				flowPane.getChildren().remove(i);
				return;
			}
			
		}
	}
	
	public void setPageBtn() {
		prevPage.setDisable(true);
		pageNumber = 0;
		page.setText(pageNumber.toString());
		
		if (maxItem >= itemInSaleBeanList.size() ) {
			nextPage.setDisable(true);
			return;
		}
		nextPage.setDisable(false);
		
	}
	
	public void nextPage() {
		if(pageNumber == 0) {
			prevPage.setDisable(false);
		}
		if ( (pageNumber+2)*maxItem >= itemInSaleBeanList.size() ) {
			nextPage.setDisable(true);
		}
		
		pageNumber += 1;
		page.setText(pageNumber.toString());
		fillCatalogue();
	}
	
	public void prevPage() {
		if (nextPage.isDisable() ) {
			nextPage.setDisable(false);
		}
		
		if (pageNumber == 1) {
			prevPage.setDisable(true);
		}
		
		pageNumber -= 1;
		page.setText(pageNumber.toString());
		fillCatalogue();
		
	}
	
	public void search(){
    	var searchStr = searchBar.getText();
    	
		if (searchStr != null && !searchStr.isBlank()) {
			filters.put("searchKey", searchStr );
		}else {
			filters.remove("searchKey");
		}
    	doSearch();
    }
	
	public void all(){
		filters.remove("type");
		filters.remove(genreStr);
		filters.remove(consoleStr);
		
		genre.setVisible(false);
		genreLabel.setVisible(false);
		consoleLabel.setVisible(false);
		consoleList.setVisible(false);
		doSearch();
		
    }
	
	public void book(){
		var typeStr = "B";
		filters.put("type", typeStr);
		filters.remove(genreStr);
		filters.remove(consoleStr);
		
		setGenreList(typeStr);
		consoleLabel.setVisible(false);
		consoleList.setVisible(false);
		doSearch();
		
    }
	
	public void movie(){
		var typeStr = "M";
		filters.put("type", typeStr);
		filters.remove(genreStr);
		filters.remove(consoleStr);
		
		setGenreList(typeStr);
		consoleLabel.setVisible(false);
		consoleList.setVisible(false);
		doSearch();
	}	
	
	public void videogame(){
		var typeStr = "V";
		filters.put("type", typeStr);
		filters.remove(genreStr);
		filters.remove(consoleStr);
		
		setGenreList(typeStr);
		setConsoleList();
		doSearch();
		
    }
	
	public void genre() {
		var gen = genre.getValue();
	
		if(gen != null) {
		filters.put(genreStr, gen);
		}else {
			filters.remove(genreStr);
		}
		doSearch();
	}
	
	public void console() {
		var con = consoleList.getValue();
		
		if(con != null) {
		filters.put(consoleStr, con);
		}else {
			filters.remove(consoleStr);
		}
		doSearch();
	}
	
	public void orderBy() {
		var orderStr = orderBy.getValue();
		if (orderStr != null) {
			filters.put("orderBy", orderBy.getValue());
		} else {
			filters.remove("orderBy");
		}
		doSearch();
	}
	
	public void setGenreList(String type) {
		genre.getItems().clear();
		genre.setVisible(true);
		genreLabel.setVisible(true);
		genres = (ArrayList<String>) controller.getGenre(type.charAt(0));
		
		for(String gen : genres) {
			genre.getItems().add(gen);
		}
	
	}
	
	public void setConsoleList() {
		consoleList.getItems().clear();
		consoleList.setVisible(true);
		consoleLabel.setVisible(true);
		consoles = (ArrayList<String>) controller.getConsole();
		
		for(String con : consoles) {
			consoleList.getItems().add(con);
		}
	}
	
	public void setOrderByList() {
		orderBy.getItems().clear();
		orderBy.getItems().add("Rising Price");
		orderBy.getItems().add("Decreasing Price");
	}
	
	public void goToDetails(ItemInSaleBean item) {
	 	bundle = getBundle();
		bundle.addBean("selectedItem", item);
		goToScene("itemDetails");
	}
	
	
}

