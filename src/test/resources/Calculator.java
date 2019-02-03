package sample;

public class Calculator {

  public static void main(String[] args) {
    System.out.println(new Calculator().evaluate("1+1"));
  }

  public int evaluate(String expression) {
    int sum = 0;
    for (String summand: expression.split("\\+"))
      sum += Integer.valueOf(summand);
    return sum;
  }
}
