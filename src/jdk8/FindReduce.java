package jdk8;

import static jdk8.StreamsBasic.menu;

public class FindReduce {

  public static void main(String[] args) {
    menu.forEach(System.out::println);
    System.out.println("***** Finding from the menu *****");
    System.out.println("Vegetarian friendly menu ? " + menu.stream().anyMatch(Dish::isVegetarian));
    System.out
        .println("Is the menu healthy ? " + menu.stream().allMatch(d -> d.getCalories() < 1000));
    System.out.println(
        "No items with empty names  ? " + menu.stream().noneMatch(d -> d.getName() == null));
    // findFirst - always returns the Option<T> for the 1st result
    System.out.println("The 1st dish name " + menu.stream().findFirst().get().getName());
    // findAny - just like findFirst except the result could be 1st or any other result. The result
    // is computed based on the parallel processing of the stream
    System.out.println("A random dish name " + menu.parallelStream().findAny().get().getName());
    System.out.println("***** Reduce *****");
    System.out.println("The total calories in the menu "
        + menu.stream().map(Dish::getCalories).reduce(0, (a, b) -> a + b));
    // Using method reference of Integer.sum(int,int)
    System.out.println("The total calories in the menu "
        + menu.stream().map(Dish::getCalories).reduce(0, Integer::sum));
    // Returns an Optional<Integer>
    System.out.println("The total calories in the menu "
        + menu.stream().map(Dish::getCalories).reduce(Integer::sum).get());
    // Reduce operation to produce a maximum value
    System.out.println("Item with the maximum calorie value "
        + menu.stream().map(Dish::getCalories).reduce(Integer::max).get());
    System.out.println("Item with the minimum calorie value "
        + menu.stream().map(Dish::getCalories).reduce(Integer::min).get());
    // Count a particular object/value in a Stream of objects. Using the built in method it is
    // "menu.stream().count()". But without using it,
    System.out
        .println("Total items in the menu " + menu.stream().map(d -> 1).reduce(0, Integer::sum));

  }

}
