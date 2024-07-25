package supportingMethods;

import java.util.HashMap;
import java.util.Map;

public class CustomerDummyDatabase {

    static Map<String,String> DummyDatabase;

    CustomerDummyDatabase()
    {
        DummyDatabase = new HashMap<>();
    }

    public static void addDummyCustomerDB(String Key, String Value)
    {
        DummyDatabase.put(Key, Value);
    }

    public static String getDummyCustomerDB(String Key)
    {
        return DummyDatabase.get(Key);
    }
}
