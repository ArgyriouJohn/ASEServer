package pkg;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;

import com.example.ase_map.ServerData;

public class Android 
{
	public Android(){}
	
	@Resource(name="jdbc/Users")
	public String getData(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  ServerData appData = new ServerData("","",0.0,0.0);
			  try
			  { 
				  //byte[] decoded = Base64Coder.decode(data.toString());
				  appData = (ServerData) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String email=appData.getEmail();
			  double longitude=appData.getLongitude();
			  double latitude=appData.getLatitude();
			  
			  PreparedStatement statement =  con.prepareStatement("INSERT INTO GPSDATA VALUES('"+email+"','"+longitude+"','"+latitude+"')");
			  statement.executeUpdate();
		  
			  /*while(result.next())
			  {
				  //ring customerInfo = result.toString();
				  //Here "&"s are added to the return string. This is help to split the string in Android application 
			  }*/
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
}
