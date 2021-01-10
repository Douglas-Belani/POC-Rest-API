package suite;

import entities.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AddressTest.class,
                    CategoryTest.class,
                    CityTest.class,
                    CommentsTest.class,
                    OrderItemTest.class,
                    OrderTest.class,
                    ProductTest.class,
                    RateTest.class,
                    StateTest.class,
                    UserTest.class})
public class EntitiesSuite {
}
