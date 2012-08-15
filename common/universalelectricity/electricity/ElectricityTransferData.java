package universalelectricity.electricity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IElectricUnit;

public class ElectricityTransferData
{
    public IElectricUnit eletricUnit;
    public float watts;
    public float voltage;
    public ForgeDirection side;

    public ElectricityTransferData(IElectricUnit eletricUnit, ForgeDirection side, float watts, float voltage)
    {
        this.eletricUnit = eletricUnit;
        this.side = side;
        this.watts = watts;
        this.voltage = voltage;
    }
}
