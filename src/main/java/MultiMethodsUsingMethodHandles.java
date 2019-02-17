import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

class Parent {

  public void printWeight(double weight) {
    System.out.println("Parent::Weight");
  }

  public void printHeight(double height) {
    System.out.println("Parent::Height");
  }
}

class Child extends Parent {

  public void printWeight(double weight) {
    System.out.println("Child::Weight");
  }

  public void printHeight(int height) {
    System.out.println("Child::Height");
  }
}

public class MultiMethodsUsingMethodHandles {
  public static void main(String[] args) throws Throwable {
    Parent p = new Child();

    p.printWeight(60);
    p.printWeight(6.0);

    System.out.println("======================");

    p.printWeight(50);
    p.printHeight(5.0);

    System.out.println("========================");

    callMultiMethod(p, 50, int.class);
    callMultiMethod(p, 5.0, double.class);
  }

  private static void callMultiMethod(Object instance, Object parameter, Class<?> parameterType) throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle printHeightHandle = lookup.findVirtual(
      instance.getClass(),
      "printHeight",
      MethodType.methodType(void.class, parameterType));

    printHeightHandle.bindTo(instance).invoke(parameter);
  }
}
