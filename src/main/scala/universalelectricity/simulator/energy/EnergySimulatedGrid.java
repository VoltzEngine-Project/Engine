package universalelectricity.simulator.energy;

import universalelectricity.api.EnergyStorage;
import universalelectricity.api.core.grid.electric.IEnergyNode;
import universalelectricity.simulator.grid.SimulatedGrid;
import universalelectricity.simulator.grid.component.NetworkNode;

/**
 * Created by robert on 8/30/2014.
 */
public class EnergySimulatedGrid extends SimulatedGrid
{
	protected EnergyStorage buffer;
	protected EnergySimulator simulator;

    public EnergySimulatedGrid(NetworkNode... nodes)
    {
        super(nodes);
        buffer = new EnergyStorage(1000);
        simulator = new EnergySimulator();
    }

    public double addEnergy(double wattage, boolean doAdd) {
        return buffer.receiveEnergy(wattage, doAdd);
    }

    public double removeEnergy(double wattage, boolean doRemove) {
        return buffer.extractEnergy(wattage, doRemove);
    }

    public double getEnergy() {
        return buffer.getEnergy();
    }

    public double getEnergyCapacity() {
        return buffer.getEnergyCapacity();
    }

    @Override
    public void add(NetworkNode node)
    {
        if(node instanceof IEnergyNode)
            super.add(node);
    }

    public void buildEntireNetwork()
    {

    }
}
