package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TheseusController {

    @Autowired
    private CustomerRepository CustomerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired private CustomerBean customerBean;

    @Autowired private ShopBean shopBean;


    @GetMapping("/")
    public String index(Model model) {
        List<Product> notHidden = productRepository.findAll();
        for (int i=0; i < notHidden.size(); i++){
            if(notHidden.get(i).isHidden())
                notHidden.remove(i);
        }
        model.addAttribute("Product", notHidden);
        return "index.html";
    }

    @GetMapping("/error")
    public String error(){return "error.html";}

    @GetMapping("/search")
    public String search(@RequestParam String search, Model model) {

        String delims="[, ]+"; //delims are comma and white space
        String[] words = search.split(delims);

        List<Product> productsMatching = new ArrayList<>();

        List<Product> searchTask = productRepository.findAll();
        for (int i=0; i < searchTask.size(); i++){
            for(int j = 0; j < words.length; j++)
            {
                if(searchTask.get(i).isHidden())
                    break;

                if(searchTask.get(i).getDescription().toLowerCase().contains(words[j].toLowerCase()))
                    productsMatching.add(searchTask.get(i));
                else if (searchTask.get(i).getName().toLowerCase().contains(words[j].toLowerCase()))
                    productsMatching.add(searchTask.get(i));

                break;
            }
        }

        model.addAttribute("Product", productsMatching);
        return "search.html";

    }

    @GetMapping("/payment")
    public String payment(){return "payment.html";}

    @GetMapping("/product")
    public String product(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("Product", productRepository.getOne(id));
        model.addAttribute("customerBean.getName()", customerBean.getName());
        return "product.html";
    }

    @GetMapping("/account")
    public String account(Model model) {

        if(customerBean.getName().matches("default"))
        {
            return "account.html";
        }
        else
        {
            return "loggedin.html";
        }
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<OrderStruct> allOrders = orderRepository.findAll();
        List<Product> orderHistory = new ArrayList<>();
        double total = 0;

        for(int i=0; i < allOrders.size(); i++)
        {
            if(allOrders.get(i).getCustomer().getName().matches(customerBean.getName()) && allOrders.get(i).getStatus().matches("In Cart"))
            {
                orderHistory.add(productRepository.getOne(allOrders.get(i).getProduct().getId()));
                total += productRepository.getOne(allOrders.get(i).getProduct().getId()).getPrice();
            }
        }

        model.addAttribute("products", orderHistory);
        model.addAttribute("total", total);

        return "checkout.html";
    }



    @PostMapping("/account/add")
    public @ResponseBody Customer account(@RequestBody Customer customer) {
        CustomerRepository.save(customer);
        return customer;
    }

    @GetMapping("/logout")
    public String logout(Model model)
    {
        customerBean.setName("default");
        model.addAttribute("Product", productRepository.findAll());

        return "index.html";
    }

    @GetMapping("/save")
    public String save(@RequestParam String username, Model model)
    {
        if(CustomerRepository.findByName(username) == null){
            return "account.html";
        }
        model.addAttribute("Product", productRepository.findAll());
        customerBean.setName(username);
        return "index.html";
    }

    @PostMapping("/product/add")
    public @ResponseBody void addProduct(@RequestParam int id)
    {
        Product product = productRepository.getOne(id);
        Customer customer = CustomerRepository.findByName(customerBean.getName());
        OrderStruct orderStruct = new OrderStruct();
        orderStruct.setProduct(product);
        orderStruct.setCustomer(customer);
        orderStruct.setStatus("In Cart");
        orderRepository.save(orderStruct);
    }

    @GetMapping("/product/remove")
    public @ResponseBody Integer removeProduct(@RequestParam int id)
    {
        List<OrderStruct> allOrders = orderRepository.findAll();
        OrderStruct orderStruct = null;

        for(int i=0; i < allOrders.size(); i++)
        {
            if(allOrders.get(i).getCustomer().getName().matches(customerBean.getName()))
            {
                if(allOrders.get(i).getProduct().getId() == id)
                {
                    orderStruct = allOrders.get(i);
                    break;
                }
            }
        }

        if (orderStruct != null)
            orderRepository.delete(orderStruct);


        return id;
    }

    @GetMapping("/shop")
    public String shop(Model model)
    {
        Customer customer = CustomerRepository.findByName(customerBean.getName());

        if(customer.getShop() == null)
        {
            model.addAttribute(customerBean.getName());
            return "createshop.html";
        }
        else{
            Shop current = shopRepository.findByName(shopBean.getName());
            model.addAttribute("products", current.getProducts());
            return "shop.html";
        }
    }

    @PostMapping("/shop/product/add")
    public @ResponseBody Product addProduct(@RequestBody Product product){
        product.setCustomer(CustomerRepository.getOne(1));
        product.setShop(shopRepository.findByName(shopBean.getName()));
        productRepository.save(product);
        return product;
    }

    @PostMapping("/shop/product/edit")
    public @ResponseBody Product editProduct(@RequestParam int id, @RequestBody Product product){
        Product curr = productRepository.getOne(id);
        curr.setDescription(product.getDescription());
        curr.setName(product.getName());
        curr.setImagePath(product.getImagePath());
        product.setId(id);
        productRepository.save(curr);
        return product;
    }

    @GetMapping ("/shop/save")
    public String saveShop(@RequestParam String shopname, Model model){

        shopBean.setName(shopname);
        Customer customer = CustomerRepository.findByName(customerBean.getName());

        List<Shop> allShops = shopRepository.findAll();

        for(int i=0; i < allShops.size(); i++)
        {
            if(allShops.get(i).getName().matches(shopname))
            {
                return "createshop.html";
            }
        }

        Shop shop = new Shop();
        shop.setName(shopname);
        shop.setOwner(customer);
        shopRepository.save(shop);
        customer.setShop(shop);
        CustomerRepository.save(customer);
        return "goToStore.html";
    }

    @GetMapping("/orders")
    public String order(Model model) {
        List<OrderStruct> allOrders = orderRepository.findAll();
        List<Product> orderHistory = new ArrayList<>();


        for(int i=0; i < allOrders.size(); i++)
        {
            OrderStruct currentOrder = allOrders.get(i);
            if(currentOrder.getCustomer().getName().matches(customerBean.getName()) && !currentOrder.getStatus().matches("In Cart"))
            {
                orderHistory.add(productRepository.getOne(allOrders.get(i).getProduct().getId()));
            }
        }

        model.addAttribute("products", orderHistory);
        return "order.html";
    }

    @GetMapping("/shop/orders")
    public String shoporders(Model model) {
        List<OrderStruct> allOrders = orderRepository.findAll();
        List<Product> orderHistory = new ArrayList<>();
        List<Integer> orderID = new ArrayList<>();

        System.out.println("Entered /shop/orders");

        for(int i=0; i < allOrders.size(); i++)
        {
            System.out.println("Inside for loop with allOrders size: " + allOrders.size());
            if(allOrders.get(i).getProduct().getShop().getId() == CustomerRepository.findByName(customerBean.getName()).getShop().getId())
            {
                orderHistory.add(productRepository.getOne(allOrders.get(i).getProduct().getId()));
                orderID.add(i + 1);
            }
        }

        model.addAttribute("products", orderHistory);
        model.addAttribute("IDS", orderID);
        return "shoporder.html";
    }

    @GetMapping("/shop/status")
    public String status(@RequestParam String status, Model model) {

        int id = 0;

        if(status.contains("Processing")) {
            id = Integer.parseInt(status.substring(10));
            status = status.substring(0, 10);
        }
        else{
            id = Integer.parseInt(status.substring(9));
            status = status.substring(0, 9);
        }

        OrderStruct orderStruct = orderRepository.getOne(id);
        orderStruct.setStatus(status);
        orderRepository.save(orderStruct);

        List<OrderStruct> allOrders = orderRepository.findAll();
        List<Product> orderHistory = new ArrayList<>();
        List<Integer> orderID = new ArrayList<>();

        for(int i=0; i < allOrders.size(); i++)
        {
            if(allOrders.get(i).getProduct().getShop().getId() == CustomerRepository.findByName(customerBean.getName()).getShop().getId())
            {
                orderHistory.add(productRepository.getOne(allOrders.get(i).getProduct().getId()));
                orderID.add(i + 1);
            }
        }

        model.addAttribute("products", orderHistory);
        model.addAttribute("IDS", orderID);

        return "shoporder.html";
    }

    @GetMapping("/history")
    public String history(Model model) throws InterruptedException {
        List<OrderStruct> allOrders = orderRepository.findAll();


        for(int i=0; i < allOrders.size(); i++){
            if(allOrders.get(i).getCustomer().getName().matches(customerBean.getName())){
                allOrders.get(i).setStatus("Processing");
                orderRepository.save(allOrders.get(i));
            }
        }

        TimeUnit.SECONDS.sleep(5);

        List<Product> notHidden = productRepository.findAll();
        for (int i=0; i < notHidden.size(); i++){
            if(notHidden.get(i).isHidden())
                notHidden.remove(i);
        }
        model.addAttribute("Product", notHidden);
        return "index.html";
    }

    @GetMapping("/product/hide")
    public String hideProduct(@RequestParam int id){
        System.out.println("ID:"+id);
        Product product = productRepository.getOne(id);
        System.out.println(product.getName());
        product.setHidden(!product.isHidden());
        productRepository.save(product);

        return "shop.html";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model model){
        Product product = productRepository.getOne(id);
        model.addAttribute("product", product);
        return "editProduct.html";
    }

    @GetMapping("/order/rate")
    public String rateProduct(@RequestParam int id, @RequestParam int rating){

        Product product = productRepository.getOne(id);
        product.setNoReviews(product.getNoReviews() + 1);

        double average = product.getRating();

        System.out.println(average);


        average = average + ((rating - product.getRating()) / product.getNoReviews());

        System.out.println(average);

        product.setRating(average);

        productRepository.save(product);

        return "order.html";
    }
}



