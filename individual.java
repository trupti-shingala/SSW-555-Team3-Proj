import java.awt.List;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;


public class individual {
	private String id;
	private String name;
	private String gender;
	private Date birt;
	private Date death;
	
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
	public Date getBirthDate() {
	return birt;
	}
	public void setBirthDate(Date birt) {
	this.birt = birt;
	}

	public Date getDeath() {
	return death;
	}
	public void setDeath(Date death) {
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

