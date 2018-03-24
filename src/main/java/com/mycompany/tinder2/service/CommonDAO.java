package com.mycompany.tinder2.service;

import com.mycompany.tinder2.model.vk.UserVK;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author delet
 */
@Component
public class CommonDAO {
     // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/demonet";
    private static final String user = "root";
    private static final String password = "12345";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    
    
    public List<UserVK> getUsers(List<String> ids){
        List<UserVK> result = new ArrayList<UserVK>();
    
        String query = "select count(*) from users";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total number of books in the table : " + count);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        
        return result;
    }
    
    public void setUsers(List<UserVK> users){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO demonet.users")
           .append("(id,")
           .append("first_name,")
           .append("last_name,")
           .append("relation_partner,")
           .append("maiden_name,")
           .append("photo_100,")
           .append("sex,")
           .append("relation,")
           .append("deactivated)")
           .append("VALUES");
       
        for(UserVK  user: users){
            sql.append("(")
               .append("'").append(user.getId()).append("'")
               .append(",").append("'").append(user.getFirstName()).append("'")
               .append(",").append("'").append(user.getLastName()).append("'")
               .append(",").append("'").append(user.getRelationPartner()).append("'")
               .append(",").append("'").append(user.getMaidenName()).append("'")
               .append(",").append("'").append(user.getPhotoURL()).append("'")
               .append(",").append("'").append(user.getSex()).append("'")
               .append(",").append(user.getDeactivated())
               .append(",").append("'").append(user.getDeactivated()).append("'")
               .append(")").append(",");
         }

         String query = sql.substring(0, sql.length()-1).toString();
                
                

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            stmt.executeUpdate(query);

            while (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total number of books in the table : " + count);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        
       
    }
    
    
    public static void main(String[] args){
        CommonDAO cd = new CommonDAO();
        cd.setUsers(new ArrayList<UserVK>());
    }
    
}
