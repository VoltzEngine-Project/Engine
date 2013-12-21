package universalelectricity.api.energy;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.net.INetwork;

/**
 * The Electrical Network in interface form.
 * 
 * @author Calclavia
 * 
 */
public interface IEnergyNetwork extends INetwork<IEnergyNetwork, IConductor, TileEntity>
{

}
