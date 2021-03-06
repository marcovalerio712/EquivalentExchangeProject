package logic.entity;

import java.util.Date;

import logic.enumeration.MovieGenre;

public class Movie extends Item {
	private Integer duration;
	private MovieGenre genre;
	
	public Movie(String name, Date publishingDate,Integer duration, String genre, String language) {
		this.setName(name);
		this.setPublishingDate(publishingDate);
		this.duration = duration;
		setGenre(genre);
		setLanguage(language);
		
		this.setItemID(name.hashCode() + genre.hashCode() + publishingDate.hashCode() % 2147483647);
	}
	
	public Integer getDuration() {
		return this.duration;
	}
	
	public MovieGenre getGenre() {
		return this.genre;
	}
	
	public Character getType() {
		return 'M';
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public void setGenre(MovieGenre genre) {
		this.genre = genre;
	}
	public void setGenre(String genre) {
		if(genre != null) {
			for (MovieGenre value : MovieGenre.values()) {
				if (genre.equals(value.toString())){
					  this.genre = value;
					  return;
				}
			}
		}
		this.genre = null;
	}
}
