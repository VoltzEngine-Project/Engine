package universalelectricity.electricity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IMachine;

public class ElectricityTransferData
{
    public IMachine electricUnit;
    public double amps;
    public double voltage;
    public ForgeDirection side;

    public ElectricityTransferData(IMachine electricUnit, ForgeDirection side, double amps, double voltage)
    {
        this.electricUnit = electricUnit;
        this.side = side;
        this.amps = amps;
        this.voltage = voltage;
    }
}
