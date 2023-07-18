package app;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CustomerBean {
    String name;

    public String getName() {
        return name;
    }

    public CustomerBean(){this.name = "default";}

    public void setName(String name) {
        this.name = name;
    }
}
