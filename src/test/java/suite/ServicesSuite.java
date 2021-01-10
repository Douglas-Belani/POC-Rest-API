package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import services.impl.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({AddressServiceTest.class,
                     CommentServiceTest.class,
                     OrderServiceTest.class,
                     ProductServiceTest.class,
                     RateServiceTest.class,
                     UserServiceTest.class})
public class ServicesSuite {
}
