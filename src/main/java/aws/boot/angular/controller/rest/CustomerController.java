package aws.boot.angular.controller.rest;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aws.boot.angular.entity.Address;
import aws.boot.angular.entity.Customer;
import aws.boot.angular.entity.CustomerImage;
import aws.boot.angular.exception.CustomerNotFoundException;
import aws.boot.angular.exception.InvalidCustomerRequestException;
import aws.boot.angular.repoistory.CustomerRepository;
import aws.boot.angular.service.FileArchiveService;

/**
* @author Krishna Bhat
*
*/

@RestController
public class CustomerController {

 @Autowired
 private CustomerRepository customerRepository;
 
 @Autowired
 private FileArchiveService fileArchiveService; 
  
 
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public @ResponseBody Customer createCustomer(            
            @RequestParam(value="firstName", required=true) String firstName,
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(value="street", required=true) String street,
            @RequestParam(value="town", required=true) String town,
            @RequestParam(value="county", required=true) String county,
            @RequestParam(value="postcode", required=true) String postcode,
            @RequestParam(value="image", required=true) MultipartFile image) throws Exception {

         CustomerImage customerImage = fileArchiveService.saveFileToS3(image);         
         Customer customer = new Customer(firstName, lastName, dateOfBirth, customerImage, 
                                          new Address(street, town, county, postcode));
 
         customerRepository.save(customer);
         return customer;            
    }
    
    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("customerId") Long customerId) {
      
        /* validate customer Id parameter */
        if (null==customerId) {
           throw new InvalidCustomerRequestException();
        }
      
        Customer customer = customerRepository.findOne(customerId);
      
        if(null==customer){
           throw new CustomerNotFoundException();
        }
      
        return customer;
    }
    
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> getCustomers() {
      
        return (List<Customer>) customerRepository.findAll();
    }
    
    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.DELETE)
    public void removeCustomer(@PathVariable("customerId") Long customerId, HttpServletResponse httpResponse) {

        if(customerRepository.exists(customerId)){
            Customer customer = customerRepository.findOne(customerId);
            fileArchiveService.deleteImageFromS3(customer.getCustomerImage());
            customerRepository.delete(customer); 
        }
      
        httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
