package com.builtbroken.lib.prefab.tile.multiblock.synthetic;

import com.builtbroken.mod.BBL;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.builtbroken.lib.prefab.tile.multiblock.reference.IMultiBlock;
import com.builtbroken.lib.transform.vector.Vector3;

/**
 * @author Calclavia
 */
public class SyntheticMultiblock
{
	public static SyntheticMultiblock instance;

	public Block blockMulti;

	public SyntheticMultiblock()
	{
		blockMulti = BBL.contentRegistry.newBlock(TileSyntheticPart.class).setCreativeTab(null);
	}

	public void create(IMultiBlock tile)
	{
		TileEntity tileEntity = (TileEntity) tile;
		Iterable<Vector3> positions = tile.getMultiBlockVectors();

		for (Vector3 position : positions)
		{
			makeFakeBlock(tileEntity.getWorldObj(), new Vector3(tileEntity).add(position), new Vector3(tileEntity));
		}
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		// Creates a fake block, then sets the relative main block position.
		worldObj.setBlock(position.xi(), position.yi(), position.zi(), blockMulti);
		((TileSyntheticPart) worldObj.getTileEntity(position.xi(), position.yi(), position.zi())).setMainBlock(mainBlock);
	}

	public void destroy(IMultiBlock tile)
	{
		TileEntity tileEntity = (TileEntity) tile;
		Iterable<Vector3> positions = tile.getMultiBlockVectors();

		for (Vector3 position : positions)
		{
			new Vector3(tileEntity).add(position).setBlock(tileEntity.getWorldObj(), Blocks.air);
		}

		new Vector3(tileEntity).setBlock(tileEntity.getWorldObj(), Blocks.air);
	}
}
