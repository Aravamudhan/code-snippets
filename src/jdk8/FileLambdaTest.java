package jdk8;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLambdaTest {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    // Before lambdas
    System.out.println("-----Without using lambdas-----");
    System.out.println(getFileData());
    // After lambdas
    // An implementation of the BufferedReaderProcessor interface's abstract method 'process', that
    // two lines of the file
    System.out.println("-----Reading just two lines-----");
    getFileData((br) -> System.out.println(br.readLine() + "\n" + br.readLine()));
    // Reading everything in the file
    System.out.println("-----Reading the entire file-----");
    getFileData(br -> {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    });


  }

  public static String getFileData() throws FileNotFoundException, IOException {
    try (BufferedReader br =
        new BufferedReader(new FileReader("/home/amudhan/Desktop/motivation"))) {
      // One line of read is done. What if more lines are to be read in an another implementation?
      // Or some other processing using the file data needs to done ? That would make us write
      // repeated boiler plate code
      return br.readLine();
    }
  }

  // This method is used so that file processing boiler plate code can be avoided
  // By using process method of the BufferedReaderProcessor the file can be processed in different
  // ways be it reading only one line, or the full file or sending the file information to another
  // system. This is possible due to lambdas. Less verbosity.
  public static void getFileData(BufferedReaderProcessor bp)
      throws FileNotFoundException, IOException {
    try (BufferedReader br =
        new BufferedReader(new FileReader("/home/amudhan/Desktop/motivation"))) {
      bp.process(br);
    }
  }

}


@FunctionalInterface
interface BufferedReaderProcessor {
  void process(BufferedReader br) throws FileNotFoundException, IOException;
}
