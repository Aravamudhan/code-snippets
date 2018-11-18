package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseApp {

  public static void main(String[] args) throws Exception {
    // We are using a database named h2
    // The Driver class in the package org.h2 helps the program in
    // connecting with the h2 database
    // If we are using some other database say MySQL, we would be using the
    // driver class like com.mysql.jdbc.Driver
    Class.forName("org.h2.Driver");

    // First, connect with the database
    // This is done by providing databse url, user name and password
    String databaseUrl = "jdbc:h2:~/address_database";
    String userName = "-user";
    String password = "";
    // This line gets the connection from the database and stores
    // the connection in the object conn
    Connection conn = DriverManager.getConnection(databaseUrl, userName, password);
    // After connection is acquired, create statement, which executes
    // queries
    // Connection is like a path to the database. Once it is established we
    // request that connection object to create a Statement object
    Statement stmt = conn.createStatement();
    // create a table named phone
    String createQuery = "CREATE TABLE PHONE(COUNTRY_CODE VARCHAR(5), PHONE_NUMBER VARCHAR(15))";
    // stmt object executes the query which results in creation of a table
    // named PHONE in the
    // database named address_database
    stmt.execute(createQuery);
    // Inserting a record
    String insert = "INSERT INTO PHONE VALUES('+91','9898989898')";
    // this is where insertion happens. Statement object executes the query
    // that is passed into it
    // as a string
    stmt.execute(insert);
    // Get details from the database
    String getDetails = "SELECT * FROM PHONE";
    // executeQuery method of the stmt object, executes the given query and
    // returns the result.
    // The result of such queries are of type ResultSet. Hence we are
    // creating an object called
    // result of type ResultSet
    ResultSet result = stmt.executeQuery(getDetails);
    // The next method of the result object moves to the next record every
    // time
    while (result.next()) {
      String countryCode = result.getString("COUNTRY_CODE");
      String phoneNumber = result.getString("PHONE_NUMBER");
      System.out.println("Country code " + countryCode + " Phone number " + phoneNumber);
    }
    // dropping(deleting) the table from the database
    String dropTable = "DROP TABLE PHONE";
    stmt.execute(dropTable);
    // closing the connection
    conn.close();
  }

}
