package sample;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {

  @Test
  public void evaluatesExpression() {
    Calculator calculator = new Calculator();
    int sum = calculator.evaluate("1+2+3");
    assertEquals(6, sum);
  }
}
