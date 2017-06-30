package jdk8;

import java.util.Comparator;
import java.util.List;

public class PersonProcessorTest {

  public static void main(String[] args) {
    List<Person> persons = MethodReferenceTest.persons;
    persons.sort(Comparator.comparing(Person::getId));
    System.out.println("\n***** Sorted list based on id*****");
    persons.forEach(System.out::println);
    persons.sort(Comparator.comparing(Person::getId).reversed());
    System.out.println("\n***** Reverse sorted list based on id*****");
    persons.forEach(System.out::println);
    System.out.println(
        "\n***** Sorted list based on id, if they are same then name, if they are same then occupation, if they are same too then country*****");
    // Chaining comparators
    persons.sort(Comparator.comparing(Person::getId).thenComparing(Person::getName)
        .thenComparing(Person::getOccupation).thenComparing(Person::getCountry));
    persons.forEach(System.out::println);
  }

}
