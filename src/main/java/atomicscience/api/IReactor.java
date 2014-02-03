package atomicscience.api;

public interface IReactor extends ITemperature
{
	public void heat(long enery);

	public boolean isOverToxic();
}
