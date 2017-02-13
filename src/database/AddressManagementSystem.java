package database;

import java.util.List;
import java.util.Scanner;

/**
 * This application serves as an address management system which stores name, contact and address
 * details of a person
 * 
 * @author amudhan
 *
 */
public class AddressManagementSystem {
  public static void main(String[] args) {
    System.out.println("***************Welcome to address management system***************\n\n");
    System.out.println("\tAdd/view/delete/search the details of a person\n");
    Scanner input = new Scanner(System.in);
    String choice;
    AddressStorageSystem storage = new AddressStorageSystem();
    System.out.println("\tPress c to create the table named ADDRESS"
        + "\n\tPress r to delete the table ADDRESS" + "\n\tPress v to view all address records"
        + "\n\tPress a to add an address" + "\n\tPress d to delete an address"
        + "\n\tPress s to search for an address\n\tPress q to quit :");
    do {
      System.out.println("Enter your choice :");
      choice = input.next().trim();
      // To consume the newline character
      input.nextLine();
      switch (choice) {
        case "v":
          System.out.println("-----------------The available address data-------------------");
          // Calling the method that connects to database, gets all the available address from it,
          // and returns the value
          List<Address> addressList = storage.viewAll();
          // Only if the addressList contains some data, we display it
          if (addressList != null && addressList.size() > 0) {
            for (Address address : addressList) {
              System.out.println(address.toString());
            }
          } else {
            System.out.println("\nThe table is empty or does not exist\n");
          }
          System.out.println("--------------------------------------------------------------");
          break;
        case "a":
          // Create a new address object and store the values that was given by users
          Address address = new Address();
          System.out.println("Enter name");
          address.name = input.nextLine();
          System.out.println("Enter mobile number");
          address.mobile = input.nextLine();
          System.out.println("Enter door number :");
          address.doorNumber = input.nextLine();
          System.out.println("Enter street name :");
          address.street = input.nextLine();
          System.out.println("Enter city name :");
          address.city = input.nextLine();
          System.out.println("Enter country name :");
          address.country = input.nextLine();
          // call the save method from of the storage object, that saves/inserts the information
          // into the ADDRESS table
          boolean result = storage.save(address);
          if (result == true) {
            System.out.println("Successfully saved the details");
          } else {
            System.out.println("Failed to save the details");
          }
          break;
        case "d":
          // Deleting a row in the database
          System.out.println("Enter the address id to be deleted :");
          // getting the id of a record
          Long addressIdToDelete = input.nextLong();
          // if we give an id that exists in the database, that will be deleted and true will be
          // returned otherwise we get false from the deleteAddress method
          boolean isDeleted = storage.deleteAddress(addressIdToDelete);
          if (isDeleted) {
            System.out.println("The address with the id " + addressIdToDelete + " is deleted");
          } else {
            System.out.println("The address with the id " + addressIdToDelete
                + " is not deleted or it does not exist");
          }
          break;
        case "s":
          // To get an address record using the address id
          System.out.println("Enter the address id to be searched :");
          Long addressId = input.nextLong();
          Address addressFound = storage.getAddress(addressId);
          if (addressFound != null) {
            System.out.println("The address with the id " + addressId);
            System.out.println(addressFound);
          } else {
            System.out
                .println("The address with the id " + addressId + " was not found in the database");
          }
          break;
        case "c":
          // We have to create a table before performing any other operation
          if (storage.createTable()) {
            System.out.println("Table created for the first time");
          }
          break;
        case "r":
          // We can remove a table from the database
          if (storage.removeTable()) {
            System.out.println("Table was deleted successfully");
          }
          break;
        case "q":
          System.out.println("Quitting the application");
          input.close();
          break;
        default:
          System.out.println("Invalid choice. Try again");
      }
    } while (!choice.equals("q"));
  }

}


class Address {
  Long id;
  String name;
  String mobile;
  String doorNumber;
  String street;
  String city;
  String country;

  @Override
  public String toString() {
    return "Name: [" + name + "] Mobile: [" + mobile + "] Door number: [" + doorNumber
        + "] Street: [" + street + "] City: [" + city + "] Country: [" + country + "] Id :[" + id
        + "]";
  }
}

