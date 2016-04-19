package ch.abertschi.sct.domain;

public interface CustomerService
{
    Customer getCustomer(String key);

    class DummyImpl implements CustomerService {

        @Override
        public Customer getCustomer(String key)
        {
            return new Customer("Peter Parker", 1980);
        }
    }

}


