package cyberdyne.generator.Http.View;

import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;

public class HttpView
{
    //Get view function start
    public static String View(String ViewName)
    {
        try
        {
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            String result = Resources.toString(Resources.getResource("View/"+ViewName+".html"), Charsets.UTF_8);

            //Get check include file
            if(result.contains("@include('"))
            {
                String lines[]=result.split("\n");
                for(int i=0;i<lines.length;i++)
                {
                    if(lines[i].contains("@include('"))
                    {
                        int index_qoute_start=lines[i].indexOf("'")+1;
                        int index_qoute_end=lines[i].indexOf("'",index_qoute_start+1);

                        String include_value = Resources.toString(Resources.getResource("View/"+lines[i].substring(index_qoute_start,index_qoute_end)+".html"), Charsets.UTF_8);

                        //replace include value
                        result = result.replace("@include('"+lines[i].substring(index_qoute_start,index_qoute_end)+"')",include_value);
                    }
                }
            }

            return result;
        }
        catch (Exception e)
        {
            return "Internal server error : "+e.getMessage();
        }
    }
    //Get view function end

}
