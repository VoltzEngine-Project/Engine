package calclavia.lib.multiblock.reference;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;

/**
 * A reference-based multiblock structure uses a central block as the "primary block" and have all
 * the blocks around it be "dummy blocks". This handler should be extended. Every single block will
 * have a reference of this object.
 * 
 * @author Calclavia
 * 
 */
public class MultiBlockHandler<W extends IMultiBlockStructure>
{
	/** The main block used for reference */
	protected W primary = null;
	/** The relative primary block position to be loaded in once the tile is initiated. */
	protected Vector3 newPrimary = null;
	protected final W self;
	protected Class<? extends W> wrapperClass;

	public MultiBlockHandler(W wrapper)
	{
		this.self = wrapper;
		wrapperClass = (Class<? extends W>) wrapper.getClass();
	}

	public void update()
	{
		if (self.getWorld() != null && newPrimary != null)
		{
			W checkWrapper = getWrapperAt(newPrimary.clone().translate(self.getPosition()));

			if (checkWrapper != null)
			{
				primary = checkWrapper;
			}

			newPrimary = null;
			self.onMultiBlockChanged();
		}
	}

	/**
	 * Try to construct the structure, otherwise, deconstruct it.
	 * 
	 * @return True if operation is successful.
	 */
	public boolean toggleConstruct()
	{
		if (!construct())
		{
			return deconstruct();
		}

		return true;
	}

	/**
	 * Gets the structure blocks of the multiblock.
	 * 
	 * @return Null if structure cannot be created.
	 */
	public Set<W> getStructure()
	{
		Set<W> structure = new LinkedHashSet<W>();
		Vector3[] vectors = self.getMultiBlockVectors();

		for (Vector3 vector : vectors)
		{
			W checkWrapper = getWrapperAt(vector.translate(self.getPosition()));

			if (checkWrapper != null)
			{
				structure.add(checkWrapper);
			}
			else
			{
				structure.clear();
				return null;
			}
		}

		return structure;
	}

	/**
	 * Called to construct the multiblock structure. Example: Wrenching the center block or checking
	 * if a placement was done correct. Note that this block will become the PRIMARY block.
	 * 
	 * @return True if the construction was successful.
	 */
	public boolean construct()
	{
		if (!isConstructed())
		{
			Set<W> structures = getStructure();

			if (structures != null)
			{
				primary = self;
				for (W structure : structures)
				{
					structure.getMultiBlock().primary = primary;
				}

				for (W structure : structures)
				{
					structure.onMultiBlockChanged();
				}
			}

			return true;
		}

		return false;
	}

	public boolean deconstruct()
	{
		if (isConstructed())
		{
			if (isPrimary())
			{
				Set<W> structures = getStructure();

				if (structures != null)
				{
					for (W structure : structures)
					{
						structure.getMultiBlock().primary = null;
					}

					for (W structure : structures)
					{
						structure.onMultiBlockChanged();
					}
				}
			}
			else
			{
				primary.getMultiBlock().deconstruct();
			}

			return true;
		}

		return false;
	}

	public W getWrapperAt(Vector3 position)
	{
		TileEntity tile = position.getTileEntity(self.getWorld());

		if (tile != null && wrapperClass.isAssignableFrom(tile.getClass()))
		{
			return (W) tile;
		}

		return null;
	}

	public boolean isConstructed()
	{
		return primary != null;
	}

	public boolean isPrimary()
	{
		return !isConstructed() || primary == self;
	}

	public W getPrimary()
	{
		return primary;
	}

	public W get()
	{
		return primary != null ? primary : self;
	}

	/**
	 * Only the primary wrapper of the multiblock saves and loads data.
	 */
	public void load(NBTTagCompound nbt)
	{
		if (nbt.hasKey("primaryMultiBlock"))
		{
			newPrimary = new Vector3(nbt.getCompoundTag("primaryMultiBlock"));
			update();
		}
		else
		{
			primary = null;
		}
	}

	/**
	 * @param saveWrapper - The wrapper that is saving the NBT data.
	 * @param nbt
	 */
	public void save(W saveWrapper, NBTTagCompound nbt)
	{
		if (isConstructed())
		{
			nbt.setTag("primaryMultiBlock", primary.getPosition().subtract(self.getPosition()).writeToNBT(new NBTTagCompound()));
		}
	}

}
