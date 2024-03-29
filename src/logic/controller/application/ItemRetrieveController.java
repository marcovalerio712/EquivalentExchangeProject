package logic.controller.application;

import java.util.ArrayList;
import java.util.List;

import logic.bean.ItemBean;
import logic.dao.ItemDAO;
import logic.entity.Book;
import logic.entity.Item;
import logic.entity.Movie;
import logic.entity.Videogame;

public class ItemRetrieveController {
	
	public List<ItemBean> getItemsList(){
		ItemDAO itemDAO = new ItemDAO(); 
		ArrayList<Item> items = (ArrayList<Item>) itemDAO.getItemsList();
		
		ArrayList<ItemBean> itemData = new ArrayList<>();
		
		for(Item item : items) {
				ItemBean itemBean = fromItemToItemBean(item);
				itemData.add(itemBean);
		}
		return itemData;
	}
	
	private ItemBean fromItemToItemBean(Item item) {
		ItemBean itemBean = new ItemBean(item.getItemID(), item.getName());
		itemBean.setType(item.getType());
		
		if (item.getLanguage()!= null) {
			itemBean.setLanguage(item.getLanguage().toString());
		}
		//aggiunti per mostrare console e edizione nella lista di item in PostAD
		char itemType = item.getType();
		if (itemType == 'B') {
			Book book = (Book)item;
			itemBean.setAuthor(book.getAuthor()); 
			itemBean.setEdition(book.getEdition()); 
			itemBean.setNumberOfPages(book.getNumberOfPages());
			if(book.getGenre() != null)
				itemBean.setGenre(book.getGenre().toString());	
			itemBean.setPublishingHouse(book.getPublishingHouse());
		}
		else if (itemType == 'M') {
			Movie movie = (Movie)item;
			itemBean.setDuration(movie.getDuration()); 
			if(movie.getGenre() != null)
				itemBean.setGenre(movie.getGenre().toString()); 
		}
		else {
			Videogame videogame = (Videogame)item;
			if(videogame.getGenre() != null)
				itemBean.setGenre(videogame.getGenre().toString()); 
			if (videogame.getConsole() != null)
				itemBean.setConsole(videogame.getConsole().toString());
		}
		return itemBean;
	}
	
	public List<ItemBean> getBooksList(){
		List<ItemBean> books = new ArrayList<>();
		
		for(ItemBean item : getItemsList()) {
			if(item.getType() == 'B') {
				books.add(item);
			}
		}
		
		return books;
	}
	public List<ItemBean> getMoviesList(){
		List<ItemBean> movies = new ArrayList<>();
		
		for(ItemBean item : getItemsList()) {
			if(item.getType() == 'M') {
				movies.add(item);
			}
		}
		
		return movies;
	}
	public List<ItemBean> getVideogamesList(){
		List<ItemBean> vgs = new ArrayList<>();
		
		for(ItemBean item : getItemsList()) {
			if(item.getType() == 'V') {
				vgs.add(item);
			}
		}
		
		return vgs;
	}
}
