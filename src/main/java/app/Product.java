package app;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
//import java.util.Currency;


@Entity
public class Product {
    public Customer getPurchased() {
        return purchased;
    }

    public void setPurchased(Customer purchased) {
        this.purchased = purchased;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    String name;
    @Column
    String description;
    @Column
    String imagepath;
    @Column
    double rating;
    @Column
    int noreviews;
    @Column
    double price;
    @Column
    boolean ishidden;

    @ManyToOne
    @JsonIgnore
    Customer purchased;

    @ManyToOne
    @JsonIgnore
    Shop shop;

    @ManyToOne
    @JsonIgnore
    Customer customer;

    @ManyToOne
    OrderStruct orders;

    public Product(){}

    public boolean isHidden() {
        return ishidden;
    }

    public void setHidden(boolean hidden) {
        ishidden = hidden;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagepath;
    }

    public void setImagePath(String imagePath) {
        this.imagepath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoReviews() {
        return noreviews;
    }

    public void setNoReviews(int noReviews) {
        this.noreviews = noReviews;
    }

    public boolean isIshidden() {
        return ishidden;
    }

    public void setIshidden(boolean ishidden) {
        this.ishidden = ishidden;
    }

    public OrderStruct getOrders() {
        return orders;
    }

    public void setOrders(OrderStruct orders) {
        this.orders = orders;
    }

    public double getRating() {
        return rating;
    }

    public String getRatingAsFraction() {
        String fraction = Double.toString(rating);
        fraction = fraction + "/5";

        return fraction;
    }

    public String getRatingStars() {
        String starRating = " ";
        for (int i = 0; i < rating; i++) {
            starRating = starRating + " ★ ";
        }
        return starRating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceAsCurrency() {
        String tempPrice = String.format("%.2f", price);
        tempPrice = "€" + tempPrice;
        return tempPrice;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }


}
