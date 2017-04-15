package aws.boot.angular.repoistory;

/**
* @author Krishna Bhat
*
*/

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import aws.boot.angular.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public List<Customer> findByFirstName(String firstName); 
}