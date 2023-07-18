package app;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ShopBean {
    String name;

    public String getName() {
        return name;
    }

    public ShopBean(){}

    public void setName(String name) {
        this.name = name;
    }
}
