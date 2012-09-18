package universalelectricity.electricity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IElectricUnit;

public class ElectricityTransferData
{
    public IElectricUnit electricUnit;
    public double amps;
    public double voltage;
    public ForgeDirection side;

    public ElectricityTransferData(IElectricUnit electricUnit, ForgeDirection side, double amps, double voltage)
    {
        this.electricUnit = electricUnit;
        this.side = side;
        this.amps = amps;
        this.voltage = voltage;
    }
}
