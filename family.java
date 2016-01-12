
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Family {
	private String Id;
	private String husband;
	private String wife;
	
	private ArrayList<String> children = new ArrayList<String>();
	private Calendar marriageDate;
	private Calendar divorceDate;
	
	private boolean mdAvailable=false;
	private boolean ddAvailable=false;
	
	
	public boolean isMdAvailable() {
		return mdAvailable;
	}
	public void setMdAvailable(boolean mdAvailable) {
		this.mdAvailable = mdAvailable;
	}
	public boolean isDdAvailable() {
		return ddAvailable;
	}
	public void setDdAvailable(boolean ddAvailable) {
		this.ddAvailable = ddAvailable;
	}

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getHusband() {
		return husband;
	}
	public void setHusband(String husband) {
		this.husband = husband;
	}
	public String getWife() {
		return wife;
	}
	public void setWife(String wife) {
		this.wife = wife;
	}
	
	public ArrayList<String> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}
	public Calendar getMarriageDate() {
		return marriageDate;
	}
	public void setMarriageDate(Calendar marriageDate) {
		this.marriageDate = marriageDate;
	}
	public Calendar getDivorceDate() {
		return divorceDate;
	}
	public void setDivorceDate(Calendar divorceDate) {
		this.divorceDate = divorceDate;
	}
	
	public void addChildren(String id){
		children.add(id);
	}

}
