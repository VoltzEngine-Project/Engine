package universalelectricity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.Configuration;
import universalelectricity.electricity.ElectricityManager;

public class UniversalElectricity
{
    public static final Configuration CONFIGURATION = new Configuration(new File("config/UniversalElectricity/UniversalElectricity.cfg"));
    public static final List<Object> MODS = new ArrayList<Object>();
    public static final String VERSION = "0.6.1";
    
    public static final ElectricityManager electricityManager = new ElectricityManager();
    
    public static void registerMod(Object networkmod, String modName, String version)
    {
        String[] versionNumbers = VERSION.split("\\.");
        String[] addonVersionNumbers = version.split("\\.");

        if (Integer.parseInt(addonVersionNumbers[0]) != Integer.parseInt(versionNumbers[0]))
        {
            throw new RuntimeException("Universal Electricity mod major version mismatch, expecting " + version);
        }
        else if (Integer.parseInt(addonVersionNumbers[1]) > Integer.parseInt(versionNumbers[1]))
        {
        	throw new RuntimeException("Universal Electricity too old, need at least " + version);
        }
        else if (Integer.parseInt(addonVersionNumbers[1]) < Integer.parseInt(versionNumbers[1]))
        {
        	throw new RuntimeException("Universal Electricity mod minor version mismatch, need at least " + version);
        }
        else if (Integer.parseInt(addonVersionNumbers[2]) != Integer.parseInt(versionNumbers[2]))
        {
            System.out.println("UE mod minor version " + version + " mismatch with version " + version);
        }

        MODS.add(networkmod);
        System.out.println("Loaded Universal Electricity Mod: " + modName);
    }

    /*------------------ FUNCTIONS ----------------------
    	Some formulas to note:
    *   Wattage = Voltage x Amps (W = V X I)
    *   Voltage = Amperage x Resistance (V = I x R)
    */
    /**
     * Returns the amount of amps.
     * @param watts
     * @param volts
     * @return The amount of amps
     */
    public static double getAmps(double watts, int volts)
    {
        return (double)watts / (double)volts;
    }

    /**
     * Return a string with the amount of amps for displaying.
     * @param amps
     * @return The string for displaying amps
     */
    public static String getAmpDisplay(double amps)
    {
        String displayAmps;

        if (amps < 1.0)
        {
            displayAmps = (int)(amps * 1000) + " mA";
        }
        else if (amps > 1000)
        {
            displayAmps = roundOneDecimal(amps / 1000) + " KA";
        }
        else
        {
            displayAmps = roundTwoDecimals(amps) + " A";
        }

        return displayAmps;
    }

    public static String getAmpDisplayFull(double amps)
    {
        String displayAmps;

        if (amps < 1.0)
        {
            displayAmps = (int)(amps * 1000) + " Milliamps";
        }
        else if (amps > 1000)
        {
            displayAmps = roundOneDecimal(amps / 1000) + " Kiloamps";
        }
        else
        {
            displayAmps = roundTwoDecimals(amps) + " Amps";
        }

        return displayAmps;
    }

    /**
     * Return a string with the amount of volts for displaying.
     * @param volts
     * @return The string for displaying volts
     */
    public static String getVoltDisplay(int volts)
    {
        String displayVolt;

        if (volts > 1000000)
        {
            displayVolt = roundOneDecimal(volts / 1000000) + " MV";
        }

        if (volts > 1000)
        {
            displayVolt = roundOneDecimal(volts / 1000) + " KV";
        }
        else
        {
            displayVolt = volts + " V";
        }

        return displayVolt;
    }

    public static String getVoltDisplayFull(int volts)
    {
        String displayVolt;

        if (volts > 1000000)
        {
            displayVolt = roundOneDecimal(volts / 1000000) + " Megavolts";
        }

        if (volts > 1000)
        {
            displayVolt = roundOneDecimal(volts / 1000) + " Kilovolts";
        }
        else if (volts == 1)
        {
            displayVolt = volts + " volt";
        }
        else
        {
            displayVolt = volts + " volts";
        }

        return displayVolt;
    }

    /**
     * Return a string with the amount of amp-hour for displaying.
     * @param amp-hours
     * @return The string for displaying amp-hours
     */
    public static String getAmpHourDisplay(float ampHours)
    {
        String displayAmpHours;
        
        if (ampHours > 1000)
        {
            displayAmpHours = roundTwoDecimals(ampHours / 1000) + " kAh";
        }
        else if (ampHours > 0)
        {
            displayAmpHours = (int)ampHours + " Ah";
        }
        else
        {
            displayAmpHours = roundOneDecimal(ampHours * 1000) + " mAh";
        }

        return displayAmpHours;
    }
    
    /**
     * Gets the amp hours based on the watts and the voltage per second
     * @return
     */
    public static String getAmpHourDisplay(float watts, float voltage)
    {    			
    	return getAmpHourDisplay((watts/voltage)*3600);
    }

    public static String getAmpHourDisplayFull(double ampHours)
    {
    	String displayAmpHours;
        
        if (ampHours > 1000)
        {
            displayAmpHours = roundTwoDecimals(ampHours / 1000) + " kiloamp-hours";
        }
        else if (ampHours > 0)
        {
            displayAmpHours = (int)ampHours + " amp-hours";
        }
        else
        {
            displayAmpHours = roundOneDecimal(ampHours * 1000) + " milliamp-hours";
        }

        return displayAmpHours;
    }
    
    public static String getAmpHourDisplayFull(float watts, float voltage)
    {    			
    	return getAmpHourDisplayFull((watts/voltage)*3600);
    }
    
    /**
     * Return a string with the amount of watts for displaying.
     * @param watts
     * @return The string for displaying watts
     */
    public static String getWattDisplay(double watts)
    {
        String displayWatt;

        if (watts > 1000000)
        {
            displayWatt = roundOneDecimal(watts / 1000000) + " MW";
        }

        if (watts > 1000)
        {
            displayWatt = roundOneDecimal(watts / 1000) + " KW";
        }
        else
        {
            displayWatt = (int)watts + " W";
        }

        return displayWatt;
    }

    public static String getWattDisplayFull(double watts)
    {
        String displayWatt;

        if (watts > 1000000)
        {
            displayWatt = roundOneDecimal(watts / 1000000) + " Megawatts";
        }

        if (watts > 1000)
        {
            displayWatt = roundOneDecimal(watts / 1000) + " Kilowatts";
        }
        else if (watts == 1)
        {
            displayWatt = (int)watts + " Watt";
        }
        else
        {
            displayWatt = (int)watts + " Watts";
        }

        return displayWatt;
    }

    /**
     * Rounds a number to two decimal places
     * @param The number
     * @return The rounded number
     */
    public static double roundTwoDecimals(double d)
    {
        int j = (int)(d * 100);
        return j / 100.0;
    }

    /**
     * Rounds a number to one decimal place
     * @param The number
     * @return The rounded number
     */
    public static double roundOneDecimal(double d)
    {
        int j = (int)(d * 10);
        return j / 10.0;
    }

    /**
     * Gets the ID of a block from the configuration file
     * @param name - The name of the block or item
     * @param defaultID - The default ID of the block or item. Any errors will restore this block/item ID
     * @return The block or item ID
     */
    public static int getBlockConfigID(Configuration configuration, String name, int defaultID)
    {
        configuration.load();
        int id = defaultID;

        id = Integer.parseInt(configuration.getOrCreateIntProperty(name, Configuration.CATEGORY_BLOCK, defaultID).value);

        if (id <= 136)
        {
            return defaultID;
        }
          
        configuration.save();
        return id;
    }
    
    public static int getItemConfigID(Configuration configuration, String name, int defaultID)
    {
        configuration.load();
        int id = defaultID;

        id = Integer.parseInt(configuration.getOrCreateIntProperty(name, Configuration.CATEGORY_ITEM, defaultID).value);

        if (id < 256)
        {
            return defaultID;
        }

        configuration.save();
        return id;
    }
}
