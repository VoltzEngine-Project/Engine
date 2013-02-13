package basiccomponents.common.item;

import basiccomponents.common.BasicComponents;
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
