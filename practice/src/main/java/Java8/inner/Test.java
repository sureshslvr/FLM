package Java8.inner;


public class Test {
    public static void main(String[] args) {
        /*Bike class implements Vehicle and override method in bike*/
        Bike bike=new Bike();
        bike.start();

        /*anonymous inner class */
        Vehicle lorry= new Vehicle() {
            @Override
            public void start() {

                System.out.println("lorry started");
            }
        };
        lorry.start();

        /*Lambda expression*/
        Vehicle car=()-> System.out.println("car started");
        car.start();

    }
}
