package pkg;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;

import com.example.ase_map.UserAuth;

public class Android 
{
	public Android(){}
	
	@Resource(name="jdbc/Users")
	public String getData(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  UserAuth appData = new UserAuth("","","");
			  try
			  { 
				  //byte[] decoded = Base64Coder.decode(data.toString());
				  appData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  if(appData.getEmail()==null)
			  {
				  String name=appData.getUsername();
				  String password=appData.getPassword();
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"' AND password='"+password+"'");
				  ResultSet result = statement.executeQuery();
				  
				  if(!result.next())
				  {
					  output = output.concat("STATEMENT ERROR");
				  }
				  else
				  {
					  output="LoginTrue";
				  }
			  }
			  else
			  {
				  String name=appData.getUsername();
				  String password=appData.getPassword();
				  String email=appData.getEmail();
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"' OR email='"+email+"'");
				  ResultSet result = statement.executeQuery();
				  
				  if(!result.next())
				  {
					  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO User VALUES('"+name+"','"+password+"','"+email+"')");
					  insertStatement.executeUpdate();
					  output="RegisterTrue";
				  }
				  else
				  {
					  output="RegisterFalse";
				  }
			  }
		  	 
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
}
