package cyberdyne.generator.Conf;

import java.io.FileReader;
import java.util.Properties;

public class Config
{

    //Global variables
    public static String Root_Dir;
    public static int Http_Port;
    public static boolean Http_Debug;
    public static boolean Http_File_Upload;
    public static boolean Http_Access_File;
    public static String Http_User_Encript_Key;
    public static String Http_Log_File_Address;
    public static int FileSize;
    public static int FileSizeSpli;


    public Config()
    {
        try
        {
            FileReader reader = new FileReader("Protocol.properties");
            Properties properties = new Properties();
            properties.load(reader);

            //Files repository address
            Root_Dir=properties.getProperty("root_dir").toString();

            //Http config
            Http_Port=Integer.parseInt(properties.getProperty("http_port").toString());;
            Http_User_Encript_Key=properties.getProperty("http_user_encript_key").toString();
            Http_Log_File_Address=properties.getProperty("http_log_file_address").toString();
            Http_Debug=Boolean.parseBoolean(properties.getProperty("http_debug"));
            Http_File_Upload=Boolean.parseBoolean(properties.getProperty("http_file_upload"));
            Http_Access_File=Boolean.parseBoolean(properties.getProperty("http_access_file"));

            //File size conf
            FileSize=Integer.parseInt(properties.getProperty("file_size_lim").toString());

            //File spli array size conf
            FileSizeSpli=Integer.parseInt(properties.getProperty("file_spli_size").toString());

        }
        catch (Exception e)
        {
            //Print error
            System.out.println("Config error : "+e.getMessage());
        }

    }

}