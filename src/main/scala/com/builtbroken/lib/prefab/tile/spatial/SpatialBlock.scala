package com.builtbroken.lib.prefab.tile.spatial

import _root_.java.lang.reflect.Method
import java.util

import com.builtbroken.api.items.ISimpleItemRenderer
import com.builtbroken.lib.content.prefab.TIO
import com.builtbroken.lib.render.RenderUtility
import com.builtbroken.lib.render.wrapper.RenderTileDummy
import com.builtbroken.lib.transform.region.Cuboid
import com.builtbroken.lib.transform.vector.{TVectorWorld, Vector2, Vector3, VectorWorld}
import com.builtbroken.lib.utility.{LanguageUtility, WrenchUtility}
import com.builtbroken.lib.wrapper.WrapList._
import com.builtbroken.mod.{BBL, References}
import com.google.common.collect.Maps
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.client.renderer.tileentity.{TileEntityRendererDispatcher, TileEntitySpecialRenderer}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.{Item, ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{AxisAlignedBB, IIcon, MathHelper, MovingObjectPosition}
import net.minecraft.world.{Explosion, IBlockAccess, World}
import net.minecraftforge.client.IItemRenderer
import org.lwjgl.opengl.{GL11, GL12}

import scala.beans.BeanProperty
import scala.collection.convert.wrapAll._
import scala.collection.immutable

/**
 * All blocks inherit this class.
 *
 * Note that a lot of the variables will not exist except on the primary instance of the Spatial Block,
 * hosted in BlockDummy.
 *
 * @author - Calclavia
 */
object SpatialBlock
{
  val icon = new util.HashMap[String, IIcon]
  val inventoryTileEntities: java.util.Map[Block, TileEntity] = Maps.newIdentityHashMap()

  def getClickedFace(hitSide: Byte, hitX: Float, hitY: Float, hitZ: Float): Vector2 =
  {
    hitSide match
    {
      case 0 =>
        return new Vector2(1 - hitX, hitZ)
      case 1 =>
        return new Vector2(hitX, hitZ)
      case 2 =>
        return new Vector2(1 - hitX, 1 - hitY)
      case 3 =>
        return new Vector2(hitX, 1 - hitY)
      case 4 =>
        return new Vector2(hitZ, 1 - hitY)
      case 5 =>
        return new Vector2(1 - hitZ, 1 - hitY)
      case _ =>
        return new Vector2(0.5, 0.5)
    }
  }

  def getTileEntityForBlock(block: Block): TileEntity =
  {
    var te: TileEntity = inventoryTileEntities.get(block);
    if (te == null && Minecraft.getMinecraft().thePlayer != null)
    {
      te = block.createTileEntity(Minecraft.getMinecraft().thePlayer.getEntityWorld(), 0);
      inventoryTileEntities.put(block, te);
    }
    return te;
  }

  abstract trait IComparatorInputOverride
  {
    def getComparatorInputOverride(side: Int): Int
  }

}

abstract class SpatialBlock(val material: Material) extends TileEntity with TVectorWorld
{
  /** Name of the block, unlocalized */
  var name = LanguageUtility.decapitalizeFirst(this.getClass().getSimpleName().replaceFirst("Tile", ""))
  /** ItemBlock class used to place this block */
  var itemBlock: Class[_ <: ItemBlock] = classOf[ItemBlock]
  /** ???? */
  var isCreativeTabSet = false
  /** Dummy block that is placed so this tile can spawn */
  var block: BlockDummy = null
  /** Block's bounds, used for collision checks and rendering if a standard block */
  var _bounds: Cuboid = new Cuboid(0, 0, 0, 1, 1, 1)
  /** Creative tab this block will show on */
  var _creativeTab: CreativeTabs = null
  /** How hard is the block to mine */
  @BeanProperty
  var blockHardness: Float = 1
  /** Resistance to being blown up */
  @BeanProperty
  var blockResistance: Float = 1
  /** Sound made when the player steps on this block */
  @BeanProperty
  var stepSound: Block.SoundType = Block.soundTypeStone
  /** Can this provide a redstone signal */
  @BeanProperty
  var providePower: Boolean = false
  /** Random block updates */
  @BeanProperty
  var tickRandomly: Boolean = false
  /** False will make the block glass like */
  var isOpaqueCube: Boolean = true
  /** World accessor */
  var _access: IBlockAccess = null
  /** Name of the texture */
  var textureName: String = name
  /** Domain prefix for texture registration  */
  var domain: String = null

  /** Render as a normal block if true */
  @BeanProperty
  var normalRender: Boolean = true
  /** Override to force a block to render even if not a normal renderer */
  @BeanProperty
  var renderStaticBlock: Boolean = false
  /** Forces the item block to render as a block */
  @BeanProperty
  var forceItemToRenderAsBlock: Boolean = false
  /** Flag to say this has a custom render class for the item */
  @BeanProperty
  var customItemRender: Boolean = false
  /** Sets the block to use side based textures */
  @BeanProperty
  var useSidedTextures: Boolean = false

  /** Flag that is triggered when the dynamic renderer fails, will cause the item to stop rendering */
  private var noDynamicItemRenderCrash: Boolean = true

  /** Use update() instead */
  final override def updateEntity() = update()

  def blockUpdate() = update()

  def randomDisplayTick()
  {

  }

  /**
   * Called after the block is registered. Use this to add recipes.
   */
  def onInstantiate()
  {
  }

  /** Called each tick */
  def update()
  {

  }

  @Deprecated
  def creativeTab(value: CreativeTabs)
  {
    creativeTab = value
  }

  /**
   * Sets the creative tab
   * @param value - tab to set
   */
  def setCreativeTab(value: CreativeTabs)
  {
    creativeTab = value
  }

  /** Gets the creative tab */
  def creativeTab = _creativeTab

  def creativeTab_=(value: CreativeTabs): Unit =
  {
    _creativeTab = value
    isCreativeTabSet = true
  }

  def itemBlock(item: Class[_ <: ItemBlock]): Unit =
  { itemBlock = item }

  /** Sets the bounding box of the block */
  def bounds(cuboid: Cuboid)
  {
    bounds = cuboid
  }

  /** Bounds for the block */
  def bounds = _bounds

  /** Sets the block bounds */
  def bounds_=(cuboid: Cuboid)
  {
    _bounds = cuboid

    if (block != null)
      block.setBlockBounds(_bounds.min.xf, _bounds.min.yf, _bounds.min.zf, _bounds.max.xf, _bounds.max.yf, _bounds.max.zf)
  }

  /** Sets the dummy block class uses by this spatial block */
  def setBlock(block: BlockDummy)
  {
    this.block = block
  }

  /** Sets the resistance to being broken by tools or general actions */
  @deprecated
  def blockHardness(hardness: Float): Unit = blockHardness = hardness

  /** Sets the resistance to the block being blown up */
  @deprecated
  def blockResistance(resistance: Float): Unit = blockResistance = resistance

  /** Sets the stepping sound */
  def stepSound(sound: Block.SoundType): Unit = stepSound = sound

  /** Sets the block can provide power to other blocks */
  def canProvidePower(bool: Boolean): Unit = providePower = bool

  /** When set true the block will update every so often   */
  @deprecated
  def tickRandomly(bool: Boolean): Unit = tickRandomly = bool

  /** When true renders the block as a standard block */
  @deprecated
  def normalRender(bool: Boolean): Unit = normalRender = bool

  /** Forces the renderer to render a standard block during tile rendering */
  @deprecated
  def forceStandardRender(bool: Boolean): Unit = forceItemToRenderAsBlock = bool

  /** When true tells the dummy block we have a custom item renderer */
  def customItemRender(bool: Boolean): Unit = customItemRender = bool

  /** When false the block is see threw */
  def isOpaqueCube(bool: Boolean): Unit = isOpaqueCube = bool

  /** Sets the active world */
  def world(world: World)
  {
    this.worldObj = world
  }

  override def x: Double = xCoord

  override def y: Double = yCoord

  override def z: Double = zCoord

  /** World location of the block, centered */
  def center: VectorWorld = toVectorWorld + 0.5

  /**
   * Gets all ItemStacks dropped by this machine when its destroyed
   * @param metadata - meta value of the block when broken
   * @param fortune - bonus of the tool mining it
   * @return ArrayList of Items
   */
  def getDrops(metadata: Int, fortune: Int): util.ArrayList[ItemStack] =
  {
    val drops: util.ArrayList[ItemStack] = new util.ArrayList[ItemStack]
    if (getBlockType != null)
    {
      drops.add(new ItemStack(getBlockType, quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)))
    }
    return drops
  }

  /** Block object that goes to this tile */
  override def getBlockType: Block =
  {
    if (access != null)
    {
      val b: Block = access.getBlock(xi, yi, zi)
      if (b == null)
      {
        return block
      }
      return b
    }
    return block
  }

  /**
   * Number of items dropped when broken.
   * This method is used by the default implementation of getDrops
   *
   * @param meta - meta value of the block
   * @param fortune - bonus of the tool mining it
   * @return number of items, 0 will result in no drop
   */
  def quantityDropped(meta: Int, fortune: Int): Int = 1

  /**
   * Gets the meta value when this block is dropped.
   * This method is used by the default implementation of getDrops
   *
   * @param meta - meta value of the block
   * @param fortune - bonus of the tool mining it
   * @return meta value, shouldn't be less then 0
   */
  def metadataDropped(meta: Int, fortune: Int): Int = 0

  /**
   * Detects if the player is holding the control key down.
   * Requires codechicken multipart package in order to function
   *
   * @param player - player to check
   * @return true if control key is held down
   */
  def isControlDown(player: EntityPlayer): Boolean =
  {
    try
    {
      //TODO implement a non dependent solution
      val ckm: Class[_] = Class.forName("codechicken.multipart.ControlKeyModifer")
      val m: Method = ckm.getMethod("isControlDown", classOf[EntityPlayer])
      return m.invoke(null, player).asInstanceOf[Boolean]
    }
    catch
      {
        case e: Exception =>
        {
          if (BBL.runningAsDev)
            References.LOGGER.catching(e)
        }
      }
    return false
  }

  /**
   * Gets all sub versions of this block to add to the creative menu
   * @param item - Item object of the block
   * @param creativeTabs - creative tab to list on
   * @param list - current list of items
   */
  def getSubBlocks(item: Item, creativeTabs: CreativeTabs, list: util.List[_])
  {
    list.add(new ItemStack(item, 1, 0))
  }

  /**
   * Gets the ItemStack when the player pick blocks
   * @param target - block hit by a ray trace
   * @return ItemStack of your block, can be null but shouldn't unless the block can't be placed
   */
  def getPickBlock(target: MovingObjectPosition): ItemStack = new ItemStack(getBlockType, 1, metadataDropped(metadata, 0))

  /**
   * Gets the light value of the block
   * @param access - Simple version of the world, though don't assume it is
   * @return light value from 0 - 16;
   */
  def getLightValue(access: IBlockAccess): Int = block.getLightValue

  /**
   * Called when the player left clicks the block
   */
  def click(player: EntityPlayer)
  {
  }

  /**
   * Called when the player right clicks the block.
   * Default implementation calls use() and configure()
   *
   * @param player - player who clicked the block
   * @param side - side of the block clicked as an int(0-5)
   * @param hit - Vector3 location of the spot hit on the block
   * @return true if the click event was used
   */
  def activate(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem, xi, yi, zi))
    {
      if (configure(player, side, hit))
      {
        WrenchUtility.damageWrench(player, player.inventory.getCurrentItem, xi, yi, zi)
        return true
      }
      return false
    }
    return use(player, side, hit)
  }

  /**
   * Called when the player has clicked a block with something other than a wrench
   * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
   * @param side - side of the block clicked as an int(0-5)
   * @param hit - Vector3 location of the spot hit on the block
   * @return true if the click event was used
   */
  protected def use(player: EntityPlayer, side: Int, hit: Vector3): Boolean = false

  /**
   * Called when the player uses a supported wrench on the block
   *
   * @param player - player who clicked the block, don't assume EntityPlayerMP as it can be a fake player
   * @param side - side of the block clicked as an int(0-5)
   * @param hit - Vector3 location of the spot hit on the block
   * @return true if the click event was used
   */
  protected def configure(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    if (this.isInstanceOf[TIO])
      return this.asInstanceOf[TIO].toggleIO(side, player)

    return false
  }

  /**
   * Called when the block is first added to the world
   */
  def onAdded()
  {
    onWorldJoin()
  }

  /**
   * Called when the block is added/loaded to the world
   */
  def onWorldJoin()
  {
  }

  /**
   * Called when the block is placed by a living entity
   * @param entityLiving - entity who placed the block
   * @param itemStack - ItemStack the entity used to place the block
   */
  def onPlaced(entityLiving: EntityLivingBase, itemStack: ItemStack)
  {
  }

  /**
   * Called after the block has been placed
   * @param metadata - meta of the placed block
   */
  def onPostPlaced(metadata: Int)
  {

  }

  /**
   * Called when the block is removed. Do all cleanup needed in this method.
   * @param block - Block object being removed
   * @param par6 - meta of the block
   */
  def onRemove(block: Block, par6: Int)
  {
    onWorldSeparate()
  }

  def onDestroyedByExplosion(ex: Explosion)
  {

  }

  def onWorldSeparate()
  {
  }

  /**
   * Called when a neighbor block changes
   * @param block
   */
  def onNeighborChanged(block: Block)
  {
  }

  /**
   * Called when a neighbor tile changes
   * @param pos
   */
  def onNeighborChanged(pos: Vector3)
  {
  }

  def notifyChange()
  {
    world.notifyBlocksOfNeighborChange(xi, yi, zi, block)
  }

  /**
   * Called when an entity collides with this block.
   */
  def collide(entity: Entity)
  {
  }

  /**
   * Collision Note that all bounds done in the the tile is relative to the tile's position.
   */
  def getCollisionBoxes(intersect: Cuboid, entity: Entity): Iterable[Cuboid] =
  {
    val boxes = new util.ArrayList[Cuboid]

    for (cuboid <- getCollisionBoxes)
    {
      if (intersect != null)
      {
        boxes.add(cuboid)
      }
    }
    return boxes
  }

  def getCollisionBoxes: java.lang.Iterable[Cuboid] = immutable.List[Cuboid](bounds)

  def getSelectBounds: Cuboid = bounds

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox: AxisAlignedBB = (getCollisionBounds + toVectorWorld).toAABB

  def getCollisionBounds: Cuboid = bounds

  /**
   * Called in the world.
   */
  @SideOnly(Side.CLIENT)
  def getIcon(access: IBlockAccess, side: Int): IIcon = getIcon(side, access.getBlockMetadata(xi, yi, zi))

  /**
   * Called either by an item, or in a world.
   */
  @SideOnly(Side.CLIENT)
  def getIcon(side: Int, meta: Int): IIcon =
  {
    if (useSidedTextures)
    {
      if (side == 0)
      {
        return getTopIcon(meta)
      }
      if (side == 1)
      {
        return getBottomIcon(meta)
      }
      return getSideIcon(meta, side)
    }
    return getIcon
  }

  @SideOnly(Side.CLIENT)
  def getIcon: IIcon = SpatialBlock.icon.get(getTextureName)

  @SideOnly(Side.CLIENT)
  protected def getTextureName: String =
  {
    if (textureName == null)
      return "MISSING_ICON_TILE_" + Block.getIdFromBlock(block) + "_" + name
    else
      return block.dummyTile.domain + textureName
  }

  /** Gets the icon that renders on the top
    * @param meta - placement data
    * @return icon that will render on top */
  @SideOnly(Side.CLIENT)
  protected def getTopIcon(meta: Int): IIcon =
  {
    var icon = SpatialBlock.icon.get(getTextureName + "_top")
    if (icon == null)
      icon = SpatialBlock.icon.get(getTextureName)
    return icon
  }

  /** Gets the icon that renders on the bottom
    * @param meta - placement data
    * @return icon that will render on bottom */
  @SideOnly(Side.CLIENT)
  protected def getBottomIcon(meta: Int): IIcon =
  {
    var icon = SpatialBlock.icon.get(getTextureName + "_bottom")
    if (icon == null)
      icon = SpatialBlock.icon.get(getTextureName)
    return icon
  }

  /** Gets the icon that renders on the sides
    * @param meta - placement data
    * @return icon that will render on sides */
  @SideOnly(Side.CLIENT)
  protected def getSideIcon(meta: Int): IIcon = getSideIcon(meta, 0)

  /** Gets the icon that renders on the sides
    * @param meta - placement data
    * @param side - side of the icon
    * @return icon that will render on sides */
  @SideOnly(Side.CLIENT)
  protected def getSideIcon(meta: Int, side: Int): IIcon =
  {
    var icon = SpatialBlock.icon.get(getTextureName + "_side")
    if (icon == null)
      icon = SpatialBlock.icon.get(getTextureName)
    return icon
  }

  def setTextureName(value: java.lang.String)
  {
    textureName = value
  }

  @SideOnly(Side.CLIENT)
  def registerIcons(iconRegister: IIconRegister)
  {
    if (useSidedTextures)
    {
      registerSideTextureSet(iconRegister)
    }
    else
    {
      SpatialBlock.icon.put(getTextureName, iconRegister.registerIcon(getTextureName))
    }
  }

  /** Registers a set of 3 textures(top, sides, bottom) to be used for the block renderer
    * Uses the texture name appended with _top _side _bottom
    * @param iconRegister
    */
  @SideOnly(Side.CLIENT)
  def registerSideTextureSet(iconRegister: IIconRegister)
  {
    SpatialBlock.icon.put(getTextureName, iconRegister.registerIcon(getTextureName + "_top"))
    SpatialBlock.icon.put(getTextureName, iconRegister.registerIcon(getTextureName + "_side"))
    SpatialBlock.icon.put(getTextureName, iconRegister.registerIcon(getTextureName + "_bottom"))
  }

  @SideOnly(Side.CLIENT)
  def colorMultiplier = 0xFFFFFF

  /**
   * Render the static, unmoving faces of this part into the world renderer.
   * The Tessellator is already drawing.
   * @return true if vertices were added to the tessellator
   */
  @SideOnly(Side.CLIENT)
  def renderStatic(renderer: RenderBlocks, pos: Vector3, pass: Int): Boolean =
  {
    if (renderStaticBlock)
      return renderer.renderStandardBlock(block, pos.xi, pos.yi, pos.zi)
    else
      return false
  }

  @SideOnly(Side.CLIENT)
  def hasSpecialRenderer = getSpecialRenderer != null

  @SideOnly(Side.CLIENT)
  def getSpecialRenderer: TileEntitySpecialRenderer =
  {
    val tesr: TileEntitySpecialRenderer = TileEntityRendererDispatcher.instance.getSpecialRendererByClass(getClass)

    if (tesr != null && !tesr.isInstanceOf[RenderTileDummy])
    {
      return tesr
    }

    return null
  }

  @SideOnly(Side.CLIENT)
  def renderInventory(itemStack: ItemStack)
  {
    val tesr: TileEntitySpecialRenderer = getSpecialRenderer

    if (tesr != null && tesr.isInstanceOf[ISimpleItemRenderer])
    {
      tesr.asInstanceOf[ISimpleItemRenderer].renderInventoryItem(IItemRenderer.ItemRenderType.INVENTORY, itemStack)
    }
    else if (normalRender || forceItemToRenderAsBlock)
    {
      RenderUtility.renderNormalBlockAsItem(itemStack.getItem().asInstanceOf[ItemBlock].field_150939_a, itemStack.getItemDamage(), RenderUtility.renderBlocks)
    }
    if (noDynamicItemRenderCrash)
    {
      try
      {
        renderDynamic(new Vector3(-0.5, -0.5, -0.5), 0, 0)
      }
      catch
        {
          case e: Exception =>
          {
            noDynamicItemRenderCrash = false
            System.out.println("A tile has failed to render dynamically as an item. Suppressing renderer to prevent future crashes.")
            e.printStackTrace()
          }
        }
    }
  }

  /**
   * Render the dynamic, changing faces of this part and other gfx as in a TESR.
   * The Tessellator will need to be started if it is to be used.
   * @param pos The position of this block space relative to the renderer, same as x, y, z passed to TESR.
   * @param frame The partial interpolation frame value for animations between ticks
   * @param pass The render pass, 1 or 0
   */
  @SideOnly(Side.CLIENT)
  def renderDynamic(pos: Vector3, frame: Float, pass: Int)
  {
    if (forceItemToRenderAsBlock)
    {
      RenderUtility.renderNormalBlockAsItem(block, metadata, RenderUtility.renderBlocks)
    }

    val tesr: TileEntitySpecialRenderer = getSpecialRenderer

    if (tesr != null && !tesr.isInstanceOf[RenderTileDummy])
    {
      GL11.glEnable(GL12.GL_RESCALE_NORMAL)
      GL11.glPushAttrib(GL11.GL_TEXTURE_BIT)
      GL11.glPushMatrix()
      GL11.glTranslated(-0.5, -0.5, -0.5)
      tesr.renderTileEntityAt(this, 0, 0, 0, 0)
      GL11.glPopMatrix()
      GL11.glPopAttrib()
    }
  }

  def metadata: Int = if (access != null) access.getBlockMetadata(xi, yi, zi) else 0

  def access: IBlockAccess =
  {
    if (world != null)
    {
      return world
    }

    return _access
  }

  //TODO: Get rid of parameters
  def shouldSideBeRendered(access: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean =
  {
    return if (side == 0 && this.bounds.min.y > 0.0D) true else (if (side == 1 && this.bounds.max.y < 1.0D) true else (if (side == 2 && this.bounds.min.z > 0.0D) true else (if (side == 3 && this.bounds.max.z < 1.0D) true else (if (side == 4 && this.bounds.min.x > 0.0D) true else (if (side == 5 && this.bounds.max.x < 1.0D) true else !access.getBlock(x, y, z).isOpaqueCube)))))
  }

  /** Called when a rain particle hits this block */
  def onFillRain()
  {
  }

  /** Is this block being indirectly being powered */
  def isIndirectlyPowered: Boolean = world.isBlockIndirectlyGettingPowered(xi, yi, zi)

  /** Gets the level of power provide to this block */
  def getStrongestIndirectPower: Int = world.getStrongestIndirectPower(xi, yi, zi)

  /** Gets the level of power being provided by this block */
  def getWeakRedstonePower(access: IBlockAccess, side: Int): Int = getStrongRedstonePower(access, side)

  /** Gets the level of power being provided by this block */
  def getStrongRedstonePower(access: IBlockAccess, side: Int): Int = 0

  /**
   * Is this block solid on th side
   * @param access - world accessor
   * @param side - side
   * @return true if solid
   */
  def isSolid(access: IBlockAccess, side: Int): Boolean = material.isSolid

  /** Render pass */
  def getRenderBlockPass: Int = 0

  /** Tick rate of the tile in @param world */
  def tickRate(world: World): Int = 20

  /**
   * Can the player silk touch the block
   * @param player - player mining the block
   * @param metadata - meta value of the block
   * @return true if it can
   */
  def canSilkHarvest(player: EntityPlayer, metadata: Int): Boolean = normalRender && tile == null

  /** Used to detect if the block is a tile or data object for creating blocks
    * @return Normally you want to return this class
    */
  def tile: SpatialBlock = null

  /**
   * Gets the explosive resistance of this block.
   * @param entity - The affecting entity
   * @param explosionPosition - The position in which the explosion is ocurring at
   * @return A value representing the explosive resistance
   */
  def getExplosionResistance(entity: Entity, explosionPosition: Vector3): Float = getExplosionResistance(entity)

  /**
   * Gets the explosive resistance of this block.
   * Note: Called without the world object being present.
   * @param entity - The affecting entity
   * @return A value representing the explosive resistance
   */
  def getExplosionResistance(entity: Entity): Float = blockResistance / 5f

  /**
   * Called upon bounds raytrace. World data given.
   */
  def setBlockBoundsBasedOnState()
  {

  }

  /**
   * Called upon setting up bounds for item rendering. World data NOT given.
   */
  def setBlockBoundsForItemRender() =
  {

  }

  /** Opens the main gui for this tile */
  def openGui(player: EntityPlayer, mod: AnyRef)
  {
    openGui(player, 0, mod)
  }

  /** Opens a gui by the id given */
  def openGui(player: EntityPlayer, gui: Int, mod: AnyRef)
  {
    if (server)
      player.openGui(mod, gui, world, xi, yi, zi)
  }

  /** Is the world client side */
  def client: Boolean = world.isRemote

  /** Is the world server side */
  def server: Boolean = !world.isRemote

  def setMeta(meta: Int)
  { world.setBlockMetadataWithNotify(xi, yi, zi, meta, 3) }

  override def getBlockMetadata: Int =
  {
    if (world == null)
      return 0
    else
      return super.getBlockMetadata
  }

  protected def markRender()
  {
    world.func_147479_m(xi, yi, zi)
  }

  protected def markUpdate()
  {
    world.markBlockForUpdate(xi, yi, zi)
  }

  override def world: World = getWorldObj

  protected def updateLight()
  {
    world.func_147451_t(xi, yi, zi)
  }

  protected def scheduleTick(delay: Int)
  {
    world.scheduleBlockUpdate(xi, yi, zi, block, delay)
  }

  /** gets the way this piston should face for that entity that placed it. */
  def determineOrientation(entityLiving: EntityLivingBase): Byte =
  {
    if (entityLiving != null)
    {
      if (MathHelper.func_154353_e(entityLiving.posX - x) < 2.0F && MathHelper.func_154353_e(entityLiving.posZ - z) < 2.0F)
      {
        val var5: Double = entityLiving.posY + 1.82D - entityLiving.yOffset
        if (var5 - y > 2.0D)
        {
          return 1
        }
        if (y - var5 > 0.0D)
        {
          return 0
        }
      }
      val rotation: Int = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3
      return (if (rotation == 0) 2 else (if (rotation == 1) 5 else (if (rotation == 2) 3 else (if (rotation == 3) 4 else 0)))).asInstanceOf[Byte]
    }
    return 0
  }
}