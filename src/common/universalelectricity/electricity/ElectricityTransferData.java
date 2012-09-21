package universalelectricity.electricity;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.implement.IElectricityReceiver;

public class ElectricityTransferData
{
	public TileEntity sender;
	public IElectricityReceiver receiver;
    public double amps;
    public double voltage;
    public ForgeDirection side;

    public ElectricityTransferData(TileEntity sender, IElectricityReceiver receiver, ForgeDirection side, double amps, double voltage)
    {
    	this.sender = sender;
    	this.receiver = receiver;
        this.side = side;
        this.amps = amps;
        this.voltage = voltage;
    }
}
