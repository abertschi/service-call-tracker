package ch.abertschi.sct.domain;

public interface CustomerService
{
    Customer getCustomer(String key, String key2);

    class DummyImpl implements CustomerService {

        @Override
        public Customer getCustomer(String key, String key2)
        {
            return new Customer("Peter Parker", 1980);
        }
    }

}


