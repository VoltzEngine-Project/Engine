package universalelectricity.electricity;


/**
 * The Class ElectricityTransferData.
 */
public class ElectricityTransferData
{
    
    /** The eletric unit. */
    public IElectricUnit eletricUnit;
    
    /** The watts. */
    public float watts;
    
    /** The voltage. */
    public float voltage;
    
    /** The side. */
    public byte side;

    /**
     * Instantiates a new electricity transfer data.
     *
     * @param ielectricunit the ielectricunit
     * @param byte0 the byte0
     * @param f the f
     * @param f1 the f1
     */
    public ElectricityTransferData(IElectricUnit ielectricunit, byte byte0, float f, float f1)
    {
        eletricUnit = ielectricunit;
        side = byte0;
        watts = f;
        voltage = f1;
    }
}
