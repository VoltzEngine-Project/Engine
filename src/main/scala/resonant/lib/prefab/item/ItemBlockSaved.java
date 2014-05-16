package resonant.lib.prefab.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.utility.inventory.InventoryUtility;
import resonant.lib.utility.nbt.NBTUtility;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

/** An item that can store a block's tile data.
 * 
 * @author Calclavia */
public class ItemBlockSaved extends ItemBlockTooltip
{
    public ItemBlockSaved(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        boolean flag = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null)
        {
            /** Inject essential tile data */
            NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);
            NBTTagCompound setNbt = NBTUtility.getNBTTagCompound(stack);

            if (essentialNBT.hasKey("id"))
            {
                setNbt.setString("id", essentialNBT.getString("id"));
                setNbt.setInteger("x", essentialNBT.getInteger("x"));
                setNbt.setInteger("y", essentialNBT.getInteger("y"));
                setNbt.setInteger("z", essentialNBT.getInteger("z"));
            }

            tile.readFromNBT(setNbt);
        }

        return flag;
    }

    public static ItemStack getItemStackWithNBT(VectorWorld vector)
    {
        return getItemStackWithNBT(vector.world, vector);
    }

    public static ItemStack getItemStackWithNBT(World world, Vector3 vector)
    {
        return getItemStackWithNBT(world, vector.intX(), vector.intY(), vector.intZ());
    }

    public static ItemStack getItemStackWithNBT(World world, int x, int y, int z)
    {
        return getItemStackWithNBT(Block.blocksList[world.getBlockId(x, y, z)], world, x, y, z);
    }

    public static ItemStack getItemStackWithNBT(Block b, World world, int x, int y, int z)
    {
        Block block = (b == null ? Block.blocksList[world.getBlockId(x, y, z)] : b);
        if (block != null)
        {
            int meta = world.getBlockMetadata(x, y, z);

            ItemStack dropStack = new ItemStack(block, block.quantityDropped(meta, 0, world.rand), block.damageDropped(meta));
            NBTTagCompound tag = new NBTTagCompound();

            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile != null)
                tile.writeToNBT(tag);

            tag.removeTag("id");
            tag.removeTag("x");
            tag.removeTag("y");
            tag.removeTag("z");
            dropStack.setTagCompound(tag);
            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(Block block, World world, int x, int y, int z)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            ItemStack itemStack = getItemStackWithNBT(block, world, x, y, z);

            if (itemStack != null)
                InventoryUtility.dropItemStack(world, new Vector3(x, y, z), itemStack);
        }
    }
}
