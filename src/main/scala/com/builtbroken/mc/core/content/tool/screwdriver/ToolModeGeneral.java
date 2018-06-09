package com.builtbroken.mc.core.content.tool.screwdriver;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.api.tile.connection.ConnectionColor;

public class ToolModeGeneral extends ToolMode
{
	@Override
	public String getInfoName()
	{
		return Engine.itemWrench.getUnlocalizedName() + ".mode.general.info";
	}

    @Override
    public String getItemUnlocalizedName(ConnectionColor color)
    {
        return Engine.itemWrench.getUnlocalizedName();
    }
}
