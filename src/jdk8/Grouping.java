package jdk8;

import static jdk8.StreamsBasic.menu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Grouping {

  public static void main(String[] args) {
    System.out.println("----- Dishes by type -----");
    System.out.println("Simple grouping using method references");
    // Simple grouping. For every dish type, a list of dishes are created and finally a map
    // containing DishType and List<Dish> is created
    Map<Dish.Type, List<Dish>> dishesByType =
        menu.stream().collect(Collectors.groupingBy(Dish::getType));
    dishesByType.entrySet().forEach(entry -> {
      System.out.println("\nDish type :" + entry.getKey());
      System.out.print("Items :");
      entry.getValue().forEach(v -> System.out.print(v.getName() + " "));
    });
    System.out.println("\n\n----- Dishes by calories -----");
    System.out.println("Custom grouping");
    // Based on the calorie level, a dish list is created
    Map<CalorieLevel, List<Dish>> dishesByCalorie =
        menu.stream().collect(Collectors.groupingBy(dish -> {
          if (dish.getCalories() <= 400) {
            return CalorieLevel.DIET;
          } else if (dish.getCalories() <= 700) {
            return CalorieLevel.NORMAL;
          } else {
            return CalorieLevel.FAT;
          }
        }));
    dishesByCalorie.entrySet().forEach(entry -> {
      System.out.println("\nDish type :" + entry.getKey());
      System.out.print("Items :");
      entry.getValue().forEach(v -> System.out.print(v.getName() + " "));
    });
    System.out.println("\n\n-----Dishes by type and calories-----");
    System.out.println("Multi level grouping");
    Map<Dish.Type, Map<CalorieLevel, List<Dish>>> dishesByTypeCalorie =
        menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.groupingBy(dish -> {
          if (dish.getCalories() <= 400) {
            return CalorieLevel.DIET;
          } else if (dish.getCalories() <= 700) {
            return CalorieLevel.NORMAL;
          } else {
            return CalorieLevel.FAT;
          }
        })));
    dishesByTypeCalorie.entrySet().forEach(entry -> {
      System.out.println("*****The dishtype: " + entry.getKey());
      entry.getValue().entrySet().forEach(dishSet -> {
        System.out.println("The calorie :" + dishSet.getKey());
        System.out.println("The dishes :" + dishSet.getValue());
      });
    });
  }
}


enum CalorieLevel {
  DIET, NORMAL, FAT
}
