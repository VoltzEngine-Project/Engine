package resonant.lib.content.module;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IRotatable;
import resonant.lib.prefab.item.ItemBlockTooltip;
import resonant.lib.prefab.vector.Cuboid;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.utility.WrenchUtility;
import universalelectricity.api.vector.Vector2;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** All blocks inherit this class.
 * <p/>
 * Note that a lot of the variables will not exist except on the primary instance of the TileBlock,
 * hosted in BlockDummy.
 * 
 * @author Calclavia */
public abstract class TileBlock extends TileEntity
{
    /** The unique string ID of this block. */
    public final String name;
    public final Material material;
    public Class<? extends ItemBlock> itemBlock = ItemBlockTooltip.class;
    public CreativeTabs creativeTab = null;
    public Cuboid bounds = Cuboid.full();
    public BlockDummy block;
    public float blockHardness = 1;
    public float blockResistance = 1;
    public boolean canProvidePower = false;
    public boolean tickRandomly = false;
    public boolean normalRender = true;
    public boolean forceStandardRender = false;
    public boolean customItemRender = false;
    public boolean isOpaqueCube = true;

    /** Temporary */
    public IBlockAccess access;
    /** Rendering */
    protected String textureName;
    protected String domain;
    /** Rotation */
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

    public TileBlock(String newName, Material newMaterial)
    {
        name = newName;
        material = newMaterial;
        textureName = name;
    }

    public TileBlock(Material newMaterial)
    {
        name = LanguageUtility.decapitalizeFirst(getClass().getSimpleName().replaceFirst("Tile", ""));
        material = newMaterial;
        textureName = name;
    }

    public static Vector2 getClickedFace(byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Vector2(1 - hitX, hitZ);
            case 1:
                return new Vector2(hitX, hitZ);
            case 2:
                return new Vector2(1 - hitX, 1 - hitY);
            case 3:
                return new Vector2(hitX, 1 - hitY);
            case 4:
                return new Vector2(hitZ, 1 - hitY);
            case 5:
                return new Vector2(1 - hitZ, 1 - hitY);
            default:
                return new Vector2(0.5, 0.5);
        }
    }

    /** Called after the block is registred. Use this to add recipes. */
    public void onInstantiate()
    {

    }

    public World world()
    {
        return worldObj;
    }

    public IBlockAccess access()
    {
        if (world() != null)
        {
            return world();
        }

        return access;
    }

    public int x()
    {
        assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
        return xCoord;
    }

    public int y()
    {
        assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
        return yCoord;
    }

    public int z()
    {
        assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
        return zCoord;
    }

    public VectorWorld position()
    {
        assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
        return new VectorWorld(this);
    }

    protected VectorWorld center()
    {
        assert world() != null : "TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.";
        return (VectorWorld) position().translate(0.5);
    }

    @Override
    public Block getBlockType()
    {
        if (access() != null)
        {
            Block b = Block.blocksList[access().getBlockId(this.xCoord, this.yCoord, this.zCoord)];
            if (b == null)
            {
                return block;
            }
            return b;
        }

        return block;
    }

    /** @return Return "this" if the block requires a TileEntity. */
    public TileBlock tile()
    {
        return null;
    }

    /** @return The containing block. */
    public Block block()
    {
        return Block.blocksList[blockID()];
    }

    public int blockID()
    {
        if (access() != null)
        {
            return access().getBlockId(x(), y(), z());
        }

        return block.blockID;
    }

    public int metadata()
    {
        return access().getBlockMetadata(x(), y(), z());
    }

    /** Drops */
    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        if (getBlockType() != null)
        {
            drops.add(new ItemStack(getBlockType(), quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)));
        }
        return drops;
    }

    public int quantityDropped(int meta, int fortune)
    {
        return 1;
    }

    public int metadataDropped(int meta, int fortune)
    {
        return 0;
    }

    public boolean isControlDown(EntityPlayer player)
    {
        try
        {
            Class ckm = Class.forName("codechicken.multipart.ControlKeyModifer");
            Method m = ckm.getMethod("isControlDown", EntityPlayer.class);
            return (Boolean) m.invoke(null, player);
        }
        catch (Exception e)
        {

        }
        return false;
    }

    public void getSubBlocks(int id, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(id, 1, 0));
    }

    public ItemStack getPickBlock(MovingObjectPosition target)
    {
        return new ItemStack(getBlockType(), 1, metadataDropped(metadata(), 0));
    }

    public int getLightValue(IBlockAccess access)
    {
        return Block.lightValue[access.getBlockId(x(), y(), z())];
    }

    /** Block events */
    public void click(EntityPlayer player)
    {

    }

    public boolean activate(EntityPlayer player, int side, Vector3 hit)
    {
        /** Check if the player is holding a wrench. If so, call the wrench event. */
        if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), x(), y(), z()))
        {
            if (configure(player, side, hit))
            {
                WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), x(), y(), z());
                return true;
            }

            return false;
        }

        return use(player, side, hit);
    }

    /** Called when the block is clicked by a player */
    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        return false;
    }

    /** Called when the block is clicked with a wrench */
    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        return tryRotate(side, hit);
    }

    /** Rotatable Block */
    protected boolean tryRotate(int side, Vector3 hit)
    {
        if (this instanceof IRotatable)
        {
            byte result = getSideToRotate((byte) side, hit.x, hit.y, hit.z);

            if (result != -1)
            {
                setDirection(ForgeDirection.getOrientation(result));
                return true;
            }
        }

        return false;
    }

    /** @author Based of Greg (GregTech) */
    public byte getSideToRotate(byte hitSide, double hitX, double hitY, double hitZ)
    {
        byte tBack = (byte) (hitSide ^ 1);
        switch (hitSide)
        {
            case 0:
            case 1:
                if (hitX < 0.25)
                {
                    if (hitZ < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitZ > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(4))
                    {
                        return 4;
                    }
                }
                if (hitX > 0.75)
                {
                    if (hitZ < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitZ > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(5))
                    {
                        return 5;
                    }
                }
                if (hitZ < 0.25)
                {
                    if (canRotate(2))
                    {
                        return 2;
                    }
                }
                if (hitZ > 0.75)
                {
                    if (canRotate(3))
                    {
                        return 3;
                    }
                }
                if (canRotate(hitSide))
                {
                    return hitSide;
                }
            case 2:
            case 3:
                if (hitX < 0.25)
                {
                    if (hitY < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitY > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(4))
                    {
                        return 4;
                    }
                }
                if (hitX > 0.75)
                {
                    if (hitY < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitY > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(5))
                    {
                        return 5;
                    }
                }
                if (hitY < 0.25)
                {
                    if (canRotate(0))
                    {
                        return 0;
                    }
                }
                if (hitY > 0.75)
                {
                    if (canRotate(1))
                    {
                        return 1;
                    }
                }
                if (canRotate(hitSide))
                {
                    return hitSide;
                }
            case 4:
            case 5:
                if (hitZ < 0.25)
                {
                    if (hitY < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitY > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(2))
                    {
                        return 2;
                    }
                }
                if (hitZ > 0.75)
                {
                    if (hitY < 0.25)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (hitY > 0.75)
                    {
                        if (canRotate(tBack))
                        {
                            return tBack;
                        }
                    }
                    if (canRotate(3))
                    {
                        return 3;
                    }
                }
                if (hitY < 0.25)
                {
                    if (canRotate(0))
                    {
                        return 0;
                    }
                }
                if (hitY > 0.75)
                {
                    if (canRotate(1))
                    {
                        return 1;
                    }
                }
                if (canRotate(hitSide))
                {
                    return hitSide;
                }
        }
        return -1;
    }

    public ForgeDirection determineOrientation(EntityLivingBase entityLiving)
    {
        if (MathHelper.abs((float) entityLiving.posX - x()) < 2.0F && MathHelper.abs((float) entityLiving.posZ - z()) < 2.0F)
        {
            double d0 = entityLiving.posY + 1.82D - entityLiving.yOffset;

            if (canRotate(1) && d0 - y() > 2.0D)
            {
                return ForgeDirection.UP;
            }

            if (canRotate(0) && y() - d0 > 0.0D)
            {
                return ForgeDirection.DOWN;
            }
        }

        int playerSide = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int returnSide = (playerSide == 0 && canRotate(2)) ? 2 : ((playerSide == 1 && canRotate(5)) ? 5 : ((playerSide == 2 && canRotate(3)) ? 3 : ((playerSide == 3 && canRotate(4)) ? 4 : 0)));

        if (isFlipPlacement)
        {
            return ForgeDirection.getOrientation(returnSide).getOpposite();
        }

        return ForgeDirection.getOrientation(returnSide);
    }

    public boolean canRotate(int ord)
    {
        return (rotationMask & (1 << ord)) != 0;
    }

    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(metadata());
    }

    public void setDirection(ForgeDirection direction)
    {
        world().setBlockMetadataWithNotify(x(), y(), z(), direction.ordinal(), 3);
    }

    /** Block events */
    protected void onAdded()
    {
        onWorldJoin();
    }

    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if (this instanceof IRotatable)
        {
            ((IRotatable) this).setDirection(determineOrientation(entityLiving));
        }
    }

    public void onRemove(int par5, int par6)
    {
        onWorldSeparate();
    }

    protected void onWorldJoin()
    {
    }

    protected void onWorldSeparate()
    {
    }

    protected void onNeighborChanged()
    {
    }

    protected void notifyChange()
    {
        world().notifyBlocksOfNeighborChange(x(), y(), z(), blockID());
    }

    protected void markRender()
    {
        world().markBlockForRenderUpdate(x(), y(), z());
    }

    protected void markUpdate()
    {
        world().markBlockForUpdate(x(), y(), z());
    }

    protected void updateLight()
    {
        world().updateAllLightTypes(x(), y(), z());
    }

    protected void scheduelTick(int delay)
    {
        world().scheduleBlockUpdate(x(), y(), z(), blockID(), delay);
    }

    /** Called when an entity collides with this block. */
    public void collide(Entity entity)
    {

    }

    /** Collision Note that all bounds done in the the tile is relative to the tile's position. */
    public Iterable<Cuboid> getCollisionBoxes(Cuboid intersect, Entity entity)
    {
        List<Cuboid> boxes = new ArrayList<Cuboid>();

        for (Cuboid cuboid : getCollisionBoxes())
        {
            if (intersect != null && cuboid.intersects(intersect))
            {
                boxes.add(cuboid);
            }
        }

        return boxes;
    }

    public Iterable<Cuboid> getCollisionBoxes()
    {
        return Arrays.asList(new Cuboid[] { bounds });
    }

    public Cuboid getSelectBounds()
    {
        return bounds;
    }

    public Cuboid getCollisionBounds()
    {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    public final TileRender getRenderer()
    {
        // TODO: Be careful if this might cause memory issues.
        if (!RenderInfo.renderer.containsKey(this))
        {
            RenderInfo.renderer.put(this, newRenderer());
        }

        return RenderInfo.renderer.get(this);
    }

    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer()
    {
        return null;
    }

    /** Called in the world. */
    @SideOnly(Side.CLIENT)
    public Icon getIcon(IBlockAccess access, int side)
    {
        return getIcon(side, access.getBlockMetadata(x(), y(), z()));
    }

    /** Called either by an item, or in a world. */
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return getIcon();
    }

    public Icon getIcon()
    {
        return RenderInfo.icon.get(getTextureName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        RenderInfo.icon.put(getTextureName(), iconRegister.registerIcon(getTextureName()));
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        return textureName == null ? "MISSING_ICON_TILE_" + getBlockType().blockID + "_" + name : block.dummyTile.domain + textureName;
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        return side == 0 && this.bounds.min.y > 0.0D ? true : (side == 1 && this.bounds.max.y < 1.0D ? true : (side == 2 && this.bounds.min.z > 0.0D ? true : (side == 3 && this.bounds.max.z < 1.0D ? true : (side == 4 && this.bounds.min.x > 0.0D ? true : (side == 5 && this.bounds.max.x < 1.0D ? true : !access.isBlockOpaqueCube(x, y, z))))));
    }

    public void onFillRain()
    {

    }

    public boolean isIndirectlyPowered()
    {
        return world().isBlockIndirectlyGettingPowered(x(), y(), z());
    }

    public int getStrongestIndirectPower()
    {
        return world().getStrongestIndirectPower(x(), y(), z());
    }

    public int getWeakRedstonePower(IBlockAccess access, int side)
    {
        return getStrongRedstonePower(access, side);
    }

    public int getStrongRedstonePower(IBlockAccess access, int side)
    {
        return 0;
    }

    public boolean isSolid(IBlockAccess access, int side)
    {
        return material.isSolid();
    }

    public int getRenderBlockPass()
    {
        return 0;
    }

    public int tickRate(World world)
    {
        return 20;
    }

    public interface IComparatorInputOverride
    {
        public int getComparatorInputOverride(int side);
    }

    /** Rendering */
    @SideOnly(Side.CLIENT)
    public static class RenderInfo
    {
        @SideOnly(Side.CLIENT)
        private static final WeakHashMap<TileBlock, TileRender> renderer = new WeakHashMap<TileBlock, TileRender>();

        @SideOnly(Side.CLIENT)
        private static final HashMap<String, Icon> icon = new HashMap<String, Icon>();
    }

}
