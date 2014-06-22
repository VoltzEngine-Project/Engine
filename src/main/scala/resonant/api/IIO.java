package resonant.api;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

/**
 * @author Calclavia
 */
public interface IIO
{
	public EnumSet<ForgeDirection> getInputDirections();

	public EnumSet<ForgeDirection> getOutputDirections();

	public void setIO(ForgeDirection dir, int type);

	public int getIO(ForgeDirection dir);
}
