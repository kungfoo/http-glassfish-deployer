package ch.mollusca.sample.war;

import javax.ejb.Stateless;

@Stateless
public class CanHasBeans {

    public String canHasMethod() {
        return "Hello Beans";
    }
}
