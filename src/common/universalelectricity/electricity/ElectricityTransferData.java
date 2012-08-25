package universalelectricity.electricity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IElectricUnit;

public class ElectricityTransferData
{
    public IElectricUnit electricUnit;
    public float watts;
    public float voltage;
    public ForgeDirection side;

    public ElectricityTransferData(IElectricUnit electricUnit, ForgeDirection side, float watts, float voltage)
    {
        this.electricUnit = electricUnit;
        this.side = side;
        this.watts = watts;
        this.voltage = voltage;
    }
}
