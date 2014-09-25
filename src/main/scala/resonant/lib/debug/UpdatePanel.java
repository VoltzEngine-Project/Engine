package resonant.lib.debug;

import universalelectricity.api.core.grid.IUpdate;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

/** Version of JPanel that can be triggered to update its components
 * @author Darkguardsman */
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
