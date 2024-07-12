package cyberdyne.generator.Http;

import cyberdyne.generator.Conf.Config;
import cyberdyne.generator.Http.Assets.Assets;
import cyberdyne.generator.Http.File.HTTPFiles;
import cyberdyne.generator.Http.Models.FileResponseModel;
import cyberdyne.generator.Http.Models.ResponseModel;
import cyberdyne.generator.Http.View.HttpView;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;


public class HttpServer
{

    //Global variables
    ServerSocket HttpSocket;


    //Constrator function start
    public HttpServer()
    {

        //Get start http sever
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get start http
                BeginHttp();

            }
        }).start();

    }
    //Constrator function end

    //Get begin server socket start
    public void BeginHttp()
    {
        try
        {

            //Get initilze server socket
            HttpSocket = new ServerSocket(Config.Http_Port);

            //Get Log
            System.out.println("Http socket is ready on port "+Config.Http_Port);

            //Get wait for request
            while(true)
            {
                //Get new request
                Socket request=HttpSocket.accept();

                //Get Log
                //InetSocketAddress socketAddress = (InetSocketAddress) request.getRemoteSocketAddress();
                //System.out.println("Ip is "+socketAddress.getAddress().getHostAddress());

                //Get handle multi request
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(Config.Http_File_Upload)
                        {
                            //Get handle request
                            GetSocketFileHandller(request);
                        }
                        else
                        {
                            //Get handle request
                            GetSocketHandller(request);
                        }

                    }
                }).start();

            }

        }
        catch (Exception e)
        {
            //Get error log
            System.out.println("Http Sever Error : "+e.getMessage());
        }
    }
    //Get begin server socket end


    //Get socket handller function start
    public void GetSocketHandller(Socket request)
    {
        try
        {

            //Get Log
//        System.out.println("New Request from "+request.getRemoteSocketAddress().toString());

            //Get request and response data stream
            DataInputStream input = new DataInputStream(request.getInputStream());
            DataOutputStream output = new DataOutputStream(request.getOutputStream());

            //Get read request from user
            byte[] request_text = new byte[4096];
            input.read(request_text);
            String request_value = new String(request_text);

            if(request_value.contains("GET ") && request_value.split("\n")[0].split(" ")[1].contains("."))
            {
                if(Config.Http_Access_File)
                {
                    try
                    {
                        File requested_file = Assets.AssetFile(request_value.split("\n")[0].split(" ")[1]);

                        if (requested_file.exists())
                        {
                            byte[] FileBytes = Files.readAllBytes(requested_file.toPath());

                            //Get file
                            FileResponseModel response = new FileResponseModel();
                            response.setStatusCode("200");
                            response.setContentType(RequestDetector.GetResponseContentTypeStaticByRequest(request_value));
                            response.setPath(requested_file.getPath());
                            response.setContent(FileBytes);

                            File fileToSend = new File(response.getPath());

                            //Http response
                            String HttpResponse = "HTTP/1.1 " + response.getStatusCode()
                                    + "\nContent-type:" + Files.probeContentType(fileToSend.toPath())
                                    + "\n\n";

                            //get response
                            if (!response.getPath().trim().equals(""))
                            {
                                output.write(HttpResponse.getBytes());
                                output.write(response.getContent());
                                output.flush();
                            }
                        }
                        else
                        {
                            //Http response
                            String HttpResponse = "HTTP/1.1 404"
                                    + "\nContent-type: text/html"
                                    + "\n\n"
                                    + HttpView.View("404");

                            output.write(HttpResponse.getBytes());
                        }

                    }
                    catch (Exception e)
                    {
                        //Http response
                        String HttpResponse = "HTTP/1.1 500"
                                + "\nContent-type: text/html"
                                + "\n\n"
                                + HttpView.View("404");

                        output.write(HttpResponse.getBytes());
                    }
                }
                else
                {

                    //Http response
                    String HttpResponse = "HTTP/1.1 403"
                            + "\nContent-type: text/html"
                            + "\n\n"
                            + HttpView.View("403");

                    output.write(HttpResponse.getBytes());
                }
            }
            else
            {
                //Get handle request
                ResponseModel response = GetHandleRequest(request_value);

                //Http response
                String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                        + "\nContent-type:" + response.getContentType()
                        + "\nAccess-Control-Allow-Origin : * "
                        + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                        + "\nAccess-Control-Allow-Credentials : true "
                        + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                        + "\n\n" + response.getContent();

                //get response
                output.write(HttpResponse.getBytes());
            }

            //Get close all socket and streams
            input.close();
            output.close();
            request.close();

        }
        catch (Exception e)
        {
            System.out.println("Error on http request handller " + e.getMessage());
        }

    }
    //Get socket handller function end


    //Get socket handller with file upload function start
    public void GetSocketFileHandller(Socket request)
    {
        try
        {

            //Get Log
//        System.out.println("New Request from "+request.getRemoteSocketAddress().toString());

            //Get request and response data stream
            DataInputStream input = new DataInputStream(request.getInputStream());
            DataOutputStream output = new DataOutputStream(request.getOutputStream());

            //Get read request from user
            byte[] request_text = new byte[4096];
            input.read(request_text);
            String request_value = new String(request_text);


            //Get check request file
            if(request_value.contains("Content-Type: multipart/form-data;") && request_value.contains("POST /FileUpload"))
            {
                //Get check user is auth
                if(request_value.contains("Auth"))
                {

                    String Boundary = GetBoundary(request_value).replace("=","").replace("boundary","");
                    String FileExtention = GetFileExtention(request_value.split(Boundary)[2]).split("\\.")[1].replace("\"","");
                    int FileSize=GetFileSize(request_value);

//                    System.out.println("File size is "+FileSize);

                    //Get check file size
                    if(FileSize <= Config.FileSize)
                    {

                        byte file_all_result[]=new byte[Config.FileSize];
                        byte[] request_text_append = new byte[Config.FileSizeSpli];
                        ByteBuffer bf = ByteBuffer.wrap(file_all_result);
                        bf.put(request_text);

                        while(true)
                        {
                            try
                            {

                                int input_bytes=input.read(request_text_append);
                                bf.put(request_text_append);
                                if(input_bytes<Config.FileSizeSpli)
                                {
                                    break;
                                }

                            }
                            catch (Exception e)
                            {
                                System.out.println("Error loop "+e.getMessage());
                                break;
                            }
                        }

                        HTTPFiles.CreateFile(file_all_result,FileExtention);

                        //Get response
                        ResponseModel response=new ResponseModel("200","text/json","{\"message\":\"File Uploaded\"}");

                        //Http response
                        String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                                + "\nContent-type:" + response.getContentType()
                                + "\nAccess-Control-Allow-Origin : * "
                                + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                                + "\nAccess-Control-Allow-Credentials : true "
                                + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                                + "\n\n" + response.getContent();

                        //get response
                        output.write(HttpResponse.getBytes());

                    }
                    else
                    {
                        //Get response
                        ResponseModel response=new ResponseModel("500","text/json","{\"message\":\"File size is big\"}");

                        //Http response
                        String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                                + "\nContent-type:" + response.getContentType()
                                + "\nAccess-Control-Allow-Origin : * "
                                + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                                + "\nAccess-Control-Allow-Credentials : true "
                                + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                                + "\n\n" + response.getContent();

                        //get response
                        output.write(HttpResponse.getBytes());
                    }
                }
                else
                {
                    //Get response
                    ResponseModel response=new ResponseModel("403","text/json","{\"message\":\"You not authenticated\"}");

                    //Http response
                    String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                            + "\nContent-type:" + response.getContentType()
                            + "\nAccess-Control-Allow-Origin : * "
                            + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                            + "\nAccess-Control-Allow-Credentials : true "
                            + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                            + "\n\n" + response.getContent();

                    //get response
                    output.write(HttpResponse.getBytes());
                }
            }
            else if(request_value.contains("GET /File/"))
            {
                //Get file
                FileResponseModel response=new HttpFileController().GetFile(GetParametsByUrl("/File",request_value));

                File fileToSend = new File(response.getPath());

                //Http response
                String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                        + "\nContent-type:" + Files.probeContentType(fileToSend.toPath())
                        + "\n\n";

                //get response
                if(!response.getPath().trim().equals(""))
                {
                    output.write(HttpResponse.getBytes());
                    output.write(response.getContent());
                    output.flush();
                }

            }
            else if(request_value.contains("GET ") && request_value.split("\n")[0].split(" ")[1].contains("."))
            {
                if(Config.Http_Access_File)
                {
                    try
                    {
                        File requested_file = Assets.AssetFile(request_value.split("\n")[0].split(" ")[1]);

                        if (requested_file.exists())
                        {
                            byte[] FileBytes = Files.readAllBytes(requested_file.toPath());

                            //Get file
                            FileResponseModel response = new FileResponseModel();
                            response.setStatusCode("200");
                            response.setContentType(RequestDetector.GetResponseContentTypeStaticByRequest(request_value));
                            response.setPath(requested_file.getPath());
                            response.setContent(FileBytes);

                            File fileToSend = new File(response.getPath());

                            //Http response
                            String HttpResponse = "HTTP/1.1 " + response.getStatusCode()
                                    + "\nContent-type:" + Files.probeContentType(fileToSend.toPath())
                                    + "\n\n";

                            //get response
                            if (!response.getPath().trim().equals(""))
                            {
                                output.write(HttpResponse.getBytes());
                                output.write(response.getContent());
                                output.flush();
                            }
                        }
                        else
                        {
                            //Http response
                            String HttpResponse = "HTTP/1.1 404"
                                    + "\nContent-type: text/html"
                                    + "\n\n"
                                    + HttpView.View("404");

                            output.write(HttpResponse.getBytes());
                        }

                    }
                    catch (Exception e)
                    {
                        //Http response
                        String HttpResponse = "HTTP/1.1 500"
                                + "\nContent-type: text/html"
                                + "\n\n"
                                + HttpView.View("404");

                        output.write(HttpResponse.getBytes());
                    }
                }
                else
                {

                    //Http response
                    String HttpResponse = "HTTP/1.1 403"
                            + "\nContent-type: text/html"
                            + "\n\n"
                            + HttpView.View("403");

                    output.write(HttpResponse.getBytes());
                }
            }
            else
            {
                //Get handle request
                ResponseModel response = GetHandleRequest(request_value);

                //Http response
                String HttpResponse="HTTP/1.1 " + response.getStatusCode()
                        + "\nContent-type:" + response.getContentType()
                        + "\nAccess-Control-Allow-Origin : * "
                        + "\nAccess-Control-Allow-Headers : origin, content-type, accept, authorization "
                        + "\nAccess-Control-Allow-Credentials : true "
                        + "\nAccess-Control-Allow-Methods : GET, POST, PUT, DELETE, OPTIONS, HEAD "
                        + "\n\n" + response.getContent();

                //get response
                output.write(HttpResponse.getBytes());
            }

            //Get close all socket and streams
            input.close();
            output.close();
            request.close();

        }
        catch (Exception e)
        {
            System.out.println("Error on http request handller " + e.getMessage());
        }

    }
    //Get socket handller with file upload function end


    //Request handler function start
    public ResponseModel GetHandleRequest(String HttpRequest)
    {
//        System.out.println(HttpRequest.trim());

        String []requests=HttpRequest.split("\n");

        //First line splite
        String []FirstLine=requests[0].split(" ");

//        System.out.println(FirstLine[0]);

        JSONObject Headers = GetHttpHeaders(HttpRequest);

        if(FirstLine[0].equals("POST") || FirstLine[0].equals("OPTIONS"))
        {
            System.out.println("Post request : "+requests[0]);
            AddLog("Post request : "+requests[0]);
            return Routes.GetHandlePostMethod(HttpRequest,requests,Headers);
        }
        else if(FirstLine[0].equals("GET"))
        {
            System.out.println("Get request : "+requests[0]);
            AddLog("Get request : "+requests[0]);
            return Routes.GetHandleGetMethod(HttpRequest,requests,Headers);
        }
        else
        {
            return new ResponseModel("404","text/json","{\"message\":\"not found\"}");
        }

    }
    //Request handler function end

    //Get all headers function start
    public JSONObject GetHttpHeaders(String HttpRequest)
    {
        JSONObject result=new JSONObject();

        //Get split all http request by "\n"
        String []split_request=HttpRequest.split("\n");

        for (int i=1;i<split_request.length;i++)
        {
            try
            {
//                System.out.println("Header " + split_request[i].split(":")[0] + "-" + split_request[i].split(":")[1]);
                result.put(split_request[i].split(":")[0].trim(),split_request[i].split(":")[1].trim());
            }
            catch (Exception e)
            {

            }
        }
//        System.out.println(result.toString());
        return result;
    }
    //Get all headers function end


    //Get parametrs function start
    public String GetParametsByUrl(String Parametr,String URL)
    {
        URL=URL.split("\n")[0].split(" ")[1];
        System.out.println("URL is "+URL );
        String [] url_sections = URL.split("\n")[0].split(Parametr+"/");
        return url_sections[1];
    }
    //Get parametrs function end


    //Http log file function start
    public void AddLog(String request)
    {
        try
        {
            //Initilze log file
            File LogFile=new File(Config.Http_Log_File_Address);
            LogFile.createNewFile();

            //Write request log on file
            if(LogFile.canWrite())
            {
                PrintWriter Writer=new PrintWriter(new BufferedWriter(new FileWriter(LogFile,true)));
                Writer.append(request + "\n\r******************************************************************************************************************************\n\r");
                Writer.close();
            }

        }
        catch (Exception e)
        {
            System.out.println("Error on Logfile : "+e.getMessage());
        }
    }
    //Http log file function end


    //Get http content size function start
    private static int GetContentLength(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Length: ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return Integer.parseInt(requestHeaders.substring(start, end).trim());
    }
    //Get http content size function end


    //Get boundary function start
    private static String GetBoundary(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Type: multipart/form-data; ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return requestHeaders.substring(start, end).trim();
    }
    //Get boundary function end


    //Get file extention function start
    private static String GetFileExtention(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Type: multipart/form-data; ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return requestHeaders.substring(start, end).trim();
    }
    //Get file extention function end


    //Get file size function start
    private static int GetFileSize(String requestHeaders)
    {
        final String contentLengthHeader = "Content-Length: ";
        int start = requestHeaders.indexOf(contentLengthHeader) + contentLengthHeader.length();
        int end = requestHeaders.indexOf("\r\n", start);
        return Integer.parseInt(requestHeaders.substring(start, end).trim());
    }
    //Get file size function end


    //Get remove lines of string funcition start
    private static String GetRemoveLines(String value,int from)
    {
        String result="";
        String []lines=value.split("\n");

        for (int i=from;i<lines.length;i++)
            result+=lines[i]+"\r\n";

        return result;
    }
    //Get remove lines of string funcition end


}
