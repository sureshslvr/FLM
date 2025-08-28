package Java8.functionalinterfaceex;

public class Impl implements Interface1,Interface2{
    @Override
    public void sayHi() {
        System.out.println("from impl sayHi method");
    }
    @Override
    public void sayHello() {
        Interface1.super.sayHello();
    }
    @Override
    public void hi() {
        System.out.println("from impl hi method");
    }
}
