package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.IElectricUnit;
import universalelectricity.extend.TileEntityConductor;

public class ElectricityConnection
{
    private int ID;
    public List<TileEntityConductor> conductors = new ArrayList<TileEntityConductor>();

    public ElectricityConnection(int ID, TileEntityConductor conductor)
    {
        this.ID = ID;
        this.addConductor(conductor);
    }

    public void addConductor(TileEntityConductor newConductor)
    {
        this.cleanUpArray();

        if (!conductors.contains(newConductor))
        {
            conductors.add(newConductor);
            newConductor.connectionID = this.ID;
        }
    }

    /**
     * Get only the electric units that can receive electricity from the given side.
     */
    public List<IElectricUnit> getConnectedElectricUnits()
    {
        this.cleanUpArray();
        List<IElectricUnit> returnArray = new ArrayList<IElectricUnit>();

        for (TileEntityConductor conductor : conductors)
        {
            for (byte i = 0; i < conductor.connectedBlocks.length; i++)
            {
                TileEntity tileEntity = conductor.connectedBlocks[i];

                if (tileEntity != null)
                {
                    if (tileEntity instanceof IElectricUnit)
                    {
                        if (!returnArray.contains((IElectricUnit)tileEntity) && ((IElectricUnit)tileEntity).canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                        {
                            returnArray.add((IElectricUnit)tileEntity);
                        }
                    }
                }
            }
        }

        return returnArray;
    }

    public void cleanUpArray()
    {
        for (int i = 0; i < conductors.size(); i++)
        {
            if (conductors.get(i) == null)
            {
                conductors.remove(i);
            }
            else if (conductors.get(i).isInvalid())
            {
                conductors.remove(i);
            }
        }
    }

    public void setID(int ID)
    {
        this.ID = ID;
        this.cleanUpArray();

        for (TileEntityConductor conductor : this.conductors)
        {
            conductor.connectionID = this.ID;
        }
    }

    public int getID()
    {
        return this.ID;
    }
}
