package cyberdyne.generator.Http;

public class RequestDetector
{


    //Get static response type function start
    public static String GetResponseContentTypeStaticRequest(String request_path)
    {
        if(request_path.contains(".js") || request_path.contains(".css"))
        {
            return "text/"+request_path.split("\\.")[1];
        }
        else if(request_path.contains(".png") || request_path.contains(".jpeg") || request_path.contains(".jpg"))
        {
            return "image/"+request_path.split("\\.")[1];
        }
        else
        {
            return "file/*";
        }
    }
    //Get static response type function end


    //Get static response type function start
    public static String GetResponseContentTypeStaticByRequest(String request)
    {
        String request_path = request.split("\n")[0].split(" ")[1];

        if(request_path.contains(".js") || request_path.contains(".css"))
        {
            return "text/"+request_path.split("\\.")[1];
        }
        else if(request_path.contains(".png") || request_path.contains(".jpeg") || request_path.contains(".jpg"))
        {
            return "image/"+request_path.split("\\.")[1];
        }
        else
        {
            return "file/*";
        }
    }
    //Get static response type function end


}
