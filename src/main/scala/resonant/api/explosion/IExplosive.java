package resonant.api.explosion;

import net.minecraft.world.World;

/**
 * An interface used to find various types of explosive's information.
 *
 * @author Calclavia
 */
public interface IExplosive
{
	/**
	 * @return The unique name key in the ICBM language file.
	 */
	public String getUnlocalizedName();

	/**
	 * Creates a new explosion at a given location.
	 *
	 * @param world  The world in which the explosion takes place.
	 * @param x      The X-Coord
	 * @param y      The Y-Coord
	 * @param z      The Z-Coord
	 * @param entity Entity that caused the explosion.
	 */
	public void createExplosion(World world, double x, double y, double z, Trigger trigger);

}
