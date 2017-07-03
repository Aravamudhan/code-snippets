package jdk8;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public class StreamsBasic {
  public static final List<Dish> menu = Arrays.asList(
      new Dish("pork", false, 800, Dish.Type.MEAT, Arrays.asList("Tuesday", "Friday")),
      new Dish("beef", false, 700, Dish.Type.MEAT, Arrays.asList("Sunday", "Monday")),
      new Dish("chicken", false, 400, Dish.Type.MEAT, Arrays.asList("Wednesday", "Thursday")),
      new Dish("french fries", true, 530, Dish.Type.OTHER, Arrays.asList("Sunday", "Monday")),
      new Dish("rice", true, 350, Dish.Type.OTHER, Arrays.asList("Sunday", "Monday")),
      new Dish("season fruit", true, 120, Dish.Type.OTHER,
          Arrays.asList("Monday", "Tuesday", "Saturday")),
      new Dish("pizza", true, 550, Dish.Type.OTHER, Arrays.asList("Sunday", "Friday")),
      new Dish("prawns", false, 400, Dish.Type.FISH, Arrays.asList("Saturday", "Sunday")),
      new Dish("salmon", false, 450, Dish.Type.FISH, Arrays.asList("Sunday", "Monday")));

  public static void main(String[] args) {
    // ======================================================================================
    System.out.println("***** A list of high calorie dishes using java 8 *****");
    List<String> highCalorieDishes = menu.stream().filter(d -> d.getCalories() > 400)
        .map(Dish::getName).limit(3).collect(toList());
    highCalorieDishes.forEach(name -> System.out.println("High calorie dish name " + name));
    // To do this in java 7
    // ======================================================================================
    System.out.println("***** Now we are doing it in java 7 *****");
    List<Dish> highCalDishes = new ArrayList<>();
    int limit = 0;
    // filter
    for (Dish dish : menu) {
      if (dish.getCalories() > 300) {
        limit++;
        highCalDishes.add(dish);
      }
      // limit
      if (limit == 3) {
        break;
      }
    }
    // map - get the names of the dishes
    // collect - store the names in an another list
    List<String> highCalDishNames = new ArrayList<>();
    for (Dish dish : highCalDishes) {
      highCalDishNames.add(dish.getName());
    }
    for (String name : highCalDishNames) {
      System.out.println("High calorie dish name " + name);
    }
    // ======================================================================================
    // Get high calorie vegetarian food
    List<Dish> highCalVegDishes =
        menu.stream().filter(d -> d.getCalories() > 300 && d.isVegetarian()).collect(toList());
    highCalVegDishes.forEach(d -> System.out.println("High calorie vegetarian dish" + d));
    // ======================================================================================
    // Get distinct even numbers
    System.out.println("***** The entire array *****");
    Integer[] numbers = {10, 10, 23, 21, 45, 54, 23, 21, 54, 54, 15, 12, 12, 12};
    Arrays.asList(numbers).forEach(i -> System.out.print(i + " "));
    System.out.println("\n***** Distinct even numbers from the array *****");
    // Get all the even numbers from the array and remove the duplicates
    Arrays.stream(numbers).filter(n -> n % 2 == 0).distinct().collect(toList())
        .forEach(i -> System.out.print(i + " "));
    // Sort the array and get the last 3 elements
    // skip method just skips elements. We are skipping from the start till 3 elements before the
    // end element
    System.out.println("\n***** Get the last 3 elements from the sorted array *****");
    Arrays.stream(numbers).sorted().skip(numbers.length - 3)
        .forEach(i -> System.out.print(i + " "));
    // ======================================================================================
    // Get all available dish names
    System.out
        .println("\n***** Get the names of all items in the menu and print them in full caps*****");
    menu.stream().map(Dish::getName).map(String::toUpperCase).forEach(System.out::println);
    System.out.println("***** Dish with the longest name and its length*****");
    menu.stream().sorted((d1, d2) -> d2.getName().length() - d1.getName().length()).limit(1)
        .forEach(d -> System.out.println(d.getName().toUpperCase() + " " + d.getName().length()));
    // ======================================================================================
    // Get unique characters in a string
    // Now we have an array of words
    System.out.println("***** Get distinct characters from a list of strings *****");
    System.out.println("Without using flatMap - wrong method");
    List<String> words = Arrays.asList("Hello", "World", "In", "Java8");
    // We need H,e,l,0,w,r,d,I,n,J,a,v,8. Not possible with the map
    // This is wrong. The map returns a stream for a String[].
    words.stream().map(word -> word.split("")).distinct().forEach((w) -> System.out.print(w + " "));
    // What is needed is Stream<String> not Stream<String[]>
    System.out.println("\nUsing flatMap");
    // Every string in the words list is converted to a String[] by the split and then each String[]
    // is converted to a Stream<String>. Finally all the Stream<String> are combined and returned as
    // a single Stream<String>
    words.stream().flatMap(s -> Arrays.stream(s.split(""))).distinct()
        .forEach((w) -> System.out.print(w + "  "));
    // ======================================================================================
    // Get all values of the list days of all the objects of type Dish
    System.out.println(
        "\n***** Using flatMaps to find whether all days of a week are covered in the menu****");
    // Use of flatMap, taking the values of the list 'days' of all the objects that are in the menu
    // list and combining them together to find distinct days
    boolean entireWeekCovered =
        menu.stream().flatMap(dish -> dish.getDays().stream()).distinct().count() == 7;
    System.out.println(entireWeekCovered == true ? "The menu is complete for the week"
        : "The menu is not complete for the week");
    menu.stream().flatMap(dish -> dish.getDays().stream()).distinct().forEach(System.out::println);
    // ======================================================================================
    // Get all pairs between two given arrays
    System.out.println("***** Between two arrays generate their pairs *****");
    int n1[] = {1, 2, 3};
    int n2[] = {3, 4};
    List<Integer[]> numberPairs = new ArrayList<>();
    for (int i = 0; i < n1.length; i++) {
      for (int j = 0; j < n2.length; j++) {
        Integer[] n = new Integer[2];
        n[0] = n1[i];
        n[1] = n2[j];
        numberPairs.add(n);
      }
    }
    for (Integer[] n : numberPairs) {
      for (Integer value : n) {
        System.out.print(value + " ");
      }
      System.out.println();
    }
    System.out.println("***** Between two arrays generate their pairs - using streams *****");
    List<Integer> numbers1 = Arrays.asList(1, 2, 3);
    List<Integer> numbers2 = Arrays.asList(3, 4);
    // For every element i of numbers1, convert number2 into a stream and create an array with i and
    // every element j as being pairs
    List<int[]> pairs = numbers1.stream().flatMap(i -> numbers2.stream().map(j -> new int[] {i, j}))
        .collect(toList());
    for (int[] numberArray : pairs) {
      for (int number : numberArray) {
        System.out.print(number + " ");
      }
      System.out.println();
    }
    // Get pairs divisible by 3
    System.out.println("***** Pairs that are divisible by 3 *****");
    List<int[]> pairsDivBy3 = numbers1.stream()
        .flatMap(i -> numbers2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[] {i, j}))
        .collect(toList());
    for (int[] numberArray : pairsDivBy3) {
      for (int number : numberArray) {
        System.out.print(number + " ");
      }
      System.out.println();
    }
    // ======================================================================================
    // Display a 3X3 matrix using streams and lambdas
    System.out.println("***** A 3X3 matrix *****");
    List<Integer> numbers3 = Arrays.asList(0, 1, 2);
    List<Integer> numbers4 = Arrays.asList(0, 1, 2);
    List<int[]> resultPairs = numbers3.stream()
        .flatMap(i -> numbers4.stream().map(j -> new int[] {i, j})).collect(toList());
    for (int[] numberArray : resultPairs) {
      for (int number : numberArray) {
        System.out.print(number + " ");
      }
      System.out.println();
    }

  }

}


@Data
@AllArgsConstructor
class Dish {
  private String name;
  private final boolean vegetarian;
  private final int calories;
  private final Type type;
  private List<String> days;

  enum Type {
    MEAT, FISH, OTHER
  }

}

