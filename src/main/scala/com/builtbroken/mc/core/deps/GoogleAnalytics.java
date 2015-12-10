package com.builtbroken.mc.core.deps;

import com.builtbroken.mc.lib.mod.loadable.ILoadable;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/10/2015.
 */
public class GoogleAnalytics implements ILoadable
{
    //http://www.dmurph.com/2011/01/google-analytics-tracking-with-java/

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public void postInit()
    {
        JGoogleAnalyticsTracker.setProxy(System.getenv("http_proxy"));
        AnalyticsConfigData config = new AnalyticsConfigData("UA-58617158-3");
        JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(config, JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2);
        tracker.setDispatchMode(JGoogleAnalyticsTracker.DispatchMode.MULTI_THREAD);


        for (ModContainer mod : Loader.instance().getActiveModList())
        {
            String modPage = mod.getModId() + "-" + mod.getVersion() + "-" + FMLCommonHandler.instance().getEffectiveSide();
            tracker.trackPageView(modPage, null, null);
        }

        tracker.completeBackgroundTasks(1000);
    }
}
