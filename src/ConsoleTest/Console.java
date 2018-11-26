package ConsoleTest;

import TransactionHandler.Transaction;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;


import java.net.Inet4Address;
import java.net.InetAddress;


public class Console extends Application{


    InetAddress ip;
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            System.out.println("Starting Server");
            ip = InetAddress.getLocalHost();
            System.out.println(ip.getHostAddress());
            WebServer webServer = new WebServer(8080,ip);

            XmlRpcServer xmlServer = webServer.getXmlRpcServer();

            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("Calculate",TransactionHandler.CalculationHandler.class);

            phm.addHandler("Transaction",TransactionHandler.Transaction.class);
            phm.addHandler("Transaction2",TransactionHandler.DatabaseHandler.class);
            xmlServer.setHandlerMapping(phm);

            XmlRpcServerConfigImpl serverConfig =
                    (XmlRpcServerConfigImpl) xmlServer.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);

            webServer.start();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
