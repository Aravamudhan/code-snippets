package files.docs;

import static files.docs.Constants.INPUT_FILE;
import static files.docs.Constants.OUTPUT_FILE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCharacters {

  public static void main(String[] args) {
    try (FileReader reader = new FileReader(INPUT_FILE);
        FileWriter writer = new FileWriter(OUTPUT_FILE);) {
      int c = -1;
      while ((c = reader.read()) != -1) {
        writer.write(c);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
