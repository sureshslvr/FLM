package sim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Scope("prototype")
/*Common Scopes in Spring with @Scope:
singleton (default): One shared instance per Spring container.
prototype: A new instance is created every time the bean is requested.
request: (Web applications) A new bean instance is created for each HTTP request.
session: (Web applications) A new bean instance is created for each HTTP session.
application: (Web applications) One bean instance per ServletContext lifecycle.
websocket: One instance per WebSocket lifecycle.*/
public class SimHolder {
    @Autowired
    @Qualifier("jio")
    //if we have more than two beans we need to tell the spring to witch been to be used
    private Sim sim;

    public void call(){
        sim.call();
    }
    //without calling this method it will execute after dependencies are injected
    /*bean life cycle:
    *1. container started
    *2. beans initialised
    *3. dependencies injected
    *4. init (@PostConstruct)
    *5. normal methods
    *6. destroy (@PreDestroy) */
 @PostConstruct
 public void call2(){
     System.out.println("test method for @PostContruct");
 }

 /*to test the PreDestory method/annotaton we can use the  ConfigurableApplicationContext and close method*/
 @PreDestroy
 public void cleanup() {
     // Code to release resources or do cleanup tasks before bean destruction
     System.out.println("Bean is being destroyed, cleaning up resources...");
 }
 /*PreDestroy and PostConstruct annotation by defaults comes in spring boot but to use in spring we need to add Jakarta-annotations dependency*/
}
