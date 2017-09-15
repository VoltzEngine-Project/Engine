package com.builtbroken.mc.debug.gui;

import com.builtbroken.mc.debug.gui.panels.PanelJsonConsole;
import com.builtbroken.mc.debug.gui.panels.PanelJsonData;
import com.builtbroken.mc.debug.gui.panels.PanelRecipes;
import com.builtbroken.mc.framework.json.JsonContentLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/16/2017.
 */
public class FrameDebug extends JFrame
{
    public void init()
    {
        //Setup this
        setSize(new Dimension(1000, 800));
        setResizable(false);
        setTitle("JSON debug window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        final JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/middle.gif");

        //Create console panel
        PanelJsonConsole consolePanel = new PanelJsonConsole();
        JsonContentLoader.INSTANCE.debug.add(new PanelJsonConsole.DebugListener(consolePanel));
        tabbedPane.addTab("Debug", icon, consolePanel,
                "Shows debug output console");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        //Create data panel
        JComponent jsonDataPanel = new PanelJsonData();
        tabbedPane.addTab("Data", icon, jsonDataPanel,
                "Shows list of loaded JSON data");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //Create recipe panel
        JComponent recipePanel = new PanelRecipes();
        tabbedPane.addTab("Recipes", icon, recipePanel,
                "Shows list of recipes register to Minecraft and VoltzEngine");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        //Add tabbed pane to main
        add(tabbedPane, BorderLayout.CENTER);

        pack();

    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path)
    {
        URL imgURL = FrameDebug.class.getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
