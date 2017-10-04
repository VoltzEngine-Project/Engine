package com.builtbroken.mc.framework.block.imp;

/**
 * Applied to objects that listen for block action events
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/3/2017.
 */
public interface IToolListener extends ITileEventListener
{
    /**
     * Called to get the tool that can harvest a block
     * <p>
     * WARNING: This is a non-location based method
     *
     * @param metadata - meta of the block
     * @return name of the tool type
     */
    default String getBlockHarvestTool(int metadata)
    {
        return "";
    }

    /**
     * Called to get the tool that can harvest the tile
     * <p>
     * This method does support location but make sure to handle
     * non-local based called to handle mod compatibility. As well
     * correctly update break speed as the player and other methods
     * in MC/Forge use the non-location based method. This can not
     * be corrected in all cases. So it is based to broad range
     * break-ability if possible.
     *
     * @param metadata - meta of the block
     * @return name of the tool type
     */
    default String getHarvestTool(int metadata)
    {
        return getBlockHarvestTool(metadata);
    }

    /**
     * Called to get the harvest level of the block
     * <p>
     * WARNING: This is a non-location based method
     *
     * @param metadata - meta of the block
     * @return harvest level
     */
    default int getBlockHarvestLevel(int metadata)
    {
        return -1;
    }

    /**
     * Called to get the harvest level of the tile
     * <p>
     * This method does support location but make sure to handle
     * non-local based called to handle mod compatibility. As well
     * correctly update break speed as the player and other methods
     * in MC/Forge use the non-location based method. This can not
     * be corrected in all cases. So it is based to broad range
     * break-ability if possible.
     *
     * @param metadata - meta of the block
     * @return harvest level
     */
    default int getHarvestLevel(int metadata)
    {
        return getBlockHarvestLevel(metadata);
    }

    @Override
    default String getListenerKey()
    {
        return "tool";
    }
}
