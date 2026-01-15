package com.fs.main.service.interfaces;

import com.fs.main.entity.Customer;

public interface ServiceInterface {

    /**
     * Purpose: Validate login credentials against stored user login info
     * @param username
     * @param password
     * @return
     */
     boolean validateLogin(String username, String password);

    /**
     * Purpose: Create new customer login and detail entries<br/>
     * @param customer
     * @return
     */
    void createCustomer(Customer customer);

    /**
     * Purpose: Update existing customer detail and login info<br/>
     * @param customerStructure
     * @return
     */
   // boolean updateCustomer(CustomerStructure customerStructure);

    /**
     * Purpose: Delete customer detail using ID<br/>
     * @param username
     * @return
     */
    //boolean deleteCustomer(String username);

    /**
     * Purpose: Retrieve all customers detail records<br/>
     * @return
     */
    //List<CustomerStructure> fetchAllCustomers();
}
