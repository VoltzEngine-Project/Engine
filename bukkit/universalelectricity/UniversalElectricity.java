/*
 * 
 */
package universalelectricity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import universalelectricity.electricity.IElectricUnit;
import universalelectricity.extend.TileEntityConductor;
import forge.Configuration;
import forge.MinecraftForge;
import forge.NetworkMod;


/**
 * The Class UniversalElectricity.
 */
public class UniversalElectricity
{
    
    /** The Constant configuration. */
    public static final Configuration configuration = new Configuration(new File("config/UniversalElectricity/UniversalElectricity.cfg"));
    
    /** The Constant addons. */
    public static final List<NetworkMod> addons = new ArrayList<NetworkMod>();

    /**
     * Instantiates a new universal electricity.
     */
    public UniversalElectricity()
    {
    }

    /**
     * Register addon.
     *
     * @param networkmod the networkmod
     * @param s the s
     */
    public static void registerAddon(NetworkMod networkmod, String s)
    {
        String as[] = getVersion().split("\\.");
        String as1[] = s.split("\\.");

        if (Integer.parseInt(as1[0]) != Integer.parseInt(as[0]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", (new StringBuilder()).append("Add-on major version mismatch, expecting ").append(getVersion()).toString());
        }
        else if (Integer.parseInt(as1[1]) > Integer.parseInt(as[1]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", (new StringBuilder()).append("Universal Electricity too old, need at least ").append(s).toString());
        }
        else if (Integer.parseInt(as1[1]) < Integer.parseInt(as[1]))
        {
            MinecraftForge.killMinecraft("Universal Electricity", (new StringBuilder()).append("Add-on minor version mismatch, need at least ").append(getVersion()).toString());
        }
        else if (Integer.parseInt(as1[2]) != Integer.parseInt(as[2]))
        {
            System.out.println((new StringBuilder()).append("Universal Electricity add-on minor version ").append(s).append(" mismatch with version ").append(getVersion()).toString());
        }

        addons.add(networkmod);
        System.out.println((new StringBuilder()).append("Loaded Universal Add-On: ").append(networkmod.getName()).toString());
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public static String getVersion()
    {
        return "0.4.2";
    }

    /**
     * Gets the amps.
     *
     * @param d the d
     * @param i the i
     * @return the amps
     */
    public static double getAmps(double d, int i)
    {
        return d / (double)i;
    }

    /**
     * Gets the amp display.
     *
     * @param d the d
     * @return the amp display
     */
    public static String getAmpDisplay(double d)
    {
        String s;

        if (d < 1.0D)
        {
            s = (new StringBuilder()).append((int)(d * 1000D)).append(" mA").toString();
        }
        else if (d > 1000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000D)).append(" KA").toString();
        }
        else
        {
            s = (new StringBuilder()).append(roundTwoDecimals(d)).append(" A").toString();
        }

        return s;
    }

    /**
     * Gets the amp display full.
     *
     * @param d the d
     * @return the amp display full
     */
    public static String getAmpDisplayFull(double d)
    {
        String s;

        if (d < 1.0D)
        {
            s = (new StringBuilder()).append((int)(d * 1000D)).append(" Milliamps").toString();
        }
        else if (d > 1000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000D)).append(" Kiloamps").toString();
        }
        else
        {
            s = (new StringBuilder()).append(roundTwoDecimals(d)).append(" Amps").toString();
        }

        return s;
    }

    /**
     * Gets the volt display.
     *
     * @param i the i
     * @return the volt display
     */
    public static String getVoltDisplay(int i)
    {
        String s;

        if (i > 0xf4240)
        {
            s = (new StringBuilder()).append(roundOneDecimal(i / 0xf4240)).append(" MV").toString();
        }

        if (i > 1000)
        {
            s = (new StringBuilder()).append(roundOneDecimal(i / 1000)).append(" KV").toString();
        }
        else
        {
            s = (new StringBuilder()).append(i).append(" V").toString();
        }

        return s;
    }

    /**
     * Gets the volt display full.
     *
     * @param i the i
     * @return the volt display full
     */
    public static String getVoltDisplayFull(int i)
    {
        String s;

        if (i > 0xf4240)
        {
            s = (new StringBuilder()).append(roundOneDecimal(i / 0xf4240)).append(" Megavolts").toString();
        }

        if (i > 1000)
        {
            s = (new StringBuilder()).append(roundOneDecimal(i / 1000)).append(" Kilovolts").toString();
        }
        else if (i == 1)
        {
            s = (new StringBuilder()).append(i).append(" volt").toString();
        }
        else
        {
            s = (new StringBuilder()).append(i).append(" volts").toString();
        }

        return s;
    }

    /**
     * Gets the watt display.
     *
     * @param d the d
     * @return the watt display
     */
    public static String getWattDisplay(double d)
    {
        String s;

        if (d > 1000000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000000D)).append(" MW").toString();
        }

        if (d > 1000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000D)).append(" KW").toString();
        }
        else
        {
            s = (new StringBuilder()).append((int)d).append(" W").toString();
        }

        return s;
    }

    /**
     * Gets the watt display full.
     *
     * @param d the d
     * @return the watt display full
     */
    public static String getWattDisplayFull(double d)
    {
        String s;

        if (d > 1000000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000000D)).append(" Megawatts").toString();
        }

        if (d > 1000D)
        {
            s = (new StringBuilder()).append(roundOneDecimal(d / 1000D)).append(" Kilowatts").toString();
        }
        else if (d == 1.0D)
        {
            s = (new StringBuilder()).append((int)d).append(" Watt").toString();
        }
        else
        {
            s = (new StringBuilder()).append((int)d).append(" Watts").toString();
        }

        return s;
    }

    /**
     * Round two decimals.
     *
     * @param d the d
     * @return the double
     */
    public static double roundTwoDecimals(double d)
    {
        int i = (int)(d * 100D);
        return (double)i / 100D;
    }

    /**
     * Round one decimal.
     *
     * @param d the d
     * @return the double
     */
    public static double roundOneDecimal(double d)
    {
        int i = (int)(d * 10D);
        return (double)i / 10D;
    }

    /**
     * Gets the orientation from side.
     *
     * @param byte0 the byte0
     * @param byte1 the byte1
     * @return the orientation from side
     */
    public static byte getOrientationFromSide(byte byte0, byte byte1)
    {
        switch (byte0)
        {
            case 0:
                switch (byte1)
                {
                    case 0:
                        return 3;

                    case 1:
                        return 4;

                    case 2:
                        return 1;

                    case 3:
                        return 0;

                    case 4:
                        return 4;

                    case 5:
                        return 5;
                }

            case 1:
                switch (byte1)
                {
                    case 0:
                        return 4;

                    case 1:
                        return 3;

                    case 2:
                        return 0;

                    case 3:
                        return 1;

                    case 4:
                        return 4;

                    case 5:
                        return 5;
                }

            case 2:
                switch (byte1)
                {
                    case 0:
                        return 0;

                    case 1:
                        return 1;

                    case 2:
                        return 3;

                    case 3:
                        return 2;

                    case 4:
                        return 5;

                    case 5:
                        return 4;
                }

            case 3:
                return byte1;

            case 4:
                switch (byte1)
                {
                    case 0:
                        return 0;

                    case 1:
                        return 1;

                    case 2:
                        return 5;

                    case 3:
                        return 4;

                    case 4:
                        return 3;

                    case 5:
                        return 2;
                }

            case 5:
                switch (byte1)
                {
                    case 0:
                        return 0;

                    case 1:
                        return 1;

                    case 2:
                        return 4;

                    case 3:
                        return 5;

                    case 4:
                        return 2;

                    case 5:
                        return 3;
                }

            default:
                return -1;
        }
    }

    /**
     * Gets the uE unit from side.
     *
     * @param world the world
     * @param vector3 the vector3
     * @param byte0 the byte0
     * @return the uE unit from side
     */
    public static TileEntity getUEUnitFromSide(World world, Vector3 vector3, byte byte0)
    {
        vector3 = getPositionFromSide(vector3, byte0);
        TileEntity tileentity = world.getTileEntity(vector3.intX(), vector3.intY(), vector3.intZ());

        if (tileentity instanceof TileEntityConductor)
        {
            return tileentity;
        }

        if ((tileentity instanceof IElectricUnit) && ((IElectricUnit)tileentity).canConnect(getOrientationFromSide(byte0, (byte)2)))
        {
            return tileentity;
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets the position from side.
     *
     * @param vector3 the vector3
     * @param byte0 the byte0
     * @return the position from side
     */
    public static Vector3 getPositionFromSide(Vector3 vector3, byte byte0)
    {
        switch (byte0)
        {
            case 0:
                vector3.y--;
                break;

            case 1:
                vector3.y++;
                break;

            case 2:
                vector3.z++;
                break;

            case 3:
                vector3.z--;
                break;

            case 4:
                vector3.x++;
                break;

            case 5:
                vector3.x--;
                break;
        }

        return vector3;
    }

    /**
     * Gets the config id.
     *
     * @param configuration1 the configuration1
     * @param s the s
     * @param i the i
     * @param flag the flag
     * @return the config id
     */
    public static int getConfigID(Configuration configuration1, String s, int i, boolean flag)
    {
        configuration1.load();
        int j = i;

        if (flag)
        {
            j = Integer.parseInt(configuration1.getOrCreateIntProperty(s, "block", i).value);

            if (j < 100)
            {
                return i;
            }
        }
        else
        {
            j = Integer.parseInt(configuration1.getOrCreateIntProperty(s, "item", i).value);

            if (j < 256)
            {
                return i;
            }
        }

        configuration1.save();
        return j;
    }
}
