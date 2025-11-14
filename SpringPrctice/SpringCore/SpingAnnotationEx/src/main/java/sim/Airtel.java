package sim;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
//this annotation will make the been to call always if no bean is used @qualified
//@primary is having higher precedence if no Qualifier is there
//@if we use both annotations @Qualifier will have higher precedence
public class Airtel  implements Sim{
    @Override
    public void call() {
        System.out.println("Airtel is calling");
    }
}
