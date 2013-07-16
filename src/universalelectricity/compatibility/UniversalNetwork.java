package universalelectricity.compatibility;

import ic2.api.energy.tile.IEnergySink;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.INetworkConnection;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricalEvent.ElectricityProductionEvent;
import universalelectricity.core.electricity.ElectricalEvent.ElectricityRequestEvent;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.ElectricityNetwork;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.path.Pathfinder;
import universalelectricity.core.path.PathfinderChecker;
import universalelectricity.core.vector.Vector3;
import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.common.FMLLog;

/**
 * A universal network that words with multiple energy systems.
 * 
 * @author micdoodle8, Calclavia, Aidancbrady
 * 
 */
public class UniversalNetwork extends ElectricityNetwork
{
    @Override
    public float produce(ElectricityPack electricity, TileEntity... ignoreTiles)
    {
        ElectricityProductionEvent evt = new ElectricityProductionEvent(electricity, ignoreTiles);
        MinecraftForge.EVENT_BUS.post(evt);

        float energy = electricity.getWatts();
        float voltage = electricity.voltage;

        if (!evt.isCanceled())
        {
            Set<TileEntity> avaliableEnergyTiles = this.getAcceptors();

            if (!avaliableEnergyTiles.isEmpty())
            {
                final float totalEnergyRequest = this.getRequest(ignoreTiles).getWatts();

                if (totalEnergyRequest > 0)
                {
                    for (TileEntity tileEntity : avaliableEnergyTiles)
                    {
                        if (tileEntity instanceof IElectrical && !Arrays.asList(ignoreTiles).contains(tileEntity))
                        {
                            IElectrical electricalTile = (IElectrical) tileEntity;
                            // TODO: Fix Direction
                            float energyToSend = energy * (electricalTile.getRequest(ForgeDirection.UNKNOWN) / totalEnergyRequest);

                            if (energyToSend > 0)
                            {
                                ElectricityPack electricityToSend = ElectricityPack.getFromWatts(energyToSend, voltage);

                                // Calculate energy loss caused by resistance.
                                float ampsReceived = electricityToSend.amperes - (electricityToSend.amperes * electricityToSend.amperes * this.getTotalResistance()) / electricityToSend.voltage;
                                float voltsReceived = electricityToSend.voltage - (electricityToSend.amperes * this.getTotalResistance());

                                electricityToSend = new ElectricityPack(ampsReceived, voltsReceived);

                                // TODO: Fix unknown direction!
                                energy -= ((IElectrical) tileEntity).receiveElectricity(ForgeDirection.UNKNOWN, electricityToSend, true);
                            }
                        }
                    }
                }
            }
        }

        return energy;
    }
    
    @Override
    public ElectricityPack getRequest(TileEntity... ignoreTiles)
    {
        List<ElectricityPack> requests = new ArrayList<ElectricityPack>();

        Iterator<TileEntity> it = this.getAcceptors().iterator();

        while (it.hasNext())
        {
            TileEntity tileEntity = it.next();

            if (Arrays.asList(ignoreTiles).contains(tileEntity))
            {
                continue;
            }

            if (tileEntity instanceof IElectrical)
            {
                if (!tileEntity.isInvalid())
                {
                    if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
                    {
                        requests.add(ElectricityPack.getFromWatts(((IElectrical) tileEntity).getRequest(/* TODO: Fix unkown direction */ForgeDirection.UNKNOWN), ((IElectrical) tileEntity).getVoltage()));
                        continue;
                    }
                }
            }

            if (Compatibility.isIndustrialCraft2Loaded() && tileEntity instanceof IEnergySink)
            {
                if (!tileEntity.isInvalid())
                {
                    if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
                    {
                        requests.add(ElectricityPack.getFromWatts(Math.min(((IEnergySink)tileEntity).demandsEnergy(), ((IEnergySink)tileEntity).getMaxSafeInput()) * Compatibility.IC2_RATIO, 120));
                        continue;
                    }
                }
            }

            if (Compatibility.isBuildcraftLoaded() && tileEntity instanceof IPowerReceptor)
            {
                if (!tileEntity.isInvalid())
                {
                    if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
                    {
                        requests.add(ElectricityPack.getFromWatts(((IPowerReceptor) tileEntity).powerRequest(/* TODO: Fix unkown direction */ForgeDirection.UNKNOWN) * Compatibility.BC3_RATIO, 120));
                        continue;
                    }
                }
            }

            it.remove();

        }

        ElectricityPack mergedPack = ElectricityPack.merge(requests);
        ElectricityRequestEvent evt = new ElectricityRequestEvent(mergedPack, ignoreTiles);
        MinecraftForge.EVENT_BUS.post(evt);
        return mergedPack;
    }
    
    @Override
    public void refresh()
    {
        this.electricalTiles.clear();

        try
        {
            Iterator<IConductor> it = this.getConductors().iterator();

            while (it.hasNext())
            {
                IConductor conductor = it.next();

                if (conductor == null)
                {
                    it.remove();
                }
                else if (((TileEntity) conductor).isInvalid())
                {
                    it.remove();
                }
                else
                {
                    conductor.setNetwork(this);
                }

                for (TileEntity acceptor : conductor.getAdjacentConnections())
                {
                    if (!(acceptor instanceof IConductor))
                    {
                        if (acceptor instanceof IElectrical)
                        {
                            this.electricalTiles.add(acceptor);
                            continue;
                        }

                        if (Compatibility.isIndustrialCraft2Loaded() && acceptor instanceof IEnergySink)
                        {
                            this.electricalTiles.add(acceptor);
                            continue;
                        }

                        if (Compatibility.isBuildcraftLoaded() && acceptor instanceof IPowerReceptor)
                        {
                            this.electricalTiles.add(acceptor);
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            FMLLog.severe("Universal Electricity: Failed to refresh conductor.");
            e.printStackTrace();
        }
    }

    @Override
    public void split(IConductor splitPoint)
    {
        if (splitPoint instanceof TileEntity)
        {
            this.getConductors().remove(splitPoint);

            /**
             * Loop through the connected blocks and attempt to see if there are connections between
             * the two points elsewhere.
             */
            TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

            for (int i = 0; i < connectedBlocks.length; i++)
            {
                TileEntity connectedBlockA = connectedBlocks[i];

                if (connectedBlockA instanceof INetworkConnection)
                {
                    for (int ii = 0; ii < connectedBlocks.length; ii++)
                    {
                        final TileEntity connectedBlockB = connectedBlocks[ii];

                        if (connectedBlockA != connectedBlockB && connectedBlockB instanceof INetworkConnection)
                        {
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).worldObj, (INetworkConnection) connectedBlockB, splitPoint);
                            finder.init(new Vector3(connectedBlockA));

                            if (finder.results.size() > 0)
                            {
                                /**
                                 * The connections A and B are still intact elsewhere. Set all
                                 * references of wire connection into one network.
                                 */

                                for (Vector3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

                                    if (nodeTile instanceof INetworkProvider)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            ((INetworkProvider) nodeTile).setNetwork(this);
                                        }
                                    }
                                }
                            }
                            else
                            {
                                /**
                                 * The connections A and B are not connected anymore. Give both of
                                 * them a new network.
                                 */
                                IElectricityNetwork newNetwork = new UniversalNetwork();

                                for (Vector3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

                                    if (nodeTile instanceof INetworkProvider)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            newNetwork.getConductors().add((IConductor) nodeTile);
                                        }
                                    }
                                }

                                newNetwork.refresh();
                            }
                        }
                    }
                }
            }
        }
    }
}
