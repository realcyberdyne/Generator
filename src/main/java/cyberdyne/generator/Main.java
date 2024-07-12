package cyberdyne.generator;

import cyberdyne.generator.Conf.Config;
import cyberdyne.generator.Http.HttpServer;

public class Main
{
    public static void main(String[] args)
    {
        //Log
        System.out.println("Welcome to Cyberdyne");
        System.out.println("Cyberdyne service : get start....");

        //Get read properties values
        new Config();
        System.out.println("Cyberdyne service : Config file is loaded");

        //Get begin http server
        new HttpServer();
        System.out.println("Cyberdyne service : Http server is started");


    }
}