package universalelectricity.electricity;

import universalelectricity.extend.IElectricUnit;

public class ElectricityTransferData
{
    public IElectricUnit eletricUnit;
    public float watts;
    public float voltage;
    public byte side;

    public ElectricityTransferData(IElectricUnit eletricUnit, byte side, float watts, float voltage)
    {
        this.eletricUnit = eletricUnit;
        this.side = side;
        this.watts = watts;
        this.voltage = voltage;
    }
}
