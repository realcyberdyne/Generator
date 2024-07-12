function OnClickLoginButtonEvent(e)
{
    var username = document.getElementById("username_textbox").value;
    var password = document.getElementById("password_textbox").value;

    var http = new XMLHttpRequest();
    var url = '/Login';
    var params = '?username='+username+'&password='+password;
    http.open('POST', url, true);

    //Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    http.setRequestHeader('TTS', '$%12558585^%%vdfvdGGQ52cdsc8585RFVFVV');

    http.onreadystatechange = function()
    {
        if(http.readyState == 4 && http.status == 200)
        {

            var res = JSON.parse(http.responseText);
            if(!res.message.includes("not"))
            {
                // localStorage.setItem("auth",res.token);
                setCookie("auth",res.token,1);
                window.location.href="/Dashboard";
            }
            else
            {
                alert("User not found")
            }

        }
    }

    http.send(params);
}

function OnClickLogotButton(e)
{
    setCookie("auth","",1);
    window.location.href="/";
}

function setCookie(name,value,days)
{
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

function getCookie(name)
{
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name)
{
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}