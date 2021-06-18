package logic.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import logic.support.interfaces.Bean;
import logic.support.other.ProfileRules;

public class RegistrationBean implements Bean{
	
	private String username;
	private String name;
	private String lastName;
	private String email;
	private String password;
	private Date birthDate;
	
	
	public String getUsername() {
		return this.username;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}
	
	public boolean setUsername(String username) {
		
		username = username.toLowerCase();
		
		if(!username.matches("[a-z0-9]*"))
			return false;
		
		this.username = username;
		return true;
	}
	
	public boolean setPassword(String password) {
		
		boolean presenceFlag = false;
		
		if(password.length() < ProfileRules.getMinPasswordLenght() &&
			password.length() > ProfileRules.getMaxPasswordLenght()) {
			return false;
		}
		
		for(Character disabledChar : ProfileRules.getPassNotAllowed()) {
			if(password.contains(disabledChar.toString())) {
				return false;
			}
		}
		
		for(String requiredSet : ProfileRules.getPassRequiredCharactersSet()) {
			for(int i = 0; i < requiredSet.length(); i++) {
				if(password.contains(Character.toString(requiredSet.charAt(i)))) {
					presenceFlag = true;
					break;
				}
			}
			if(!presenceFlag)
				return false;
			presenceFlag = false;			
		}
		
		this.password = password;
		return true;	
	}
	
	public boolean setName(String name) {
		
		if(!isAlphabetic(name))
			return false;
		this.name = name;
		return true;

	}
	
	public boolean setLastName(String lastName) {

		if(!isAlphabetic(lastName))
			return false;
		this.lastName = lastName;
		return true;
	}
	
	

	public boolean setEmail(String email){

		email = email.toLowerCase();
	
		if(!email.matches("[0-9a-z]+[\\.[0-9a-z]+]*@[0-9a-z]+[\\.[0-9a-z]+]*"))
			return false;
		
		this.email = email;
		return true;
	
	}
	
	public boolean setBirthDate(Date birthDate) {
		int minimumAge = ProfileRules.getMinimumAge();
		
		int userAge = calculateAge(birthDate);
		
		if(userAge < minimumAge) {
			return false;
		}
		
		this.birthDate = birthDate;
		return true;
	}
	
	private Integer calculateAge(Date birthDate) {
		Calendar calendar = new GregorianCalendar();
		Date now = new Date();
		calendar.setTime(now);
		Integer todayYear = calendar.get(Calendar.YEAR);
		Integer todayMonth = calendar.get(Calendar.MONTH);
		Integer todayDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(birthDate);
		Integer birthYear = calendar.get(Calendar.YEAR);
		Integer birthMonth = calendar.get(Calendar.MONTH);
		Integer birthDay = calendar.get(Calendar.DAY_OF_MONTH);
		
				
		Integer age = todayYear - birthYear;
		
		if(todayMonth < birthMonth) 
			age--;
		else if(todayMonth.equals(birthMonth) && todayDay < birthDay)
			age = age - 1;
		return age;
	}
	
	private boolean isAlphabetic(String word) {
		return word.matches("[a-zA-Z ]*");
	}
	
	
}
