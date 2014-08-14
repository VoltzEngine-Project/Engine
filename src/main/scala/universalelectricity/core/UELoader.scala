package universalelectricity.core

import java.util.logging.Logger

import _root_.net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common._
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartedEvent, FMLServerStoppedEvent}
import universalelectricity.api.core.grid.NodeRegistry
import universalelectricity.api.core.grid.electric.{IElectricNode, IEnergyNode}
import universalelectricity.compatibility.Compatibility
import universalelectricity.compatibility.module.{ModuleBuildCraft, ModuleThermalExpansion, ModuleUniversalElectricity}
import universalelectricity.core.grid.UpdateTicker
import universalelectricity.core.grid.node.{NodeElectric, NodeEnergy}

@Mod(modid = UniversalElectricity.id, version = UniversalElectricity.version, name = UniversalElectricity.name, dependencies = "before:ForgeMultipart", modLanguage = "scala")
object UELoader
{
  /** The Universal Electricity configuration file. */
  var config: Configuration = null

  @SidedProxy(clientSide = "universalelectricity.core.ClientProxy", serverSide = "universalelectricity.core.CommonProxy")
  var proxy: CommonProxy = _

  val logger = Logger.getLogger(UniversalElectricity.name)

  @EventHandler
  def preInit(evt: FMLPreInitializationEvent)
  {
    config = new Configuration(evt.getSuggestedConfigurationFile)
    config.load

    UpdateTicker.useThreads = config.get(Configuration.CATEGORY_GENERAL, "Use multithreading", UpdateTicker.useThreads).getBoolean(UpdateTicker.useThreads)

    /**
     * Node registration
     */
    NodeRegistry.register(classOf[IEnergyNode], classOf[NodeEnergy])
    NodeRegistry.register(classOf[IElectricNode], classOf[NodeElectric])

    Compatibility.register(ModuleUniversalElectricity)

    Array[Compatibility.CompatibilityModule](ModuleThermalExpansion, ModuleBuildCraft) foreach (module =>
    {
      module.reciprocal_ratio = config.get("Compatibility", module.moduleName + " Conversion Ratio", module.reciprocal_ratio).getDouble(module.reciprocal_ratio)
      module.ratio = 1d / module.reciprocal_ratio
      module.isEnabled = config.get("Compatibility", "Load " + module.moduleName + " Module", true).getBoolean(true)

      if (module.isEnabled)
      {
        Compatibility.register(module)
      }
    })

    config.save
    proxy.init
  }

  @EventHandler
  def postInit(evt: FMLPostInitializationEvent)
  {
    //TODO: Check this, might be better way!S
    if (UpdateTicker.useThreads && !UpdateTicker.isAlive())
    {
      UpdateTicker.start();
    }
    // TODO: register Thermal Grid
    //UpdateTicker.addNetwork(ResonantEngine.thermalGrid);
  }

  @EventHandler
  def serverStartedEvent(evt: FMLServerStartedEvent)
  {
    println("Started")
  }

  @EventHandler
  def serverStoppedEvent(evt: FMLServerStoppedEvent)
  {
    println("Stopped")
  }
}