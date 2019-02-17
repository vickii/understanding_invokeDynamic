import java.util.ArrayList;
import java.util.List;

public class BasicInvocationMethodsAtBytecode {

  public static void main(String[] args){

    String.valueOf(1); // static method call

    "lowercase".toLowerCase();; //virtual method call

    List list = new ArrayList<>(); //constructor call for object creation

    list.size(); // call method on an interface

    Demo dem0 = new Demo();

    dem0.doSomething();
    dem0.doSomething();

  }
}

class Demo{

  public final void doSomething(){}
}
