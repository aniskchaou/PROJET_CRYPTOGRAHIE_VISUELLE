
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

public class DataBaseManagement {

    //initalisation
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    String user = null;

    public String readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from feedback.comments");
            // writeResultSet(resultSet);
            while (resultSet.next()) {
                String puser = resultSet.getString("myuser");
                System.out.println(" user " + puser);
                this.user = puser;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        return this.user;
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    void addvalue() throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  feedback.comments values (default, ?)");
            preparedStatement.setString(1, "hello");

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void generate_images(String token, String key2, String key1) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  projet_securite.current_captcha values (?, ?,?,?)");

            preparedStatement.setString(1, token);
            preparedStatement.setString(2, key1);
            preparedStatement.setString(3, key2);
            preparedStatement.setInt(4, 1);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int key_generation() throws ClassNotFoundException, SQLException {

        Random rand = new Random();

        String captcha[] = {"5E6Z", "5SK3", "66YN", "YZB3"};
        int rand_captcha = rand.nextInt(captcha.length - 1) + 1;
        int rand_code = rand.nextInt(65568) + 1;
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            preparedStatement = connect
                    .prepareStatement("insert into  projet_securite.key_generation values (?, ?,?,?,?,?,?,?)");

            preparedStatement.setInt(1, rand_code);
            preparedStatement.setString(2, captcha[rand_captcha] + ".png");
            preparedStatement.setString(3, "key1_" + rand_code + ".png");
            preparedStatement.setString(4, "key2_" + rand_code + ".png");
            preparedStatement.setString(5, "gen_" + rand_code + ".png");
            preparedStatement.setInt(6, 1);
            preparedStatement.setString(7, captcha[rand_captcha]);
            preparedStatement.setInt(8, 1);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rand_code;
    }

    String find_captcha() {
        String vtoken = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.current_captcha where generate = 1");
            // writeResultSet(resultSet);
            while (resultSet.next()) {
                String token = resultSet.getString("token");
                System.out.println(" token " + token);
                vtoken = token;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vtoken != null) {
            return vtoken;
        } else {
            return null;
        }
    }

    String find_captcha_check() {
        String vtoken = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.key_generation where check_key = 0");
            // writeResultSet(resultSet);
            while (resultSet.next()) {
                //retrieve data
                String token = resultSet.getString("code");

                vtoken = token;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return data
        if (vtoken != null) {
            return vtoken;
        } else {
            return null;
        }
    }

    String find_key1(String token) {
        String vkey1 = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.current_captcha where token = '" + token + "'");
            // writeResultSet(resultSet);
            while (resultSet.next()) {

                String key1 = resultSet.getString("key1");
                System.out.println(" key1 " + key1);
                vkey1 = key1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vkey1 != null) {
            return vkey1;
        } else {
            return null;
        }
    }

    String find_key2(String token) {
        String vkey2 = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.current_captcha where token = '" + token + "'");
            // writeResultSet(resultSet);
            while (resultSet.next()) {

                String key2 = resultSet.getString("key2");
                System.out.println(" key2 " + key2);
                vkey2 = key2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vkey2 != null) {
            return vkey2;
        } else {
            return null;
        }
    }

    void finish_generation(String token) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("UPDATE projet_securite.current_captcha  SET generate =? WHERE token=?");

            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String check_capcha(String code) {
        String code_captcha = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.captcha where uncrypted_image = '" + code + "'");
            // writeResultSet(resultSet);
            while (resultSet.next()) {

                String key1 = resultSet.getString("uncrypted_image");

                code_captcha = key1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (code_captcha != null) {
            return code_captcha;
        } else {
            return null;
        }
    }

    public String find_key_generation() {
        String vcode = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.key_generation where generation = 1");
            // writeResultSet(resultSet);
            while (resultSet.next()) {

                String code = resultSet.getString("code");
                System.out.println(" token " + code);
                vcode = code;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vcode != null) {
            return vcode;
        } else {
            return null;
        }
    }

    public String get_captcha_from_code(String code) {
        String vcaptcha = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.key_generation where `code` = " + code + "");
            // writeResultSet(resultSet);
            while (resultSet.next()) {

                String captcha = resultSet.getString("captcha");
                System.out.println(" token " + code);
                vcaptcha = captcha;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vcaptcha != null) {
            return vcaptcha;
        } else {
            return null;
        }
    }

    void finish_key_generation(String code) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("UPDATE projet_securite.key_generation  SET generation =? WHERE code=?");

            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, code);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String get_captcha(String pcode) {
        String vcaptcha = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.key_generation where `generated` ='" + pcode + "' ");
            //fetch result
            while (resultSet.next()) {
                String captcha = resultSet.getString("captcha");
                vcaptcha = captcha;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return result
        if (vcaptcha != null) {
            return vcaptcha;
        } else {
            return null;
        }
    }

    public boolean check_user_connexion(String user, String password) {
        String vname = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from projet_securite.users where `name` ='" + user + "' and `password`='" + password + "'");

            while (resultSet.next()) {

                String name = resultSet.getString("name");
                System.out.println(" name " + name);
                vname = name;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (vname != null) {
            return true;
        } else {
            return false;
        }
    }
    
    
        void finish_key_checking(String code) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/projet_securite?" + "user=root&password=");

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

        try {
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("UPDATE projet_securite.key_generation  SET check_key =? WHERE code=?");

            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, code);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
