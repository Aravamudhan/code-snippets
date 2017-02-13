package files;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileIoTest {

  public static void main(String[] args) {
    PrintWriter writer;
    try {
      // writing
      writer = new PrintWriter(new FileWriter("src/files/letter.txt", true));
      writer.append("Hello world");
      writer.println();
      writer.println("Hello again");
      writer.close();

      // reading
      Scanner input = new Scanner(new File("src/files/letter.txt"));
      System.out.println("The contents of the file are");
      while (input.hasNextLine()) {
        System.out.println(input.nextLine());
      }
      input.close();
    } catch (Exception e) {
      System.out.println("File does not exist");
    }
  }

}
