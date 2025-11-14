package sim;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class simTestMain {

    public static void main(String[] args) {
        ApplicationContext context= new AnnotationConfigApplicationContext(SimConfig.class);
        SimHolder s=context.getBean("simHolder",SimHolder.class);
        s.call();
        SimHolder s1=context.getBean("simHolder",SimHolder.class);
        //compare two beans
        //if we use single ton scope it will be true if we use prototype scope it will be false
        //because prototype will create new instance every time the bean is requested.
        System.out.println(s==s1);


        /*to test the PreDestory method/annotaton we can use the  ConfigurableApplicationContext and close method*/
        ConfigurableApplicationContext ctx= new AnnotationConfigApplicationContext(SimConfig.class);
        ctx.close();
    }
}
