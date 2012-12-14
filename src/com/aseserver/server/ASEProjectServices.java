package com.aseserver.server;

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

import com.aseproject.checkin.CheckIn;
import com.aseproject.location.Locations;
import com.aseproject.login.UserAuth;
import com.aseproject.review.Review;

/**
 * This class provides all the necessery methods that the Android application requires.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 * @see CheckIn
 * @see Review
 * @see UserAuth
 * @see Locations
 */
public class ASEProjectServices 
{
	public ASEProjectServices(){}
	
	/**
    * This method is used for loging in or registering a user.
    * @param data UserAuth
    * @return debugging information. 
    */	
	@Resource(name="jdbc/Users")
	public String loginOrRegister(byte[] data)
	{
		  // initialize debuging information.
		  String output="WS : ";
		  try
		  {
			  UserAuth appData = new UserAuth("","","");
			  try
			  { 
				  // get input and cast it to UserAuth object.
				  appData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  // if user's email is null , that means that he asks for login.
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
				  String picture = "";
				  int isInvis = 0;
				  int day = 0;
				  int month = 0;
				  int year = 0;
				  
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"' OR email='"+email+"'");
				  ResultSet result = statement.executeQuery();
				  
				  // if user doesnt exist on our database , register him.
				  if(!result.next())
				  {
					  PreparedStatement insertStatement =  con.prepareStatement("INSERT INTO User VALUES('"+name+"','"+password+"','"+email+"','"+firstName+ "','" +lastName+"','"
				  +gender+"','"+day+"','"+month+"','"+year+"','"+picture+"','"+isInvis+"')");
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
	
	/**
    * This method is used for retrieving a user's profile.
    * @param data UserAuth
    * @return user's profile UserAuth. 
    */	
	@Resource(name="jdbc/Users")
	public byte[] retrieveProfile(byte[] data)
	{
		  byte[] byteData = new byte[1];
		  UserAuth userData = new UserAuth("");
		  try
		  {
			  try
			  { 
				// get input and cast it to UserAuth object.
				  userData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String nickname = userData.getUsername();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+nickname+"'");
			  ResultSet result = statement.executeQuery();
			  
			  // if the user exists , send back his data.
			  if(result.next())
			  {
				  userData = new UserAuth(result.getString("username"),result.getString("firstName"),result.getString("lastName"),result.getString("gender"),result.getInt("day"),result.getInt("month"),result.getInt("year"),result.getString("IMAGE"),result.getInt("isInvis"));
			  }
			  else
			  {
				  
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
	/**
    * This method is used for updating a user's profile.
    * @param data UserAuth
    * @return debugging information. 
    */	
	public String updateProfile(byte[] data)
	{
		  String output="WS : ";
		  try
		  
		  {
			  UserAuth updateData = new UserAuth("","","","",0,0,0);
			  try
			  { 
				// get input and cast it to UserAuth object.
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
				  
				  // if the user exists , update his data.
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
	/**
    * This method is used for changing a user's visibility.
    * @param data UserAuth
    * @return debugging information. 
    */	
	public String addVisibility(byte[] data)
	{
		  String output="WS : ";
		  try	  
		  {
			  UserAuth updateData = new UserAuth("", 0);
			  try
			  { 
				  // get input and cast it to UserAuth object.
				  updateData = (UserAuth) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = updateData.getUsername();
			  int isInvis = updateData.getVisibility();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
			  ResultSet result = statement.executeQuery();
			  
			  //if the user exists , update his entries.
			  if(result.next())
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("UPDATE User SET isInvis='"+isInvis+"'");
				  insertStatement.executeUpdate();
				  output="VisibilityChanged";
			  }
			  else
			  {
				  output="VisibilityNotChanged";
			  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	/**
    * This method is used for deleting a user's profile.
    * @param data UserAuth
    * @return debugging information. 
    */		
	public String deleteProfile(byte[] data)
	{
		  String output="WS : ";
		  try		  
		  {
			  // get input and cast it to UserAuth object.
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
			  
			  // if the user exists delete his data.
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
	/**
    * This method is used for altering a user's review.
    * @param data Review
    * @return debugging information. 
    */	
	public String alterReview(byte[] data)
	{
		  String output="WS : ";
		  try		  
		  {
			  Review reviewData = new Review("","","",0,0,0);
			  try
			  { 
				  // get input and cast it to Review object.
				  reviewData = (Review) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = reviewData.getUsername();		
			  String review = reviewData.getReviewText();
			  Timestamp dat = reviewData.getTimeDate();
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
			  ResultSet result = statement.executeQuery();
			  
			  // if the review exists , alter its entries.
			  if(result.next())
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("UPDATE Reviews set review='"+review+"' where date='"+dat+"'");
				  insertStatement.executeUpdate();
				  output="ReviewAlterTrue";
			  }
			  else
			  {
				  output="ReviewAlterFalse";
			  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	/**
    * This method is used for deleting a user's checkin.
    * @param data CheckIn
    * @return debugging information.  
    */	
	public String deleteCheckIn(byte[] data)
	{
		  String output="WS : ";
		  try		  
		  {			 
			  Timestamp dat = null;
			  CheckIn checkInData = new CheckIn("","",dat);
			  try
			  { 
				  // get input and cast it to CheckIn object.
				  checkInData = (CheckIn) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = checkInData.getUsername();				  
			  dat = checkInData.getTimeDate();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
			  ResultSet result = statement.executeQuery();
			  
			  // if the check in exists , delete its entries.
			  if(result.next())
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("DELETE FROM CheckIn where username='"+name+"' AND date='"+dat+"'");
				  insertStatement.executeUpdate();
				  output="DeleteCheckInTrue";
			  }
			  else
			  {
				  output="DeleteCheckInFalse";
			  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	/**
    * This method is used for deleting a user's review.
    * @param data Review
    * @return debugging information. 
    */	
	public String deleteReview(byte[] data)
	{
		  String output="WS : ";
		  try		  
		  {			  
			  Timestamp dat = null;
			  Review reviewData = new Review("",dat);
			  try
			  { 
				// get input and cast it to Review object.
				  reviewData = (Review) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e)
			  {
				  output = output.concat("CAST error "+e.toString());
			  }
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String name = reviewData.getUsername();				  
			  dat = reviewData.getTimeDate();
			  
			  PreparedStatement statement =  con.prepareStatement("SELECT * FROM User WHERE username='"+name+"'");
			  ResultSet result = statement.executeQuery();
			  
			  //if the review exists , delete its entries.
			  if(result.next())
			  {
				  PreparedStatement insertStatement =  con.prepareStatement("DELETE FROM Reviews where username='"+name+"' AND date='"+dat+"'");
				  insertStatement.executeUpdate();
				  output="DeleteReviewTrue";
			  }
			  else
			  {
				  output="DeleteReviewFalse";
			  }
		  }
		  catch(Exception e)
		  {
			  output = output.concat("GENERAL error "+e.toString());
		  }
		   
		  return output;
	}
	/**
    * This method is used for adding a user's location.
    * @param data Locations
    * @return debugging information.  
    */	
	@Resource(name="jdbc/Users")
	public String addLocations(byte[] data)
	{
		  String output="WS : ";
		  try
		  {			 
			  Locations locData = new Locations("",0.0,0.0);
			  try
			  { 
				  // get input and cast it to Locations object.
				  locData = (Locations) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
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
			  
			  // insert location information into the database
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
	/**
    * This method is used for adding a user's checkin.
    * @param data CheckIn
    * @return debugging information. 
    */	
	@Resource(name="jdbc/Users")
	public String addCheckIn(byte[] data)
	{
		  String output="WS : ";
		  try
		  {			  
			  Timestamp dat = null;
			  CheckIn chInData = new CheckIn("","",dat);
			  try
			  { 
				  // get input and cast it to CheckIn object.
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
			  
			  // insert the new Check in into the database.
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
	/**
    * This method is used for getting a list of checkins.
    * @param data CheckIn
    * @return a list of checkins. 
    */	
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
				  // get input and cast it to CheckIn object.
				  chInData = (CheckIn) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String location = chInData.getLocation();
			  String nickname = chInData.getUsername();
			  ResultSet result = null;
			 
			  // if username is null , list of checkins based on location , else based on username.
			  if(chInData.getUsername()==null) 
			  {
				  PreparedStatement statement =  con.prepareStatement("select CheckIn.username, CheckIn.location, CheckIn.date, User.IMAGE from CheckIn inner join User on CheckIn.username=User.username where CheckIn.location='"+location+"' order by date desc");
				  result = statement.executeQuery();
				  while (result.next())
				  {
					  chInData = new CheckIn(result.getString("username"),result.getString("location"),result.getTimestamp("date"),result.getString("IMAGE"));
					  checkIns.add(chInData);
				  }
			  } 
			  else 
			  {
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM CheckIn WHERE username='"+nickname+"' order by date desc");
				  result = statement.executeQuery();
				  while (result.next())
				  {
					  chInData = new CheckIn(result.getString("username"),result.getString("location"),result.getTimestamp("date"));
					  checkIns.add(chInData);
				  }
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
	/**
    * This method is used for adding a review.
    * @param data UserAuth
    * @return debugging information. 
    */	
	@Resource(name="jdbc/Users")
	public String addReview(byte[] data)
	{
		  String output="WS : ";
		  try
		  {
			  Review review = new Review("","","",0,0,0);
			  try
			  { 
				  // get input and cast it to Review object.
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
			  
			  // check if the user has already submitted a review. if not insert it to the database , else alter the relevant entries.
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
	/**
    * This method is used for getting a list of reviews.
    * @param data Review
    * @return a list of Reviews. 
    */	
	@Resource(name="jdbc/Users")
	public byte[] getReviews(byte[] data)
	{
		  ArrayList<Review> reviews = new ArrayList<Review>();
		  byte[] byteData = new byte[1];
		  try
		  {
			  Review review = new Review("","","",0,0,0);
			  try
			  { 
				  // get input and cast it to Review object.
				  review = (Review) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
			  }
			  catch(Exception e){e.printStackTrace();}
			  
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Users","root","root");
			  
			  String location = review.getLocation();
			  String nickname = review.getUsername();
			  ResultSet result = null;
			  
			  // if username is null , list of Reviews based on location , else based on username.
			  if(review.getUsername()==null) 
			  {
				  PreparedStatement statement =  con.prepareStatement("SELECT Reviews.username,Reviews.location,Reviews.review,Reviews.rating,Reviews.likes,Reviews.dislikes,User.IMAGE,Reviews.date FROM Reviews inner join User on Reviews.username=User.username WHERE Reviews.location='"+location+"' order by date desc");
				  result = statement.executeQuery();
				  
				  while (result.next())
				  {
					  review = new Review(result.getString("username"),result.getString("location"),result.getString("review"),result.getInt("rating"),result.getInt("likes"),result.getInt("dislikes"),result.getString("IMAGE"),result.getTimestamp("date"));
					  reviews.add(review);
				  }
			  } 
			  else 
			  {
				  PreparedStatement statement =  con.prepareStatement("SELECT * FROM Reviews WHERE username='"+nickname+"' order by date desc");
				  result = statement.executeQuery();
				  
				  while (result.next())
				  {
					  review = new Review(result.getString("username"),result.getString("location"),result.getString("review"),result.getInt("rating"),result.getInt("likes"),result.getInt("dislikes"),result.getTimestamp("date"));
					  reviews.add(review);
				  }
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
