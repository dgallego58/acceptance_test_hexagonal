package co.com.bancolombia.health;

import com.intuit.karate.junit5.Karate;

public class ServiceUPAvailabilittyRunner {


    @Karate.Test
    Karate checkHealt(){
        return Karate.run("health").relativeTo(getClass());
    }

}
