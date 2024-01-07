package Klub.DbConnection;


import Klub.GunsCollection;
import Klub.Member_list;

import java.sql.*;

public class DbConn{

    public Connection connection;
    public Statement statement;
    public ResultSet result;
    public static DbConn handler = null;
    private static final String DATABASE_URL = "jdbc:mysql://localhost/klub_strzelecki";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";
//    private static final String SELECT_QUERY_LOGIN = "SELECT * FROM login WHERE name = ? and password = ?";
    private static final String INSERT_QUERY_GUN = "INSERT INTO `bron` (`rodzaj`, `typ`, `marka`, `kaliber`, `nr_fabryczny`, `rok_produkcji`) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_QUERY_MEMBER = "INSERT INTO `klubowicze` (`imie`, `nazwisko`, `plec`, `data_urodzenia`, `miejsce_urodzenia`, `email`, `nr_telefonu`, `adres_zamieszkania`, `pesel`, `data_wstapienia`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_GUNS = "DELETE FROM `bron` WHERE nr_fabryczny = ?";
    private static final String IS_GUN_ISSUED = "SELECT COUNT(*) FROM `uzyczenia` WHERE bronId = ?";
    private static final String UPDATE_GUN = "UPDATE `bron` SET rodzaj = ?,typ = ?, marka = ?, kaliber = ?, rok_produkcji = ? WHERE nr_fabryczny = ?";
    private static final String DELETE_MEMBER = "DELETE FROM `klubowicze` WHERE pesel = ?";
    private static final String DID_MEMBER_ISSUED_GUNS = "SELECT COUNT(*) FROM `uzyczenia` WHERE klubowiczId = ?";
    private static final String UPDATE_MEMBER = "UPDATE `klubowicze` SET imie = ? ,nazwisko = ? , plec = ?, data_urodzenia = ?, miejsce_urodzenia = ?, email = ?, nr_telefonu = ?, adres_zamieszkania = ?, data_wstapienia = ? WHERE pesel = ?";


    public static String UserId;

    public DbConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
              e.printStackTrace();
        }
    }

    //Books inserteion Metthod

    public int insert_Guns_query_Executer(String kind, String type, String brand, String caliber, String number, String productionDate) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_GUN) ;
            preparedStatement.setString(1, kind);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, brand);
            preparedStatement.setString(4, caliber);
            preparedStatement.setString(5, number);
            preparedStatement.setString(6, productionDate);
//            preparedStatement.setString(7, Date);
    
            System.out.println(preparedStatement);
              int resultSet =  preparedStatement.executeUpdate();
              return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Member insertion
    public int insert_Member_query_Executer(String name, String lastName, String gender, String birthDate, String birthPlace, String email, String phone, String address, String pesel, String date) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_MEMBER) ;
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, birthDate);
            preparedStatement.setString(5, birthPlace);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, phone);
            preparedStatement.setString(8, address);
            preparedStatement.setString(9, pesel);
            preparedStatement.setString(10, date);

            System.out.println(preparedStatement);
            int resultSet =  preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Guns deletion
    public boolean DeleteGuns(GunsCollection.Guns gun){
        try {
             PreparedStatement statement = connection.prepareStatement(DELETE_GUNS);
             statement.setString(1,gun.getNumber());
             int result = statement.executeUpdate();
             if(result ==  1){
                 return true;
             }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //checking issued Gun that it already exist
    public boolean IsGunAlreadyIssued(GunsCollection.Guns guns){
        try {
            PreparedStatement statement = connection.prepareStatement(IS_GUN_ISSUED);
            statement.setString(1,guns.getNumber());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                System.out.println("Already issued : "+count);
                return count > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Update Book
    public boolean updateGun(GunsCollection.Guns gun){
       try{
           PreparedStatement statement = connection.prepareStatement(UPDATE_GUN);
           statement.setString(1, gun.getKind());
           statement.setString(2, gun.getType());
           statement.setString(3, gun.getBrand());
           statement.setString(4, gun.getCaliber());
           statement.setString(5, gun.getProductionDate());
//           statement.setString(6, gun.getInsertdate());
           statement.setString(6, gun.getNumber());
           System.out.println("DbConn " + statement);
           int result = statement.executeUpdate();
           return (result>0);
       }catch (SQLException e){
           e.printStackTrace();
       }
       return false;
    }

    //Members crud

    //Member deletion
    public boolean DeleteMembers(Member_list.Members members){
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_MEMBER);
            statement.setString(1,members.getPesel());
            int result = statement.executeUpdate();
            if(result ==  1){
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean IsMemberIsAlreadyIssued(Member_list.Members members){
        try {
            PreparedStatement statement = connection.prepareStatement(DID_MEMBER_ISSUED_GUNS);
            statement.setString(1, members.getPesel());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                System.out.println("Already issued : "+count);
                return count > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Update Member
    public boolean updateMember(Member_list.Members members){
        try{
            PreparedStatement statement = connection.prepareStatement(UPDATE_MEMBER);

            statement.setString(1, members.getName());
            statement.setString(2, members.getLastName());
            statement.setString(3, members.getGender());
            statement.setDate(4, members.getBirthDate());
            statement.setString(5, members.getBirthPlace());
            statement.setString(6, members.getEmail());
            statement.setString(7, members.getPhone());
            statement.setString(8, members.getAddress());

            statement.setDate(9, members.getDate());
//            statement.setDate(9, members.getDate());

            statement.setString(10, members.getPesel());
            //pesel jako ostatni bo update where pesel = ''
            //inaczej (statement) sqlowy UPDATE zamienia pesel z data
            System.out.println("fff "+ statement );

            int result = statement.executeUpdate();
            return (result>0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet execQuery(String query){
        try{
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        }
         catch(SQLException e){
         printSQLException(e);
         return null;
        }
        finally { }
          return result;
    }

    public boolean execAction(String query){
        ResultSet resultSet;
        try{
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        }catch (SQLException exception){
            printSQLException(exception);
            System.out.println("Error Message Of DataBase Connection Class : "+exception.getLocalizedMessage());
            return false;
        }finally {

        }
    }

    //get Instance

    public static DbConn getInstance(){
        if(handler == null){
            handler = new DbConn();
        }
        return handler;
    }

    //print the error
    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
