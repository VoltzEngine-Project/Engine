package com.builtbroken.mc.debug.gui.panels.recipes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class PanelRecipes extends JPanel
{
    public PanelRecipes()
    {
        //setup panel
        setLayout(new BorderLayout());

        //Build tab panel
        JTabbedPane tabbedPane = new JTabbedPane();

        //add tabs
        JComponent panel = new TabPanelFurnaceRecipes();
        tabbedPane.addTab("Furnace", null, panel,
                "Shows list of furnace recipes");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        panel = new TabPanelShapedRecipes();
        tabbedPane.addTab("Shaped", null, panel,
                "Shows list of shaped crafting grid recipes");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        panel = new TabPanelShapelessRecipes();
        tabbedPane.addTab("Shapeless", null, panel,
                "Shows list of shapeless crafting grid recipes");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        //Add tab panel
        add(tabbedPane, BorderLayout.CENTER);
    }
}
