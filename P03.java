
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class P03 {
	private static HashMap<String,individual> gedcomIndi=new HashMap <String,individual>();
	private static HashMap<String,family> gedcomFamily=new HashMap <String,family>();
	
	
	public static HashMap<String, individual> getGedcomIndiList() {
		return  gedcomIndi;
	}
	
	public static void setGedcomIndiList(HashMap<String, individual> gedcomIndiList) {
		P03.gedcomIndi = gedcomIndi;
	}
	
	public static HashMap<String,family>getGedcomFamily() {
		return  gedcomFamily;
	}
	
	public static void setGedcomFamily(HashMap<String,family>gedcomFamily) {
		P03.gedcomFamily = gedcomFamily;
	}
	
	public static void read(String FileName){
	
	Path filepath=Paths.get(FileName);
	
	try{
		BufferedReader reader=Files.newBufferedReader(filepath,StandardCharsets.UTF_8);
		String line=null;
		individual indi=null;
		family f=null;
		
		while((line=reader.readLine())!=null){
			//System.out.println(line);
			String[] gedcomContent = line.split(" ");
			if(gedcomContent.length>0 && gedcomContent[0]!=" "){
				int levelNumber= Integer.parseInt(gedcomContent[0]);
				switch(levelNumber){
					case 0:
						if(gedcomContent[1].indexOf("@")>=0 && gedcomContent[2].equals("INDI")){
							if(indi!=null){
								gedcomIndi.put(indi.gettID(),indi);
								indi=null;
							}
							indi=new individual();
							indi.setId(gedcomContent[1]); 
						
						}
						else if(gedcomContent[1].indexOf("@")>=0 && gedcomContent[2].equals("FAM")){
							//Add the last read person object in the map
	        				if(indi != null){
	        					gedcomIndi.put(indi.gettID(),indi);
	        					//System.out.println("added Person: " + indi.gettID());
	        					indi = null;
	        				}
							if(f!=null){
								gedcomFamily.put(f.getId(), f);
								f=null;
							}
							f= new family();
							f.setId(gedcomContent[1]);
						}
						break;
				    case 1: 
				    	processLevel1(reader, gedcomContent, indi, f);
				    	break;
				}
			}			 
		}
		//Add the last read family object in the map
	    if(f != null ){
			gedcomFamily.put(f.getId(), f);
			//System.out.println("added family: " + f.getId());
			f = null;
		}
	}
	
	catch (IOException exp){
		System.out.println("Error has occured while reading the file:" + exp);
		exp.printStackTrace();
	}
	
	}


	public static void processLevel1(BufferedReader reader,String [] gedcomContent,individual indi,family f)throws IOException{
		String line="";
		String[] DateContent;
		
			if((gedcomContent[1].equals("NAME"))){
				StringBuilder name = new StringBuilder();
				for(int i=2;i<gedcomContent.length;i++){
					if(name.length()>0){
						name.append(" ");
					}
					name.append(gedcomContent[i]);
				}
				if(indi!=null){
					indi.setName(name.toString());
				}
			}
			else if(gedcomContent[1].equals("SEX")){
				String sex = gedcomContent[2];
				if(indi!=null){
					indi.setGender(sex);
				}
			}
			else if(gedcomContent[1].equals("FAMS")){
				if(indi != null){
					indi.addFams(gedcomContent[2]);
				}
			}
			else if (gedcomContent[1].equals("FAMC")){
				if(indi!= null){
					indi.addFamc(gedcomContent[2]);
					
				}
			}
			else if(gedcomContent[1].equals("MARR")){
				line=reader.readLine();
				DateContent=line.split(" ");
				if(f!=null && line.indexOf("DATE")>0){
					f.setMarriageDate(processLevel2(DateContent));
				}
			}
			else if (gedcomContent[1].equals("DIV")){
				line=reader.readLine();
				DateContent=line.split(" ");
				if(f!=null && line.indexOf("DATE")>0){
					f.setDivorceDate(processLevel2(DateContent));
				}
				
			}
			
			else if(gedcomContent[1].equals("CHIL")){
					if( f!=null){
						f.addChildren(gedcomContent[2]);
					}
			}
			else if(gedcomContent[1].equals("HUSB")){
					if(f!=null){
						f.setHusband(gedcomContent[2]);
					}
			}
			else if(gedcomContent[1].equals("WIFE")){
					if(f != null ){
						f.setWife(gedcomContent[2]);
					}
			}
		
		}
	
	
	
	public static Calendar processLevel2(String [] gedcomContent){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		StringBuilder dateStr = new StringBuilder();
		for(int i =2; i< gedcomContent.length ; i++){
			if(dateStr.length() > 0){
				dateStr.append(" ");
			}
			dateStr.append(gedcomContent[i]);
		}

		java.util.Date date = null;
		try {
			date = dateFormat.parse(dateStr.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar dateCal = new GregorianCalendar();
		dateCal.setTime(date);
			
		return dateCal;
	}
	
	 void validTag(List<String> validTags){
		validTags.add("INDI");
		validTags.add("NAME");
		validTags.add("SEX");
		validTags.add("BIRT");
		validTags.add("DEAT");
		validTags.add("FAMC");
		validTags.add("FAMS");
		validTags.add("FAM");
		validTags.add("MARR");
		validTags.add("HUSB");
		validTags.add("WIFE");
		validTags.add("CHIL");
		validTags.add("DIV");
		validTags.add("DATE");
		validTags.add("TRLR");
		validTags.add("NOTE");
		//validTags.add("BIRTH");
	
	}
	
	public static void main(String args[]){
		String filename;
		if(args.length > 0){
			filename  = args[0];
		}
		else{
			filename = "src/p03/sample1.ged";
			P03.read(filename);
			
		}
		
		
		for (Map.Entry<String,individual> entry : getGedcomIndiList().entrySet()) {
			System.out.println("Name of Individua l" +entry.getValue().gettID() +": " + entry.getValue().gettName());
		}
		for (Map.Entry<String,family> entry :getGedcomFamily().entrySet()) {
			System.out.println("Name of Husband " +entry.getValue().getId() +": " + getGedcomIndiList().get(entry.getValue().getHusband()).gettName());
		}
		for (Map.Entry<String,family> entry : getGedcomFamily().entrySet()) {
			System.out.println("Name Of wife " +entry.getValue().getId() +": " + getGedcomIndiList().get(entry.getValue().getWife()).gettName());
		}
		
	}
	 
	
}
