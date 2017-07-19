package jdk8;

import static jdk8.TransactionProcessor.transactions;

import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class NumericStreams {

  public static void main(String[] args) {
    // Converts a stream of Transaction into DoubleStream
    System.out.println("***** Combined value of all transactions *****");
    System.out.println(transactions.stream().mapToDouble(Transaction::getValue).sum());

    // Easier way to get a maximum value from a Stream<T>, by converting the stream to IntStream or
    // LongStream or whichever one is necessary and calling the in-built max method
    System.out.println("*****Maximum transaction*****");
    // An Optional for Double
    OptionalDouble maxTransaction = transactions.stream().mapToDouble(Transaction::getValue).max();
    // Get the max, if not give -1
    System.out.println(maxTransaction.orElse(-1));

    System.out.println("***** Range of numbers between 1 and 10 inclusive*****");
    IntStream.rangeClosed(1, 10).forEach(i -> System.out.print(i + " "));

    System.out.println("\n***** Range of numbers between 1 and 10 exclusive*****");
    IntStream.range(1, 10).forEach(i -> System.out.print(i + " "));

    System.out.println("\n***** All odd numbers between 1 and 50*****");
    IntStream.rangeClosed(1, 50).filter(i -> i % 2 != 0).forEach(i -> System.out.print(i + " "));
  }

}
