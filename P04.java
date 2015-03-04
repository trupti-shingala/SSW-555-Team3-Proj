
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
import java.util.*;


public class P04 {
	private static HashMap<String,Individual> gedcomIndi=new HashMap <String,Individual>();
	private static HashMap<String,Family> gedcomFamily=new HashMap <String,Family>();
	
	
	
	public static HashMap<String, Individual> getGedcomIndiList() {
		return  gedcomIndi;
	}
	
	public static void setGedcomIndiList(HashMap<String, Individual> gedcomIndiList) {
		P04.gedcomIndi = gedcomIndi;
	}
	
	public static HashMap<String,Family>getGedcomFamily() {
		return  gedcomFamily;
	}
	
	public static void setGedcomFamily(HashMap<String,Family>gedcomFamily) {
		P04.gedcomFamily = gedcomFamily;
	}
	
	public static void read(String FileName){
	
	Path filepath=Paths.get(FileName);
	
	try{
		BufferedReader reader=Files.newBufferedReader(filepath,StandardCharsets.UTF_8);
		String line=null;
		Individual indi=null;
		Family f=null;
		
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
							indi=new Individual();
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
							f= new Family();
							f.setId(gedcomContent[1]);
						}
						break;
				    case 1: 
				    	processLevel1(reader, gedcomContent, indi, f);
				    	break;
				}
			}			 
		}
		//Add the last read Family object in the map
	    if(f != null ){
			gedcomFamily.put(f.getId(), f);
			//System.out.println("added Family: " + f.getId());
			f = null;
		}
	}
	
	catch (IOException exp){
		System.out.println("Error has occured while reading the file:" + exp);
		exp.printStackTrace();
	}
	
	}


	public static void processLevel1(BufferedReader reader,String [] gedcomContent,Individual indi,Family f)throws IOException
	{
		String line="";
		String[] DateContent;
		boolean famsSet=false;
		
			if((gedcomContent[1].equals("NAME")))
			{
				StringBuilder name = new StringBuilder();
				for(int i=2;i<gedcomContent.length;i++)
				{
					if(name.length()>0)
					{
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
			else if(gedcomContent[1].equals("WIFE"))
			{
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
	 
	 public static void checkHusbandWifeGenderError(){//HashMap<String,  individual> gedcomPersonList, HashMap<String, family> gedcomFamilyList ){
			
		 for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet())  {
		        String husband = entry.getValue().getHusband() ;
		        Individual husbandperson  = getGedcomIndiList().get(husband);
		
		        String sexh=husbandperson.gettGender();
		        String husname =husbandperson.gettName();
		
		        if (husband != null){
			        if(sexh.equalsIgnoreCase("F")){
			        	System.out.println("ERROR: " + husname+" has wrong gender ");
			        }
		        }
		       
		        String wife = entry.getValue().getWife() ;
		        Individual wifeperson  = getGedcomIndiList().get(wife);
		        String winame =wifeperson.gettName();
		        String sexw=wifeperson.gettGender();
		
		        if (wife != null){
			        if(sexw.equalsIgnoreCase("M")){
			        	System.out.println("ERROR: " +winame+" has wrong gender ");
			         }
		       }
		   }
		}
	 
	
	 
	 
	 public static void checkDivorceBeforeMarriageError()
	 {
		 System.out.println("\n\ncheck:divorce before marriage\n\n");
		 
		 for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet()) {
				
				if(entry1.getValue().getMarriageDate()!=null && entry1.getValue().getDivorceDate()!=null)
					{
						if(entry1.getValue().getMarriageDate().before(entry1.getValue().getDivorceDate()))
							System.out.println("Error:Divorce before Marriage");
					}
				else
					System.out.println("Pass");
			}
	 }
	 
	
	 
	
	 
	
	
	
	 
	 public static void main(String args[]){
		String filename;
		if(args.length > 0){
			filename  = args[0];
		}
		else{
			filename = "C:\\Users\\Poonam\\Desktop\\study\\s.ged";
			P04.read(filename);
			
		}
		
	
		for(Map.Entry<String,Family> entry :getGedcomFamily().entrySet())
		{
			System.out.println("\nFamily " +entry.getValue().getId());
			System.out.println("Name of Husband " +": " + getGedcomIndiList().get(entry.getValue().getHusband()).gettName());
			System.out.println("Name Of wife "  +": " + getGedcomIndiList().get(entry.getValue().getWife()).gettName());
			for(int i=0;i<entry.getValue().getChildren().size();i++)
				System.out.println("Name Of child " +": " + getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettName());
			System.out.println();
		}
		
		checkDivorceBeforeMarriageError();
		checkHusbandWifeGenderError();
		
		
		
		}
			
	
	}
	
	

