package com.builtbroken.mc.debug.gui.windows;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.item.RenderStateItem;
import com.builtbroken.mc.client.json.render.state.ModelState;
import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.debug.data.DebugData;
import com.builtbroken.mc.debug.data.DebugJsonData;
import com.builtbroken.mc.debug.data.IJsonDebugData;
import com.builtbroken.mc.debug.gui.panels.imp.PanelDataList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class FrameRenderData extends JFrame
{
    RenderData renderData;

    public FrameRenderData(RenderData renderData)
    {
        this.renderData = renderData;
        setSize(new Dimension(600, 600));
        setResizable(false);
        setTitle("JSON RenderData debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = null;

        tabbedPane.addTab("States", icon, new TabModels(),
                "Lists all of the render states");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new JPanel();
        tabbedPane.addTab("...", icon, panel2,
                "not implemented");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabbedPane, BorderLayout.CENTER);
    }

    protected final class TabModels extends PanelDataList<IJsonDebugDisplay>
    {
        @Override
        protected IJsonDebugData getDataEntryFor(IJsonDebugDisplay object)
        {
            if (object instanceof IJsonDebugDisplay)
            {
                return new DebugJsonData((IJsonDebugDisplay) object);
            }
            return new DebugData("MOD: " + object.getMod() + "  ID: " + object.getContentID());
        }

        @Override
        protected void buildData()
        {
            for (Map.Entry<String, IRenderState> entry : renderData.renderStatesByName.entrySet())
            {
                if (entry.getValue() instanceof IJsonDebugDisplay)
                {
                    dataModel.addElement(new DebugJsonData((IJsonDebugDisplay) entry.getValue()));
                }
                else
                {
                    String msg = "Key: " + entry.getKey() + " value:" + entry.getValue();
                    dataModel.addElement(new DebugData(msg));
                }
            }
        }
    }

    public static void main(String... args)
    {
        RenderData data = new RenderData(null, "someRender", "item");

        for (int i = 0; i < 3; i++)
        {
            RenderStateItem state = new RenderStateItem("item." + i);
            data.add("item." + i, state);
        }

        for (int i = 0; i < 3; i++)
        {
            ModelState state = new ModelState("model" + i);
            data.add("model." + i, state);
        }

        FrameRenderData gui = new FrameRenderData(data);

        gui.show();
    }
}
