package com.builtbroken.mc.debug.gui.panels.json;

import com.builtbroken.mc.framework.json.JsonContentLoader;

import javax.swing.*;
import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/6/2017.
 */
public class PanelJsonData extends JPanel
{
    protected final JTabbedPane jsonDataPanel;

    public PanelJsonData()
    {
        setLayout(new BorderLayout());

        jsonDataPanel = new JTabbedPane();

        JPanel panel1 = new JPanel();

        //Init load button, removed when data is loaded
        Button initButton = new Button("Load");
        initButton.addActionListener(e -> reloadJsonData());
        panel1.add(initButton);
        jsonDataPanel.addTab("Init", null, panel1, "The only panel until you hit load");
        add(jsonDataPanel, BorderLayout.CENTER);

        //Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setMaximumSize(new Dimension(-1, 100));

        //Reload button
        Button button = new Button("Rebuild JSON Data");
        button.addActionListener(e -> reloadJsonData());
        menuPanel.add(button);

        add(menuPanel, BorderLayout.NORTH);
    }

    public void reloadJsonData()
    {
        jsonDataPanel.removeAll();
        for (String processor : JsonContentLoader.INSTANCE.generatedObjects.keySet())
        {
            jsonDataPanel.addTab(processor, null, new TabPanelJsonData(processor));
        }
    }
}
