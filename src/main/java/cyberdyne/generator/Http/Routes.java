package cyberdyne.generator.Http;

import cyberdyne.generator.Functions.Hash;
import cyberdyne.generator.Http.Models.ResponseModel;
import cyberdyne.generator.Http.Models.UserAuthModel;
import cyberdyne.generator.Http.View.HttpView;
import org.json.JSONObject;
import cyberdyne.generator.Conf.Config;

import java.time.LocalDate;
import java.time.LocalTime;

public class Routes
{

    //Get handle Get request function start
    public static ResponseModel GetHandleGetMethod(String request,String []requests,JSONObject Header)
    {
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //parametrs
        JSONObject parametrs_json=new JSONObject();
        if(request_path.toString().contains("?"))
        {
            String all_parametrs = request_path.toString().split("\\?")[1];
            String[] parametrs = all_parametrs.split("&");

            for (int i = 0; i < parametrs.length; i++) {
                String data[] = parametrs[i].split("=");
                parametrs_json.put(data[0], data[1]);
            }
        }

        //Get Routes
        switch (request_path)
        {
            case "/":
                response=new HttpHandlerController().Index();
        }

        return response;
    }
    //Get handle Get request function end


    //Get handle Post request function start
    public static ResponseModel GetHandlePostMethod(String request, String []requests, JSONObject Header)
    {
        System.out.println(request.toString().trim());
        ResponseModel response=new ResponseModel();

        //Get splite first line
        String request_path=requests[0].split(" ")[1];

        //parametrs
        JSONObject parametrs_json=new JSONObject();
        if(request_path.toString().contains("?"))
        {
            String all_parametrs = request_path.toString().split("\\?")[1];
            String[] parametrs = all_parametrs.split("&");

            for (int i = 0; i < parametrs.length; i++)
            {
                String data[] = parametrs[i].split("=");
                parametrs_json.put(data[0], data[1]);
            }
        }

        if(parametrs_json.isEmpty())
        {
            String request_data_line=request.toString().trim().split("\n")[request.toString().trim().split("\n").length-1];

            String []para_sam=request_data_line.toString().split("\\?");
            String all_parametrs=para_sam[para_sam.length - 1];


            String[] parametrs;
            if(all_parametrs.isEmpty())
                parametrs = request_data_line.toString().split("&");
            else
                parametrs = all_parametrs.split("&");

            for (int i = 0; i < parametrs.length; i++)
            {
                String data[] = parametrs[i].split("=");
                parametrs_json.put(data[0], data[1]);
            }
        }

        //Get Routes
//        switch (request_path.split("\\?")[0])
//        {
//            case "/Login":
//                response=new HttpHandlerController().LoginDone(parametrs_json,Header);
//                break;
//            case "/LoginDone":
//                if(GetApiAuthCheckCookie(Header))
//                    response=new HttpHandlerController().AddHubDone(parametrs_json,Header);
//                else
//                    response=new ResponseModel("403","text/html", HttpView.View("403"));
//                break;
//        }

        return response;
    }
    //Get handle Post request function end


    //Get auth check middleware function start
    public static boolean GetApiAuthCheck(JSONObject Headers)
    {
        try
        {
            String AuthToken = Headers.get("auth").toString();
            new UserAuthModel(AuthToken);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Auth Error");
        }

        return false;
    }
    //Get auth check middleware function end


    //Get auth check middleware function start
    public static boolean GetApiAuthCheckCookie(JSONObject Headers)
    {
        try
        {
            String []Cookies=Headers.get("Cookie").toString().split(";");
            String AuthVal="";
            for (int i=0;i<Cookies.length;i++)
            {
                if(Cookies[i].contains("auth="))
                {
                    AuthVal=Cookies[i].split("=")[1];
                }
            }

            if(AuthVal.isEmpty())
            {
                return false;
            }
            else
            {
                //Get generate login token
                String token = (LocalDate.now().getDayOfMonth() * 850 + LocalTime.now().getHour() * 20) + Config.Http_User_Encript_Key;
                token = Hash.GetSha256(token);


                if(AuthVal.equals(token))
                {
                    return true;
                }
                else
                {
                    return false;
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("Auth Error");
        }

        return false;
    }
    //Get auth check middleware function end



}
