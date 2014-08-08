package resonant.content.spatial.block

import _root_.java.lang.reflect.Method
import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.{Item, ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{AxisAlignedBB, IIcon, MovingObjectPosition}
import net.minecraft.world.{IBlockAccess, World}
import resonant.content.prefab.itemblock.ItemBlockTooltip
import resonant.content.wrapper.BlockDummy
import resonant.lib.content.prefab.{TIO, TRotatable}
import resonant.lib.render.RenderUtility
import resonant.lib.utility.{LanguageUtility, WrenchUtility}
import resonant.lib.wrapper.WrapList._
import universalelectricity.core.transform.region.Cuboid
import universalelectricity.core.transform.vector.{Vector2, Vector3, VectorWorld}

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

  abstract trait IComparatorInputOverride
  {
    def getComparatorInputOverride(side: Int): Int
  }
}

abstract class SpatialBlock(val material: Material) extends TileEntity
{
  var name = LanguageUtility.decapitalizeFirst(this.getClass().getSimpleName().replaceFirst("Tile", ""))
  /**
   * The unique string ID of this block.
   */
  var itemBlock: Class[_ <: ItemBlock] = classOf[ItemBlockTooltip]
  var isCreativeTabSet = false
  var block: BlockDummy = null
  var _bounds: Cuboid = Cuboid.full
  var _creativeTab: CreativeTabs = null
  def creativeTab = _creativeTab
  var blockHardness: Float = 1
  var blockResistance: Float = 1
  var stepSound: Block.SoundType = Block.soundTypeStone
  var canProvidePower: Boolean = false
  var tickRandomly: Boolean = false
  var normalRender: Boolean = true
  var forceStandardRender: Boolean = false
  var customItemRender: Boolean = false
  var isOpaqueCube: Boolean = true
  var _access: IBlockAccess = null
  var textureName: String = name
  var domain: String = null

  def setTextureName(value: String)
  {
    textureName = value;
  }

  def creativeTab(value: CreativeTabs)
  {
    creativeTab = value
  }

  def creativeTab_=(value: CreativeTabs): Unit =
  {
    _creativeTab = value
    isCreativeTabSet = true
  }

  def itemBlock(item: Class[_ <: ItemBlock]) : Unit = {itemBlock = item}

  def bounds_=(cuboid: Cuboid)
  {
    _bounds = cuboid

    if (block != null)
      block.setBlockBounds(_bounds.min.xf, _bounds.min.yf, _bounds.min.zf, _bounds.max.xf, _bounds.max.yf, _bounds.max.zf)
  }

  def bounds(cuboid: Cuboid)
  {
    bounds = cuboid
  }

  def bounds = _bounds

  def setBlock(block: BlockDummy)
  {
    this.block = block
  }

  def blockHardness(hardness: Float): Unit = blockHardness = hardness

  def blockResistance(resistance: Float): Unit = blockResistance = resistance

  def stepSound(sound: Block.SoundType): Unit = stepSound = sound

  def canProvidePower(bool: Boolean): Unit = canProvidePower = bool

  def tickRandomly(bool: Boolean): Unit = tickRandomly = bool

  def normalRender(bool: Boolean): Unit = normalRender = bool

  def forceStandardRender(bool: Boolean): Unit = forceStandardRender = bool

  def customItemRender(bool: Boolean): Unit = customItemRender = bool

  def isOpaqueCube(bool: Boolean): Unit = isOpaqueCube = bool

  /**
   * Called after the block is registered. Use this to add recipes.
   */
  def onInstantiate()
  {
  }

  def world: World = worldObj

  def world(world: World)
  {
    this.worldObj = world
  }

  def access: IBlockAccess =
  {
    if (world != null)
    {
      return world
    }

    return _access
  }

  def x: Int =
  {
    //assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return xCoord
  }

  def y: Int =
  {
    //assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return yCoord
  }

  def z: Int =
  {
    //assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return zCoord
  }

  def position: VectorWorld =
  {
    //assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return new VectorWorld(this)
  }

  def center: VectorWorld =
  {
    //assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return position.add(0.5).asInstanceOf[VectorWorld]
  }

  override def getBlockType: Block =
  {
    if (access != null)
    {
      val b: Block = access.getBlock(x, y, z)
      if (b == null)
      {
        return block
      }
      return b
    }
    return block
  }

  /**
   * @return Return "this" if the block requires a TileEntity.
   */
  def tile: SpatialBlock =
  {
    return null
  }

  def metadata: Int = access.getBlockMetadata(x, y, z)

  /**
   * Update
   */
  final override def updateEntity() = update()

  def blockUpdate() = update()

  def update()
  {

  }

  /**
   * Drops
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

  def quantityDropped(meta: Int, fortune: Int): Int =
  {
    return 1
  }

  def metadataDropped(meta: Int, fortune: Int): Int =
  {
    return 0
  }

  def isControlDown(player: EntityPlayer): Boolean =
  {
    try
    {
      val ckm: Class[_] = Class.forName("codechicken.multipart.ControlKeyModifer")
      val m: Method = ckm.getMethod("isControlDown", classOf[EntityPlayer])
      return m.invoke(null, player).asInstanceOf[Boolean]
    }
    catch
      {
        case e: Exception =>
        {
        }
      }
    return false
  }

  def getSubBlocks(item: Item, creativeTabs: CreativeTabs, list: util.List[_])
  {
    list.add(new ItemStack(item, 1, 0))
  }

  def getPickBlock(target: MovingObjectPosition): ItemStack =
  {
    return new ItemStack(getBlockType, 1, metadataDropped(metadata, 0))
  }

  def getLightValue(access: IBlockAccess): Int =
  {
    return block.getLightValue
  }

  /**
   * Block events
   */
  def click(player: EntityPlayer)
  {
  }

  def activate(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem, x, y, z))
    {
      if (configure(player, side, hit))
      {
        WrenchUtility.damageWrench(player, player.inventory.getCurrentItem, x, y, z)
        return true
      }
      return false
    }
    return use(player, side, hit)
  }

  /**
   * Called when the block is clicked by a player
   */
  protected def use(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    return false
  }

  /**
   * Called when the block is clicked with a wrench
   */
  protected def configure(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
  {
    val both = this.isInstanceOf[TIO] && this.isInstanceOf[TRotatable]

    if (both)
    {
      if (!player.isSneaking)
      {
        return this.asInstanceOf[TIO].toggleIO(side, player)
      }
    }
    if (this.isInstanceOf[TRotatable])
      return this.asInstanceOf[TRotatable].rotate(side, hit)
    if (this.isInstanceOf[TIO])
      return this.asInstanceOf[TIO].toggleIO(side, player)

    return false
  }

  /**
   * Block events
   */
  def onAdded
  {
    onWorldJoin
  }

  def onPlaced(entityLiving: EntityLivingBase, itemStack: ItemStack)
  {
    if (this.isInstanceOf[TRotatable])
    {
      this.asInstanceOf[TRotatable].setDirection(this.asInstanceOf[TRotatable].determineRotation(entityLiving))
    }
  }

  def onPostPlaced(metadata: Int)
  {

  }

  def onRemove(block: Block, par6: Int)
  {
    onWorldSeparate
  }

  def onWorldJoin()
  {
  }

  def onWorldSeparate()
  {
  }

  def onNeighborChanged(block: Block)
  {
  }

  def notifyChange()
  {
    world.notifyBlocksOfNeighborChange(x, y, z, block)
  }

  protected def markRender()
  {
    world.func_147479_m(x, y, z)
  }

  protected def markUpdate()
  {
    world.markBlockForUpdate(x, y, z)
  }

  protected def updateLight()
  {
    world.func_147451_t(x, y, z)
  }

  protected def scheduleTick(delay: Int)
  {
    world.scheduleBlockUpdate(x, y, z, block, delay)
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
      if (intersect != null && cuboid.intersects(intersect))
      {
        boxes.add(cuboid)
      }
    }
    return boxes
  }

  def getCollisionBoxes: Iterable[Cuboid] =
  {
    return immutable.List[Cuboid](bounds)
  }

  def getSelectBounds: Cuboid =
  {
    return bounds
  }

  def getCollisionBounds: Cuboid =
  {
    return bounds
  }

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox: AxisAlignedBB = (getCollisionBounds + position).toAABB

  /**
   * Called in the world.
   */
  @SideOnly(Side.CLIENT)
  def getIcon(access: IBlockAccess, side: Int): IIcon =
  {
    return getIcon(side, access.getBlockMetadata(x, y, z))
  }

  /**
   * Called either by an item, or in a world.
   */
  @SideOnly(Side.CLIENT)
  def getIcon(side: Int, meta: Int): IIcon =
  {
    return getIcon
  }

  @SideOnly(Side.CLIENT)
  def getIcon: IIcon =
  {
    return SpatialBlock.icon.get(getTextureName)
  }

  @SideOnly(Side.CLIENT)
  def registerIcons(iconRegister: IIconRegister)
  {
    SpatialBlock.icon.put(getTextureName, iconRegister.registerIcon(getTextureName))
  }

  @SideOnly(Side.CLIENT)
  protected def getTextureName: String = if (textureName == null) "MISSING_ICON_TILE_" + Block.getIdFromBlock(block) + "_" + name else block.dummyTile.domain + textureName

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
    return renderer.renderStandardBlock(block, pos.xi, pos.yi, pos.zi)
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

  }

  @SideOnly(Side.CLIENT)
  def renderInventory(itemStack: ItemStack)
  {
    RenderUtility.renderNormalBlockAsItem(itemStack.getItem().asInstanceOf[ItemBlock].field_150939_a, itemStack.getItemDamage(), RenderUtility.renderBlocks)
  }

  //TODO: Get rid of parameters
  def shouldSideBeRendered(access: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean =
  {
    return if (side == 0 && this.bounds.min.y > 0.0D) true else (if (side == 1 && this.bounds.max.y < 1.0D) true else (if (side == 2 && this.bounds.min.z > 0.0D) true else (if (side == 3 && this.bounds.max.z < 1.0D) true else (if (side == 4 && this.bounds.min.x > 0.0D) true else (if (side == 5 && this.bounds.max.x < 1.0D) true else !access.getBlock(x, y, z).isOpaqueCube)))))
  }

  def onFillRain()
  {
  }

  def isIndirectlyPowered: Boolean = world.isBlockIndirectlyGettingPowered(x, y, z)

  def getStrongestIndirectPower: Int = world.getStrongestIndirectPower(x, y, z)

  def getWeakRedstonePower(access: IBlockAccess, side: Int): Int = getStrongRedstonePower(access, side)

  def getStrongRedstonePower(access: IBlockAccess, side: Int): Int = 0

  def isSolid(access: IBlockAccess, side: Int): Boolean = material.isSolid

  def getRenderBlockPass: Int = 0

  def tickRate(world: World): Int = 20

  def canSilkHarvest(player: EntityPlayer, metadata: Int): Boolean = normalRender && tile == null

  /**
   * Gets the explosive resistance of this block.
   * Note: Called without the world object being present.
   * @param entity - The affecting entity
   * @return A value representing the explosive resistance
   */
  def getExplosionResistance(entity: Entity): Float = blockResistance / 5f

  /**
   * Gets the explosive resistance of this block.
   * @param entity - The affecting entity
   * @param explosionPosition - The position in which the explosion is ocurring at
   * @return A value representing the explosive resistance
   */
  def getExplosionResistance(entity: Entity, explosionPosition: Vector3): Float = getExplosionResistance(entity)

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

  /** Is the world server side */
  def server() : Boolean  = !world.isRemote

  /** Is the world client side */
  def client() : Boolean  = !world.isRemote

  /** Opens the main gui for this tile */
  def openGui(player : EntityPlayer, mod : AnyRef)
  {
    openGui(player, 0, mod)
  }

  /** Opens a gui by the id given */
  def openGui(player : EntityPlayer, gui : Int, mod : AnyRef)
  {
    if(server)
      player.openGui(mod,gui, world, x, y, z)
  }

  def setMeta(meta: Int) { world.setBlockMetadataWithNotify(x, y, z, meta, 3)}
}