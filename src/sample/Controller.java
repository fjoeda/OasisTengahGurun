package sample;

import TransactionHandler.DatabaseHandler;
import TransactionHandler.Transaction;
import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;

import java.net.InetAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class Controller implements Initializable {
    public Label lbl_Yuda_Bal;
    public Label lbl_Yuda_Status;
    public Label lbl_Zulfa_Bal;
    public Label lbl_Zulfa_Status;
    public Label lbl_Henson_Bal;
    public Label lbl_Henson_Status;
    public Label lbl_IP;

    InetAddress ip;
    private AnimationTimer timer,timer2;
    long lastTimerCall = 0;

    Transaction transaction;

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        try{
            transaction = new Transaction();
            System.out.println("Starting Server");
            ip = InetAddress.getLocalHost();
            System.out.println(ip.getHostAddress());
            WebServer webServer = new WebServer(8080,ip);

            XmlRpcServer xmlServer = webServer.getXmlRpcServer();

            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("Transaction2",TransactionHandler.DatabaseHandler.class);

            phm.addHandler("Transaction",TransactionHandler.Transaction.class);
            xmlServer.setHandlerMapping(phm);

            XmlRpcServerConfigImpl serverConfig =
                    (XmlRpcServerConfigImpl) xmlServer.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);

            webServer.start();
            lastTimerCall = System.nanoTime();

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(now > lastTimerCall + 100_000_000L){
                        try {
                            updateData();
                            lbl_IP.setText(readRandomData());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        lastTimerCall = now;
                    }
                }
            };

            timer2 = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(now > lastTimerCall + 100_000_000L){
                        try {
                            lbl_IP.setText(readRandomData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        lastTimerCall = now;
                    }
                }
            };

            timer.start();
            timer2.start();

            updateData();
            System.out.println(transaction.getSaldo(45370));
            System.out.println(transaction.getLock(45370));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateData()throws SQLException{
        lbl_Yuda_Bal.setText(String.valueOf(transaction.getSaldo(45370)));
        lbl_Zulfa_Bal.setText(String.valueOf(transaction.getSaldo(44699)));
        lbl_Henson_Bal.setText(String.valueOf(transaction.getSaldo(45376)));
        lbl_Yuda_Status.setText(getStatus(45370));
        lbl_Zulfa_Status.setText(getStatus(44699));
        lbl_Henson_Status.setText(getStatus(45376));
    }


    private String getStatus(int id) throws SQLException{
        if(transaction.getLock(id)==0)
            return "UNLOCKED";
        else
            return "LOCKED";
    }

    int[] accNumber = {44699,45370,45376};

    private String readRandomData(){
        int rnd1 = new Random().nextInt(3);
        int rnd2 = new Random().nextInt(2);
        if(rnd2==0){
            return String.valueOf(transaction.getSaldo(accNumber[rnd1]));
        }else{
            return String.valueOf(transaction.getName(accNumber[rnd1]));
        }
    }

    
}
