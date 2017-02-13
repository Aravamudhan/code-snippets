package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressStorageSystem {


  /**
   * This method saves/inserts the address object that is passed to it into the address_database's
   * ADDRESS table
   * 
   * @param address - address object
   * @return
   */
  public boolean save(Address address) {
    // if we successfully insert the values, this becomes true
    boolean result = false;
    // the insert query that we must run to insert values
    // what ever value we pass in the address object, they are added to this query
    String insert =
        "INSERT INTO ADDRESS (NAME, MOBILE, DOOR_NUMBER, STREET, CITY, COUNTRY) VALUES ('"
            + address.name + "', '" + address.mobile + "', '" + address.doorNumber + "','"
            + address.street + "','" + address.city + "','" + address.country + "')";
    Connection connection = getConnection();
    Statement statement = null;
    try {
      statement = connection.createStatement();
      result = statement.execute(insert);
      // if we reach this line, we have successfully inserted a record
      result = true;
    } catch (Exception e) {
      System.out.println("Problem when saving the address. Exception has been thrown " + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return result;
  }

  /**
   * This method returns all the addresses that are stored in ADDRESS table
   * 
   * @return
   */
  public List<Address> viewAll() {
    // This list object stores the result
    List<Address> addressList = new ArrayList<>();
    // The query we have to run to get all the values from the Address table
    String viewAll = "SELECT * FROM ADDRESS";
    Connection connection = getConnection();
    Statement statement = null;
    try {
      statement = connection.createStatement();
      // Executing the query. The result is stored in the result set
      ResultSet result = statement.executeQuery(viewAll);
      // There might be many rows of data. Until we reach the end of the table, we keep going to the
      // next row
      while (result.next()) {
        Address address = new Address();
        address.id = result.getLong("ID");
        address.name = result.getString("NAME");
        address.mobile = result.getString("MOBILE");
        address.doorNumber = result.getString("DOOR_NUMBER");
        address.street = result.getString("STREET");
        address.city = result.getString("CITY");
        address.country = result.getString("COUNTRY");
        // store the result in the list
        addressList.add(address);
      }
    } catch (Exception e) {
      System.out.println("Problem when viewing all the addresses. Exception has been thrown " + e);
    } finally {
      // the connection that was opened must be closed
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return addressList;
  }

  /**
   * If the given addressId is present in the database the entire address record is found and
   * returned
   * 
   * @param addressId
   * @return
   */
  public Address getAddress(Long addressId) {
    Address address = null;
    Connection connection = getConnection();
    Statement statement = null;
    try {
      // query to get a particular record
      String findAddress = "SELECT * FROM ADDRESS WHERE ID=" + addressId;
      statement = connection.createStatement();
      ResultSet result = statement.executeQuery(findAddress);
      if (result.next()) {
        address = new Address();
        address.id = result.getLong("ID");
        address.name = result.getString("NAME");
        address.mobile = result.getString("MOBILE");
        address.doorNumber = result.getString("DOOR_NUMBER");
        address.street = result.getString("STREET");
        address.city = result.getString("CITY");
        address.country = result.getString("COUNTRY");
      }
    } catch (Exception e) {
      System.out.println("Exception when trying to the address with id " + addressId + " :" + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return address;
  }

  /**
   * This method deletes a record from the database
   * 
   * @param addressId
   * @return
   */
  public boolean deleteAddress(Long addressId) {
    Connection connection = getConnection();
    Statement statement = null;
    boolean result = false;
    try {
      String deleteAddress = "DELETE FROM ADDRESS WHERE ID=" + addressId;
      statement = connection.createStatement();
      // This returns the number of rows affected.
      int resultCount = statement.executeUpdate(deleteAddress);
      // if there is a record with the given id, that record/row is deleted. That means affected
      // rows are 1. If there are no rows/records with the given id, the affected records are 0
      // (i.e.) we no record exists for the given id
      if (resultCount > 1) {
        result = true;
      }
    } catch (Exception e) {
      result = false;
      System.out.println("Exception when trying to the address with id " + addressId + " :" + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return result;
  }

  /**
   * This method creates a table named ADDRESS
   * 
   * @return
   */
  public boolean createTable() {
    boolean result = false;
    Connection connection = null;
    connection = getConnection();
    try {
      // This piece of code checks whether a table named ADDRESS exists in the database
      // We check before creating a table
      DatabaseMetaData dbm = connection.getMetaData();
      ResultSet tables = dbm.getTables(null, null, "ADDRESS", new String[] {"TABLE"});
      // if there exists a table with the name ADDRESS, the table.next() will be true
      if (tables.next()) {
        result = false;
      } else {
        // no table exists for the name ADDRESS
        Statement statement = connection.createStatement();
        // we are creating it here
        statement.execute(
            "CREATE TABLE ADDRESS(ID BIGINT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(50), MOBILE VARCHAR(25), DOOR_NUMBER VARCHAR(50), STREET VARCHAR(100), CITY VARCHAR(50), COUNTRY VARCHAR(50))");
        result = true;
      }
    } catch (SQLException e) {
      System.out.println("Exception in creating the table ADDRESS " + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return result;
  }

  public boolean removeTable() {
    boolean result = false;
    Connection connection = null;
    connection = getConnection();
    try {
      DatabaseMetaData dbm = connection.getMetaData();
      ResultSet tables = dbm.getTables(null, null, "ADDRESS", new String[] {"TABLE"});
      if (tables.next()) {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE ADDRESS");
        result = true;
      } else {
        System.out.println("The table ADDRESS does not exist in the database");
        result = false;
      }
    } catch (SQLException e) {
      System.out.println("Exception in deleting the table ADDRESS " + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Exception when trying to close the connection " + e);
        }
      }
    }
    return result;
  }

  public Connection getConnection() {
    Connection connection = null;
    try {
      Class.forName("org.h2.Driver");
      connection = DriverManager.getConnection("jdbc:h2:~/address_database", "-user", "");
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Exception when trying to get the connection and statement " + e);
      System.out.println("Application terminating----------------");
      System.exit(-1);
    }
    return connection;
  }

}
