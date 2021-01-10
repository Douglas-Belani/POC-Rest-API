package dao;

import entities.State;

public interface IStateDao {

    public abstract State getStateByInitials(String initials);

    public abstract Integer insertState(State state);

}
