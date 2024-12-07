package com.boc.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

import filenet.vw.api.VWException;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWRoster;
import filenet.vw.api.VWRosterQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWWorkObject;

/**
 * 
 * @author C734363
 *
 */
public class FileNetConnection implements Runnable {
	
	private Thread t;
	private String threadName;

	 public static Logger log = Logger.getLogger(FileNetConnection.class);
	 private static Calendar lastLog4jPropertiesReloadedOn = null;
	 public static String log4jpath;
	 
	  public FileNetConnection() {
		// TODO Auto-generated constructor stub
		 super();
	}
	  
	  public static void init() 
	  {
	     // String propPath = "/opt/subscription/log4j.xml";
	 	try {
	 		PropertyReader property=new PropertyReader();
	 		Properties prop=property.loadPropertyFile();
	 		log4jpath=PropertyReader.getProperty(prop, "LOGPATH");
	 		if(log4jpath!=null){
	 			File fin = new File(log4jpath);
	 		
	 		 Calendar lastModCal = Calendar.getInstance();
	 		 lastModCal.setTimeInMillis(fin.lastModified());
	 		 if(lastLog4jPropertiesReloadedOn != null)
	 		 {
	 		     log.debug((new StringBuilder("Log4j property file last loaded on:[")).append(lastLog4jPropertiesReloadedOn.getTime()).append("] ").append("Log4j property file last modified on:[").append(lastModCal.getTime()).append("]").toString());
	 		 }
	 		 if(lastLog4jPropertiesReloadedOn == null || lastLog4jPropertiesReloadedOn.before(lastModCal))
	 		 {
	 		     DOMConfigurator.configure(log4jpath);
	 		     lastLog4jPropertiesReloadedOn = lastModCal;
	 		     log.debug("Reloaded the Log4j property file as it has been modified since its last loaded time");
	 		 }
	 		}
	 	} catch (Exception e) {
	 		
	 		 log.error("BOCAttachmentEventhandler : init() Failed due to "+e.fillInStackTrace());
	 	}
	  }
	  
	/**
	 * @param args
	 */
	@SuppressWarnings("finally")
	public ObjectStore getConnection() {
	
		if(log.isDebugEnabled()){
			log.debug("FileNetConnection : getConnection() begin");
		}
		ObjectStore ceObjectStore = null;
		try {
			PropertyReader property=new PropertyReader();
			Properties prop=property.loadPropertyFile();
			Connection ceConnection;
			Domain ceDomain;
			String url=PropertyReader.getProperty(prop, "CONNECTION_URL");
			ceConnection = Factory.Connection.getConnection(url);
			Subject ceSubject = UserContext.createSubject(ceConnection, PropertyReader.getProperty(prop, "CONNECTION_USER"), PropertyReader.getProperty(prop, "CONNECTION_PASSWORD"), "FileNetP8WSI");
			UserContext.get().pushSubject(ceSubject);
			
			ceDomain = Factory.Domain.fetchInstance(ceConnection, null, null);
			ceObjectStore = Factory.ObjectStore.fetchInstance(ceDomain, PropertyReader.getProperty(prop, "CONNECTION_OS"), null);
			if(ceObjectStore!=null ){
				if(log.isDebugEnabled()){
					log.debug("FileNetConnection : getConnection() successfull. Object Store connected --"+ceObjectStore.get_DisplayName());
				}	
			}
		} catch (Exception e) {
			log.error("FileNetConnection : getConnection() failed due to "+e.fillInStackTrace());
			
		}finally{
			
			if(log.isDebugEnabled()){
				log.debug("FileNetConnection : getConnection() end");
			}
			return ceObjectStore;
		}
		
		
    
	}
	public VWSession connectToProcessEngine() {
        VWSession peSession = null;
        try {
        	PropertyReader property=new PropertyReader();
			Properties prop=property.loadPropertyFile();
            String userName = PropertyReader.getProperty(prop, "CONNECTION_USER");    
	    String password = PropertyReader.getProperty(prop, "CONNECTION_PASSWORD");
	    String uri = PropertyReader.getProperty(prop, "CONNECTION_URL");
	    
            String connectionpoint = "fncp";
            String jaasConfig = "C:\\workspace\\EventHandler\\PEPurge\\jaas.conf.WebSphere";
            
            System.setProperty("java.security.auth.login.config", jaasConfig);

            com.filenet.api.core.Connection conn = Factory.Connection.getConnection(uri);
            javax.security.auth.Subject subject = UserContext.createSubject(conn, userName, password, "FileNetP8WSI");
            log.debug("Subject Created");
            UserContext uc = UserContext.get();
            uc.pushSubject(subject);
            log.debug("Subject pushed to user context ");

            peSession = new VWSession();
            peSession.setBootstrapCEURI(uri);
            peSession.logon(userName, password, connectionpoint);
            System.out.println(peSession.getObjectStoreSymbolicName());
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while connecting to Process Engine " + e.getMessage());
        }
        return peSession;
    }
	@SuppressWarnings("finally")
	public ObjectStore getPEConnection() {
	
		if(log.isDebugEnabled()){
			log.debug("getPEConnection : getConnection() begin");
		}
		ObjectStore ceObjectStore = null;
		try {
			PropertyReader property=new PropertyReader();
			Properties prop=property.loadPropertyFile();
			Connection ceConnection;
			Domain ceDomain;
			String url=PropertyReader.getProperty(prop, "CONNECTION_URL");
			ceConnection = Factory.Connection.getConnection(url);
			Subject ceSubject = UserContext.createSubject(ceConnection, PropertyReader.getProperty(prop, "CONNECTION_USER"), PropertyReader.getProperty(prop, "CONNECTION_PASSWORD"), "FileNetP8WSI");
			UserContext.get().pushSubject(ceSubject);
			
			ceDomain = Factory.Domain.fetchInstance(ceConnection, null, null);
			ceObjectStore = Factory.ObjectStore.fetchInstance(ceDomain, PropertyReader.getProperty(prop, "CONNECTION_OS"), null);
			if(ceObjectStore!=null ){
				if(log.isDebugEnabled()){
					log.debug("FileNetConnection : getConnection() successfull. Object Store connected --"+ceObjectStore.get_DisplayName());
				}	
			}
		} catch (Exception e) {
			log.error("FileNetConnection : getConnection() failed due to "+e.fillInStackTrace());
			
		}finally{
			
			if(log.isDebugEnabled()){
				log.debug("FileNetConnection : getConnection() end");
			}
			return ceObjectStore;
		}
		
		
    
	}
	
	public static void main(String args[]) throws Exception{
		
	//	VWSession peSession =new FileNetConnection().connectToProcessEngine();
		//PeConn pc = new PeConn();
		//pc.meth();
		//log.debug(peSession.getPEServerName());
	//	String l ="01/10/2016" ;//args[0];
    //    String m ="13/10/2016"; //args[1];
		
		//new FileNetConnection().connectToProcessEngine();
		  FileNetConnection R1 = new FileNetConnection();
	      R1.start();
	      
	      FileNetConnection R2 = new FileNetConnection();
	      R2.start();
	      
	      FileNetConnection R3 = new FileNetConnection();
	      R3.start();
		
	}
	
	 public static Integer doConnectPE_WSI() throws Exception  {
	       log.debug("doConnectPE_WSI()");
	       
	       PropertyReader property=new PropertyReader();
			Properties prop=property.loadPropertyFile();
           String userName = PropertyReader.getProperty(prop, "CONNECTION_USER");    
           String password = PropertyReader.getProperty(prop, "CONNECTION_PASSWORD");
           String uri = PropertyReader.getProperty(prop, "CONNECTION_URL");
	    	
           String connectionpoint =PropertyReader.getProperty(prop, "CONNECTION_POINT");
           String jaasConfig = PropertyReader.getProperty(prop, "CONNECTION_JAAS_PATH");
           
           System.setProperty("java.security.auth.login.config", jaasConfig);

           com.filenet.api.core.Connection conn = Factory.Connection.getConnection(uri);
           javax.security.auth.Subject subject = UserContext.createSubject(conn, userName, password, "FileNetP8WSI");
           log.debug("Subject Created");
           UserContext uc = UserContext.get();
           uc.pushSubject(subject);
           log.debug("Subject pushed to user context ");
	       log.debug("Logon to "+uri);
	       VWSession vwSession = new VWSession();
	       try {
	    	   vwSession.setBootstrapCEURI(uri);
	    	   vwSession.logon(userName, password, connectionpoint);
	    	   log.debug("Logon to ObjectStore "+vwSession.getObjectStoreSymbolicName());
	      } catch (VWException e) {
	         e.printStackTrace();
	         return -1;
	      }
	       
	       log.debug("Connect to PE Success!");
	       log.debug("Getting roster info...!");
	       
	      VWRoster roster=null;
	         try {
	            roster = vwSession.getRoster(PropertyReader.getProperty(prop, "CONNECTION_ROSTER"));
	         } catch (VWException e) {
	            e.printStackTrace();
	            return -1;
	         }
	           try {
	          //  log.debug("Workflow Count: " + roster.fetchCount());
	            int queryFlags=VWRoster.QUERY_READ_UNWRITABLE;
	            int queryType=VWFetchType.FETCH_TYPE_WORKOBJECT;
	            
	            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	            Date startDate = (Date)formatter.parse(PropertyReader.getProperty(prop, "START_DATE")); 
	          //  java.util.Date startDate = SimpleFormatter.
	            
	            long startDateMS = startDate.getTime()/1000; 
	            Date endDate = (Date)formatter.parse(PropertyReader.getProperty(prop, "END_DATE")); 
	            
	            long endDateMS = endDate.getTime()/1000; 
	            
	            String queryFilter = "F_StartTime > "+startDateMS+" AND F_StartTime < "+endDateMS;
	         //   roster.setBufferSize(500);
	            VWRosterQuery query = roster.createQuery(null,null,null,queryFlags,queryFilter, null,queryType);
	            log.debug("VWRosterQuery Count: " + query.fetchCount());
	            System.out.println("VWRosterQuery Count: " + query.fetchCount());  
	            int count=0;
	            int deleteCount= Integer.parseInt(PropertyReader.getProperty(prop, "DELETE_COUNT"));
	            while(count<=deleteCount && query.hasNext()){
	            VWWorkObject xc =(VWWorkObject) query.next();
	            if(PropertyReader.getProperty(prop, "DELETE_FLAG").equalsIgnoreCase("true")){
	            	String wob=xc.getWorkflowNumber();
	            	xc.doDelete(true, true);
	            	log.debug("Deleted work item successfully -" + wob); 
	            	count++;	
	            }
	            }  
	            log.debug("Total Deleted work items count -" + count); 
	         } catch (VWException e) {
	            e.printStackTrace();
	            return -1;
	         }
	         
	       
	       
	       log.debug("Logoff from PE...");
	       try {
	         vwSession.logoff();
	      } catch (VWException e) {
	         e.printStackTrace();
	         return -1;
	      }
	      log.debug("doConnectPE_WSI() - end");
	      return 1;
	   }

	@Override
	public void run() {
		System.out.println("Running " +  threadName );
		init();
		try {
			doConnectPE_WSI();
			Thread.sleep(50);
		} catch (Exception e) {
			System.out.println("Thread " +  threadName + " interrupted.");
			e.printStackTrace();
		}
		
	}
	 public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	 }

}
