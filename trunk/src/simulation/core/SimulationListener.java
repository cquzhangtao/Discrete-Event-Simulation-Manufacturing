package simulation.core;

public interface SimulationListener {
	public void onStart();
	public void onEnd(long time);
	public void onAdvance(long time);
}
