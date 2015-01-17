package com.builtbroken.mc.testing.debug;

import com.builtbroken.mc.api.IUpdate;

import javax.swing.*;
import java.awt.*;

/**
 * Version of JPanel that can be triggered to update its components
 *
 * @author Darkguardsman
 */
@SuppressWarnings("serial")
public class UpdatePanel extends JPanel implements IUpdate
{
	public UpdatePanel()
	{
	}

	public UpdatePanel(BorderLayout borderLayout)
	{
		super(borderLayout);
	}

	@Override
	public boolean update()
	{
		for (Component component : getComponents())
		{
			if (component instanceof IUpdate)
			{
				((IUpdate) component).update();
			}
		}
        return true;
	}
}
