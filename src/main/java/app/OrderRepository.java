package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderStruct, Integer>{
    OrderStruct findByCustomer(Customer customer);
}
