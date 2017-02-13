package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This application reads address data from the user and stores it in a file<br/>
 * Later when required reads the data back and displays in the console.
 * 
 * @author amudhan
 *
 */
public class AddressToFiles {

  public static void main(String[] args) {
    System.out.println("***************Welcome to address management system***************\n\n");
    System.out.println("\tAdd an address or view already added address list\n");
    PrintWriter fileOut = null;
    Scanner reader = null;
    Scanner input = null;
    String record = "src/files/record.txt";
    try {
      fileOut = new PrintWriter(new FileWriter(record, true));
      input = new Scanner(System.in);
    } catch (IOException e) {
      System.out.println("File does not exist. Quitting the app.........");
      System.exit(0);
    }
    String choice;
    do {
      System.out.println("\tPress v to view, press a to add, press q to quit :");
      choice = input.next().trim();
      // To consume the newline character
      input.nextLine();
      switch (choice) {
        case "v":
          System.out.println("-----------------The available address data-------------------");
          try {
            reader = new Scanner(new File(record));
          } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Quitting the app.........");
            System.exit(0);
          }
          while (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
          }
          System.out.println("--------------------------------------------------------------");
          break;
        case "a":
          System.out.println("Adding new address data");
          Address address = new Address();
          System.out.println("Enter door number :");
          address.doorNumber = input.nextLine();
          System.out.println("Enter street name :");
          address.street = input.nextLine();
          System.out.println("Enter city name :");
          address.city = input.nextLine();
          System.out.println("Enter country name :");
          address.country = input.nextLine();
          fileOut.println(address.toString());
          fileOut.flush();
          break;
        case "q":
          System.out.println("Quitting the application");
          fileOut.close();
          reader.close();
          input.close();
          break;
        default:
          System.out.println("Invalid choice. Try again");
      }
    } while (!choice.equals("q"));
  }
}


class Address {
  String doorNumber;
  String street;
  String city;
  String country;

  @Override
  public String toString() {
    return "Door number: " + doorNumber + " Street: " + street + " City: " + city + " Country: "
        + country;
  }
}
