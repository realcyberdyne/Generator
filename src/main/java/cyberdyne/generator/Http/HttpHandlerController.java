package cyberdyne.generator.Http;

import cyberdyne.generator.Conf.Config;
import cyberdyne.generator.Functions.Hash;
import com.sun.management.OperatingSystemMXBean;
import cyberdyne.generator.Http.Models.ResponseModel;
import cyberdyne.generator.Http.View.HttpView;
import org.json.JSONObject;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class HttpHandlerController
{

    //Get home page start
    public ResponseModel Index()
    {
        return new ResponseModel("200","text/html",new HttpView().View("Index"));
    }
    //Get home page end

}
