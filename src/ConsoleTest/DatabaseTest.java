package ConsoleTest;

import java.sql.*;

public class DatabaseTest {


    public static void main(String[] args) {
        String url = "jdbc:sqlite:H://Riset/TugasSister/src/Database/DBMaster.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            String sql = "INSERT INTO BankDB(NomorRek,Nama,Saldo,Lock) VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,12981);
            pstmt.setString(2,"Yudasa");
            pstmt.setInt(3,300000);
            pstmt.setInt(4,0);
            pstmt.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
