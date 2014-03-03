package calclavia.lib.content.module;

import net.minecraft.block.material.Material;

/**
 * All tiles inherit this class.
 * 
 * @author Calclavia
 */
public abstract class TileBase extends TileBlock
{
	public TileBase(String name, Material material)
	{
		super(name, material);
	}

	public TileBase(Material material)
	{
		super(material);
	}

	public TileBlock tile()
	{
		return this;
	}
}
