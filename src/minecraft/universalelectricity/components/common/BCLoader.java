package universalelectricity.components.common;

import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = BasicComponents.CHANNEL, name = BasicComponents.NAME, version = UniversalElectricity.VERSION)
@NetworkMod(channels = BasicComponents.CHANNEL, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class BCLoader
{

	@Instance("BasicComponents")
	public static BCLoader instance;

	@SidedProxy(clientSide = "universalelectricity.components.client.ClientProxy", serverSide = "universalelectricity.components.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		BasicComponents.registerOres(0, true);
		BasicComponents.registerIngots(0, true);
		BasicComponents.registerPlates(0, true);
		BasicComponents.registerBronzeDust(0, true);
		BasicComponents.registerSteelDust(0, true);
		BasicComponents.registerBattery(0);
		BasicComponents.registerWrench(0);
		BasicComponents.registerCopperWire(0);
		BasicComponents.registerMachines(0);
		BasicComponents.registerCircuits(0);
		BasicComponents.registerMotor(0);
		BasicComponents.registerInfiniteBattery(0);
		proxy.preInit();
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		proxy.init();
		BasicComponents.register(this);
	}
}
