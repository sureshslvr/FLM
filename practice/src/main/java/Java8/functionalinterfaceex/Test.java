package Java8.functionalinterfaceex;

public class Test {
    public static void main(String[] args) {
        Interface2 impl=new Impl();
        impl.hi();
        impl.sayHello();
        Interface2.staticMethod();
    }
}
