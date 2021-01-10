package entities;

import util.StateUtil;

import java.util.Objects;

public class State {

    private Integer stateId;
    private String initials;

    public State() {

    }

    public State(Integer stateId, String initials) {
        this.stateId = stateId;
        this.initials = initials;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        if (initials == null || initials.isBlank()) {
            throw new IllegalArgumentException("Initials must not be blank.");
        }

        if (!StateUtil.checkInitials(initials.toUpperCase())) {
            throw new IllegalArgumentException("Invalid state initials.");
        }
        this.initials = initials.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(stateId, state.stateId) &&
                Objects.equals(getInitials(), state.getInitials());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateId, getInitials());
    }
}
