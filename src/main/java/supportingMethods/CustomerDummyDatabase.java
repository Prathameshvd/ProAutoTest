package supportingMethods;

import java.util.HashMap;
import java.util.Map;

public class CustomerDummyDatabase {

    //Creating static so that all object will able to access same data
    static Map<String,String> DummyDatabase = new HashMap<>();

    public CustomerDummyDatabase()
    {

    }

    public void addDummyCustomerDB(String Key, String Value)
    {
        DummyDatabase.put(Key, Value);
    }

    public String getDummyCustomerDB(String Key)
    {
        return DummyDatabase.get(Key);
    }
}
