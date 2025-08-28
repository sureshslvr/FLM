package Java8.functionalinterfaceex;

@FunctionalInterface
public interface Interface1 {

    void sayHi();
    default void sayHello(){
        System.out.println("from interface1");
    }
    static void method(){
        System.out.println("from interface1 static method");
    }

}
