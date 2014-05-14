package resonant.lib.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import resonant.lib.utility.WorldUtility;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderUtility
{
    public static final ResourceLocation PARTICLE_RESOURCE = new ResourceLocation("textures/particle/particles.png");
    public static final HashMap<String, ResourceLocation> resourceCahce = new HashMap<String, ResourceLocation>();
    /** Icon loading map for external icon registration. */
    public static final HashMap<String, Icon> loadedIconMap = new HashMap<String, Icon>();
    public static RenderBlocks renderBlocks = new RenderBlocks();

    public static ResourceLocation getResource(String domain, String name)
    {
        String cacheName = domain + ":" + name;

        if (!resourceCahce.containsKey(cacheName))
        {
            resourceCahce.put(cacheName, new ResourceLocation(domain, name));
        }

        return resourceCahce.get(cacheName);
    }

    public static void setTerrainTexture()
    {
        setSpriteTexture(0);
    }

    public static void setSpriteTexture(ItemStack itemStack)
    {
        setSpriteTexture(itemStack.getItemSpriteNumber());
    }

    public static void setSpriteTexture(int sprite)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(getSpriteTexture(sprite));
    }

    public static ResourceLocation getSpriteTexture(int sprite)
    {
        return FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(sprite);
    }

    public static void registerIcon(String name, TextureMap textureMap)
    {
        loadedIconMap.put(name, textureMap.registerIcon(name));
    }

    public static Icon getIcon(String name)
    {
        return loadedIconMap.get(name);
    }

    /** Enables blending. */
    public static void enableBlending()
    {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /** Disables blending. */
    public static void disableBlending()
    {
        glShadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glDisable(GL_BLEND);
    }

    public static void enableLighting()
    {
        RenderHelper.enableStandardItemLighting();
    }

    /** Disables lighting and turns glow on. */
    public static void disableLighting()
    {
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void disableLightmap()
    {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void enableLightmap()
    {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void renderNormalBlockAsItem(Block block, int metadata, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void renderFloatingText(String text, Vector3 position)
    {
        renderFloatingText(text, position, 0xFFFFFF);
    }

    /** Renders a floating text in a specific position.
     * 
     * @author Briman0094 */
    public static void renderFloatingText(String text, Vector3 position, int color)
    {
        renderFloatingText(text, position.x, position.y, position.z, color);
    }

    public static void renderFloatingText(String text, double x, double y, double z, int color)
    {
        RenderManager renderManager = RenderManager.instance;
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float scale = 0.027f;
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        int yOffset = 0;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
        tessellator.addVertex(-stringMiddle - 1, -1 + yOffset, 0.0D);
        tessellator.addVertex(-stringMiddle - 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, -1 + yOffset, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void renderText(String text, int side, float maxScale, double x, double y, double z)
    {
        renderText(text, ForgeDirection.getOrientation(side), maxScale, x, y, z);
    }

    public static void renderText(String text, ForgeDirection side, float maxScale, double x, double y, double z)
    {
        GL11.glPushMatrix();

        GL11.glPolygonOffset(-10, -10);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);

        float displayWidth = 1 - (2 / 16);
        float displayHeight = 1 - (2 / 16);
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();

        switch (side)
        {
            case SOUTH:
                GL11.glTranslatef(0, 1, 0);
                GL11.glRotatef(0, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);

                break;
            case NORTH:
                GL11.glTranslatef(1, 1, 1);
                GL11.glRotatef(180, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);

                break;
            case EAST:
                GL11.glTranslatef(0, 1, 1);
                GL11.glRotatef(90, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);

                break;
            case WEST:
                GL11.glTranslatef(1, 1, 0);
                GL11.glRotatef(-90, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);
                break;
        }

        // Find Center
        GL11.glTranslatef(displayWidth / 2, 1F, displayHeight / 2);
        GL11.glRotatef(-90, 1, 0, 0);

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;

        int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        int lineHeight = fontRenderer.FONT_HEIGHT + 2;
        int requiredHeight = lineHeight * 1;
        float scaler = 0.8f;
        float scaleX = (displayWidth / requiredWidth);
        float scaleY = (displayHeight / requiredHeight);
        float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GL11.glScalef(scale, -scale, scale);
        GL11.glDepthMask(false);

        int offsetX;
        int offsetY;
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);

        offsetX = (realWidth - requiredWidth) / 2;
        offsetY = (realHeight - requiredHeight) / 2;

        GL11.glDisable(GL11.GL_LIGHTING);
        fontRenderer.drawString("\u00a7f" + text, offsetX - (realWidth / 2), 1 + offsetY - (realHeight / 2), 1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    /** Pre-translated and rotated version. The current position will be the center of the text
     * render. */
    public static void renderText(String text, float scaler, float maxScale)
    {
        GL11.glPushMatrix();

        GL11.glPolygonOffset(-10, -10);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);

        float displayWidth = 1 - (2 / 16);
        float displayHeight = 1 - (2 / 16);

        // Rotate so we're facing up.
        GL11.glRotatef(-90, 1, 0, 0);

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;

        int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        int lineHeight = fontRenderer.FONT_HEIGHT;
        int requiredHeight = lineHeight;
        float scaleX = (displayWidth / requiredWidth);
        float scaleY = (displayHeight / requiredHeight);
        float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GL11.glScalef(scale, -scale, scale);
        GL11.glDepthMask(false);

        int offsetX;
        int offsetY;
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);

        offsetX = (realWidth - requiredWidth) / 2;
        offsetY = (realHeight - requiredHeight) / 2;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        fontRenderer.drawString("\u00a7f" + text, offsetX - (realWidth / 2), 2 + offsetY - (realHeight / 2), 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontRenderer.drawString("\u00a7f" + text, offsetX - (realWidth / 2), 2 + offsetY - (realHeight / 2), 1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

        GL11.glPopMatrix();
    }

    /** @author OpenBlocks */
    public static void rotateFacesOnRenderer(ForgeDirection rotation, RenderBlocks renderer, boolean fullRotation)
    {
        if (fullRotation)
        {
            switch (rotation)
            {
                case DOWN:
                    renderer.uvRotateSouth = 3;
                    renderer.uvRotateNorth = 3;
                    renderer.uvRotateEast = 3;
                    renderer.uvRotateWest = 3;
                    break;
                case EAST:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 2;
                    renderer.uvRotateWest = 1;
                    renderer.uvRotateEast = 2;
                    break;
                case NORTH:
                    renderer.uvRotateNorth = 2;
                    renderer.uvRotateSouth = 1;
                    break;
                case SOUTH:
                    renderer.uvRotateTop = 3;
                    renderer.uvRotateBottom = 3;
                    renderer.uvRotateNorth = 1;
                    renderer.uvRotateSouth = 2;
                    break;
                case UNKNOWN:
                    break;
                case UP:
                    break;
                case WEST:
                    renderer.uvRotateTop = 2;
                    renderer.uvRotateBottom = 1;
                    renderer.uvRotateWest = 2;
                    renderer.uvRotateEast = 1;
                    break;
                default:
                    break;

            }
        }
        else
        {
            switch (rotation)
            {
                case EAST:
                    renderer.uvRotateTop = 1;
                    break;
                case WEST:
                    renderer.uvRotateTop = 2;
                    break;
                case SOUTH:
                    renderer.uvRotateTop = 3;
                    break;
                default:
                    break;
            }
        }
    }

    public static void resetFacesOnRenderer(RenderBlocks renderer)
    {
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateWest = 0;
        renderer.flipTexture = false;
    }

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation)
    {
        renderInventoryBlock(renderer, block, rotation, -1);
    }

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier)
    {
        renderInventoryBlock(renderer, block, rotation, colorMultiplier, null);
    }

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier, Set<ForgeDirection> enabledSides)
    {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        float r;
        float g;
        float b;
        if (colorMultiplier > -1)
        {
            r = (colorMultiplier >> 16 & 255) / 255.0F;
            g = (colorMultiplier >> 8 & 255) / 255.0F;
            b = (colorMultiplier & 255) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
        }
        // Learn to matrix, please push and pop :D -- NC
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        int metadata = rotation.ordinal();
        if (enabledSides == null || enabledSides.contains(ForgeDirection.DOWN))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
        }
        if (enabledSides == null || enabledSides.contains(ForgeDirection.UP))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
            tessellator.draw();
        }
        if (enabledSides == null || enabledSides.contains(ForgeDirection.SOUTH))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
            tessellator.draw();
        }
        if (enabledSides == null || enabledSides.contains(ForgeDirection.NORTH))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
            tessellator.draw();
        }
        if (enabledSides == null || enabledSides.contains(ForgeDirection.WEST))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
            tessellator.draw();
        }
        if (enabledSides == null || enabledSides.contains(ForgeDirection.EAST))
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
            tessellator.draw();
        }
        GL11.glPopMatrix();
    }

    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block)
    {
        renderCube(x1, y1, z1, x2, y2, z2, block, null);
    }

    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, Icon overrideTexture)
    {
        renderCube(x1, y1, z1, x2, y2, z2, block, overrideTexture, 0);
    }

    /** Renders a cube with custom block boundaries. */
    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, Icon overrideTexture, int meta)
    {
        GL11.glPushMatrix();
        Tessellator t = Tessellator.instance;

        GL11.glColor4f(1, 1, 1, 1);

        renderBlocks.setRenderBounds(x1, y1, z1, x2, y2, z2);

        t.startDrawingQuads();

        Icon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, meta);
        t.setNormal(0.0F, -1.0F, 0.0F);
        renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(1, meta);
        t.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(2, meta);
        t.setNormal(0.0F, 0.0F, -1.0F);
        renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(3, meta);
        t.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(4, meta);
        t.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(5, meta);
        t.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture);
        t.draw();

        GL11.glPopMatrix();
    }

    /** Rotates a render based on the direction it is placed on. Used for things like flat wires or
     * panels. The model will need to be centered and be facing upright to begin with.
     * 
     * @param placementSide */
    @SuppressWarnings("incomplete-switch")
    public static void rotateFaceBlockToSide(ForgeDirection placementSide)
    {
        switch (placementSide)
        {
            case DOWN:
                GL11.glTranslatef(0, -0.45f, 0);
                break;
            case UP:
                GL11.glTranslatef(0, 0.45f, 0);
                GL11.glRotatef(180, 1, 0, 0);
                break;
            case NORTH:
                GL11.glTranslatef(0, 0, -0.45f);
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case SOUTH:
                GL11.glTranslatef(0, 0, 0.45f);
                GL11.glRotatef(-90, 1, 0, 0);
                break;
            case WEST:
                GL11.glTranslatef(-0.45f, 0, 0);
                GL11.glRotatef(-90, 0, 0, 1);
                break;
            case EAST:
                GL11.glTranslatef(0.45f, 0, 0);
                GL11.glRotatef(90, 0, 0, 1);
                break;
        }
    }

    @SuppressWarnings("incomplete-switch")
    public static void rotateFaceToSideNoTranslate(ForgeDirection placementSide)
    {
        switch (placementSide)
        {
            case DOWN:
                break;
            case UP:
                GL11.glRotatef(180, 1, 0, 0);
                break;
            case NORTH:
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case SOUTH:
                GL11.glRotatef(-90, 1, 0, 0);
                break;
            case WEST:
                GL11.glRotatef(-90, 0, 0, 1);
                break;
            case EAST:
                GL11.glRotatef(90, 0, 0, 1);
                break;
        }
    }

    @SuppressWarnings("incomplete-switch")
    public static void rotateFaceBlockToSideOutwards(ForgeDirection placementSide)
    {
        switch (placementSide)
        {
            case DOWN:
                GL11.glTranslatef(0, 0.45f, 0);
                GL11.glRotatef(180, 1, 0, 0);

                break;
            case UP:
                GL11.glTranslatef(0, -0.45f, 0);
                break;
            case NORTH:
                GL11.glTranslatef(0, 0, 0.45f);
                GL11.glRotatef(90, 1, 0, 0);
                GL11.glRotatef(180, 0, 0, 1);
                break;
            case SOUTH:
                GL11.glTranslatef(0, 0, -0.45f);
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case WEST:
                GL11.glTranslatef(0.45f, 0, 0);
                GL11.glRotatef(90, 0, 0, 1);
                GL11.glRotatef(-90, 0, 1, 0);
                break;
            case EAST:
                GL11.glTranslatef(-0.45f, 0, 0);
                GL11.glRotatef(-90, 0, 0, 1);
                GL11.glRotatef(90, 0, 1, 0);
                break;
        }
    }

    /** Rotates a block based on the direction it is facing.
     * 
     * @param direction */
    public static void rotateBlockBasedOnDirection(ForgeDirection direction)
    {
        switch (direction)
        {
            default:
                glRotatef(WorldUtility.getAngleFromForgeDirection(direction), 0, 1, 0);
                break;
            case DOWN:
                glRotatef(90, 1, 0, 0);
                break;
            case UP:
                glRotatef(-90, 1, 0, 0);
                break;
        }
    }

    /** Use this for models that are facing up by default.
     * 
     * @param direction */
    public static void rotateBlockBasedOnDirectionUp(ForgeDirection direction)
    {
        switch (direction)
        {
            default:
                glRotatef(WorldUtility.getAngleFromForgeDirection(direction), 0, 1, 0);
                glRotatef(-90, 0, 0, 1);
                break;
            case DOWN:
                glRotatef(180, 0, 0, 1);
                break;
            case UP:
                break;
        }
    }

    public static void bind(String name)
    {
        bind(getResource("minecraft", name));
    }

    public static void bind(String domain, String name)
    {
        bind(getResource(domain, name));
    }

    public static void bind(ResourceLocation location)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
    }
}
