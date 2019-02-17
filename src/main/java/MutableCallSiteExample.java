import java.lang.invoke.*;
import java.util.Random;

public class MutableCallSiteExample {

  public static void toUpperCase(String data) {
    System.out.println(data.toUpperCase());
  }

  public static void toLowerCase(String data) {
    System.out.println(data.toLowerCase());
  }

  static MethodHandle upperCaseHandle;
  static MethodHandle lowerCaseHandle;

  public static void main(String[] args) throws Throwable {
    //create a mutable CallSite
    CallSite mutableCallSite = createMutableCallSite();


    MethodHandle methodHandle = mutableCallSite.dynamicInvoker();

    for (int i = 0; i < 25; i++) {
      methodHandle.invoke("MutableCallSite Example");
      switchHandle(mutableCallSite);
      Thread.sleep(700);
    }
  }

  private static void switchHandle(CallSite callSite) {
    if (RANDOM.nextBoolean())
      callSite.setTarget(lowerCaseHandle);
    else
      callSite.setTarget(upperCaseHandle);
  }

  private static final Random RANDOM = new Random(System.currentTimeMillis());


  private static CallSite createMutableCallSite() throws NoSuchMethodException, IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    upperCaseHandle = lookup.findStatic(MutableCallSiteExample.class, "toUpperCase", MethodType.methodType(void.class,String.class));
    lowerCaseHandle = lookup.findStatic(MutableCallSiteExample.class, "toLowerCase", MethodType.methodType(void.class,String.class));

    return new MutableCallSite(upperCaseHandle);
  }
}