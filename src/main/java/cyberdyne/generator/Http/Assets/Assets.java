package cyberdyne.generator.Http.Assets;

import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Assets
{

    public static String Asset(String AssetName)
    {
        try
        {
            String result = Resources.toString(Resources.getResource("Assets/"+AssetName), Charsets.UTF_8);
            return result;
        }
        catch (Exception e)
        {
            return "Internal server error : "+e.getMessage();
        }
    }

    public static File AssetFile(String AssetName) throws Exception
    {
        return new File(Resources.getResource("Assets/"+AssetName).toURI());
    }

}
