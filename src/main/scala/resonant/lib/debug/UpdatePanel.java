package resonant.lib.debug;

import resonant.api.IUpdate;

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
	public void update(double deltaSecs)
	{
		for (Component component : getComponents())
		{
			if (component instanceof IUpdate)
			{
				((IUpdate) component).update(deltaSecs);
			}
		}
	}

	@Override
	public boolean canUpdate()
	{
		return this.getComponents().length > 0;
	}

	@Override
	public boolean continueUpdate()
	{
		return true;
	}
}
