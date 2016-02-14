public enum State {
    CORRECT(null),
    INCORRECT(CORRECT),
    IMPLICITLY_INCORRECT(INCORRECT),
    UNKNOWN(INCORRECT),
    ;

    private final State nextState;

    State(State nextState) {
        this.nextState = nextState;
    }

    public State next() {
        return nextState == null ? UNKNOWN : nextState;
    }
}
