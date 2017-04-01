package com.builtbroken.mc.codegen.processors;

import com.builtbroken.mc.codegen.processors.data.Field;
import com.builtbroken.mc.codegen.processors.data.Method;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/1/2017.
 */
public class Processor
{
    private List<String> imports;
    private List<String> interfaces;
    private HashMap<String, Method> methods = new HashMap();
    private HashMap<String, Field> fields = new HashMap();

    public void loadFile(File file)
    {
        //TODO load the file
        //TODO parse out all data
        //TODO ensure extends TileEntityWrapper and nothing else
    }

    public List<String> getImports()
    {
        return imports;
    }
}
