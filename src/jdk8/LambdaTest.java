package jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// Playing with lambda expressions
public class LambdaTest {

  private int count = 0;
  private static int staticCount = 0;

  public static void main(String[] args) {
    // Lambda multiple lines
    ActionInterface i11 = () -> {
      System.out.println("Lambdas in action");
      System.out.println("Multiple lines");
    };
    i11.performAction();

    // Lambda single line
    ActionInterface i12 = () -> System.out.println("Single line implementation");
    i12.performAction();

    // Another example of lambdas from a java interface
    Runnable r1 = new Runnable() {
      @Override
      public void run() {
        System.out.println("Run method implementation without lambdas");
      }
    };
    r1.run();
    Runnable r2 = () -> System.out.println("Run method implementation with lambdas");
    r2.run();

    // Lambda expression that returns value
    // Implement the interface here
    Interface2 other = (int n) -> n * n;
    // Call the implemented method
    int result = other.square(500);
    System.out.println(result);

    // Multiple lines
    Interface2 another = (int n) -> {
      System.out.println("Calculating square for the number " + n);
      return n * n;
    };
    result = another.square(300);
    System.out.println(result);

    // Implementing comparator
    Comparator<Integer> sortOrder = (Integer i1, Integer i2) -> {
      return i1.compareTo(i2);
    };
    // This here is type inference on the parameters i1 and i2. No need to specify they are integers
    Comparator<Integer> reverseSortOrder = (i1, i2) -> {
      return -i1.compareTo(i2);
    };

    System.out.println("Comparing 100 and 200 " + sortOrder.compare(100, 200));
    System.out.println("Comparing 200 and 100 " + sortOrder.compare(200, 100));
    Integer[] a = {10, 5, 15, 25, 35, 30};
    System.out.println("Before sorting---");
    for (Integer arrayValue : a) {
      System.out.print(arrayValue + " ");
    }
    System.out.println("\nAfter sorting---");
    Arrays.sort(a, sortOrder);
    for (Integer arrayValue : a) {
      System.out.print(arrayValue + " ");
    }
    System.out.println("\nReverse sorting---");
    Arrays.sort(a, reverseSortOrder);
    for (Integer arrayValue : a) {
      System.out.print(arrayValue + " ");
    }
    System.out.println();
    // Lambdas have access to the next outermost scope.
    StringBuilder message = new StringBuilder();
    Runnable r = () -> {
      System.out.println(message);
      System.out.println("----Calling a method from an object in the outer scope----");
      i11.performAction();
    };
    message.append("Howdy, ");
    message.append("world!");
    r.run();
    Interface2 sq1 = (n) -> n * n;
    System.out.println("Square of 60 " + sq1.square(60));

    // Check if string is empty
    // Predicate<T> interface
    // Create an implementation of the test method of Predicate. The type parameter of the Predicate
    // signifies the type of the parameter that the test method can receive. Here it is string. The
    // 'String' of 'String s' is not necessary
    Predicate<String> isNonEmptyString = (String s) -> s != null && s.length() > 0;
    System.out.println("Is this empty ? " + isNonEmptyString.test(""));
    System.out.println("Is this empty ? " + isNonEmptyString.test("Test"));
    System.out.println("Is this empty ? " + isNonEmptyString.test(" "));
    // Consumer interface
    System.out.println("Consumer interface");
    Consumer<List<String>> consumeList = (list) -> {
      for (String s : list) {
        System.out.println(s);
      }
    };
    processList(Arrays.asList("Tycho", "Gumo", "Carillio"), consumeList);
    // Function<T,R> interface. Can be used for converting one type of object into another type
    Function<List<Apple>, Integer> appleWeightConvertor = (apples) -> {
      int weight = 0;
      for (Apple apple : apples) {
        weight = weight + apple.getWeight();
      }
      return weight;
    };
    processApples(Arrays.asList(new Apple(80, "green"), new Apple(155, "green")),
        appleWeightConvertor);
    // Create a list of apples
    List<Apple> apples = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"),
        new Apple(120, "red"), new Apple(130, "light green"), new Apple(150, "dark red"));
    // Create a lambda expression to display apples in the console
    Consumer<List<Apple>> applesConsoleDisplay = (appleList) -> {
      for (Apple apple : appleList) {
        System.out.println(apple);
      }
    };
    System.out.println("-----All apples-----");
    consumeList(apples, applesConsoleDisplay);
    System.out.println("-----Filtered apples-----");
    // The lambda expression on the appleFilter is resolved by type inference
    consumeList(appleFilter(apples, apple -> "green".equals(apple.getColor())),
        applesConsoleDisplay);
    // This tests the ability of lambdas to access instance variables
    new LambdaTest().testInstanceVariableScope();
    // Lambdas can access static variables too
    System.out
        .println("***** Accessing the static variable staticCount from a lambda expression*****");
    ActionInterface staticVariableTest =
        () -> System.out.println("Static count " + (++staticCount));
    staticVariableTest.performAction();
    staticVariableTest.performAction();
    staticVariableTest.performAction();
    staticVariableTest.performAction();
    staticVariableTest.performAction();
    // Accessing local variables that are final or effectively final
    boolean localFlag = true;
    ActionInterface localVariableTest =
        () -> System.out.println("Can lambas access local variables too ? " + localFlag);
    localVariableTest.performAction();
    // Comparing predicates
    // Using this predicate red apples can be filtered
    System.out.println("*** Getting RED apples **");
    Predicate<Apple> redApples = redApple -> redApple.getColor().equalsIgnoreCase("red");
    Predicate<Apple> greenApples = greenApple -> greenApple.getColor().equalsIgnoreCase("green");
    Predicate<Apple> moreThan150 = heavyApple -> heavyApple.getWeight() > 150;
    appleFilter(apples, redApples).forEach(System.out::println);
    System.out.println("*** Getting NOT RED apples **");
    appleFilter(apples, redApples.negate()).forEach(System.out::println);
    System.out.println("*** Getting apples that are either GREEN or RED **");
    appleFilter(apples, redApples.or(greenApples)).forEach(System.out::println);
    System.out.println("*** Getting apples that are GREEN and more than 150kg**");
    appleFilter(apples, greenApples.and(moreThan150)).forEach(System.out::println);
  }

  // This method accepts a list and a consumer and perform operations implemented on the consumer
  static <T> void consumeList(List<T> list, Consumer<List<T>> consumer) {
    consumer.accept(list);
  }

  // This method filters apples based on how Predicate#test is implemented
  static List<Apple> appleFilter(List<Apple> apples, Predicate<Apple> p) {
    List<Apple> filteredApples = new ArrayList<>();
    for (Apple apple : apples) {
      if (p.test(apple)) {
        filteredApples.add(apple);
      }
    }
    return filteredApples;
  }

  static void processList(List<String> list, Consumer<List<String>> consumer) {
    consumer.accept(list);
  }

  static void processApples(List<Apple> apples,
      Function<List<Apple>, Integer> appleWeightCalculator) {
    System.out.println(appleWeightCalculator.apply(apples));
  }

  void testInstanceVariableScope() {
    System.out.println("***** Accessing the instance variable count from a lambda expression*****");
    ActionInterface instanceVariableTest = () -> System.out.println("Count " + (++count));
    instanceVariableTest.performAction();
    instanceVariableTest.performAction();
    instanceVariableTest.performAction();
    instanceVariableTest.performAction();
    instanceVariableTest.performAction();
  }
}


// This is a functional interface where only one method is to be implemented. Can be used in lambdas
// The abstract method takes no argument and returns no value.
interface ActionInterface {
  void performAction();
}


interface Interface2 {
  int square(int n);
}
