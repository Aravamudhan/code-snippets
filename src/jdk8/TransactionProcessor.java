package jdk8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.Data;

public class TransactionProcessor {

  // Some traders and transactions
  static Trader raoul = new Trader("Raoul", "Cambridge");
  static Trader mario = new Trader("Mario", "Milan");
  static Trader alan = new Trader("Alan", "Cambridge");
  static Trader brian = new Trader("Brian", "Cambridge");
  static List<Trader> traders = Arrays.asList(raoul, mario, alan, brian);
  static List<Transaction> transactions =
      Arrays.asList(new Transaction(brian, 2011, 300), new Transaction(raoul, 2012, 1000),
          new Transaction(raoul, 2011, 400), new Transaction(mario, 2012, 710),
          new Transaction(mario, 2012, 700), new Transaction(alan, 2012, 950));

  public static void main(String[] args) {
    System.out.println("***** All trasaction in the year 2011, sorted by value *****");
    transactions.stream().filter(t -> t.getYear() == 2011)
        .sorted(Comparator.comparing(Transaction::getValue)).forEach(System.out::println);

    System.out.println("***** Cities that traders work *****");
    traders.stream().map(t -> t.getCity()).distinct().forEach(System.out::println);

    System.out.println("***** Traders from Cambridge sorted by name *****");
    traders.stream().filter(t -> "Cambridge".equalsIgnoreCase(t.getCity()))
        .sorted((t1, t2) -> t1.getName().compareTo(t2.getName())).forEach(System.out::println);

    System.out.println("***** Sorted list of the names of all traders *****");
    String names = traders.stream().map(t -> t.getName()).sorted((n1, n2) -> n1.compareTo(n2))
        .reduce("", (n1, n2) -> n1 + " " + n2);
    System.out.println(names);

    System.out.println("***** Are there any traders from Milan ?*****");
    System.out.println(traders.stream().anyMatch(t -> "Milan".equalsIgnoreCase(t.getCity())));

    System.out.println("***** Transaction values of all traders from Cambridge *****");
    transactions.stream().filter(t -> "Cambridge".equalsIgnoreCase(t.getTrader().getCity()))
        .map(Transaction::getValue).forEach(TransactionProcessor::out);

    System.out.println("***** Highest transaction *****");
    transactions.stream().sorted(Comparator.comparing(Transaction::getValue).reversed()).limit(1)
        .map(Transaction::getValue).forEach(TransactionProcessor::out);
    System.out.println(transactions.stream().map(t -> t.getValue()).reduce(Integer::max).get());

    System.out.println("***** Lowest transaction *****");
    transactions.stream().sorted(Comparator.comparing(Transaction::getValue)).limit(1)
        .map(Transaction::getValue).forEach(TransactionProcessor::out);
    System.out.println(transactions.stream().map(t -> t.getValue()).reduce(Integer::min).get());

  }

  static <T> void out(T t) {
    System.out.println(t);
  }

}


@Data
class Trader {
  private final String name;
  private final String city;
}


@Data
class Transaction {
  private final Trader trader;
  private final int year;
  private final int value;
}
