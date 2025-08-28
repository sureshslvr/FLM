package Java8.functionalinterfaceex;

@FunctionalInterface
public interface Interface2 {
    void hi();
    default void sayHello(){
        System.out.println("from interface2");
    }
    static void staticMethod(){
        System.out.println("from interface2 static method");
    }

}
