
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
					f.setMarriageDate(processLevel2(DateContent[2]+"-"+DateContent[3]+"-"+DateContent[4]));
				}
			}
			else if (gedcomContent[1].equals("DIV")){
				line=reader.readLine();
				DateContent=line.split(" ");
				if(f!=null && line.indexOf("DATE")>0){
					f.setDivorceDate(processLevel2(DateContent[2]+"-"+DateContent[3]+"-"+DateContent[4]));
				}
				
			}
			else if (gedcomContent[1].equals("BIRTH")){
				line=reader.readLine();
				DateContent=line.split(" ");
				if(indi!=null && line.indexOf("DATE")>0){
					indi.setBirthDate(processLevel2(DateContent[2]+"-"+DateContent[3]+"-"+DateContent[4]));
				}
				
			}
			else if (gedcomContent[1].equals("DEAT")){
				line=reader.readLine();
				DateContent=line.split(" ");
				if(indi!=null && line.indexOf("DATE")>0){
					indi.setDeath(processLevel2(DateContent[2]+"-"+DateContent[3]+"-"+DateContent[4]));
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
	
	
public static Calendar processLevel2(String strDate )
	{	
	  try {
		  DateFormat formatter ; 
		  Date date ; 
			  formatter = new SimpleDateFormat("dd-MMM-yyyy");

		date = (Date)formatter.parse(strDate);
		// System.out.println(date);
		  Calendar cal=Calendar.getInstance();
		  cal.setTime(date);
		 return cal;
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	 
	
	}	
	

	
	public static void checkDivorceafterTodayDate()
         {
             System.out.println("\n\nCheck Divorce After Today's date\n\n");
             int x;
             for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet()){
                 
                 if(!entry.getValue().getDivorceDate().after(getGedcomIndiList().get(entry.getValue().getcurrentDate())))
						System.out.println("Error:Divorce after Current date for " +getGedcomIndiList().get(entry.getValue().gettName();
					else
						System.out.println("Pass");
                 
             }
         }
         
         public static void checkIndividualBirthdatebeforeParentBirthdate()
         {
             System.out.println("\n\n check Individual's Birthdate before Parents' Birthdate\n\n");
             for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet())
			{
				
				if(entry.getValue().getBirthDate()!=null)
					
				{
					if(entry.getValue().getBirthDate().before(getGedcomIndiList().get(entry.getValue().getHusband()).getBirthDate()))
						System.out.println("Error:Person Birthdate before Parent's Birthdate "+getGedcomIndiList().get(entry.getValue().getHusband()).gettID().getName());
					else
						System.out.println("Pass");
				
					if(!entry.getValue().getBirthDate().before(getGedcomIndiList().get(entry.getValue().getWife()).getBirthDate()))
						System.out.println("Error:Person Birthdate before Parent's Birthdate "+getGedcomIndiList().get(entry.getValue().getWife()).gettName());
					else
						System.out.println("Pass");
			
				
				}
                                
                                else
					System.out.println("Pass");
			}
             
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
	 
	 public static void checkDivorceBeforeBirthError()
	 {
		 System.out.println("\n\ncheck divorce before birth\n\n");
			
			for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
			{
				
				if(entry1.getValue().getDivorceDate()!=null)
					
				{
					if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getHusband()).getBirthDate()))
						System.out.println("PassH");
					else
						System.out.println("Error:Divorce before Birth for"+getGedcomIndiList().get(entry1.getValue().getHusband()).gettID());
				
					if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getWife()).getBirthDate()))
						System.out.println("PassW");
					else
						System.out.println("Error:Divorce before Birth for"+getGedcomIndiList().get(entry1.getValue().getWife()).gettID());
			
				
				}else
					System.out.println("Pass");
			}
			
	 
	 }
	 
	  public static void checkMarriageWithSiblingError()
	 {
		 System.out.println("\n\ncheck:marriage between siblings\n\n");
		 
		 for(Map.Entry<String,Family> entry :getGedcomFamily().entrySet())
			{
			 if(entry.getValue().getChildren().size()>1)
				 for(int i=0;i<entry.getValue().getChildren().size();i++)
					 for(int j=i+1;j<entry.getValue().getChildren().size();j++)
					 {
						 if(
							getGedcomIndiList().get(entry.getValue().getChildren().get(i)).getFams().equals(entry.getKey())
						    &&getGedcomIndiList().get(entry.getValue().getChildren().get(j)).getFams().equals
						    (getGedcomIndiList().get(entry.getValue().getChildren().get(j)).getFams())
						   )
						 {
								System.out.println("Error:Marriage between siblings for "+getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettID()+" & "+getGedcomIndiList().get(entry.getValue().getChildren().get(j)).gettID());
						
						 }
						 else
							 System.out.println("pass");
							 
					 }
			}
	 }
	 
	public static void checkMarriageBeforeBirthError()
	 {
		 System.out.println("\n\ncheck marriage before birth\n\n");
			
			for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
			{
				
				if(entry1.getValue().getMarriageDate()!=null)
					
				{
					if(!entry1.getValue().getMarriageDate().before(getGedcomIndiList().get(entry1.getValue().getHusband()).getBirthDate()))
						System.out.println("PassH");
					else
						System.out.println("Error:Marriage before Birth for"+getGedcomIndiList().get(entry1.getValue().getHusband()).gettID());
				
					if(!entry1.getValue().getMarriageDate().before(getGedcomIndiList().get(entry1.getValue().getWife()).getBirthDate()))
						System.out.println("PassW");
					else
						System.out.println("Error:Marriage before Birth for"+getGedcomIndiList().get(entry1.getValue().getWife()).gettID());
			
				
				}
			}
			
	 
	 }
	 
	 
	
	 public static void checkMarriageAfterDeathError()
	 {
		 System.out.println("\n\ncheck marriage after death\n\n");
			
			for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
			{
				
				if(entry1.getValue().getMarriageDate()!=null)
					if(getGedcomIndiList().get(entry1.getValue().getHusband()).getDeath()!=null&&getGedcomIndiList().get(entry1.getValue().getWife()).getDeath()!=null)
				{
					if(!entry1.getValue().getMarriageDate().before(getGedcomIndiList().get(entry1.getValue().getHusband()).getBirthDate()))
						System.out.println("PassH");
					else
						System.out.println("Error:Marriage after death for"+getGedcomIndiList().get(entry1.getValue().getHusband()).gettID());
				
					if(!entry1.getValue().getMarriageDate().before(getGedcomIndiList().get(entry1.getValue().getWife()).getBirthDate()))
						System.out.println("PassW");
					else
						System.out.println("Error:Marriage after death for"+getGedcomIndiList().get(entry1.getValue().getWife()).gettID());
			
				
				}else
					System.out.println("Pass");
			}
			
	 
	 }
	 
	 
	 public static void checkMarriageBeforeLegalAgeError(HashMap<String, Individual> gedcomIndividualList, HashMap<String, Family> gedcomFamilyList){
			
			System.out.println("\n");
			System.out.println("Check Marriage before Legal Age Errors");
		    System.out.println("--------------------------------------");
		    
			for (Map.Entry<String,Individual> entry : gedcomIndividualList.entrySet()) {
				Individual p = entry.getValue();
				Calendar birthDate = entry.getValue().getBirthDate();
				List<String> sFam = entry.getValue().getFams();
				
				for(String sFamily: sFam){
					Family marriedFamily = gedcomFamilyList.get(sFamily);
					Calendar marriageDate  = marriedFamily.getMarriageDate();
					
					if(marriageDate != null && birthDate !=null){
						long yeardiff = marriageDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
						
						if(yeardiff< 18){
							System.out.println("ERROR: " + p.gettName()+ " (Id: "+ p.gettID() + ") is married before legal age");
						}
					}
					
				}
				
			}
	 }
	 
	 public static void checkBirthAfterDeathError(HashMap<String, Individual> gedcomIndividualList, HashMap<String, Family> gedcomFamilyList ){

			System.out.println("\n");
			System.out.println("Birth after the Death of an Individual Errors");
		    System.out.println("---------------------------------------------");
				
				for (Map.Entry<String,Individual> entry : gedcomIndividualList.entrySet()) {
					Calendar birthDate = entry.getValue().getBirthDate();
					Calendar deathDate = entry.getValue().getDeath();
				
					
					if (birthDate != null && deathDate!=null && birthDate.after(deathDate))
						System.out.println("ERROR: " + entry.getValue().gettName()+ " (Id: "+ entry.getValue().gettID() + ") was born after death");
					
					
				}
		}
	 
	
	
	 public static void checkBirthOfChildAfterDeathOfMotherError(HashMap<String, Individual> gedcomPersonList, HashMap<String, Family> gedcomFamilyList ){

			System.out.println("\n");
			System.out.println("Check for Birth of child after the Death of the Mother Errors");
		    System.out.println("---------------------------------------------");
			
		 //   Calendar birthDate = entry.getValue().getBirthDate();
			
		    for (Map.Entry<String,Family> entry : gedcomFamilyList.entrySet())  {
		    	Calendar deathDateMother = gedcomPersonList.get(entry.getValue().getWife()).getDeath();
		    	if(!entry.getValue().getChildren().isEmpty())
		    	{
		    		int flag=0;
		    		 for(int i=0;i<entry.getValue().getChildren().size();i++)
						 {
		    			Calendar birthDateChild = gedcomPersonList.get(entry.getValue().getChildren().get(i)).getBirthDate();
						 if(birthDateChild.after(deathDateMother))
							 System.out.println("Error: birth of child "+gedcomPersonList.get(entry.getValue().getChildren().get(i)).gettName()+"after death of mother"+gedcomPersonList.get(entry.getValue().getWife()).gettName());
						 flag++;
						 }
		    		 if(flag==0)
		 		    	System.out.println("Pass");
		    }
		    
		}
	 }
	
	 public static void checkDivorceAfterDeathError()
	 {
		 System.out.println("\ncheck divorce after death\n");
			
			for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
			{
				
				if(entry1.getValue().getDivorceDate()!=null)
					if(getGedcomIndiList().get(entry1.getValue().getHusband()).getDeath()!=null&&getGedcomIndiList().get(entry1.getValue().getWife()).getDeath()!=null)
				{
					if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getHusband()).getBirthDate()))
						System.out.println("PassH");
					else
						System.out.println("Error:Divorce after death for"+getGedcomIndiList().get(entry1.getValue().getHusband()).gettName());
				
					if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getWife()).getBirthDate()))
						System.out.println("PassW");
					else
						System.out.println("Error:Divorce after death for"+getGedcomIndiList().get(entry1.getValue().getWife()).gettName());
			
				
				}else
					System.out.println("Pass");
			}
			
	 
	 }
	 
	 //Sprint 3
	 public static void checkDivorceDatebeforeBirthdate()
             {
                 System.out.println("\n\nCheck Divorce Date before Birth Date");
                 System.out.println("------------------------------------------");


                            for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
                            {

                                    if(entry1.getValue().getDivorceDate()!=null)

                                    {
                                            if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getHusband()).getBirthDate()))
                                                    System.out.println("Pass");
                                            else
                                                    System.out.println("Error:Divorce Date before Birth Date for Husband: "+getGedcomIndiList().get(entry1.getValue().getHusband()).gettID());

                                            if(!entry1.getValue().getDivorceDate().before(getGedcomIndiList().get(entry1.getValue().getWife()).getBirthDate()))
                                                    System.out.println("Pass");
                                            else
                                                    System.out.println("Error:Divorce Date before Birth Date for Wife: "+getGedcomIndiList().get(entry1.getValue().getWife()).gettID());


                                    }
                                    else
                                            System.out.println("Pass");
                            }
             }

             public static void checkIndividualBirthdatebeforeParentMarriageDate()
             {
                 System.out.println("\n\n Check Individual's Birth Date before Parents' Marriage Date");
                 System.out.println("--------------------------------------------------------------------");

                     for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet()) {

                                    if(entry.getValue().getMarriageDate()!=null && entry.getValue().getBirthDate()!=null)
                                            {
                                                    if(entry.getValue().getMarriageDate().after(entry.getValue().getBirthDate()))
                                                            System.out.println("Error:Birthdate before Parents' Marriage Date of "+entry.getValue().getId());
                                            }
                                    else
                                            System.out.println("Pass");
                            }


             }
             
              public static void CheckBirthMonth(HashMap<String, Individual> gedcomIndividualList, HashMap<String, Family> gedcomFamilyList){
		 System.out.println("\n");
			System.out.println("Mother give birth to her second child within 9 months after her first child birth");
		    System.out.println("---------------------------------------------");
		    for (Map.Entry<String,Family> entry : gedcomFamilyList.entrySet())
		    {
		    	List<String> childran=entry.getValue().getChildren();
		    	if(childran!=null && childran.size()>1){
		    		//Individual child=gedcomIndividualList.get(childran);
		    		
		    		String firstchild =childran.get(0);
		    		Individual first=gedcomIndividualList.get(firstchild);
		    		Calendar birthDate = first.getBirthDate();
		    		
		    		String secondchild =childran.get(1);
		    		Individual second=gedcomIndividualList.get(secondchild);
		    		Calendar birthDate1 = second.getBirthDate();
		    		
		    		
		    	int diff=(birthDate.get(Calendar.MONTH)-(birthDate1.get(Calendar.MONTH)));
		    	if(diff<9){
		    		System.out.println("There are two childran born within 9 months for Family" +entry.getValue().getId()+" first child is "+ first.gettID() +" Second child is"	 +second.gettID());
		    	}
		    	else{
		    		System.out.println("There are no  two childran born within 9 months for Family" +entry.getValue().getId());
		    	}
		    		
		    	}
		    	
		    
		    }
		    	
		 
		 
	 }
	
	public static void checkMultipleMarriages(HashMap<String, Individual> gedcomIndividualList, HashMap<String, Family> gedcomFamilyList){
			System.out.println("\n");
			System.out.println("Multiple Marriages at the same Time Errors");
		    System.out.println("------------------------------------------");
		    
		    
		    
		    for (Map.Entry<String,Individual> entry : gedcomIndividualList.entrySet()) {
		    	int openFamily = 0;
		    	Individual I = entry.getValue();
							
				List<String> fams = I.getFams();
								
				if(fams != null && fams.size() > 1){
					for(String famId: fams){
						Family f = gedcomFamilyList.get(famId);
						if(f.getDivorceDate() == null){
							String h = f.getHusband();
							String w = f.getWife();
							
							Individual husb  = gedcomIndividualList.get(h);
							Individual wife  = gedcomIndividualList.get(w);
							
							if( I.gettID().equals(husb.gettID()) && (wife.getDeath() == null || wife.getDeath().after(husb.getDeath())) ){
								openFamily++;
							}
							else if(I.gettID().equals(wife.gettID()) && (husb.getDeath() == null|| husb.getDeath().after(wife.getDeath()))){
								openFamily++;
							}
						}
					}
					
				}
				
				if(openFamily > 1){
					System.out.println("ERROR: Person " +I.gettID() + I.gettName()+ " is married to multiple people at the same time");
				}
				
			}
		    
		}
		
	 public static void CheckTwinsInFamily(HashMap<String, Individual> gedcomIndividualList, HashMap<String, Family> gedcomFamilyList){
		 System.out.println("\n");
			System.out.println("Twins in the Family");
		    System.out.println("---------------------------------------------");
		   HashMap<Calendar,String>MapBirth=new HashMap<Calendar,String>();
		    
		    for (Map.Entry<String,Family> entry : gedcomFamilyList.entrySet())
		    {
		    	List<String> children=entry.getValue().getChildren();
		    	
		    	String h=entry.getValue().getHusband();
		    	String w=entry.getValue().getWife();
		    	
		    	//Individual husb=gedcomIndividualList.get(h);
		    	//Individual wife=gedcomIndividualList.get(w);
		    	
		    	for(int i =0;i<children.size();i++){
		    		Individual child=gedcomIndividualList.get(children);
		    		
		    		Calendar birthDate = child.getBirthDate();
		    		if(MapBirth.containsKey(birthDate)){
		    			String twin=MapBirth.get(birthDate);
		    			System.out.println("There are Twins in the Family");
		    			System.out.println("Twins in the Family"+ entry.getValue().getId()+" " + child.gettID() +"	"+	twin);
		    			
		    		}
		    		else{
		    			MapBirth.put(birthDate,child.gettID());
		    		}
		    		
		    		
		    		
		    	}
		    	
		    	
		    }
		 
		 
	 }
	 
	  public static void ListMemberBySameBirthMonth()
             {
                 
                 int month = 0, cnt=0;
                 String[] month_name = {"January", "February", "March", "Aprl","May", "June", "July","Augest","September","October","November","December"};
                 System.out.println("\n\n List of all memeber whose Birth Month is same");
                 System.out.println("------------------------------------------");
                 
                 for(int i=1; i<12;i++)
                 {                    
                     for(Map.Entry<String,Family> entry :getGedcomFamily().entrySet())
                     {  
                         int id= Integer.parseInt(entry.getValue().getId());
                         for(int j=0;j<id;j++)
                         {
                         Date Bdate= entry.getValue().getBirthDate();
                         month=Bdate.getMonth();
                         if(month==i)
                         {
                             System.out.println("Name Of Members who born in same month of " +month_name +  " are: " + getGedcomIndiList().get(entry.getValue().getName()).gettName());
                         }
                         }

                     }
                 }
  
             }
             
            public static void listSiblingsInOrder()
	 {
		 System.out.println("\n print siblings in a family in increasing order of their age\n");
		 for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet())
			{
			 int size=entry.getValue().getChildren().size();
			 if(size>1)
				 {
				 	Calendar[] dates=new Calendar[size];
					String[] Id=new String[size];
					for(int i =0;i<size;i++)
					{
						dates[i]=getGedcomIndiList().get(entry.getValue().getChildren().get(i)).getBirthDate();
						Id[i]=getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettID();
					}
			        for (int m = size; m >= 0; m--) 
			        {
			            for (int i = 0; i < size - 1; i++) 
			            {
			                int k = i + 1;
			                if(dates[i].after(dates[k]))
			                {
			                	Calendar temp;
			                	String tem;
			    		        temp = dates[i];
			    		        dates[i] = dates[k];
			    		        dates[k] = temp;
			    		        tem=Id[i];
			    		        Id[i]=Id[k];
			    		        Id[k]=tem;
			    		        

			                }
			            }
			            
			        }
			        System.out.println("children of family "+entry.getValue().getId()+"with their date of births in ascending order" );
			        for(int i=0;i<size;i++)
			        	System.out.println(Id[i]+" "+dates[i].getTime().toString());
						
				 }else if(size==1)
				 {
					 System.out.println("child of family "+entry.getValue().getId() );
			 			System.out.println(getGedcomIndiList().get(entry.getValue().getChildren().get(0)).gettID()+" "+getGedcomIndiList().get(entry.getValue().getChildren().get(0)).getBirthDate().getTime().toString());
				 }
			}
		 
	 }
	 
	  public static void childWithDifferentSurname()
	  {
		  
				  System.out.println("\n print name of the child in a family with a surname different from the family name\n");
					 for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet())
						{
						 String[] FName = getGedcomIndiList().get(entry.getValue().getHusband()).gettName().split(" ");
						 String lastName=FName[FName.length-1];
						
						 int flag=0;
						 int size=entry.getValue().getChildren().size();
						 if(size>=1)
							 {
								String[] childName=new String[size];
								String[] childID=new String[size];
								
								for(int i =0;i<size;i++)
									{
										childName[i]=getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettName();
										childID[i]=getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettID();
										
										String[] childLastName=childName[i].split(" ");
									
												if(!childLastName[childLastName.length-1].equalsIgnoreCase(lastName))
												{	flag=1;
													System.out.println("Child "+" "+childID[i]+childName[i]+" is having different surname from family name "+lastName);
												}
									}
										
							 }
						 if(flag==0)
							 System.out.println("Family "+entry.getValue().getId()+" pass");
						}
		
		 
	  }
	  
	 
		 public static void parentAndChildHaveSameName()
	  	{
		  	System.out.println("\n print siblings in a family in increasing order of their age\n");
			 for (Map.Entry<String,Family> entry : getGedcomFamily().entrySet())
				{
				 String motherName=getGedcomIndiList().get(entry.getValue().getWife()).gettName();
				 String fatherName=getGedcomIndiList().get(entry.getValue().getHusband()).gettName();
				 int flag=0;
				 int size=entry.getValue().getChildren().size();
				 if(size>=1)
					 {
						String[] childName=new String[size];
						for(int i =0;i<size;i++)
							childName[i]=getGedcomIndiList().get(entry.getValue().getChildren().get(i)).gettName();
						for(int i=0;i<size;i++)
							if(childName[i].equalsIgnoreCase(motherName)||childName[i].equalsIgnoreCase(fatherName))
								{	flag=1;
									System.out.println("Error: child "+childName[i]+" has the same name as a parent");
								}
						
								
					 }
				 if(flag==0)
					 System.out.println("Family "+entry.getValue().getId()+" pass");
				}
		 }
			 
		  }
		 public static void marraigeAnniverseryInSixtyDays()
	  	{
		  System.out.println("\n print couples whose anniversery is in the next 60 days\n");
		  
		  int flag=0;
		 // System.out.println(Calendar.getInstance().getTime().toString());
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE, 60);
			//System.out.println(cal.getTime().toString());
			
			 for (Map.Entry<String,Family> entry1 : getGedcomFamily().entrySet())
				 if(entry1.getValue().getMarriageDate()!=null)
				 {
					 Calendar calObj=entry1.getValue().getMarriageDate();
					//	System.out.println(calObj.getTime().toString());
					//	System.out.println();
						calObj.set(Calendar.YEAR,2015);
					
					//	System.out.println(calObj.getTime().toString());
						
						if(calObj.after(Calendar.getInstance())&&calObj.before(cal))
							{
									System.out.println(getGedcomIndiList().get(entry1.getValue().getHusband()).gettName()
									+" and "+getGedcomIndiList().get(entry1.getValue().getWife()).gettName()
									+" have their anniversary in the next 60 days");
									flag=1;
							}
				
				 }
				 if(flag==0)
					 System.out.println("No such record");
				 
					 
	  }
	  
	  public static void printMaleFemale()
		  {
			  
					  System.out.println("\n print name of all the male members and female members in the file seperatelye\n");
						 
					  System.out.println("\n Male members in file\n");

					  for (Map.Entry<String,Individual> entry : getGedcomIndiList().entrySet())
					  {
						  if(entry.getValue().gettGender().equals("M"))
							  System.out.println(entry.getValue().gettName());
					  }
					  
					  System.out.println("\n Female members in file\n");

					  for (Map.Entry<String,Individual> entry : getGedcomIndiList().entrySet())
					  {
						  if(entry.getValue().gettGender().equals("F"))
							  System.out.println(entry.getValue().gettName());
					  }
							 
			
			 
		  }
		  
		  public static void CheckDeathdateAfterTodaydate()
	         {
	             System.out.println("\n\nCheck Death After Today's date\n\n");
	             
	             for (Map.Entry<String,Individual> entry : getGedcomIndiList().entrySet())
	             {
	            	 
	            	 Calendar cal=Calendar.getInstance();
	            	 if(entry.getValue().getDeath()!=null)
	            	 {	
	            		 
	            		 if(entry.getValue().getDeath().after(cal))
	            			 System.out.println("Error:Death date after Current date for "+ entry.getValue().gettName());
	            		 else
	            			 System.out.println(entry.getValue().gettName()+" passed the check" );
	            			 
	            	 }
	                    
	             }
	         }

	 
	 public static void main(String args[]){
		String filename;
		if(args.length > 0){
			filename  = args[0];
		}
		else{
			filename = "sample1.ged";
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
		
		//checkDivorceBeforeMarriageError();
		//checkHusbandWifeGenderError();
		//checkMarriageWithSiblingError();
		//checkDivorceBeforeBirthError();
		//checkMarriageBeforeBirthError();
		//checkMarriageAfterDeathError();
		//checkMarriageBeforeLegalAgeError(gedcomIndi, gedcomFamily);
		//checkBirthAfterDeathError(gedcomIndi, gedcomFamily);
		//checkBirthOfChildAfterDeathOfMotherError(gedcomIndi, gedcomFamily);
		
		//checkDivorceafterTodayDate();
		//checkIndividualBirthdatebeforeParentBirthdate();
		//checkDivorceAfterDeathError();
		
	//	checkDivorceDatebeforeBirthdate();
         //       checkIndividualBirthdatebeforeParentMarriageDate();
          //      CheckBirthMonth(gedcomIndi, gedcomFamily);
	//	checkMultipleMarriages(gedcomIndi, gedcomFamily);
	/	
	//	ListMemberBySameBirthMonth();
	//	CheckTwinsInFamily(gedcomIndi, gedcomFamily);
	
		listSiblingsInOrder();
		childWithDifferentSurname();
		parentAndChildHaveSameName();
		marraigeAnniverseryInSixtyDays();
		
		printMaleFemale();
                CheckDeathdateAfterTodaydate();
                
		}
			
	
	}
	
	

