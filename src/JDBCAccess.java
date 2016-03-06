//Samuel Walton
//Program Assignment1

import java.io.*;
import java.sql.*;
//import java.util.*;


//This is the main class

class JDBCAccess
{

	public static void main(String[] args)
	{
		int ch = 0;

		while (ch != 9)
		{
			try{
				Connection con = getConnection();
				con.setAutoCommit(false);
				Statement stm = con.createStatement();
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String line;

				ch = 0;
				System.out.println("\nEnter 1 to retrieve the detials");
				System.out.println(" 2 to update database");
				System.out.println(" 3 to insert into database");
				System.out.println(" 4 to delete from the database");
				System.out.println(" 5 to search the database");
				System.out.println(" 9 to quit");

				line = br.readLine();
				ch=Integer.parseInt(line); 
				System.out.println("\nThe choice is "+ch);

				switch(ch)
				{
				case 1:getTableInfo("Guest",stm);
				stm.close();
				break;

				case 2:update1("Guest",con);
				con.commit();
				stm.close();
				break;

				case 3:insert("Guest",con);
				con.commit();
				stm.close();
				break;

				case 4:delete("Guest",stm);
				con.commit();
				stm.close();
				break;

				case 5:search("Guest",stm);
				con.commit();
				stm.close();
				break;

				default:break; 
				}//endsw
			}//endtr

			catch(SQLException e){
				System.out.println("SQLException:");
				System.out.println("Message: " + e.getMessage());
				break;
			}//endca

			catch(IOException e){
				System.out.println("IOException:");
				System.out.println("Message: " + e.getMessage());
				break;
			}//endca
		}//endwh
	}//endfn


	
	private static Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/project1?" + "user=root&password=greatsqldb");
			System.out.println("Got Mysql database connection");
			return conn;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}
	//This method does the retrieving of the data

	public static void getTableInfo(String table, Statement stm)throws SQLException,IOException
	{
		String qry = "Select * From "+ table;
		ResultSet rs = stm.executeQuery(qry);
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();

		//For each row in the table
		System.out.print("Fname,Lname,Email,Address1,Address2,Phone,City,State,Zip,LoginID,Password\n");

		while (rs.next())
		{
			System.out.println();	

			//For Each Column in the row
			for (int i=1;i<=count; i++)
			{
				System.out.print(rs.getString(i) + ((count-i!=0)?", ":"\n\r"));
			}//endfr
		}//endwh
		System.out.println("\n");
		rs.close();
	}//endfn


	//This method does the updating part
	public static void update1(String table, Connection conn)throws SQLException,IOException
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String Fname,Lname,Email,Address1,Address2,Phone,City,State,Zip,LoginID,Password;
			//Initialize null objects

			System.out.println("\nYou must enter a value for everything\n");
			System.out.println("Enter Login ID: "); 		LoginID 	                = br.readLine();
			System.out.println("Enter password: "); 		Password 	= br.readLine();
			System.out.println("Enter First Name: "); 		Fname 		= br.readLine();
			System.out.println("Enter Last Name: "); 		Lname 		= br.readLine();
			System.out.println("Enter e-mail: "); 		Email 		= br.readLine();
			System.out.println("Enter Address line 1: ");	Address1  	= br.readLine();
			System.out.println("Enter Address line 2: "); 	Address2  	= br.readLine();
			System.out.println("Enter City: "); 		City		= br.readLine();
			System.out.println("Enter State: "); 		State 		= br.readLine();
			System.out.println("Enter Zip code: "); 		Zip 		= br.readLine();
			System.out.println("Enter Phone Number: "); 	Phone 		= br.readLine();

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE "+table+" SET Fname=?, Lname=?, Email=?, "
							+"Address1=?, Address2=?, Phone=?, City=?, State=?, Zip=?, Password=? "
							+"WHERE LoginID='"+LoginID+"'");

			if (Fname.length() > 1) 	pstmt.setString(1,Fname);		else pstmt.setNull(1,Types.VARCHAR);
			if (Lname.length() > 1) 	pstmt.setString(2,Lname); 		else pstmt.setNull(2,Types.VARCHAR);
			if (Email.length() > 1)	pstmt.setString(3,Email);		else pstmt.setNull(3,Types.VARCHAR);
			if (Address1.length() > 1)         pstmt.setString(4,Address1);		else pstmt.setNull(4,Types.VARCHAR);
			if (Address2.length() > 1)         pstmt.setString(5,Address2);		else pstmt.setNull(5,Types.VARCHAR);
			if (Phone.length() > 1) 	pstmt.setString(6,Phone);		else pstmt.setNull(6,Types.VARCHAR);
			if (City.length() > 1) 		pstmt.setString(7,City);		else pstmt.setNull(7,Types.VARCHAR);
			if (State.length() > 1) 	pstmt.setString(8,State);		else pstmt.setNull(8,Types.VARCHAR);
			if (Zip.length() > 1) 		pstmt.setInt(9,Integer.parseInt(Zip));	else pstmt.setNull(9,Types.DECIMAL);
			if (Password.length() > 1) 	pstmt.setString(10,Password);		else pstmt.setNull(10,Types.VARCHAR);

			// Execute the update statement
			if (pstmt.executeUpdate() == 1) 
			{
				System.out.println("Database Updated, 1 Row Changed");
			}else{
				System.out.println("Database not changed, LoginID not Found");
			}//endif

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}//endca

	}//endfn

	//This method does the inserting part

	public static void insert(String table, Connection conn)throws SQLException,IOException
	{
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String Fname,Lname,Email,Address1,Address2,Phone,City,State,Zip,LoginID,Password;
			System.out.println("\nYou must enter a value for everything\n");
			System.out.println("Enter Login ID: "); 		LoginID 	= br.readLine();
			System.out.println("Enter password: "); 		Password = br.readLine();
			System.out.println("Enter First Name: "); 		Fname 	= br.readLine();
			System.out.println("Enter Last Name: "); 		Lname 	= br.readLine();
			System.out.println("Enter e-mail: "); 		Email 	= br.readLine();
			System.out.println("Enter Address line 1: ");	Address1 	= br.readLine();
			System.out.println("Enter Address line 2: "); 	Address2 	= br.readLine();
			System.out.println("Enter City: "); 		City	= br.readLine();
			System.out.println("Enter State: "); 		State 	= br.readLine();
			System.out.println("Enter Zip code: "); 		Zip 	= br.readLine();
			System.out.println("Enter Phone Number: "); 	Phone 	= br.readLine();

			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO "+table+" (Fname,Lname,Email,"
							+"Address1,Address2,Phone,City,State,Zip,LoginID,Password)"
							+" VALUES (?,?,?,?,?,?,?,?,?,?,?)");

			if (Fname.length() > 1) 	pstmt.setString(1,Fname);		else pstmt.setNull(1,Types.VARCHAR);
			if (Lname.length() > 1) 	pstmt.setString(2,Lname); 		else pstmt.setNull(2,Types.VARCHAR);
			if (Email.length() > 1)	pstmt.setString(3,Email);		else pstmt.setNull(3,Types.VARCHAR);
			if (Address1.length() > 1) 	pstmt.setString(4,Address1);		else pstmt.setNull(4,Types.VARCHAR);
			if (Address2.length() > 1) 	pstmt.setString(5,Address2);		else pstmt.setNull(5,Types.VARCHAR);
			if (Phone.length() > 1) 	pstmt.setString(6,Phone);		else pstmt.setNull(6,Types.VARCHAR);
			if (City.length() > 1) 		pstmt.setString(7,City);		else pstmt.setNull(7,Types.VARCHAR);
			if (State.length() > 1) 	pstmt.setString(8,State);		else pstmt.setNull(8,Types.VARCHAR);
			if (Zip.length() > 1) 		pstmt.setInt(9,Integer.parseInt(Zip));	else pstmt.setNull(9,Types.DECIMAL);
			if (LoginID.length() > 1) 	pstmt.setString(10,LoginID);		else pstmt.setNull(10,Types.VARCHAR);
			if (Password.length() > 1) 	pstmt.setString(11,Password);		else pstmt.setNull(11,Types.VARCHAR);

			// Execute the update statement
			if (pstmt.executeUpdate() == 1) 
			{
				System.out.println("Database Updated, 1 Row Inserted");
			}//endif

		}//endtr 

		catch (SQLException e) {
			System.out.println(e.getMessage());
		}//endca

	}//endfn

	//This method does the deletion part
	public static void delete(String table, Statement stmt)throws SQLException,IOException
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			System.out.println("Enter LoginID");
			line = br.readLine();
			String sqlString = new String("DELETE FROM "+table+" WHERE LoginID='"+line+"'");
			// Execute the delete statement
			if (stmt.executeUpdate(sqlString) == 1)
			{
				System.out.println("Database Updated, 1 record deleted\n");
			}else{
				System.out.println("Database not changed, no match found\n");
			}//endif
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}//endca

	}//endfn


	//This method does the search part
	public static void search(String table, Statement stm)throws SQLException, IOException
	{
		try {

			String Fname,Lname,Email,Address1,Address2,Phone,City,State,Zip,LoginID,Password;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = null,attr = null; 

			System.out.println("\nEnter a value to search for in the appropriate field, only one value at a time,\n");
			System.out.println("Enter Login ID: "); 		LoginID 	= br.readLine();
			System.out.println("Enter password: "); 		Password = br.readLine();
			System.out.println("Enter First Name: "); 		Fname 	= br.readLine();
			System.out.println("Enter Last Name: "); 		Lname 	= br.readLine();
			System.out.println("Enter e-mail: "); 		Email 	= br.readLine();
			System.out.println("Enter Address line 1: ");	Address1 	= br.readLine();
			System.out.println("Enter Address line 2: "); 	Address2 	= br.readLine();
			System.out.println("Enter City: "); 		City	= br.readLine();
			System.out.println("Enter State: "); 		State 	= br.readLine();
			System.out.println("Enter Zip code: "); 		Zip 	= br.readLine();
			System.out.println("Enter Phone Number: "); 	Phone 	= br.readLine();

			if (LoginID.length() > 1) 	{attr = "LoginID";line = LoginID;}
			if (Password.length() > 1) 	{attr = "Password";line = Password;}
			if (Fname.length() > 1) 	{attr = "Fname";line = Fname;}
			if (Lname.length() > 1) 	{attr = "Lname";line = Lname;}
			if (Email.length() > 1) 	{attr = "Email";line = Email;}
			if (Address1.length() > 1) 	{attr = "Address1";line = Address1;}
			if (Address2.length() > 1) 	{attr = "Address2";line = Address2;}
			if (City.length() > 1) 		{attr = "City";line = City;}
			if (State.length() > 1) 	{attr = "State";line = State;}
			if (Phone.length() > 1)	{attr = "Phone";line = Phone;}

			String qry = new String("SELECT * FROM "+table+" WHERE "+attr+" LIKE '%"+line+"%'");
			ResultSet rs = stm.executeQuery(qry);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();

			//For each row in the table
			System.out.print("Fname,Lname,Email,Address1,Address2,Phone,City,State,Zip,LoginID,Password\n");

			while (rs.next())
			{
				System.out.println();	

				//For Each Column in the row
				for (int i=1;i<=count; i++)
				{
					System.out.print(rs.getString(i) + ((count-i!=0)?", ":"\n\r"));
				}//endfr
			}//endwh

			System.out.println("\n");
			rs.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}//endca
	}//endfn		
}//endcl
