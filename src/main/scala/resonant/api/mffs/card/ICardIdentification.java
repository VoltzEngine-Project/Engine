package resonant.api.mffs.card;

import net.minecraft.item.ItemStack;
import resonant.lib.access.Permission;

/** Applied to Item ID cards.
 * 
 * @author Calclavia */
public interface ICardIdentification extends ICard
{
    public boolean hasPermission(ItemStack itemStack, Permission permission);

    public boolean addPermission(ItemStack itemStack, Permission permission);

    public boolean removePermission(ItemStack itemStack, Permission permission);

    public String getUsername(ItemStack itemStack);

    public void setUsername(ItemStack itemStack, String username);
}
