package resonant.lib.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

/** Created by Henry on 3/24/2014. */
public class RenderBlockAdvanced extends RenderBlocks
{
    public int limitedSide = -1;

    @Override
    public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (this.getBlockIcon(par1Block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (this.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        float f7;
        int i1;

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) && limitedSide == 0)
        {
            if (this.renderMinY <= 0.0D)
            {
                --par3;
            }

            this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (this.renderMinY <= 0.0D)
            {
                ++par3;
            }

            i1 = l;

            if (this.renderMinY <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + f7 + this.aoLightValueScratchYZNN) / 4.0F;
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.5F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) && limitedSide == 1)
        {
            if (this.renderMaxY >= 1.0D)
            {
                ++par3;
            }

            this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (this.renderMaxY >= 1.0D)
            {
                --par3;
            }

            i1 = l;

            if (this.renderMaxY >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (this.aoLightValueScratchYZPP + f7 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        Icon icon;

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) && limitedSide == 2)
        {
            if (this.renderMinZ <= 0.0D)
            {
                --par4;
            }

            this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (this.renderMinZ <= 0.0D)
            {
                ++par4;
            }

            i1 = l;

            if (this.renderMinZ <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            f3 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (this.aoLightValueScratchYZNN + f7 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
            f6 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + f7) / 4.0F;
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
            this.renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) && limitedSide == 3)
        {
            if (this.renderMaxZ >= 1.0D)
            {
                ++par4;
            }

            this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (this.renderMaxZ >= 1.0D)
            {
                --par4;
            }

            i1 = l;

            if (this.renderMaxZ >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            f3 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + f7 + this.aoLightValueScratchYZPP) / 4.0F;
            f6 = (f7 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            f5 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
            f4 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + f7) / 4.0F;
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
            this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) && limitedSide == 4)
        {
            if (this.renderMinX <= 0.0D)
            {
                --par2;
            }

            this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (this.renderMinX <= 0.0D)
            {
                ++par2;
            }

            i1 = l;

            if (this.renderMinX <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            f6 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + f7 + this.aoLightValueScratchXZNP) / 4.0F;
            f3 = (f7 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
            f4 = (this.aoLightValueScratchXZNN + f7 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
            f5 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + f7) / 4.0F;
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
            this.renderFaceXNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) && limitedSide == 5)
        {
            if (this.renderMaxX >= 1.0D)
            {
                ++par2;
            }

            this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (this.renderMaxX >= 1.0D)
            {
                --par2;
            }

            i1 = l;

            if (this.renderMaxX >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            f3 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + f7 + this.aoLightValueScratchXZPP) / 4.0F;
            f4 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + f7) / 4.0F;
            f5 = (this.aoLightValueScratchXZPN + f7 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
            f6 = (f7 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
            this.renderFaceXPos(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        this.enableAO = false;
        return flag;
    }

    /** Renders non-full-cube block with ambient occusion. Args: block, x, y, z, red, green, blue
     * (lighting) */
    @Override
    public boolean renderStandardBlockWithAmbientOcclusionPartial(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (this.getBlockIcon(par1Block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (this.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        float f7;
        int i1;

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) && limitedSide == 0)
        {
            if (this.renderMinY <= 0.0D)
            {
                --par3;
            }

            this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (this.renderMinY <= 0.0D)
            {
                ++par3;
            }

            i1 = l;

            if (this.renderMinY <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + f7 + this.aoLightValueScratchYZNN) / 4.0F;
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.5F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) && limitedSide == 1)
        {
            if (this.renderMaxY >= 1.0D)
            {
                ++par3;
            }

            this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (this.renderMaxY >= 1.0D)
            {
                --par3;
            }

            i1 = l;

            if (this.renderMaxY >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (this.aoLightValueScratchYZPP + f7 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        Icon icon;

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) && limitedSide == 2)
        {
            if (this.renderMinZ <= 0.0D)
            {
                --par4;
            }

            this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (this.renderMinZ <= 0.0D)
            {
                ++par4;
            }

            i1 = l;

            if (this.renderMinZ <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            f9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            f8 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
            f11 = (this.aoLightValueScratchYZNN + f7 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
            f10 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + f7) / 4.0F;
            f3 = (float) (f9 * this.renderMaxY * (1.0D - this.renderMinX) + f8 * this.renderMinY * this.renderMinX + f11 * (1.0D - this.renderMaxY) * this.renderMinX + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
            f4 = (float) (f9 * this.renderMaxY * (1.0D - this.renderMaxX) + f8 * this.renderMaxY * this.renderMaxX + f11 * (1.0D - this.renderMaxY) * this.renderMaxX + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
            f5 = (float) (f9 * this.renderMinY * (1.0D - this.renderMaxX) + f8 * this.renderMinY * this.renderMaxX + f11 * (1.0D - this.renderMinY) * this.renderMaxX + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
            f6 = (float) (f9 * this.renderMinY * (1.0D - this.renderMinX) + f8 * this.renderMinY * this.renderMinX + f11 * (1.0D - this.renderMinY) * this.renderMinX + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
            k1 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            j1 = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
            i2 = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
            l1 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);
            this.brightnessTopLeft = this.mixAoBrightness(k1, j1, i2, l1, this.renderMaxY * (1.0D - this.renderMinX), this.renderMaxY * this.renderMinX, (1.0D - this.renderMaxY) * this.renderMinX, (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
            this.brightnessBottomLeft = this.mixAoBrightness(k1, j1, i2, l1, this.renderMaxY * (1.0D - this.renderMaxX), this.renderMaxY * this.renderMaxX, (1.0D - this.renderMaxY) * this.renderMaxX, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
            this.brightnessBottomRight = this.mixAoBrightness(k1, j1, i2, l1, this.renderMinY * (1.0D - this.renderMaxX), this.renderMinY * this.renderMaxX, (1.0D - this.renderMinY) * this.renderMaxX, (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
            this.brightnessTopRight = this.mixAoBrightness(k1, j1, i2, l1, this.renderMinY * (1.0D - this.renderMinX), this.renderMinY * this.renderMinX, (1.0D - this.renderMinY) * this.renderMinX, (1.0D - this.renderMinY) * (1.0D - this.renderMinX));

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
            this.renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) && limitedSide == 3)
        {
            if (this.renderMaxZ >= 1.0D)
            {
                ++par4;
            }

            this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (this.renderMaxZ >= 1.0D)
            {
                --par4;
            }

            i1 = l;

            if (this.renderMaxZ >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            f9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + f7 + this.aoLightValueScratchYZPP) / 4.0F;
            f8 = (f7 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            f11 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
            f10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + f7) / 4.0F;
            f3 = (float) (f9 * this.renderMaxY * (1.0D - this.renderMinX) + f8 * this.renderMaxY * this.renderMinX + f11 * (1.0D - this.renderMaxY) * this.renderMinX + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
            f4 = (float) (f9 * this.renderMinY * (1.0D - this.renderMinX) + f8 * this.renderMinY * this.renderMinX + f11 * (1.0D - this.renderMinY) * this.renderMinX + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
            f5 = (float) (f9 * this.renderMinY * (1.0D - this.renderMaxX) + f8 * this.renderMinY * this.renderMaxX + f11 * (1.0D - this.renderMinY) * this.renderMaxX + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
            f6 = (float) (f9 * this.renderMaxY * (1.0D - this.renderMaxX) + f8 * this.renderMaxY * this.renderMaxX + f11 * (1.0D - this.renderMaxY) * this.renderMaxX + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
            k1 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
            j1 = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
            i2 = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            l1 = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);
            this.brightnessTopLeft = this.mixAoBrightness(k1, l1, i2, j1, this.renderMaxY * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * this.renderMinX, this.renderMaxY * this.renderMinX);
            this.brightnessBottomLeft = this.mixAoBrightness(k1, l1, i2, j1, this.renderMinY * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * this.renderMinX, this.renderMinY * this.renderMinX);
            this.brightnessBottomRight = this.mixAoBrightness(k1, l1, i2, j1, this.renderMinY * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * this.renderMaxX, this.renderMinY * this.renderMaxX);
            this.brightnessTopRight = this.mixAoBrightness(k1, l1, i2, j1, this.renderMaxY * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * this.renderMaxX, this.renderMaxY * this.renderMaxX);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
            this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) && limitedSide == 4)
        {
            if (this.renderMinX <= 0.0D)
            {
                --par2;
            }

            this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (this.renderMinX <= 0.0D)
            {
                ++par2;
            }

            i1 = l;

            if (this.renderMinX <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
            f9 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + f7 + this.aoLightValueScratchXZNP) / 4.0F;
            f8 = (f7 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
            f11 = (this.aoLightValueScratchXZNN + f7 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
            f10 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + f7) / 4.0F;
            f3 = (float) (f8 * this.renderMaxY * this.renderMaxZ + f11 * this.renderMaxY * (1.0D - this.renderMaxZ) + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + f9 * (1.0D - this.renderMaxY) * this.renderMaxZ);
            f4 = (float) (f8 * this.renderMaxY * this.renderMinZ + f11 * this.renderMaxY * (1.0D - this.renderMinZ) + f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + f9 * (1.0D - this.renderMaxY) * this.renderMinZ);
            f5 = (float) (f8 * this.renderMinY * this.renderMinZ + f11 * this.renderMinY * (1.0D - this.renderMinZ) + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + f9 * (1.0D - this.renderMinY) * this.renderMinZ);
            f6 = (float) (f8 * this.renderMinY * this.renderMaxZ + f11 * this.renderMinY * (1.0D - this.renderMaxZ) + f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + f9 * (1.0D - this.renderMinY) * this.renderMaxZ);
            k1 = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
            j1 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
            i2 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
            l1 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
            this.brightnessTopLeft = this.mixAoBrightness(j1, i2, l1, k1, this.renderMaxY * this.renderMaxZ, this.renderMaxY * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * this.renderMaxZ);
            this.brightnessBottomLeft = this.mixAoBrightness(j1, i2, l1, k1, this.renderMaxY * this.renderMinZ, this.renderMaxY * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * this.renderMinZ);
            this.brightnessBottomRight = this.mixAoBrightness(j1, i2, l1, k1, this.renderMinY * this.renderMinZ, this.renderMinY * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * this.renderMinZ);
            this.brightnessTopRight = this.mixAoBrightness(j1, i2, l1, k1, this.renderMinY * this.renderMaxZ, this.renderMinY * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * this.renderMaxZ);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
            this.renderFaceXNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) && limitedSide == 5)
        {
            if (this.renderMaxX >= 1.0D)
            {
                ++par2;
            }

            this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
            this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
            this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
            this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
            this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
            this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
            this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
            this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
            flag3 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            flag2 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            flag5 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            flag4 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (this.renderMaxX >= 1.0D)
            {
                --par2;
            }

            i1 = l;

            if (this.renderMaxX >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
            {
                i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
            }

            f7 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
            f9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + f7 + this.aoLightValueScratchXZPP) / 4.0F;
            f8 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + f7) / 4.0F;
            f11 = (this.aoLightValueScratchXZPN + f7 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
            f10 = (f7 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float) (f9 * (1.0D - this.renderMinY) * this.renderMaxZ + f8 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + f11 * this.renderMinY * (1.0D - this.renderMaxZ) + f10 * this.renderMinY * this.renderMaxZ);
            f4 = (float) (f9 * (1.0D - this.renderMinY) * this.renderMinZ + f8 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + f11 * this.renderMinY * (1.0D - this.renderMinZ) + f10 * this.renderMinY * this.renderMinZ);
            f5 = (float) (f9 * (1.0D - this.renderMaxY) * this.renderMinZ + f8 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + f11 * this.renderMaxY * (1.0D - this.renderMinZ) + f10 * this.renderMaxY * this.renderMinZ);
            f6 = (float) (f9 * (1.0D - this.renderMaxY) * this.renderMaxZ + f8 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + f11 * this.renderMaxY * (1.0D - this.renderMaxZ) + f10 * this.renderMaxY * this.renderMaxZ);
            k1 = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            j1 = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
            i2 = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
            l1 = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);
            this.brightnessTopLeft = this.mixAoBrightness(k1, l1, i2, j1, (1.0D - this.renderMinY) * this.renderMaxZ, (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), this.renderMinY * (1.0D - this.renderMaxZ), this.renderMinY * this.renderMaxZ);
            this.brightnessBottomLeft = this.mixAoBrightness(k1, l1, i2, j1, (1.0D - this.renderMinY) * this.renderMinZ, (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), this.renderMinY * (1.0D - this.renderMinZ), this.renderMinY * this.renderMinZ);
            this.brightnessBottomRight = this.mixAoBrightness(k1, l1, i2, j1, (1.0D - this.renderMaxY) * this.renderMinZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), this.renderMaxY * (1.0D - this.renderMinZ), this.renderMaxY * this.renderMinZ);
            this.brightnessTopRight = this.mixAoBrightness(k1, l1, i2, j1, (1.0D - this.renderMaxY) * this.renderMaxZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), this.renderMaxY * (1.0D - this.renderMaxZ), this.renderMaxY * this.renderMaxZ);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
            this.renderFaceXPos(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        this.enableAO = false;
        return flag;
    }

    /** Renders a standard cube block at the given coordinates, with a given color ratio. Args:
     * block, x, y, z, r, g, b */
    @Override
    public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * par5;
        float f8 = f4 * par6;
        float f9 = f4 * par7;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        if (par1Block != Block.grass)
        {
            f10 = f3 * par5;
            f11 = f5 * par5;
            f12 = f6 * par5;
            f13 = f3 * par6;
            f14 = f5 * par6;
            f15 = f6 * par6;
            f16 = f3 * par7;
            f17 = f5 * par7;
            f18 = f6 * par7;
        }

        int l = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0)) && limitedSide == 0)
        {
            tessellator.setBrightness(this.renderMinY > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
            tessellator.setColorOpaque_F(f10, f13, f16);
            this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1)) && limitedSide == 1)
        {
            tessellator.setBrightness(this.renderMaxY < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
            tessellator.setColorOpaque_F(f7, f8, f9);
            this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        Icon icon;

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 - 1, 2)) && limitedSide == 2)
        {
            tessellator.setBrightness(this.renderMinZ > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
            this.renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * par5, f14 * par6, f17 * par7);
                this.renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2, par3, par4 + 1, 3)) && limitedSide == 3)
        {
            tessellator.setBrightness(this.renderMaxZ < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
            this.renderFaceZPos(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * par5, f14 * par6, f17 * par7);
                this.renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 - 1, par3, par4, 4)) && limitedSide == 4)
        {
            tessellator.setBrightness(this.renderMinX > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
            this.renderFaceXNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * par5, f15 * par6, f18 * par7);
                this.renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if ((this.renderAllFaces || par1Block.shouldSideBeRendered(this.blockAccess, par2 + 1, par3, par4, 5)) && limitedSide == 5)
        {
            tessellator.setBrightness(this.renderMaxX < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
            this.renderFaceXPos(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * par5, f15 * par6, f18 * par7);
                this.renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        return flag;
    }
}
