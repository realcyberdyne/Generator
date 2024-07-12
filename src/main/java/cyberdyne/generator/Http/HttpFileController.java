package cyberdyne.generator.Http;

import cyberdyne.generator.Http.Models.FileResponseModel;

public class HttpFileController
{

    //Get file by hash address start
    public FileResponseModel GetFile(String FileHash)
    {
//        try
//        {
//            File file = new FileManager_Service().GetFile(FileHash);
//            System.out.println("Adddress is "+file.getPath());
//            if (file.exists())
//            {
//                System.out.println("File exist");
//                byte[] FileBytes = Files.readAllBytes(file.toPath());
//                return new FileResponseModel("200", "image/jpeg", FileBytes,file.getPath());
//            }
//            else
//            {
//                System.out.println("File not exist");
//                return new FileResponseModel("404", "text/json", ("{\"message\":\"can not found file on server\"}").getBytes(),"");
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println("File error");
//            return new FileResponseModel("500", "text/json", ("{\"message\":\"Internal server error\"}").getBytes(),"");
//        }
        return new FileResponseModel("500", "text/json", ("{\"message\":\"Internal server error\"}").getBytes(),"");
    }
    //Get file by hash address end


}
