package com.boc.util;

import javax.security.auth.Subject;
import com.filenet.api.collection.*;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.util.*;
import java.io.*;

import filenet.vw.api.*;

public class PeConn {

	public String meth() 
		{
		String b="..";
		// VWSession PEsession = null;
		VWUserInfo userInfo;
		VWSecurityList users;
		String userName;
		int userID;
		VWSecurityList groups;
		String groupName="";
		int groupID;
		try{
		
		VWSession PEsession = new VWSession();
		String ceuri="iiop://172.21.20.185:2809/FileNet/Engine/";
		System.out.println("\n session created");
		System.setProperty("java.security.auth.login. config","C:/FNSW/CE_API/config/jaas.conf.WebSphere");
		System.setProperty("java.naming.provider.url","iiop://localhost:2809");
		System.setProperty("bootclasspath/p","C:\\Program Files\\IBM\\WebSphere\\AppServer\\java\\jre\\lib\\ibmorb.jar;C:\\Program Files\\IBM\\WebSphere\\AppServer\\profiles\\AppSrv01\\properties");
		System.setProperty("java.naming.factory.initial","com.ibm.websphere.naming.WsnInitialContextFactory");
		System.setProperty("filenet.pe.bootstrap.ceuri","iiop://172.21.20.185:2809/FileNet/Engine/");
		System.setProperty("com.ibm.CORBA.ConfigURL","C:/Program Files/IBM/WebSphere/AppServer/profiles/AppSrv01/properties/sas.client.props");
		System.setProperty("java.ext.dirs","C:\\Program Files\\IBM\\WebSphere\\AppServer\\java\\jre\\lib;C:\\Program Files\\IBM\\WebSphere\\AppServer\\java\\jre\\lib\\ext;C:\\Program Files\\IBM\\WebSphere\\AppServer\\lib\\ext;C:\\Program Files\\IBM\\WebSphere\\AppServer\\lib;");
		//String documentRoot =getServletConfig().getServletContext().getRealPath("WcmApiConfig.properties");
		//System.out.println(documentRoot);
		//File f=new File("C:/FNSW/CE_API/lib2/WcmApiConfig.properties");
		//InputStream fin=new FileInputStream(f);
		
		//com.filenet.api.util.WSILoginModule required;
		PEsession.setBootstrapCEURI(ceuri);
		//PEsession.setBootstrapConfiguration(fin);
		PEsession.logon("p8admin_test", "boc@123", "fncp");
		
		System.out.println("\nProcess Engine session is successfully created");
		b=groupName;
		}
		catch (VWException vwe) {
		System.out.println("\nVWException Key: " + vwe.getKey() + "\n");
		System.out.println("VWException Cause Class Name: " + vwe.getCauseClassName() + "\n");
		System.out.println("VWException CauseDescription: " + vwe.getCauseDescription()+ "\n");
		System.out.println("VWException Message: " + vwe.getMessage());
		b=b+" "+vwe.getKey()+"\n"+vwe.getCauseClassName()+" .. \n"+vwe.getCauseDescription()+"\n"+vwe.getMessage()+"...hai";
		}
		catch (Exception e) {
		System.out.println("Error Message:" + e.getMessage());
		b=e.getMessage();
		}
		System.out.println("Hello World!");
		return b;
		}
}