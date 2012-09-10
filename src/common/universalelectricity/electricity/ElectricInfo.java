package universalelectricity.electricity;

import net.minecraft.src.MathHelper;

/**
 * A better way to storage information on electricity
 * @author Calclavia
 */

public class ElectricInfo
{
	public static enum Unit
	{
		AMPERE, VOLTAGE, WATTAGE, WATT_HOUR
	}
	
	public float ampere;
	
	public float voltage;
	
	public ElectricInfo(float ampere, float voltage)
	{
		this.ampere = ampere;
		this.voltage = voltage;
	}
	
	public static ElectricInfo getFromWatts(float watts, float voltage)
	{
		return new ElectricInfo(watts/voltage, voltage);
	}
	
	public static ElectricInfo getFromWattHours(float wattsHours, float voltage)
	{
		return getFromWatts(wattsHours/3600, voltage);
	}
	
	public ElectricInfo copy()
	{
		return new ElectricInfo(this.ampere, this.voltage);
	}
	
	/**
	 * Amps
	 */
	public float getKiloAmps()
	{
		return this.ampere/1000;
	}
	
	public float getMegaAmps()
	{
		return this.getKiloAmps()/1000;
	}
	
	/**
	 * Amps
	 * @return
	 */
	public float getKiloVolts()
	{
		return this.voltage/1000;
	}
	
	public float getMegaVolts()
	{
		return this.getKiloVolts()/1000;
	}
	
	/**
	 * Watts
	 */
	public float getWatts()
	{
		return this.ampere * this.voltage;
	}
	
	public float getKiloWatts()
	{
		return this.getWatts()/1000;
	}
	
	public float getMegaWatts()
	{
		return this.getKiloWatts()/1000;
	}
	
	/**
	 * Watt-Hours
	 */
	public float getWattHours()
	{
		return this.getWatts()/3600;
	}
	
	public float getKiloWattHours()
	{
		return this.getWattHours()/1000;
	}
	
	public float getMegaWattHours()
	{
		return this.getKiloWattHours()/1000;
	}
	
	/**
	 * Resistance in Ohms, R
	 */
	public float getResistance()
	{
		return this.voltage/this.ampere;
	}
	
	/**
	 * Conductance measured in Siemens, G
	 */
	public float getConductance()
	{
		return this.ampere/this.voltage;
	}

	/**
	 * FUNCTIONS FOR DISPLAYING!
	 */
	
	
	public String getAmpDisplay()
	{	    
	    if(this.ampere < 1.0)
	    {
	    	return roundOneDecimal(this.ampere*1000) + " mA";
	    }
	    else if (this.ampere > 1000000)
	    {
	    	return roundOneDecimal(this.getKiloAmps()) + " MA";
	    }
	    else if (this.ampere > 1000)
	    {
	    	return roundOneDecimal(this.getKiloAmps()) + " KA";
	    }
	    else
	    {
	    	return roundOneDecimal(this.ampere) + " A";
	    }
	}
	

	public String getAmpDisplayFull()
	{	    
	    if(this.ampere < 1.0)
	    {
	    	return roundTwoDecimals(this.ampere*1000) + " Milliamps";
	    }
	    else if (this.ampere > 1000000)
	    {
	    	return roundTwoDecimals(this.getKiloAmps()) + " Megaamps";
	    }
	    else if (this.ampere > 1000)
	    {
	    	return roundTwoDecimals(this.getKiloAmps()) + " Kiloamps";
	    }
	    else
	    {
	    	return roundTwoDecimals(this.ampere) + " Amps";
	    }
	}
	
	
	public String getVoltDisplay()
	{	
	    if (this.voltage > 1000000)
	    {
	    	return roundOneDecimal(this.getMegaVolts()) + " MV";
	    }
	
	    if (this.voltage > 1000)
	    {
	    	return roundOneDecimal(this.getKiloVolts()) + " KV";
	    }
	    else
	    {
	        return roundOneDecimal(this.voltage) + " V";
	    }
	}
	
	public String getVoltDisplayFull()
	{	
	    if (this.voltage > 1000000)
	    {
	    	return roundTwoDecimals(this.getMegaVolts()) + " Megavolts";
	    }
	
	    if (this.voltage > 1000)
	    {
	    	return roundTwoDecimals(this.getKiloVolts()) + " Kilovolts";
	    }
	    else
	    {
	        return roundTwoDecimals(this.voltage) + " volts";
	    }
	}
	
	public String getWattHourDisplay()
	{	    
		if (this.getWattHours() > 1000000)
	    {
	        return roundOneDecimal(this.getMegaWattHours()) + " mWh";
	    }
		else if (this.getWattHours() > 1000)
	    {
	        return roundOneDecimal(this.getKiloWattHours()) + " kWh";
	    }
	    else
	    {
	    	return roundOneDecimal(this.getKiloWattHours()) + " Wh";
	    }
	}
	
	public String getWattHourDisplayFull()
	{	    
		if (this.getWattHours() > 1000000)
	    {
	        return roundTwoDecimals(this.getMegaWattHours()) + " Megawatt Hours";
	    }
		else if (this.getWattHours() > 1000)
	    {
	        return roundTwoDecimals(this.getKiloWattHours()) + " Kilowatt Hours";
	    }
	    else
	    {
	    	return roundTwoDecimals(this.getKiloWattHours()) + " Watt Hours";
	    }
	}
	
	public String getWattDisplay()
	{	
	    if (this.getWatts() > 1000000)
	    {
	    	return roundOneDecimal(this.getMegaWatts()) + " MW";
	    }
	
	    if (this.getWatts() > 1000)
	    {
	    	return roundOneDecimal(this.getKiloWatts()) + " KW";
	    }
	    else
	    {
	    	return roundOneDecimal(this.getWatts()) + " W";
	    }
	}
	
	public String getWattDisplayFull()
	{	
	    if (this.getWatts() > 1000000)
	    {
	    	return roundOneDecimal(this.getMegaWatts()) + " Megawatts";
	    }
	
	    if (this.getWatts() > 1000)
	    {
	    	return roundOneDecimal(this.getKiloWatts()) + " Kilowatts";
	    }
	    else
	    {
	    	return roundOneDecimal(this.getWatts()) + " Watts";
	    }
	}
	
	/**
	 * STATIC VERSIONS
	 */
	
	public static float getAmps(float watts, float volts)
	{
	    return watts/volts;
	}
	
	public static float getWatts(float amps, float volts)
	{
	    return amps*volts;
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
	public static String getWattHourDisplay(float wattHours)
	{	    
	    if (wattHours > 1000)
	    {
	        return roundTwoDecimals(wattHours / 1000) + " kAh";
	    }
	    else if (wattHours > 1.0)
	    {
	    	return (int)wattHours + " Ah";
	    }
	    else
	    {
	    	return roundOneDecimal(wattHours * 1000) + " mAh";
	    }
	}
	
	public static String getWattHourDisplayFull(float wattHours)
	{	    
	    if (wattHours > 1000)
	    {
	    	return roundTwoDecimals(wattHours / 1000) + " Kilowatt Hours";
	    }
	    else if (wattHours > 1.0)
	    {
	    	return (int)wattHours + " Watt Hours";
	    }
	    else
	    {
	        return roundOneDecimal(wattHours * 1000) + " Milliwatt Hours";
	    }
	}
	
	/**
	 * Return a string with the amount of watts for displaying.
	 * @param watts
	 * @return The string for displaying watts
	 */
	public static String getWattDisplay(float watts)
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
	
	public static String getWattDisplayFull(float watts)
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
}
