package app;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column
    String name;
    @Column
    String password;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Product> shoppingcart;
    @OneToMany(mappedBy = "purchased")
    List<Product> orderHistory;
    @ManyToOne
    OrderStruct orders;
    @OneToOne
    Shop shop;

    public Customer(){}

    public List<Product> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Product> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Product> getShoppingcart() {
        return shoppingcart;
    }

    public void setShoppingcart(List<Product> shoppingcart) {
        this.shoppingcart = shoppingcart;
    }

}
