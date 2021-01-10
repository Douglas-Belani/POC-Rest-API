package entities;

import java.util.Objects;

public class City {

    private Integer cityId;
    private String name;

    private State state;

    public City() {

    }

    public City(Integer cityId, String name, State state) {
        this.cityId = cityId;
        this.name = name;
        this.state = state;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }

        if (name.length() > 120) {
            throw new IllegalArgumentException("Name must have less than 120 characters.");
        }

        this.name = name.toUpperCase();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be empty.");
        }

        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(getCityId(), city.getCityId()) &&
                Objects.equals(getName(), city.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityId(), getName());
    }
}
