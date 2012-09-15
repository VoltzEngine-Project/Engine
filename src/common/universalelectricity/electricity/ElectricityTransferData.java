package universalelectricity.electricity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IElectricUnit;

public class ElectricityTransferData
{
    public IElectricUnit electricUnit;
    public float amps;
    public float voltage;
    public ForgeDirection side;

    public ElectricityTransferData(IElectricUnit electricUnit, ForgeDirection side, float amps, float voltage)
    {
        this.electricUnit = electricUnit;
        this.side = side;
        this.amps = amps;
        this.voltage = voltage;
    }
}
