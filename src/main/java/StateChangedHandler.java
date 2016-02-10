public interface StateChangedHandler {
    void stateChanged(int x, int y, int z, State oldState, State newState);
}
