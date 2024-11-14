package cyberdyne.generator.Http.Assets;

import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;

public class Assets
{

    public static String Asset(String AssetName)
    {
        try
        {
            String result = Resources.toString(Resources.getResource("Assets"+AssetName), Charsets.UTF_8);
            return result;
        }
        catch (Exception e)
        {
            return "Internal server error : "+e.getMessage();
        }
    }

    public static File AssetFile(String AssetName) throws Exception
    {
        if(cyberdyne.generator.Conf.Config.Http_Debug)
        {
            return new File(Resources.getResource("Assets"+AssetName).toURI());
        }
        else
        {
            String path = Resources.getResource("Assets"+AssetName).toString();

            path = path.replace("jar:", "");
            path = path.replace("file:", "");
            path = path.replace("java:", "");

            
            String temp = path.split(".jar!")[0];
            String temp2 = temp.split("/")[temp.split("/").length - 1];
            System.out.println(temp);
            System.out.println(temp2);

            path = temp.replace(temp2, "");
            path = path + "/classes/" + "Assets"+AssetName;
            
            return new File(path);
        }
    }

}
