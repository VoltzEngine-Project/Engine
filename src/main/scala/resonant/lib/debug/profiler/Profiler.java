package resonant.lib.debug.profiler;

import resonant.lib.utility.TextUtility;

import java.util.HashMap;

/** Collections several profile runs
 * Created by robert on 12/10/2014.
 */
public class Profiler
{
    public final String name;
    public HashMap<String, RunProfile> profileRuns = new HashMap();

    public Profiler(String name)
    {
        this.name = name;
    }

    public synchronized RunProfile run(String runName)
    {
        profileRuns.put(runName, new RunProfile(runName));
        return profileRuns.get(runName);
    }

    public StringBuilder getOutputSimple()
    {
        StringBuilder string = new StringBuilder();
        String head = "=== " + name + " ===";
        string.append(head);

        String end = TextUtility.padLeft("", head.length()).replace(" ", "=");
        string.append(end);
        return string;
    }
}
