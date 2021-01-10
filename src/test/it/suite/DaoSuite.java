package suite;

import dao.impl.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@org.junit.runners.Suite.SuiteClasses( {AddressDaoImplTest.class,
                                        AUXProductCategoryDaoImplTest.class,
                                        AuxRateUserDaoTest.class,
                                        CategoryDaoImplTest.class,
                                        CityDaoImplTest.class,
                                        CommentDaoImplTest.class,
                                        OrderDaoImplTest.class,
                                        OrderItemDaoImplTest.class,
                                        ProductDaoImplTest.class,
                                        RateDaoImplTest.class,
                                        StateDaoImplTest.class,
                                        UserDaoImplTest.class})
public class DaoSuite {
}
