package jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;

// Playing with method references
// Method references let the code reuse existing methods
public class MethodReferenceTest {

  static List<Person> persons = new ArrayList<>();
  static {
    persons = Arrays.asList(new Person(2010, "James Bond", "UK", "Secret agent"),
        new Person(800, "Lannister", "Casterly Rock", "Warden of the west"),
        new Person(800, "Lannister", "Casterly Rock", "Hand of the king"),
        new Person(10, "Kaladin", "Alethi", "Warrior"),
        new Person(1997, "George Weasley", "UK", "Business man"),
        new Person(1997, "Fred Weasley", "UK", "Business man"),
        new Person(1990, "Cathy Ryan", "US", "Doctor"),
        new Person(850, "Victorian Greyjoy", "Iron islands", "Iron captain"));
  }

  public static void main(String[] args) {
    List<Apple> apples = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"),
        new Apple(120, "red"), new Apple(200, "darkred"));

    System.out.println("***** Before processing the apple names *****");
    // Using method references to print
    apples.forEach(System.out::println);
    process(apples, (a) -> a.setColor(a.getColor().toUpperCase()));
    System.out.println("***** After processing the apple names *****");
    apples.forEach(System.out::println);

    System.out.println("***** A list of persons *****");
    System.out.println("-----Get all ids using lambda expression");
    // This lambda expression calls in turn another method, a candidate for applying method
    // reference.
    displayProperties(persons, (p) -> p.getId());
    System.out.println("-----Get all names");
    // Instead of creating a lambda that just calls another method, we can directly
    // reference that another method itself
    displayProperties(persons, Person::getName);
    System.out.println("-----Get all countries-----");
    displayProperties(persons, Person::getCountry);
    System.out.println("-----Get all occupations-----");
    displayProperties(persons, Person::getOccupation);

    System.out.println("***** Process a persons one by one *****");
    Person p1 = persons.get(0);
    processAndDisplay(p1, Person::getName);// Using method reference
    processAndDisplay(p1, p -> p.getName());// Using lambda expression
    processAndDisplay(p1.getName(), String::toUpperCase);
    Supplier<String> nameSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return p1.getName();
      }
    };
    System.out.println(nameSupplier.get() + "(Using supplier as an anonymous inner class)");
    System.out.println("Using lambda expression: ");
    doStuff(() -> p1.getCountry());
    System.out.println("Method reference to an instance method of an existing type: ");
    doStuff(p1::getName);

    System.out.println("***** Creating apples based on weights using lambdas *****");
    List<Integer> weights = Arrays.asList(200, 220, 250, 300, 225);
    // Using lambdas create a list of apples by weight
    // In the lambda expression, we are just calling the constructor, doing nothing else. Might as
    // well use method reference for that
    List<Apple> applesByWeight = processAndCreate(weights, (w) -> new Apple(w));
    applesByWeight.forEach(System.out::println);
    System.out.println("***** Creating apples based on colors using method reference*****");
    List<String> colors = Arrays.asList("red", "green", "darkred", "litegreen", "red");
    List<Apple> applesByColor = processAndCreate(colors, Apple::new);
    applesByColor.forEach(System.out::println);
    System.out
        .println("***** Creating apples based on weights and colors using method reference*****");
    // Send a list of weights and another list of color. A matching constructor will be called on
    // them.
    List<Apple> appleBunch = createByTwoProperties(weights, colors, Apple::new);
    appleBunch.forEach(System.out::println);
  }

  // Consumer
  static <T> void process(List<T> list, Consumer<T> processor) {
    for (T t : list) {
      processor.accept(t);
    }
  }

  // Function - Takes a parameter of type T and produces a R
  static <T, R> void displayProperties(List<T> list, Function<T, R> function) {
    for (T t : list) {
      System.out.println(function.apply(t));
    }
  }

  static <T, R> void processAndDisplay(T t, Function<T, R> function) {
    System.out.println(function.apply(t));
  }

  static <T> void doStuff(Supplier<T> supplier) {
    System.out.println(supplier.get());
  }

  // Take a list of type T, process each value in the list and produce an object of type R for each
  // of T. Add all of the Rs to an another list named result. Return result. In the examples, we
  // pass a list of weights and expect a list of apples
  static <T, R> List<R> processAndCreate(List<T> list, Function<T, R> function) {
    List<R> result = new ArrayList<>();
    for (T t : list) {
      result.add(function.apply(t));
    }
    return result;
  }

  static <T, R, U> List<U> createByTwoProperties(List<T> list1, List<R> list2,
      BiFunction<T, R, U> biFunction) {
    List<U> result = new ArrayList<>();
    int size = list1.size() > list2.size() ? list1.size() : list2.size();
    for (int i = 0; i < size; i++) {
      T t = null;
      R r = null;
      if (i < list1.size()) {
        t = list1.get(i);
      }
      if (i < list2.size()) {
        r = list2.get(i);
      }
      result.add(biFunction.apply(t, r));
    }
    return result;
  }

}


@Data
@AllArgsConstructor
class Person {
  private int id;
  private String name;
  private String country;
  private String occupation;

}
