package entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CityTest {

    private City city;

    @Before
    public void initialize() {
        this.city = new City(1, "city", null);
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Null_Name_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                city.setName(null));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Blank_Name_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                city.setName(""));
        Assert.assertEquals("Name must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setName_Should_Throw_IllegalArgumentException_When_Name_Is_Larger_Than_120_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        Assert.assertTrue(sb.length() > 120);
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                city.setName(sb.toString()));
        Assert.assertEquals("Name must have less than 120 characters.", thrown.getMessage());
    }

    @Test
    public void test_setState_Should_Throw_IllegalArgumentException_When_Null_State_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                city.setState(null));
        Assert.assertEquals("State must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_setState_Should_Successfully_Change_State_For_Non_Null_State() {
        State state = new State(1, "SP");
        city.setState(state);
        assertThat(state, samePropertyValuesAs(city.getState()));
    }

}
