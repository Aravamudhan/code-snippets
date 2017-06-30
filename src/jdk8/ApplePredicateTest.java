package jdk8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

// Strategy design pattern
// The code snippets demonstrate the use of strategy pattern with out lambas
public class ApplePredicateTest {

  public static void main(String[] args) {
    List<Apple> inventory = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"),
        new Apple(120, "red"), new Apple(200, "dark red"), new Apple(220, "lite green"));
    for (Apple apple : inventory) {
      apple.prettyPrintApple(new ApplePrettyPrint());
    }
    for (Apple apple : inventory) {
      apple.prettyPrintApple(new AppleColorPrint());
    }
    for (Apple apple : inventory) {
      apple.prettyPrintApple(new AppleFormatter() {
        @Override
        public String format(Apple apple) {
          String s = (apple.getWeight() > 100 ? "Heavy apple" : "Small apple");
          return s;
        }
      });
    }
    inventory.sort(new Comparator<Apple>() {

      @Override
      public int compare(Apple a1, Apple a2) {
        System.out.println("Comparing " + a1 + "" + a2);
        return a1.getWeight().compareTo(a2.getWeight());
      }
    });
    System.out.println("Sorted-----");
    for (

    Apple apple : inventory) {
      System.out.println(apple);
    }
  }

}


// Using the lombok library
@Data
@AllArgsConstructor
class Apple {
  private Integer weight = 0;
  private String color = "";

  public Apple(Integer weight) {
    this.weight = weight;
  }

  public Apple(String color) {
    this.color = color;
  }

  public void prettyPrintApple(AppleFormatter formatter) {
    System.out.println(formatter.format(this));
  }
}


interface AppleFormatter {
  public String format(Apple apple);
}


// A predicate
class ApplePrettyPrint implements AppleFormatter {
  @Override
  public String format(Apple apple) {
    String s = "The apple is with " + apple.getColor() + " color, weighing " + apple.getWeight()
        + " grams";
    return s;
  }
}


class AppleColorPrint implements AppleFormatter {
  @Override
  public String format(Apple apple) {
    String s = apple.getColor() + " color apple";
    return s;
  }

}


