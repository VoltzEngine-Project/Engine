package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.api.tile.IPlayerUsing;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.lib.render.block.RenderTileDummy;
import com.builtbroken.mc.lib.transform.region.Cuboid;
import com.builtbroken.mc.lib.transform.vector.IVectorWorld;
import com.builtbroken.mc.lib.transform.vector.Vector3;
import com.builtbroken.mc.lib.transform.vector.VectorWorld;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.*;

/**
 * Created by robert on 1/4/2015.
 */
public abstract class Tile extends TileEntity implements IVectorWorld, IPlayerUsing
{
    //Static block vars, never use in your tile
    private BlockTile block = null;
    private IBlockAccess access = null;
    public String domain;

    //Block vars
    public String name;
    public CreativeTabs creativeTab = CreativeTabs.tabMisc;
    public Material material = Material.clay;
    public float hardness = 1;
    public float resistance = 1;
    public boolean canEmmitRedstone = false;
    public boolean isOpaque = false;
    public Block.SoundType stepSound;
    public Class<? extends ItemBlock> itemBlock = ItemBlock.class;

    protected Cuboid bounds = new Cuboid(0, 0, 0, 1, 1, 1);

    //Icon vars
    @SideOnly(Side.CLIENT)
    protected HashMap<String, IIcon> icons;
    protected boolean useSidedTextures = false;
    protected String textureName;

    //Renderer vars
    public boolean dynamicRendererCrashed = false;
    public boolean renderNormalBlock = true;
    public boolean renderTileEntity = true;

    //Tile Vars
    protected long ticks = 0L;
    protected final Set<EntityPlayer> playersUsing = new HashSet();

    public Tile(Material material)
    {
        this.material = material;
    }

    /**
     * Used to detect if the block is a tile or data object for creating blocks
     *
     * @return Normally you want to return a new instance of this
     */
    public abstract Tile newTile();

    public Tile newTile(World world, int meta)
    {
        return newTile();
    }

    @Override
    public final void updateEntity()
    {
        if (ticks == 0)
            firstTick();
        else
            update();

        //Increase tick
        if (ticks >= Long.MAX_VALUE)
            ticks = 0;
        ticks += 1;

    }

    public void blockUpdate()
    {
        update();
    }

    /**
     * Called first update() call of the tile. Use
     * this to init any values that are needed right
     * after the tile has been fully placed into the
     * world.
     */
    public void firstTick()
    {
    }

    /**
     * Called each tick as long as the the tile can update.
     */
    public void update()
    {

    }


    //=============================
    //====== Interaction ==========
    //=============================

    /**
     * Called when the player left clicks the block
     */
    public void click(EntityPlayer player)
    {
    }

    /**
     * Called when the player right clicks the block.
     * Default implementation calls use() and configure()
     *
     * @param player - player who clicked the block
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    public boolean activate(EntityPlayer player, int side, Vector3 hit)
    {
        if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), xi(), yi(), zi()))
        {
            if (configure(player, side, hit))
            {
                WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), xi(), yi(), zi());
                return true;
            }
            return false;
        }
        return use(player, side, hit);
    }

    /**
     * Called when the player has clicked a block with something other than a wrench
     *
     * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        return false;
    }

    /**
     * Called when the player uses a supported wrench on the block
     *
     * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
     * @param side   - side of the block clicked as an int(0-5)
     * @param hit    - Vector3 location of the spot hit on the block
     * @return true if the click event was used
     */
    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        return false;
    }

    /**
     * Opens the main gui for this tile
     */
    public void openGui(EntityPlayer player, Object mod)
    {
        openGui(player, 0, mod);
    }

    /**
     * Opens a gui by the id given
     */
    public void openGui(EntityPlayer player, int gui, Object mod)
    {
        if (isServer())
            player.openGui(mod, gui, world(), xi(), yi(), zi());
    }

    //===========================
    //======= ItemStack  ========
    //===========================
    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        if (getBlockType() != null)
        {
            drops.add(new ItemStack(getBlockType(), quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)));
        }
        return drops;
    }

    /**
     * Block object that goes to this tile
     */
    public Block getBlockType()
    {
        if (access != null)
        {
            Block b = access.getBlock(xi(), yi(), zi());
            if (b == null)
            {
                return block;
            }
            return b;
        }
        return block;
    }

    /**
     * Number of items to drop
     */
    public int quantityDropped(int meta, int fortune)
    {
        return 1;
    }

    /**
     * Meta value to drop
     */
    public int metadataDropped(int meta, int fortune)
    {
        return 0;
    }


    /**
     * Gets all sub versions of this block to add to the creative menu
     *
     * @param item         - Item object of the block
     * @param creativeTabs - creative tab to list on
     * @param list         - current list of items
     */
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
    }

    /**
     * Gets the ItemStack when the player pick blocks
     *
     * @param target - block hit by a ray trace
     * @return ItemStack of your block, can be null but shouldn't unless the block can't be placed
     */
    public ItemStack getPickBlock(MovingObjectPosition target)
    {
        return toItemStack();
    }

    public ItemStack toItemStack()
    {
        return new ItemStack(getBlockType(), 1, metadataDropped(getBlockMetadata(), 0));
    }

    //==============================
    //==== Location ================
    //==============================
    @Override
    public double z()
    {
        return zCoord;
    }

    public int zi()
    {
        return zCoord;
    }

    @Override
    public double x()
    {
        return xCoord;
    }

    public int xi()
    {
        return xCoord;
    }

    @Override
    public double y()
    {
        return yCoord;
    }

    public int yi()
    {
        return yCoord;
    }

    @Override
    public World world()
    {
        return getWorldObj();
    }

    public void setBlock(BlockTile block)
    {
        this.block = block;
    }

    public void setAccess(IBlockAccess access)
    {
        this.access = access;
    }

    public IBlockAccess getAccess()
    {
        if (world() != null)
        {
            return world();
        }
        return access;
    }

    public Vector3 toVector3()
    {
        return new Vector3(x(), y(), z());
    }

    public VectorWorld toVectorWorld()
    {
        return new VectorWorld(world(), x(), y(), z());
    }

    //=========================
    //=== Events ==============
    //=========================

    /** Called after the block has been registered.
     * Use this time to register recipes, events, or
     * any other data that needs to exist with this
     * block. Do not register client, or server only
     * content as this is called both sides */
    public void onRegistered()
    {

    }

    public void onAdded()
    {
        onWorldJoin();
    }

    public void onWorldJoin()
    {
    }

    /**
     * Called when the block is placed by a living entity
     *
     * @param entityLiving - entity who placed the block
     * @param itemStack    - ItemStack the entity used to place the block
     */
    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
    }

    /**
     * Called after the block has been placed
     *
     * @param metadata - meta of the placed block
     */
    public void onPostPlaced(int metadata)
    {

    }

    /**
     * Called when the block is removed. Do all cleanup needed in this method.
     *
     * @param block - Block object being removed
     * @param par6  - meta of the block
     */
    public void onRemove(Block block, int par6)
    {
        onWorldSeparate();
    }

    public void onWorldSeparate()
    {
    }

    public void onDestroyedByExplosion(Explosion ex)
    {

    }

    /**
     * Called when a neighbor block changes
     *
     * @param block
     */
    public void onNeighborChanged(Block block)
    {
    }

    /**
     * Called when a neighbor tile changes
     *
     * @param pos
     */
    public void onNeighborChanged(Vector3 pos)
    {
    }

    /**
     * Called when an entity collides with this block.
     */
    public void onCollide(Entity entity)
    {
    }

    /**
     * Called when a rain particle hits this block
     */
    public void onFillRain()
    {
    }

    //==========================
    //==== Triggers ============
    //==========================
    public void notifyBlocksOfNeighborChange()
    {
        world().notifyBlocksOfNeighborChange(xi(), yi(), zi(), block);
    }

    protected void markRender()
    {
        world().func_147479_m(xi(), yi(), zi());
    }

    protected void markUpdate()
    {
        world().markBlockForUpdate(xi(), yi(), zi());
    }

    protected void updateLight()
    {
        world().func_147451_t(xi(), yi(), zi());
    }

    protected void scheduleTick(int delay)
    {
        world().scheduleBlockUpdate(xi(), yi(), zi(), getBlockType(), delay);
    }

    //=========================
    //==== Getters ============
    //=========================

    /**
     * Gets the light value of the block
     *
     * @return light value from 0 - 16;
     */
    public int getLightValue()
    {
        return 0;
    }


    /**
     * Collision Note that all bounds done in the the tile is relative to the tile's position.
     */
    public Iterable<Cuboid> getCollisionBoxes(Cuboid intersect, Entity entity)
    {
        List<Cuboid> boxes = new ArrayList<Cuboid>();
        boxes.add(bounds);
        return boxes;
    }

    public Cuboid getSelectBounds()
    {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return getCollisionBounds().clone().add(this).toAABB();
    }

    public Cuboid getCollisionBounds()
    {
        return bounds;
    }

    public int getMetadata()
    {
        if (getAccess() != null)
            return getAccess().getBlockMetadata(xi(), yi(), zi());
        else
            return 0;
    }

    @Override
    public int getBlockMetadata()
    {
        if (world() == null)
            return 0;
        else
            return super.getBlockMetadata();
    }

    @Override
    public Set<EntityPlayer> getPlayersUsing()
    {
        return playersUsing;
    }

    //TODO: Get rid of parameters
    public boolean shouldSideBeRendered(int side)
    {
        return (side == 0 && this.bounds.min().y() > 0.0D)
                || (side == 1 && this.bounds.max().y() < 1.0D)
                || (side == 2 && this.bounds.min().z() > 0.0D)
                || (side == 3 && this.bounds.max().z() < 1.0D)
                || (side == 4 && this.bounds.min().x() > 0.0D)
                || (side == 5 && this.bounds.max().x() < 1.0D)
                || !access.getBlock(xi(), yi(), zi()).isOpaqueCube();
    }


    /**
     * Is this block being indirectly being powered
     */
    public boolean isIndirectlyPowered()
    {
        return world().isBlockIndirectlyGettingPowered(xi(), yi(), zi());
    }

    /**
     * Gets the level of power provide to this block
     */
    public int getStrongestIndirectPower()
    {
        return world().getStrongestIndirectPower(xi(), yi(), zi());
    }

    /**
     * Gets the level of power being provided by this block
     */
    public int getWeakRedstonePower(int side)
    {
        return getStrongRedstonePower(side);
    }

    /**
     * Gets the level of power being provided by this block
     */
    public int getStrongRedstonePower(int side)
    {
        return 0;
    }

    public void setBlockBoundsBasedOnState()
    {

    }

    /**
     * Is this block solid on th side
     *
     * @param side - side
     * @return true if solid
     */
    public boolean isSolid(int side)
    {
        return material.isSolid();
    }

    /**
     * Render pass
     */
    public int getRenderBlockPass()
    {
        return 0;
    }

    /**
     * Tick rate of the tile in @param world
     */
    public int tickRate()
    {
        return 20;
    }

    /**
     * Can the player silk touch the block
     *
     * @param player   - player mining the block
     * @param metadata - meta value of the block
     * @return true if it can
     */
    public boolean canSilkHarvest(EntityPlayer player, int metadata)
    {
        return false;
    }

    /**
     * Gets the explosive resistance of this block.
     *
     * @param entity            - The affecting entity
     * @param explosionPosition - The position in which the explosion is ocurring at
     * @return A value representing the explosive resistance
     */
    public float getExplosionResistance(Entity entity, Vector3 explosionPosition)
    {
        return getExplosionResistance(entity);
    }

    /**
     * Gets the explosive resistance of this block.
     * Note: Called without the world object being present.
     *
     * @param entity - The affecting entity
     * @return A value representing the explosive resistance
     */
    public float getExplosionResistance(Entity entity)
    {
        return resistance / 5f;
    }

    public boolean isClient()
    {
        return world().isRemote;
    }

    public boolean isServer()
    {
        return !world().isRemote;
    }

    //=========================
    //==== Icons ==============
    //=========================

    /**
     * Called in the world.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side)
    {
        return getIcon(side, access.getBlockMetadata(xi(), yi(), zi()));
    }

    /**
     * Called either by an item, or in a world.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (useSidedTextures)
        {
            if (side == 0)
            {
                return getTopIcon(meta);
            }
            if (side == 1)
            {
                return getBottomIcon(meta);
            }
            return getSideIcon(meta, side);
        }
        return icons.get(getTextureName());
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        if (textureName == null)
            return "MISSING_ICON_TILE_" + Block.getIdFromBlock(block) + "_" + name;
        else
            return block.staticTile.domain + textureName;
    }

    /**
     * Gets the icon that renders on the top
     *
     * @param meta - placement data
     * @return icon that will render on top
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int meta)
    {
        IIcon icon = icons.get(getTextureName() + "_top");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    /**
     * Gets the icon that renders on the bottom
     *
     * @param meta - placement data
     * @return icon that will render on bottom
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getBottomIcon(int meta)
    {
        IIcon icon = icons.get(getTextureName() + "_bottom");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    /**
     * Gets the icon that renders on the sides
     *
     * @param meta - placement data
     * @return icon that will render on sides
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int meta)
    {
        return getSideIcon(meta, 0);
    }

    /**
     * Gets the icon that renders on the sides
     *
     * @param meta - placement data
     * @param side - side of the icon
     * @return icon that will render on sides
     */
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int meta, int side)
    {
        IIcon icon = icons.get(getTextureName() + "_side");
        if (icon == null)
            icon = icons.get(getTextureName());
        return icon;
    }

    public void setTextureName(String value)
    {
        textureName = value;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        icons = new HashMap();
        if (useSidedTextures)
        {
            registerSideTextureSet(iconRegister);
        }
        else
        {
            icons.put(getTextureName(), iconRegister.registerIcon(getTextureName()));
        }
        //TODO loop threw missing textures replacing them with textures based on material
    }

    /**
     * Registers a set of 3 textures(top, sides, bottom) to be used for the block renderer
     * Uses the texture name appended with _top _side _bottom
     *
     * @param iconRegister
     */
    @SideOnly(Side.CLIENT)
    public void registerSideTextureSet(IIconRegister iconRegister)
    {
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_top"));
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_side"));
        icons.put(getTextureName(), iconRegister.registerIcon(getTextureName() + "_bottom"));
    }

    @SideOnly(Side.CLIENT)
    public int getColorMultiplier()
    {
        return 0xFFFFFF;
    }

    //=========================
    //==== Renderers ==========
    //=========================

    /**
     * Render the static, unmoving faces of this part into the world renderer.
     * The Tessellator is already drawing.
     *
     * @return true if vertices were added to the tessellator
     */
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(RenderBlocks renderer, Vector3 pos, int pass)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasSpecialRenderer()
    {
        return getSpecialRenderer() != null;
    }

    @SideOnly(Side.CLIENT)
    public TileEntitySpecialRenderer getSpecialRenderer()
    {
        return TileEntityRendererDispatcher.instance.getSpecialRendererByClass(getClass());
    }

    @SideOnly(Side.CLIENT)
    public void renderInventory(ItemStack itemStack)
    {
        TileEntitySpecialRenderer tesr = getSpecialRenderer();

        if (!(tesr instanceof RenderTileDummy))
        {
            if (tesr instanceof ISimpleItemRenderer)
            {
                ((ISimpleItemRenderer) tesr).renderInventoryItem(IItemRenderer.ItemRenderType.INVENTORY, itemStack);
            }

            if (!dynamicRendererCrashed)
            {
                try
                {
                    renderDynamic(new Vector3(-0.5, -0.5, -0.5), 0, 0);
                } catch (Exception e)
                {
                    dynamicRendererCrashed = true;
                    renderNormalBlock = true;
                    System.out.println("A tile has failed to render dynamically as an item. Suppressing renderer to prevent future crashes.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Render the dynamic, changing faces of this part and other gfx as in a TESR.
     * The Tessellator will need to be started if it is to be used.
     *
     * @param pos   The position of this block space relative to the renderer, same as x, y, z passed to TESR.
     * @param frame The partial interpolation frame value for animations between ticks
     * @param pass  The render pass, 1 or 0
     */
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass)
    {
        TileEntitySpecialRenderer tesr = getSpecialRenderer();

        if (tesr != null && !(tesr instanceof RenderTileDummy))
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.5, -0.5, -0.5);
            tesr.renderTileEntityAt(this, 0, 0, 0, 0);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    public void randomDisplayTick()
    {

    }

    //===========================
    //==== Helpers ==============
    //===========================

    public void setMeta(int meta)
    {
        world().setBlockMetadataWithNotify(xi(), yi(), zi(), meta, 3);
    }

    @Override
    public final Packet getDescriptionPacket()
    {
        return Engine.instance.packetHandler.toMCPacket(getDescPacket());
    }

    public AbstractPacket getDescPacket()
    {
        return null;
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    public byte determineOrientation(EntityLivingBase entityLiving)
    {
        if (entityLiving != null)
        {
            if (MathHelper.func_154353_e(entityLiving.posX - x()) < 2.0F && MathHelper.func_154353_e(entityLiving.posZ - z()) < 2.0F)
            {
                double var5 = entityLiving.posY + 1.82D - entityLiving.yOffset;
                if (var5 - y() > 2.0D)
                {
                    return 1;
                }
                if (y() - var5 > 0.0D)
                {
                    return 0;
                }
            }
            int rotation = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            if (rotation == 0) return 2;
            else if (rotation == 1) return 5;
            else if (rotation == 2) return 3;
            else if (rotation == 3) return 4;
            else return 0;
        }
        return 0;
    }
}
