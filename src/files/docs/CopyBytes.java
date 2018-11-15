package files.docs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileInputStream and FileInputStream reads/writes data at the byte level<br/>
 * They should be avoided when reading character data
 * 
 * @author amudhan
 *
 */
public class CopyBytes {

  public static void main(String[] args) {
    try (FileInputStream in = new FileInputStream("src/files/docs/xandu.txt");
        FileOutputStream out = new FileOutputStream("src/files/docs/out.txt")) {
      int b = -1;
      while ((b = in.read()) != -1) {
        out.write(b);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
