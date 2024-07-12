package cyberdyne.generator.Http.Models;

public class ResponseModel
{

    String StatusCode;
    String ContentType;
    Object Content;

    public ResponseModel()
    {
    }

    public ResponseModel(String statusCode, String contentType, String content)
    {
        StatusCode = statusCode;
        ContentType = contentType;
        Content = content;
    }

    public String getStatusCode()
    {
        return StatusCode;
    }

    public void setStatusCode(String statusCode)
    {
        StatusCode = statusCode;
    }

    public String getContentType()
    {
        return ContentType;
    }

    public void setContentType(String contentType)
    {
        ContentType = contentType;
    }

    public Object getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        Content = content;
    }

}
