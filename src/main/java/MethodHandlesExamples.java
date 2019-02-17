import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.util.Random;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.methodType;

public class MethodHandlesExamples {

    //has access to everything visible to this code
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    //has access to only public fields and methods
    private static final MethodHandles.Lookup PUBLOOKUP = MethodHandles.publicLookup();

    public static void main(String[] args) throws Throwable {

        /* Classic java method invocation

         String value1 = System.getProperty("java.home");
         System.out.println("Hello, MadrasJUG");

         */

        // getProperty signature (return type, parameter type)
        MethodType type1 = MethodType.methodType(String.class, String.class);

        // println signature (return type, parameter type)
        MethodType type2 = MethodType.methodType(void.class, Object.class);

        //find static method
        MethodHandle getPropertyMH = LOOKUP
                .findStatic(System.class, "getProperty", type1);

        //find virtual method
        MethodHandle printlnMH = LOOKUP
                .findVirtual(PrintStream.class, "println", type2);

        //get java home path
        String javaHomePath = (String) getPropertyMH.invoke("java.home");

        //print java home path
        printlnMH.invoke(System.out, (Object) javaHomePath);

        //print Hello MadrasJUG
        printlnMH.invoke(System.out, (Object) "Hello, MadrasJUG");

        /* Field get examples

        PrintStream out = System.out;

        */

        MethodHandle systemOutMH = LOOKUP
                .findStaticGetter(System.class, "out", PrintStream.class);

        PrintStream out3 = (PrintStream) systemOutMH.invoke();

        /* Field Set examples

        class Person {
            public String name;
        }

        Person person = new Person();
        person.name = "Vignesh";

        */

        class Person {
            public String name;
        }

        MethodHandle nameSetter = LOOKUP
                .findSetter(Person.class, "name", String.class);

        Person vignesh = (Person) nameSetter.invoke(new Person(), "Vignesh");

        /* insert/Change Arguments to existing methodHandles */

        MethodHandle getJavaHomeMH =
                MethodHandles.insertArguments(getPropertyMH, 0, "java.home");
        MethodHandle systemOutPrintlnMH =
                MethodHandles.insertArguments(printlnMH, 0, System.out);

//        // same as getProperty("java.home")
        getJavaHomeMH.invokeWithArguments();
//        // same as System.out.println(...
        systemOutPrintlnMH.invokeWithArguments("Hello, MadrasJUG");

        /* filter example */

        MethodHandle upperCaseMethodHandle = LOOKUP.findVirtual(
                String.class,
                "toUpperCase",
                methodType(String.class));

        // Change its type to Object ...(Object)
        MethodHandle objectToUpperCaseMH =
                upperCaseMethodHandle.asType(methodType(Object.class, Object.class));

        // Make a println that always upcases
        MethodHandle upcasePrintlnMH =
                filterArguments(systemOutPrintlnMH, 0, objectToUpperCaseMH);

        // prints out TURN TO UPPERCASE
        upcasePrintlnMH.invokeWithArguments("turn to uppercase");

        /* branch based on a logic

          class UpperOrLowerCase {
             private static final Random RANDOM = ...
             public String call(String inputString) {
                 if (RANDOM.nextBoolean()) {
                     return inputString.toUpperCase();
                 } else {
                     return inputString.toLowerCase();
                 }
             }
         } */

        // pointer to String.toLowerCase
        MethodHandle toLowerCaseMH = LOOKUP.findVirtual(
                String.class,
                "toLowerCase",
                methodType(String.class));

        // randomly return true or false
        MethodHandle upperCaseOrLowerCase = LOOKUP.findStatic(
                MethodHandlesExamples.class,
                "randomBoolean",
                methodType(boolean.class));

        // ignore the incoming String by dropping it
        upperCaseOrLowerCase = dropArguments(upperCaseOrLowerCase, 0, String.class);

        MethodHandle upperDowner = guardWithTest(
          upperCaseOrLowerCase,
                upperCaseMethodHandle,
                toLowerCaseMH);

        // print out the result
        MethodHandle upperDownerPrinter = filterArguments(
                systemOutPrintlnMH,
                0,
                upperDowner.asType(methodType(Object.class, Object.class)));

        // depending on random boolean, uppercases or lowercases the String
          upperDownerPrinter.invoke("InvokeDynamic!!!");
          upperDownerPrinter.invoke("InvokeDynamic!!!");
          upperDownerPrinter.invoke("InvokeDynamic!!!");
          upperDownerPrinter.invoke("InvokeDynamic!!!");
          upperDownerPrinter.invoke("InvokeDynamic!!!");

    }
    private static final Random RANDOM = new Random(System.currentTimeMillis());    
    
    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }
}
