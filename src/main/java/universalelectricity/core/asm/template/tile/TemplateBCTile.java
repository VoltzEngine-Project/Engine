package universalelectricity.core.asm.template.tile;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.IEnergyInterface;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;

/**
 * An ASM template used to transform other @UniversalClass classes to have specific compatibility.
 * 
 * @author Calclavia
 * 
 */
public abstract class TemplateBCTile implements IPowerReceptor, IEnergyInterface
{

	/**
	 * Get the PowerReceiver for this side of the block. You can return the same
	 * PowerReceiver for all sides or one for each side.
	 * 
	 * You should NOT return null to this method unless you mean to NEVER
	 * receive power from that side. Returning null, after previous returning a
	 * PowerReceiver, will most likely cause pipe connections to derp out and
	 * engines to eventually explode.
	 * 
	 * @param side
	 * @return
	 */
	public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return StaticTileForwarder.getPowerReceiver(this, side);
	}

	/**
	 * Call back from the PowerHandler that is called when the stored power
	 * exceeds the activation power.
	 * 
	 * It can be triggered by update() calls or power modification calls.
	 * 
	 * @param workProvider
	 */
	public void doWork(PowerHandler workProvider)
	{
		StaticTileForwarder.doWork(this, workProvider);
	}

	public World getWorld()
	{
		return StaticTileForwarder.getWorld(this);
	}

}
