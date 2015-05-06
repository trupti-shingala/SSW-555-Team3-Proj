

import java.awt.List;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;


public class Individual {
	private String id;
	private String name;
	private String gender;
	private Calendar birt;
	private Calendar death;
	private boolean dobAvailable=false;
	private boolean dodAvailable=false;

	
	public boolean isDodAvailable() {
		return dodAvailable;
	}

	public void setDodAvailable(boolean dodAvailable) {
		this.dodAvailable = dodAvailable;
	}

	public boolean isDobAvailable() {
		return dobAvailable;
	}

	public void setDobAvailable(boolean dobAvailable) {
		this.dobAvailable = dobAvailable;
	}

	private ArrayList<String> fams = new ArrayList<String>();
	private ArrayList<String> famc = new ArrayList<String>();
	
		
	

	public String gettID(){
		return id;
	}
	
	public void setId(String id){
		this.id=id;
	}
	public String gettName(){
		return name;
	}
	
	public void setName(String name){
	this.name= name;
	}
	public String gettGender(){
		return gender;
	}
	public void setGender(String gender){
		this.gender=gender;
	}
	public Calendar getBirthDate() {
	return birt;
	}
	public void setBirthDate(Calendar birt) {
	this.birt = birt;
	}

	public Calendar getDeath() {
	return death;
	}
	public void setDeath(Calendar death) {
	this.death = death;
	}
	
	
	public ArrayList<String> getFams() {
		return fams;
	}
	public void setFams(ArrayList<String> fams) {
		this.fams = fams;
	}
	public void addFams(String id){
		fams.add(id);
	}
	
	public void addFamc(String id){
		famc.add(id);
	}
	
}

