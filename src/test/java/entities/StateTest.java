package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StateTest {

    private State state;

    @Before
    public void initialize() {
        this.state = new State(1, "SP");
    }

    @Test
    public void test_setInitials_Should_Throw_IllegalArgumentException_For_Null_Initials() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                state.setInitials(null));
        Assert.assertEquals("Initials must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setInitials_Should_Throw_IllegalArgumentException_For_Blank_Initials() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                state.setInitials(""));
        Assert.assertEquals("Initials must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setInitials_Should_Throw_IllegalArgumentException_For_Invalid_State_Initials() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                state.setInitials("CA"));
        Assert.assertEquals("Invalid state initials.", thrown.getMessage());
    }

    @Test
    public void test_setInitials_Should_Successfully_Change_Initials_For_Non_Null_And_Non_Blank_Valid_State_Initials() {
        String newInitials = "RJ";
        state.setInitials(newInitials);
        Assert.assertEquals(newInitials, state.getInitials());
    }

}
