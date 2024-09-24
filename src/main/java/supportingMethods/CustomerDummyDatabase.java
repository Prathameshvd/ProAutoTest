package supportingMethods;

import glueCodeImplementation.GlueCodeImplementation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CustomerDummyDatabase {

    //Creating static so that all object will be able to access same data
    static Map<String,String> DummyDatabase = new HashMap<>();
    public Logger logger = LogManager.getLogger(this.getClass());

    String Value = null;

    public CustomerDummyDatabase()
    {
        DummyDatabase = new HashMap<>();
    }

    public void addDummyCustomerDB(String Key, String Value)
    {
        DummyDatabase.put(Key, Value);
        logger.info("Value is stored into Dummy Database");
    }

    public String getDummyCustomerDB(String Key)
    {
        Value = DummyDatabase.get(Key);
        if (Value == null)
            logger.info("Value is null in the Dummy Database");
        else
            logger.info("Value is fetched from the Dummy Database");
        return DummyDatabase.get(Key);
    }
}
