package TransactionHandler;

import java.sql.SQLException;

public class Transaction {
    private DatabaseHandler dbHandler = new DatabaseHandler();

    private boolean isLocked(int id) throws SQLException{

        if(dbHandler.getLockFromId(id)==0)
            return false;
        else
            return true;

    }

    private void lock(int id){
        try {
            if(!isLocked(id))
                dbHandler.updateInstance(id,dbHandler.getNameFromId(id),dbHandler.getSaldoFromId(id),id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void unlock(int id){
        try {
            if(isLocked(id))
                dbHandler.updateInstance(id,dbHandler.getNameFromId(id),dbHandler.getSaldoFromId(id),0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addAccount(int NoRek, String name, int Saldo){
        try {
            dbHandler.addInstace(NoRek,name,Saldo,0);

            System.out.println("account added "+name);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deposit(int id, int Saldo){
       try{
           if(!isLocked(id)){
               lock(id);
               int saldoRek = dbHandler.getSaldoFromId(id) + Saldo;
               dbHandler.updateInstance(id,dbHandler.getNameFromId(id),saldoRek,id);
               unlock(id);
               System.out.println("deposit "+String.valueOf(Saldo)+" to "+dbHandler.getNameFromId(id));
               return true;
           }else {
               return false;
           }
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }

    }

    public boolean withdraw(int id, int Saldo){
        try{
            if(!isLocked(id)){
                lock(id);
                int saldoRek = dbHandler.getSaldoFromId(id) - Saldo;
                dbHandler.updateInstance(id,dbHandler.getNameFromId(id),saldoRek,id);
                unlock(id);
                System.out.println("withdraw "+String.valueOf(Saldo)+" from "+dbHandler.getNameFromId(id));
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean transfer(int idAsal,int idTujuan ,int Saldo){
        try{
            if(!isLocked(idAsal)||!isLocked(idTujuan)){
                lock(idAsal);
                lock(idTujuan);
                int saldoAsal = dbHandler.getSaldoFromId(idAsal)-Saldo;
                int saldoTujuan = dbHandler.getSaldoFromId(idTujuan)-Saldo;
                dbHandler.updateInstance(idAsal,dbHandler.getNameFromId(idAsal),saldoAsal,idAsal);
                dbHandler.updateInstance(idTujuan,dbHandler.getNameFromId(idTujuan),saldoTujuan,idTujuan);
                unlock(idAsal);
                unlock(idTujuan);
                System.out.println("Transfer "+String.valueOf(Saldo)+" from "+dbHandler.getNameFromId(idAsal)+
                        " from "+dbHandler.getNameFromId(idTujuan));
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
