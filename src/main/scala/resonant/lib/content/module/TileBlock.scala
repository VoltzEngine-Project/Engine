package resonant.lib.content.module

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.IRotatable
import resonant.lib.prefab.item.ItemBlockTooltip
import resonant.lib.utility.LanguageUtility
import resonant.lib.utility.WrenchUtility
import universalelectricity.core.transform.region.Cuboid
import universalelectricity.core.transform.vector.Vector2
import universalelectricity.core.transform.vector.Vector3
import universalelectricity.core.transform.vector.VectorWorld
import java.lang.reflect.Method
import java.util._
import resonant.lib.content.module.TileBlock.RenderInfo
import java.lang.Byte._
import scala.collection.immutable
import resonant.lib.content.prefab.TraitRotatable

/**
 * All blocks inherit this class.
 * <p/>
 * Note that a lot of the variables will not exist except on the primary instance of the TileBlock,
 * hosted in BlockDummy.
 *
 * @author Calclavia
 */
object TileBlock
{
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

  /**
   * Rendering
   */
  @SideOnly(Side.CLIENT) object RenderInfo
  {
    @SideOnly(Side.CLIENT) final val renderer: WeakHashMap[TileBlock, TileRender] = new WeakHashMap[TileBlock, TileRender]
    @SideOnly(Side.CLIENT) final val icon: HashMap[String, IIcon] = new HashMap[String, IIcon]
  }

}

abstract class TileBlock(val name: String, val material: Material) extends TileEntity
{
  /**
   * The unique string ID of this block.
   */
  var itemBlock: Class[_ <: ItemBlock] = classOf[ItemBlockTooltip]
  var creativeTab: CreativeTabs = null
  var bounds: Cuboid = Cuboid.full
  var block: BlockDummy = null
  var blockHardness: Float = 1
  var blockResistance: Float = 1
  var canProvidePower: Boolean = false
  var tickRandomly: Boolean = false
  var normalRender: Boolean = true
  var forceStandardRender: Boolean = false
  var customItemRender: Boolean = false
  var isOpaqueCube: Boolean = true
  /**
   * Temporary
   */
  var _access: IBlockAccess = null
  /**
   * Rendering
   */
  var textureName: String = name
  var domain: String = null
  /**
   * Rotation
   */
  protected var rotationMask: Byte = parseByte("111100", 2)
  protected var isFlipPlacement: Boolean = false

  def this(newMaterial: Material) = this(LanguageUtility.decapitalizeFirst(getClass.getSimpleName.replaceFirst("Tile", "")), newMaterial)


  /**
   * Called after the block is registred. Use this to add recipes.
   */
  def onInstantiate
  {
  }

  def world: World =
  {
    return worldObj
  }

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
    assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return xCoord
  }

  def y: Int =
  {
    assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return yCoord
  }

  def z: Int =
  {
    assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return zCoord
  }

  def position: VectorWorld =
  {
    assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
    return new VectorWorld(this)
  }

  protected def center: VectorWorld =
  {
    assert(world != null, "TileBlock [" + getClass.getSimpleName + "] attempted to access invalid method.")
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
  def tile: TileBlock =
  {
    return null
  }

  def metadata: Int =
  {
    return access.getBlockMetadata(x, y, z)
  }

  /**
   * Drops
   */
  def getDrops(metadata: Int, fortune: Int): ArrayList[ItemStack] =
  {
    val drops: ArrayList[ItemStack] = new ArrayList[ItemStack]
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

  def getSubBlocks(item: Item, creativeTabs: CreativeTabs, list: List[_])
  {
    def add[T](list: java.util.List[T], value: Any) = list.add(value.asInstanceOf[T])
    add(list, new ItemStack(item))
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
    if (this.isInstanceOf[TraitRotatable])
      return this.asInstanceOf[TraitRotatable].rotate(side, hit)

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
    if (this.isInstanceOf[TraitRotatable])
    {
      this.asInstanceOf[TraitRotatable].setDirection(this.asInstanceOf[TraitRotatable].determineRotation(entityLiving))
    }
  }

  def onRemove(block: Block, par6: Int)
  {
    onWorldSeparate
  }

  def onWorldJoin
  {
  }

  def onWorldSeparate
  {
  }

  def onNeighborChanged(block: Block)
  {
  }

  def notifyChange
  {
    world.notifyBlocksOfNeighborChange(x, y, z, block)
  }

  protected def markRender
  {
    world.func_147479_m(x, y, z)
  }

  protected def markUpdate
  {
    world.markBlockForUpdate(x, y, z)
  }

  protected def updateLight
  {
    world.func_147451_t(x, y, z)
  }

  protected def scheduelTick(delay: Int)
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
    val boxes: List[Cuboid] = new ArrayList[Cuboid]
    import scala.collection.JavaConversions._
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

  @SideOnly(Side.CLIENT) final def getRenderer: TileRender =
  {
    if (!RenderInfo.renderer.containsKey(this))
    {
      RenderInfo.renderer.put(this, newRenderer)
    }
    return RenderInfo.renderer.get(this)
  }

  @SideOnly(Side.CLIENT) protected def newRenderer: TileRender =
  {
    return null
  }

  /**
   * Called in the world.
   */
  @SideOnly(Side.CLIENT) def getIcon(access: IBlockAccess, side: Int): IIcon =
  {
    return getIcon(side, access.getBlockMetadata(x, y, z))
  }

  /**
   * Called either by an item, or in a world.
   */
  @SideOnly(Side.CLIENT) def getIcon(side: Int, meta: Int): IIcon =
  {
    return getIcon
  }

  def getIcon: IIcon =
  {
    return RenderInfo.icon.get(getTextureName)
  }

  @SideOnly(Side.CLIENT) def registerIcons(iconRegister: IIconRegister)
  {
    RenderInfo.icon.put(getTextureName, iconRegister.registerIcon(getTextureName))
  }

  @SideOnly(Side.CLIENT) protected def getTextureName: String =
  {
    return if (textureName == null) "MISSING_ICON_TILE_" + Block.getIdFromBlock(block) + "_" + name else block.dummyTile.domain + textureName
  }

  def shouldSideBeRendered(access: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean =
  {
    return if (side == 0 && this.bounds.min.y > 0.0D) true else (if (side == 1 && this.bounds.max.y < 1.0D) true else (if (side == 2 && this.bounds.min.z > 0.0D) true else (if (side == 3 && this.bounds.max.z < 1.0D) true else (if (side == 4 && this.bounds.min.x > 0.0D) true else (if (side == 5 && this.bounds.max.x < 1.0D) true else !access.getBlock(x, y, z).isOpaqueCube)))))
  }

  def onFillRain
  {
  }

  def isIndirectlyPowered: Boolean =
  {
    return world.isBlockIndirectlyGettingPowered(x, y, z)
  }

  def getStrongestIndirectPower: Int =
  {
    return world.getStrongestIndirectPower(x, y, z)
  }

  def getWeakRedstonePower(access: IBlockAccess, side: Int): Int =
  {
    return getStrongRedstonePower(access, side)
  }

  def getStrongRedstonePower(access: IBlockAccess, side: Int): Int =
  {
    return 0
  }

  def isSolid(access: IBlockAccess, side: Int): Boolean =
  {
    return material.isSolid
  }

  def getRenderBlockPass: Int =
  {
    return 0
  }

  def tickRate(world: World): Int =
  {
    return 20
  }

}