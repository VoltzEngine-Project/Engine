package com.builtbroken.mc.lib.world.schematic;

import com.builtbroken.mc.core.References;

import java.util.HashMap;

/**
 * Central area to register schematics for common use
 * <p/>
 * Created on 10/20/2014.
 *
 * @author Darkguardsman
 */
public final class SchematicRegistry extends HashMap<String, Schematic>
{
	public static SchematicRegistry INSTANCE = new SchematicRegistry();

	/**
	 * Registers a schematic using Schematic.getUnlocalizedName()
	 *
	 * @param schematic - schematic to register
	 */
	public static void register(Schematic schematic)
	{
		register(schematic.getName(), schematic);
	}

	/**
	 * Registers a schematic using a set name.
	 *
	 * @param name      - name to register it with. Will only be used
	 *                  for looking up the schematic. Schematic.getUnlocalizedName will
	 *                  be used for displaying in GUIs
	 * @param schematic - schematic to register
	 */
	public static void register(String name, Schematic schematic)
	{
		if (!INSTANCE.containsKey(name))
		{
			INSTANCE.put(name, schematic);
		}
		else
		{
			References.LOGGER.error("Failed to register schematic as the name '" + name + "' is already in use.\nSchematic: " + schematic
					+ " Class: " + schematic.getClass());
		}
	}

	/**
	 * Gets the schematic by its placement inside the map.
	 * Mainly used by GUIs who return an index number
	 *
	 * @param id - index
	 * @return schematic, may be null if the slot is null or out of bounds
	 */
	public Schematic getByID(int id)
	{
		if (id >= 0 && id < size())
		{
			return values().toArray(new Schematic[size()])[id];
		}
		return null;
	}
}
