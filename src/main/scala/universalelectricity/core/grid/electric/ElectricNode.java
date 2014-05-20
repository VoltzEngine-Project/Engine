package universalelectricity.core.grid.electric;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import universalelectricity.core.grid.Node;
import universalelectricity.core.grid.TickingGrid;
import universalelectricity.core.grid.api.INodeProvider;

import java.util.Iterator;
import java.util.Map;

/**
 * The node used for electrical objects.
 *
 * @author Calclavia
 */
public class ElectricNode extends Node<INodeProvider, TickingGrid, ElectricNode>
{
	public double amperage = 0.0D;
	public double voltage = 0.0D;

	public double amperage1 = 0.0D;
	public double totalAmperage = 0.0D;
	protected byte connectionMap = Byte.parseByte("111111", 2);
	double[] currents;
	double capacity = 0;

	final double timeMultiplier = 20;

	public ElectricNode(INodeProvider parent)
	{
		super(parent);
	}

	public double getCurrentCapacity()
	{
		return capacity;
	}

	public double getResistance()
	{
		return 0.01D;
	}

	public double getAmperageScale()
	{
		return 0.07000000000000001D;
	}

	public double getCondParallel()
	{
		return 0.5D;
	}

	/**
	 * Recache the connections. This is the default connection implementation.
	 */
	@Override
	public void doRecache()
	{
		connections.clear();

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = position().translate(dir).getTileEntity(world());

			if (tile instanceof INodeProvider)
			{
				ElectricNode check = ((INodeProvider) tile).getNode(ElectricNode.class, dir.getOpposite());

				if (check != null && canConnect(dir, check) && check.canConnect(dir.getOpposite(), this))
				{
					connections.put(check, dir);
				}
			}
		}

		double[] c2 = new double[connections.size()];

		this.currents = c2;
	}

	@Override
	public boolean canConnect(ForgeDirection from, Object source)
	{
		return (source instanceof ElectricNode) && (connectionMap & (1 << from.ordinal())) != 0;
	}

	public World world()
	{
		return parent instanceof TileEntity ? ((TileEntity) parent).getWorldObj() : null;
	}

	public Vector3 position()
	{
		return parent instanceof TileEntity ? new Vector3((TileEntity) parent) : null;
	}

	protected void computeVoltage()
	{
		this.totalAmperage = (0.5D * this.amperage1);
		this.amperage1 = 0.0D;

		this.voltage += 0.05D * this.amperage * getCurrentCapacity();
		this.amperage = 0.0D;
	}

	public double getVoltage()
	{
		return this.voltage;
	}

	public void applyCurrent(double amperage)
	{
		getVoltage();
		this.amperage += amperage;
		this.amperage1 += Math.abs(amperage);
	}

	public void applyPower(double wattage)
	{
		double calculatedVoltage = Math.sqrt(this.voltage * this.voltage + 0.1D * wattage * getCurrentCapacity()) - this.voltage;
		applyCurrent(timeMultiplier * calculatedVoltage / getCurrentCapacity());
	}

	public void drawPower(double wattage)
	{
		double p1 = this.voltage * this.voltage - 0.1D * wattage * getCurrentCapacity();
		double calculatedVoltage = p1 < 0.0D ? 0.0D : Math.sqrt(p1) - this.voltage;
		applyCurrent(timeMultiplier * calculatedVoltage / getCurrentCapacity());
	}

	public double getEnergy(double voltageThreshold)
	{
		double d = getVoltage();
		double tr = 0.5D * (d * d - voltageThreshold * voltageThreshold) / getCurrentCapacity();
		return tr < 0.0D ? 0.0D : tr;
	}

	@Override
	public void update(float deltaTime)
	{
		computeVoltage();

		synchronized (connections)
		{
			Iterator<Map.Entry<ElectricNode, ForgeDirection>> it = connections.entrySet().iterator();

			/**
			 * Loop through call connections
			 */
			while (it.hasNext())
			{
				/**
				 * Transfer current to adjacent node
				 */
				Map.Entry<ElectricNode, ForgeDirection> entry = it.next();

				ForgeDirection dir = entry.getValue();
				ElectricNode adjacent = entry.getKey();

				double totalResistance = getResistance() + adjacent.getResistance();
				double current = currents[dir.ordinal()];
				double voltageDifference = voltage - adjacent.getVoltage();
				this.currents[dir.ordinal()] += (voltageDifference - current * totalResistance) * getAmperageScale();
				current += voltageDifference * getCondParallel();

				applyCurrent(-current);
				adjacent.applyCurrent(current);
			}
		}
	}

	@Override
	public void load(NBTTagCompound nbt)
	{
		super.load(nbt);

		voltage = nbt.getDouble("voltage");
		amperage = nbt.getDouble("amperage");
		amperage1 = nbt.getDouble("amperage1");
		totalAmperage = nbt.getDouble("totalAmperage");
	}

	@Override
	public void save(NBTTagCompound nbt)
	{
		super.save(nbt);

		nbt.setDouble("voltage", voltage);
		nbt.setDouble("amperage", amperage);
		nbt.setDouble("amperage1", amperage1);
		nbt.setDouble("totalAmperage", totalAmperage);
	}

	@Override protected TickingGrid newGrid()
	{
		return new TickingGrid<ElectricNode>(this, ElectricNode.class);
	}
}