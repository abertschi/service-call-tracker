package org.sct;

import org.sct.domain.Customer;

public interface CustomerService
{
    Customer getCustomer(String key);

    class CustomerServiceImpl implements CustomerService {

        @Override
        public Customer getCustomer(String key)
        {
            return new Customer("Peter Parker", 1980);
        }
    }

}


