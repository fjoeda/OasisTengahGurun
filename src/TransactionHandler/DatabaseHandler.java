package TransactionHandler;

import java.sql.*;

public class DatabaseHandler {

    Connection conn;
    Connection connSlave1;
    Connection connSlave2;
    //private String urlMaster = "jdbc:sqlite:H://Riset/TugasSister/src/Database/DBMaster.db";

    private String urlMaster = "jdbc:sqlite:H://Riset/TugasSister/src/Database/DBMaster.db";
    private String master = "H://Riset/TugasSister/src/Database/DBMaster.db";
    private String urlSlave1 = "H://Riset/TugasSister/src/Database/DBSlave1.db";
    private String urlSlave2 = "H://Riset/TugasSister/src/Database/DBSlave2.db";



    public DatabaseHandler(){
        try {

            //attach();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void attach() throws SQLException{
        conn = DriverManager.getConnection(urlMaster);
        String sql = "ATTACH DATABASE '"+urlSlave2+"' as Slave2";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
        sql = "ATTACH DATABASE '"+urlSlave1+"' as Slave1";
        pstmt = conn.prepareStatement(sql);
        pstmt.execute();
        //conn.close();
    }

    private void replicate(){
        try{
            attach();
            String sql = "DELETE FROM Slave1.BankDB";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            sql = "INSERT INTO Slave1.BankDB SELECT * FROM main.BankDB";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            sql = "DELETE FROM Slave2.BankDB";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            sql = "INSERT INTO Slave2.BankDB SELECT * FROM main.BankDB";
            pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            conn.close();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void addInstace(int NoRek, String name, int Saldo, int Lock) throws SQLException{
        if(conn==null){
            //conn = DriverManager.getConnection(urlMaster);
            attach();
            String sql = "INSERT INTO main.BankDB(NomorRek,Nama,Saldo,Lock) VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,NoRek);
            pstmt.setString(2,name);
            pstmt.setInt(3,Saldo);
            pstmt.setInt(4,Lock);
            pstmt.execute();
            conn.close();
            replicate();
        }
    }

    public void updateInstance(int NoRek, String name, int Saldo, int Lock) throws SQLException{
        //if(conn==null){
            //conn = DriverManager.getConnection(urlMaster);
            attach();
            String sql = "UPDATE main.BankDB  SET  Nama = ?, Saldo = ?, Lock = ? WHERE NomorRek = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setInt(2,Saldo);
            pstmt.setInt(3,Lock);
            pstmt.setInt(4,NoRek);
            pstmt.execute();
            conn.close();
            replicate();
        //}
    }

    public void deleteInstance(int NoRek) throws SQLException{
        if(conn==null){
            //conn = DriverManager.getConnection(urlMaster);
            attach();
            String sql = "DELETE FROM main.BankDB WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,NoRek);
            pstmt.execute();
            conn.close();
            replicate();
        }
    }

    public String getNameFromId(int Id) throws SQLException{
        //if(conn==null){
            //conn = DriverManager.getConnection(urlMaster);
            attach();
            String sql = "SELECT * FROM main.BankDB WHERE NomorRek = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Id);
            ResultSet resultSet = pstmt.executeQuery();
            String Result = "";
            while (resultSet.next()){
                Result = resultSet.getString("Nama");
            }
            conn.close();
            return Result;
        //}else {
       //     return null;
        //}
    }

    public int getSaldoFromId(int Id) throws SQLException{
        //if(conn==null){
            //conn = DriverManager.getConnection(urlMaster);
            attach();
            String sql = "SELECT * FROM BankDB WHERE NomorRek = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Id);
            ResultSet resultSet = pstmt.executeQuery();
            int Result = -1;
            while (resultSet.next()){
                Result = resultSet.getInt("Saldo");
            }

            conn.close();
            return Result;
       // }else {
         //   return -1;
        //}

    }

    public int getLockFromId(int Id) throws SQLException{
       // if(conn==null){
            attach();
            //conn = DriverManager.getConnection(urlMaster);
            String sql = "SELECT * FROM main.BankDB WHERE NomorRek = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,Id);
            ResultSet resultSet = pstmt.executeQuery();
            int Result = -1;
            while (resultSet.next()){
                Result = resultSet.getInt("Lock");
            }
            conn.close();
            return Result;
        //}else return -1;

    }


}
