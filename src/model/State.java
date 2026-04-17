package model;

public class State {
    private int stateID;
    private String abbrev;

    public State() {
    }

    public State(int stateID, String abbrev) {
        this.stateID = stateID;
        this.abbrev = abbrev;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    @Override
    public String toString() {
        return "State{stateID=" + stateID
            + ", abbrev='" + abbrev + '\''
            + '}';
    }
}
