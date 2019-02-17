import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class RegularVsMethodHandleVsReflectCall {

  private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

  public static void main(String[] args) throws Throwable {
    regularCall();
    methodHandlesCall();
    reflectionCall();
  }

  public static void regularCall() throws Exception {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      String a = new String();
      a.concat("123");
    }
    System.out.println(System.currentTimeMillis() - start);
  }

  public static void methodHandlesCall() throws Throwable {
    long start = System.currentTimeMillis();
    MethodType type = MethodType.methodType(String.class, String.class);
    MethodHandle concatMH = LOOKUP
      .findVirtual(String.class, "concat", type);
    for (int i = 0; i < 10000000; i++) {
      concatMH.invoke(new String(),"123");
    }
    System.out.println(System.currentTimeMillis() - start);
  }

  public static void reflectionCall() throws Exception {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      String s = (String) Class.forName("java.lang.String").newInstance();
      s.concat("123");
    }
    System.out.println(System.currentTimeMillis() - start);
  }
}
