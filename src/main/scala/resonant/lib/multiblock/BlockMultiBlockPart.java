package resonant.lib.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import resonant.lib.network.PacketTile;
import universalelectricity.core.transform.vector.Vector3;

import java.util.Random;

/**
 * A fake-block based multiblock structure uses fake invisible blocks that are automatically placed
 * to create block bounds.
 *
 * @author Calclavia
 */
public class BlockMultiBlockPart extends BlockContainer
{
	public String textureName = null;
	@Deprecated
	public PacketTile packetType;

	public BlockMultiBlockPart()
	{
		super(Material.iron);
		this.setHardness(0.8F);
		this.setBlockName("multiBlock");
	}

	public BlockMultiBlockPart setPacketType(PacketTile packetType)
	{
		this.packetType = packetType;
		return this;
	}

	@Override
	public BlockMultiBlockPart setBlockTextureName(String name)
	{
		this.textureName = name;
		return this;
	}

	public void createMultiBlockStructure(IMultiBlock tile)
	{
		TileEntity tileEntity = (TileEntity) tile;
		Vector3[] positions = tile.getMultiBlockVectors();

		for (Vector3 position : positions)
		{
			makeFakeBlock(tileEntity.getWorldObj(), new Vector3(tileEntity).translate(position), new Vector3(tileEntity));
		}
	}

	public void destroyMultiBlockStructure(IMultiBlock tile)
	{
		TileEntity tileEntity = (TileEntity) tile;
		Vector3[] positions = tile.getMultiBlockVectors();

		for (Vector3 position : positions)
		{
			new Vector3(tileEntity).add(position).setBlock(tileEntity.getWorldObj(), Blocks.air);
		}

		new Vector3(tileEntity).setBlock(tileEntity.getWorldObj(), Blocks.air);
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		// Creates a fake block, then sets the relative main block position.
		worldObj.setBlock(position.xi(), position.yi(), position.zi(), this);
		((TileMultiBlockPart) worldObj.getTileEntity(position.xi(), position.yi(), position.zi())).setMainBlock(mainBlock);
	}

	@Override
	public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int par5)
	{
		try
		{
			Vector3 main = ((TileMultiBlockPart) blockAccess.getTileEntity(x, y, z)).getMainBlock();
			Block block = Block.blocksList[main.getBlockID(blockAccess)];

			if (block != null)
			{
				return block.getBlockTexture(blockAccess, main.intX(), main.intY(), main.intZ(), par5);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if (this.textureName != null)
		{
			this.blockIcon = iconRegister.registerIcon(this.textureName);
		}
		else
		{
			super.registerIcons(iconRegister);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileMultiBlockPart)
		{
			((TileMultiBlockPart) tileEntity).onBlockRemoval(this);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/**
	 * Called when the block is right clicked by the player. This modified version detects electric
	 * items and wrench actions on your machine block. Do not override this function. Use
	 * machineActivated instead! (It does the same thing)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileMultiBlockPart tileEntity = (TileMultiBlockPart) par1World.getBlockTileEntity(x, y, z);
		return tileEntity.onBlockActivated(par1World, x, y, z, par5EntityPlayer);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileMultiBlockPart();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World par1World, int x, int y, int z)
	{
		TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileMultiBlockPart) tileEntity).getMainBlock();

		if (mainBlockPosition != null)
		{
			int mainBlockID = par1World.getBlockId(mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());

			if (mainBlockID > 0)
			{
				return Block.blocksList[mainBlockID].getPickBlock(target, par1World, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());
			}
		}

		return null;
	}
}