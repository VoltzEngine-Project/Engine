package universalelectricity.electricity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.implement.IElectricityReceiver;
import universalelectricity.prefab.TileEntityConductor;

public class ElectricityNetwork
{
    private int ID;
    public List<TileEntityConductor> conductors = new ArrayList<TileEntityConductor>();

    public ElectricityNetwork(int ID, TileEntityConductor conductor)
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
    public List<IElectricityReceiver> getConnectedElectricUnits()
    {
        this.cleanUpArray();
        List<IElectricityReceiver> returnArray = new ArrayList<IElectricityReceiver>();

        for (TileEntityConductor conductor : conductors)
        {
            for (byte i = 0; i < conductor.connectedBlocks.length; i++)
            {
                TileEntity tileEntity = conductor.connectedBlocks[i];

                if (tileEntity != null)
                {
                    if (tileEntity instanceof IElectricityReceiver)
                    {
                        if (!returnArray.contains((IElectricityReceiver)tileEntity) && ((IElectricityReceiver)tileEntity).canReceiveFromSide(ForgeDirection.getOrientation(i).getOpposite()))
                        {
                            returnArray.add((IElectricityReceiver)tileEntity);
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
