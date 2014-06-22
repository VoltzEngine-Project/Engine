package universalelectricity.core

import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import javax.swing.ImageIcon
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.client.event.RenderPlayerEvent
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.ReflectionHelper
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import scala.collection.JavaConversions._

/**
 * @author Based off of Biomes of Plenty
 */
@SideOnly(Side.CLIENT)
object CapeEventHandler
{
  private final val serverLocation: String = "https://raw.github.com/Universal-Electricity/Universal-Electricity/master/capes/capes.txt"
  private final val timeout: Int = 1000
  private val cloaks = new HashMap[String, String]
  private val capePlayers = new ArrayList[AbstractClientPlayer]

  private final val TEST_GRAPHICS: Graphics = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB).getGraphics

  buildCloakURLDatabase

  def isOptifineInstalled: Boolean =
  {
    for (s <- (Launch.blackboard.get("TweakClasses").asInstanceOf[List[String]])) if (s.toLowerCase.contains("OptiFineTweaker".toLowerCase) || s.toLowerCase.contains("OptiFineForgeTweaker".toLowerCase)) return true
    return false
  }

  @SubscribeEvent
  def onPreRenderSpecials(event: RenderPlayerEvent.Specials.Pre)
  {
    if (Loader.isModLoaded("shadersmod") || isOptifineInstalled)
    {
      return
    }
    if (event.entityPlayer.isInstanceOf[AbstractClientPlayer])
    {
      val abstractClientPlayer: AbstractClientPlayer = event.entityPlayer.asInstanceOf[AbstractClientPlayer]
      if (!capePlayers.contains(abstractClientPlayer))
      {
        val cloakURL: String = cloaks.get(event.entityPlayer.getDisplayName.toLowerCase)
        if (cloakURL == null)
        {
          return
        }
        capePlayers.add(abstractClientPlayer)
        ReflectionHelper.setPrivateValue(classOf[ThreadDownloadImageData], abstractClientPlayer.getTextureCape, false, "textureUploaded", "field_110559_g")
        new Thread(new CloakThread(abstractClientPlayer, cloakURL)).start
        event.renderCape = true
      }
    }
  }

  def buildCloakURLDatabase
  {
    var url: URL = null
    try
    {
      url = new URL(serverLocation)
      val con: URLConnection = url.openConnection
      con.setConnectTimeout(timeout)
      con.setReadTimeout(timeout)
      val io: InputStream = con.getInputStream
      val br: BufferedReader = new BufferedReader(new InputStreamReader(io))
      var str: String = null
      var linetracker: Int = 1
      while ((({
        str = br.readLine;
        str
      })) != null)
      {
        if (!str.startsWith("--") && !str.isEmpty)
        {
          if (str.contains(":"))
          {
            val nick: String = str.substring(0, str.indexOf(":"))
            val link: String = str.substring(str.indexOf(":") + 1)
            new Thread(new CloakPreload(link)).start
            cloaks.put(nick.toLowerCase, link)
          }
          else
          {
            UELoader.logger.severe("[capes.txt] Syntax error on line " + linetracker + ": " + str)
          }
        }
        linetracker += 1
      }
      br.close
    }
    catch
      {
        case e: MalformedURLException =>
        {
          e.printStackTrace
        }
        case e: IOException =>
        {
          e.printStackTrace
        }
      }
  }

  def refreshCapes
  {
    cloaks.clear
    capePlayers.clear
    buildCloakURLDatabase
  }

  private class CloakThread extends Runnable
  {
    private[core] var abstractClientPlayer: AbstractClientPlayer = null
    private[core] var cloakURL: String = null

    def this(player: AbstractClientPlayer, cloak: String)
    {
      this()
      abstractClientPlayer = player
      cloakURL = cloak
    }

    def run
    {
      try
      {
        val cape: Image = new ImageIcon(new URL(cloakURL)).getImage
        val bo: BufferedImage = new BufferedImage(cape.getWidth(null), cape.getHeight(null), BufferedImage.TYPE_INT_ARGB)
        bo.getGraphics.drawImage(cape, 0, 0, null)
        ReflectionHelper.setPrivateValue(classOf[ThreadDownloadImageData], abstractClientPlayer.getTextureCape, bo, "bufferedImage", "field_110560_d")
      }
      catch
        {
          case e: MalformedURLException =>
          {
            UELoader.logger.fine("Failed to load capes")
            e.printStackTrace
          }
        }
    }
  }

  private class CloakPreload extends Runnable
  {
    private[core] var cloakURL: String = null

    def this(link: String)
    {
      this()
      cloakURL = link
    }

    def run
    {
      try
      {
        TEST_GRAPHICS.drawImage(new ImageIcon(new URL(cloakURL)).getImage, 0, 0, null)
      }
      catch
        {
          case e: MalformedURLException =>
          {
            e.printStackTrace
          }
        }
    }
  }

}