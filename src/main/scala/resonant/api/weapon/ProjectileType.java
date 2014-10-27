package resonant.api.weapon;

/**
 * Used by sentries to tell what type of projectile its using
 * @deprecated - Will no longer be used, and will be replaced at a later date
 * @author DarkGuardsman
 */
@Deprecated
public enum ProjectileType
{
	UNKNOWN, /* NOT A PROJECTILE */
	CONVENTIONAL, /* Classic bullets that do impact damage */
	RAILGUN, /* Ammo that can only be used by railguns */
	MISSILE, /* Ammo used by SAM sites or missile based sentries */
	EXPLOSIVE/* Ammo that a mortar or grenade launcher uses */;
}
