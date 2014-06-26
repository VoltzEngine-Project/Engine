package resonant.engine.content.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import resonant.engine.ResonantEngine;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.network.PacketTile;
import resonant.lib.utility.LanguageUtility;
import universalelectricity.core.transform.vector.Vector3;

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
		this.textFieldSize = new GuiTextField(fontRendererObj, 45, 58, 50, 12);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - 10, 58, 20, "Build"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 35, 120, 20, "Mode"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString("Creative Builder", 60, 6, 4210752);
		fontRendererObj.drawString("This is a creative only cheat", 9, 20, 4210752);
		fontRendererObj.drawString("which allows you to auto build", 9, 30, 4210752);
		fontRendererObj.drawString("structures for testing.", 9, 40, 4210752);

		fontRendererObj.drawString("Size: ", 9, 60, 4210752);
		this.textFieldSize.drawTextBox();

		((GuiButton) this.buttonList.get(1)).displayString = LanguageUtility.getLocal(TileCreativeBuilder$.MODULE$.registry().get(mode).getName());
		fontRendererObj.drawString("Mode: ", 9, 80, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		fontRendererObj.drawString("Warning!", 9, 130, 4210752);
		fontRendererObj.drawString("This will replace blocks without", 9, 140, 4210752);
		fontRendererObj.drawString("dropping it! You may lose items.", 9, 150, 4210752);

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
				ResonantEngine.instance.packetHandler.sendToServer(new PacketTile(position.xi(), position.yi(), position.zi(), new Object[] { mode, radius }));
				this.mc.thePlayer.closeScreen();
			}
		}
		else if (par1GuiButton.id == 1)
		{
			this.mode = (this.mode + 1) % (TileCreativeBuilder$.MODULE$.registry().size());
		}
	}

}