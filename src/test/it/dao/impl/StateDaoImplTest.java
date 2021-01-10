package dao.impl;

import connection.H2Connection;
import dao.IStateDao;
import entities.State;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class StateDaoImplTest {

    private static IStateDao stateDao;

    @BeforeClass
    public static void initialize() {
        stateDao = new StateDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getStateByInitials_Should_Return_State_Id_1_For_Initials_SP() {
        String stateInitials = "SP";
        State expectedState = new State(1, stateInitials);

        State state = stateDao.getStateByInitials(stateInitials);

        assertThat(state, samePropertyValuesAs(expectedState));
    }

    @Test
    public void test_getStateByInitials_Should_Return_Null_For_Nonexistent_State_Initials() {
        String nonExistingStateInitials = "AB";

        State state = stateDao.getStateByInitials(nonExistingStateInitials);

        Assert.assertNull(state);
    }

    @Test
    public void test_insertState_Should_Return_State_Id_On_Successful_Insert() {
        State newState = new State(null, "ES");

        Integer stateId = stateDao.insertState(newState);
        newState.setStateId(stateId);

        State state = stateDao.getStateByInitials("ES");
        assertThat(newState, samePropertyValuesAs(state));
    }

}
