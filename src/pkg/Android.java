package pkg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.annotation.Resource;

import com.example.ase_map.CheckIn;
import com.example.ase_map.LocShare;
import com.example.ase_map.Review;
import com.example.ase_map.UserAuth;
import com.mysql.jdbc.Blob;

public class Android 
{
	public Android(){}
	
	@Resource(name="jdbc/Users")
	public String login(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  UserAuth appData = new UserAuth("","","","","");
			  try
			  { 
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
				  String firstName = "";
				  String lastName = "";
				  String gender = "";
				  Object picture = null;
				  int day = 0;
				  int month = 0;
				  int year = 0;
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"' OR email='"+email+"'");
				  ResultSet result = statement.executeQuery();
				  
				  if(!result.next())
				  {
					  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO User VALUES('"+name+"','"+password+"','"+email+"','"+firstName+ "','" +lastName+"','"
				  +gender+"','"+day+"','"+month+"','"+year+"','"+picture+"')");
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
	
	@Resource(name="jdbc/Users")
	public byte[] retrieveProfile(byte[] data)
	{
		  byte[] byteData = new byte[1];
		  UserAuth userData = new UserAuth("");
		  try
		  {
			  try
			  { 
				  userData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String nickname = userData.getUsername();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+nickname+"'");
			  ResultSet result = statement.executeQuery();
			  
			  if(result.next())
			  {
				  userData = new UserAuth(result.getString("username"),result.getString("firstName"),result.getString("lastName"),result.getString("gender"),result.getInt("day"),result.getInt("month"),result.getInt("year"),result.getString("IMAGE"));
			  }
			  else
			  {
				  userData = new UserAuth("DebugUserException","fousekis","matsablokos","male",1,1,1,null); 
			  }
		   }
		  catch(Exception e){e.printStackTrace();}
		  	  
		  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		  ObjectOutput out = null;
		  try 
		  {
		    out = new ObjectOutputStream(bos);   
		    out.writeObject(userData);
		    byteData= bos.toByteArray();
		    out.close();
		    bos.close();
		  } 
		  catch (IOException e) {e.printStackTrace(); }

		return byteData;
	}
	
	public String update(byte[] data)
	{
		  String output="WS : ";
		  try
		  
		  {
			  UserAuth updateData = new UserAuth("","","","",0,0,0);
			  try
			  { 
				  updateData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
				  String name = updateData.getUsername();
				  String firstName = updateData.getFirstName();
				  String lastName = updateData.getLastName();
				  String gender = updateData.getGender();
				  int day = updateData.getDay();
				  int month = updateData.getMonth();
				  int year = updateData.getYear();
				  String picture = updateData.getPicture();
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
				  ResultSet result = statement.executeQuery();
				  
				  if(result.next())
				  {
					  PreparedStatement insertStatement =  con.prepareStatement("UPDATE User SET firstName='"+firstName+"', "+"lastName='"+lastName+"', "
					  +"gender='"+gender+"', "+"day='"+day+"', "+"month='"+month+"', "+"year='"+year+"', "+"IMAGE='"+picture+"' "+"WHERE username='"+name+"'");
					  
					  insertStatement.executeUpdate();
					  output="ProfileUpdateTrue";
				  }
				  else
				  {
					  output="ProfileUpdateFalse";
				  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	
	
	public String delete(byte[] data)
	{
		  String output="WS : ";
		  try
		  
		  {
			  UserAuth updateData = new UserAuth("","","","",0,0,0);
			  try
			  { 
				  updateData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
				  String name = updateData.getUsername();
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
				  ResultSet result = statement.executeQuery();
				  
				  if(result.next())
				  {
					  PreparedStatement insertStatement =  con.prepareStatement("DELETE FROM User where username='"+name+"'");
					  insertStatement.executeUpdate();
					  output="DeleteTrue";
				  }
				  else
				  {
					  output="DeleteFalse";
				  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	
	@Resource(name="jdbc/Users")
	public String getLocations(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  LocShare locData = new LocShare("",0.0,0.0);
			  try
			  { 
				  locData = (LocShare) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = locData.getUsername();
			  Double longitude = locData.getLongitude();
			  Double latitude = locData.getLatitude();
			  
			  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO Locations (username,longitude,latitude) VALUES('"+name+"','"+longitude+"','"+latitude+"')");
			  insertStatement.executeUpdate();
			  output="LocUpdateTrue";
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		  return output;
	}
	
	@Resource(name="jdbc/Users")
	public String checkIn(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  Timestamp dat = null;
			  CheckIn chInData = new CheckIn("","",dat);
			  try
			  { 
				  chInData = (CheckIn) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = chInData.getUsername();
			  String location = chInData.getLocation();
			  Timestamp date = chInData.getTimeDate();
			  
			  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO CheckIn (username,location,date) VALUES('"+name+"','"+location+"','"+date+"')");
			  insertStatement.executeUpdate();
			  output="CheckInUpdateTrue";
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		  return output;
	}
	
	@Resource(name="jdbc/Users")
	public byte[] getCheckIns(byte[] data)
	{
		  ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
		  byte[] byteData = new byte[1];
		  try
		  {
			  Timestamp dat = null;
			  CheckIn chInData = new CheckIn("","",dat);
			  try
			  { 
				  chInData = (CheckIn) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String location = chInData.getLocation();
			  String nickname = chInData.getUsername();
			  ResultSet result = null;
			  
			  if(chInData.getUsername()==null) {
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM CheckIn WHERE location='"+location+"'");
			  result = statement.executeQuery();
			  } else {
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM CheckIn WHERE username='"+nickname+"'");
				  result = statement.executeQuery();
			  }
			  
			  while (result.next())
			  {
				  chInData = new CheckIn(result.getString("username"),result.getString("location"),result.getTimestamp("date"));
				  checkIns.add(chInData);
			  }
		   }
		  catch(Exception e){e.printStackTrace();}
		  	  
		  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		  ObjectOutput out = null;
		  try 
		  {
		    out = new ObjectOutputStream(bos);   
		    out.writeObject(checkIns);
		    byteData= bos.toByteArray();
		    out.close();
		    bos.close();
		  } 
		  catch (IOException e) {e.printStackTrace(); }

		return byteData;
	}
	
	@Resource(name="jdbc/Users")
	public String review(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  Review review = new Review("","","",0,0,0);
			  try
			  { 
				  review = (Review) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = review.getUsername();
			  String location = review.getLocation();
			  String reviewText = review.getReviewText();
			  int rating = review.getRating();
			  int likes = review.getLikes();
			  int dislikes = review.getDislikes();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM Reviews WHERE location='"+location+"' AND username='"+name+"'");
			  ResultSet result = statement.executeQuery();
			  
			  if(result.next())
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("UPDATE Reviews SET likes='"+likes+"', dislikes='"+dislikes+"' ,review='"+reviewText+"' ,rating='"+rating+"' WHERE location='"+location+"' AND username='"+name+"'");
				  insertStatement.executeUpdate();
				  output="ReviewUpdateTrue , updated review!";
			  }
			  else
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO Reviews (username,location,review,rating,likes,dislikes) VALUES('"+name+"','"+location+"','"+reviewText+"','"+rating+"','"+likes+"','"+dislikes+"')");
				  insertStatement.executeUpdate();
				  output="ReviewUpdateTrue , inserted new review!";
			  }			  
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		  return output;
	}
	
	@Resource(name="jdbc/Users")
	public byte[] getReview(byte[] data)
	{
		  ArrayList<Review> reviews = new ArrayList<Review>();
		  byte[] byteData = new byte[1];
		  try
		  {
			  Review review = new Review("","","",0,0,0);
			  try
			  { 
				  review = (Review) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String location = review.getLocation();
			  String nickname = review.getUsername();
			  ResultSet result = null;
			  
			  if(review.getUsername()==null) {
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM Reviews WHERE location='"+location+"'");
			  result = statement.executeQuery();
			  } else {
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM Reviews WHERE username='"+nickname+"'");
				  result = statement.executeQuery();
			  }
			  
			  while (result.next())
			  {
				  review = new Review(result.getString("username"),result.getString("location"),result.getString("review"),result.getInt("rating"),result.getInt("likes"),result.getInt("dislikes"));
				  reviews.add(review);
			  }
		   }
		  catch(Exception e){e.printStackTrace();}
		  	  
		  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		  ObjectOutput out = null;
		  try 
		  {
		    out = new ObjectOutputStream(bos);   
		    out.writeObject(reviews);
		    byteData= bos.toByteArray();
		    out.close();
		    bos.close();
		  } 
		  catch (IOException e) {e.printStackTrace(); }

		return byteData;
	}
}
