package jdk8;

import static jdk8.MethodReferenceTest.persons;
import static jdk8.StreamsBasic.menu;
import static jdk8.TransactionProcessor.transactions;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Collecting {

  public static void main(String[] args) {
    System.out.println("***** Names of all persons *****");
    // Only get the list of persons from the List<Person>
    List<String> names = persons.stream().map(p -> p.getName()).collect(Collectors.toList());
    names.forEach(System.out::println);
    // Get a map with id as the key and a corresponding List<Person> as the value
    Map<Integer, List<Person>> personMap =
        persons.stream().collect(Collectors.groupingBy(Person::getId));
    personMap.entrySet().stream().forEach(entry -> {
      System.out.println("***** The id *****");
      System.out.println(entry.getKey());
      System.out.println("***** The values *****");
      entry.getValue().forEach(System.out::println);
    });
    System.out.println("***** Sum of all values *****");
    int total = transactions.stream().collect(Collectors.summingInt(Transaction::getValue));
    System.out.println("Total transactional value :" + total);
    System.out.println("***** Average of the values *****");
    double avg = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
    System.out.println("Average calories in the menu :" + avg);
    System.out.println("***** The summary of the calories *****");
    IntSummaryStatistics menuStatistics =
        menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
    System.out.println("The summary of the menu :" + menuStatistics);
    System.out.println("***** Joining strings ***** ");
    String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
    System.out.println("The entire menu joined with out any delimiter :" + shortMenu);
    System.out.println("The entire menu joined as comma separated values :"
        + menu.stream().map(Dish::getName).collect(Collectors.joining(",")));


  }

}
