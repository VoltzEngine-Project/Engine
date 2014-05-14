package resonant.api;

import java.util.EnumSet;

import net.minecraftforge.common.ForgeDirection;

/** @author Calclavia */
public interface IIO
{
    public EnumSet<ForgeDirection> getInputDirections();

    public EnumSet<ForgeDirection> getOutputDirections();

    public void setIO(ForgeDirection dir, int type);

    public int getIO(ForgeDirection dir);
}
