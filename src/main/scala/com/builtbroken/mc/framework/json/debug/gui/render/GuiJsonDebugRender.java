package com.builtbroken.mc.framework.json.debug.gui.render;

import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.render.item.RenderStateItem;
import com.builtbroken.mc.framework.json.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.debug.component.DebugDataCellRenderer;
import com.builtbroken.mc.framework.json.debug.data.DebugData;
import com.builtbroken.mc.framework.json.debug.data.DebugJsonData;
import com.builtbroken.mc.framework.json.debug.data.IJsonDebugData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class GuiJsonDebugRender extends JFrame
{
    JList dataLogList;
    DefaultListModel<IJsonDebugData> debugDataListModel = new DefaultListModel();

    RenderData renderData;

    public GuiJsonDebugRender(RenderData renderData)
    {
        this.renderData = renderData;
        setSize(new Dimension(400, 400));
        setResizable(false);
        setTitle("JSON RenderData debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = null;

        JComponent panel1 = createJsonDataTab();
        tabbedPane.addTab("States", icon, panel1,
                "Lists all of the render states");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new JPanel();
        tabbedPane.addTab("...", icon, panel2,
                "not implemented");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void reloadData(String filter)
    {
        debugDataListModel.clear();
        for (Map.Entry<String, IRenderState> entry : renderData.renderStatesByName.entrySet())
        {
            if (entry.getValue() instanceof IJsonDebugDisplay)
            {
                IJsonDebugDisplay display = (IJsonDebugDisplay) entry.getValue();
                String displayName = display.getDisplayName() != null ? display.getDisplayName() : display.toString();
                if (filter == null || filter.isEmpty() || displayName.contains(filter))
                {
                    debugDataListModel.addElement(new DebugJsonData(display));
                }
            }
            else
            {
                String msg = "Key: " + entry.getKey() + " value:" + entry.getValue();
                if (filter == null || filter.isEmpty() || msg.contains(filter))
                {
                    debugDataListModel.addElement(new DebugData(msg));
                }
            }
        }
    }

    protected JPanel createJsonDataTab()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //Create list
        dataLogList = new JList(debugDataListModel);
        dataLogList.setLayoutOrientation(JList.VERTICAL);
        dataLogList.setCellRenderer(new DebugDataCellRenderer());


        //Create scroll panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataLogList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        scrollPane.setMinimumSize(new Dimension(getWidth() - 100, getHeight() - 100));
        panel.add(scrollPane, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));

        //Reload button
        Button button = new Button("Reload");
        button.addActionListener(e -> reloadData(null));
        menuPanel.add(button);

        //Search box
        JTextField searchBox = new JTextField();
        searchBox.setMinimumSize(new Dimension(200, -1));
        searchBox.setPreferredSize(new Dimension(200, 30));
        searchBox.setToolTipText("Search filter");
        menuPanel.add(searchBox);

        //Search button
        button = new Button("Search");
        button.addActionListener(e -> reloadData(searchBox.getText().trim()));
        menuPanel.add(button);

        panel.add(menuPanel, BorderLayout.NORTH);

        return panel;
    }


    public static void main(String... args)
    {
        RenderData data = new RenderData(null, "someRender", "item");

        for (int i = 0; i < 30; i++)
        {
            RenderStateItem state = new RenderStateItem("item." + i);
            data.add("item." + i, state);
        }

        GuiJsonDebugRender gui = new GuiJsonDebugRender(data);

        gui.show();
    }
}
