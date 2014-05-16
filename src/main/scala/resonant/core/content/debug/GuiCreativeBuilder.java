package resonant.core.content.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import resonant.core.ResonantEngine;
import resonant.lib.References;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.utility.LanguageUtility;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiCreativeBuilder extends GuiContainerBase
{
    private GuiTextField textFieldSize;
    private int mode = 0;
    private Vector3 position;

    public GuiCreativeBuilder(Vector3 position)
    {
        super();
        this.position = position;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.textFieldSize = new GuiTextField(this.fontRenderer, 45, 58, 50, 12);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - 10, 58, 20, "Build"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 35, 120, 20, "Mode"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Creative Builder", 60, 6, 4210752);
        this.fontRenderer.drawString("This is a creative only cheat", 9, 20, 4210752);
        this.fontRenderer.drawString("which allows you to auto build", 9, 30, 4210752);
        this.fontRenderer.drawString("structures for testing.", 9, 40, 4210752);

        this.fontRenderer.drawString("Size: ", 9, 60, 4210752);
        this.textFieldSize.drawTextBox();

        ((GuiButton) this.buttonList.get(1)).displayString = LanguageUtility.getLocal(BlockCreativeBuilder.REGISTRY.get(mode).getName());
        this.fontRenderer.drawString("Mode: ", 9, 80, 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        this.fontRenderer.drawString("Warning!", 9, 130, 4210752);
        this.fontRenderer.drawString("This will replace blocks without", 9, 140, 4210752);
        this.fontRenderer.drawString("dropping it! You may lose items.", 9, 150, 4210752);

    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        this.textFieldSize.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int par3)
    {
        super.mouseClicked(x, y, par3);
        this.textFieldSize.mouseClicked(x - containerWidth, y - containerHeight, par3);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        super.actionPerformed(par1GuiButton);

        if (par1GuiButton.id == 0)
        {
            int radius = 0;

            try
            {
                radius = Integer.parseInt(this.textFieldSize.getText());
            }
            catch (Exception e)
            {
            }

            if (radius > 0)
            {
                PacketDispatcher.sendPacketToServer(References.PACKET_TILE.getPacket(position.intX(), position.intY(), position.intZ(), mode, radius));
                this.mc.thePlayer.closeScreen();
            }
        }
        else if (par1GuiButton.id == 1)
        {
            this.mode = (this.mode + 1) % (BlockCreativeBuilder.REGISTRY.size());
        }
    }

}