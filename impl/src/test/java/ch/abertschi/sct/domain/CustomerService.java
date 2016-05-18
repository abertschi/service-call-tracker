package ch.abertschi.sct.domain;

public interface CustomerService
{
    Customer getCustomer(String name, String comment);

    class CustomerServiceImpl implements CustomerService {

        @Override
        public Customer getCustomer(String key, String key2)
        {
            return new Customer(key, key2);
        }
    }

}


