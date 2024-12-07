package com.boc.util;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



public class DBConnector {

public static Logger log = Logger.getLogger(FileNetConnection.class);  
Connection conn = null;
	/*public Connection getJNDIConnection() throws NamingException,SQLException
	{
		        //String DATASOURCE_CONTEXT = "java:comp/env/jdbc/WFConfig";
		        String DATASOURCE_CONTEXT = "jdbc/WFConfig";
		        Connection conn = null;
		     //try 
		     {
		          Context initialContext = new InitialContext();
		          if ( initialContext == null){
		            log.error("JNDI problem. Cannot get InitialContext.");
		          }
		          DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
		          if (datasource != null) {
		        	  conn = datasource.getConnection();
		          }
		          else {
		        	  log.error("Failed to lookup datasource.");
		          }
		        }
		        catch ( NamingException ex ) {
		          log.error("Cannot get connection: " + ex);
		        }
		        catch(SQLException ex){
		          log.error("Cannot get connection: " + ex);
		        }
		        return conn;
		 }
	*/
	public Connection getJNDIConnection() throws NamingException,SQLException, ClassNotFoundException
	{
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		String url = "jdbc:db2://172.21.20.185:50000/WFCONFIG";
		String user = "WFCONFIG";
		String password ="password123$";
		
		conn = DriverManager.getConnection (url, user, password);   
		return conn;
	}
	
	public List<String> getUserRole(String userName)
	{
		List<String> roleList = null;
        
        try
        {
        	conn = getJNDIConnection();
			Statement statement = conn.createStatement();
			log.info("Obtained Connection object "+conn);
			roleList = new ArrayList<String>();
			String productCategoryBranchQry = "Select roleBase.role_name from  "
					+ " WFCONFIG.USER_ROLE_PRODUCT_CATEGORY_MAPPING leftProductCategory join WFCONFIG.ROLE_BASE roleBase on leftProductCategory.rid=roleBase.rid join WFCONFIG.USR_BASE usrBase "
					+ " on leftProductCategory.uid = usrBase.uid where usrBase.nt_id='"+userName+"'";
			 ResultSet rs = statement.executeQuery(productCategoryBranchQry);
			 while (rs.next()) 
			 {
				 roleList.add(rs.getString(1));
			 }
			 if(null!=roleList)
				 System.out.println("roleList size is "+roleList.size());
			conn.close();
        
    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.error(""+e.getMessage());
		//throw e;
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return roleList;
	}
	

	public static void main(String[] a)
	{
		DBConnector impl2 = new DBConnector();
		impl2.getUserRole("CreditOfficer1");
	}
	
}
