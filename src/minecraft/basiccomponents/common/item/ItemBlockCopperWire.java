package basiccomponents.common.item;

import universalelectricity.prefab.conductor.ItemBlockConductor;

public class ItemBlockCopperWire extends ItemBlockConductor
{
	public ItemBlockCopperWire(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
}
