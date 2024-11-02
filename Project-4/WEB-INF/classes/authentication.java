

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import java.util.Properties;
import java.io.FileInputStream;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "authentication", value="/authenticationServlet")
public class authentication extends HttpServlet
{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        String inboundUserName = request.getParameter("userLogin");
        String inboundPassword = request.getParameter("UserPassword");

        String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?;";
        boolean userCredentialsWork = false;

        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        ResultSet lookupResults;
        PreparedStatement pstatement;

        try
        {   
            try
            {
                // Start out with file IO
                filein = new FileInputStream("/Library/Tomcat10120/webapps/Project-4/WEB-INF/lib/systemapp.properties");
                properties.load(filein);

                // Setup the datasource and connection
                dataSource = new MysqlDataSource();
                dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
                dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
                dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));

                Connection connection = dataSource.getConnection();
                pstatement = connection.prepareStatement(credentialsSearchQuery);
        
                pstatement.setString(1,inboundUserName);
                pstatement.setString(2, inboundPassword);

                //Send the query, and see if there's any issues
                lookupResults = pstatement.executeQuery();
                if(lookupResults.next())
                {
                    userCredentialsWork = true;
                }
                else
                {
                    System.err.println("There was an issue finding the user in the database. Please try again");
                    userCredentialsWork = false;
                }
                
            } 
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        } 
        catch (Exception f)
        {
            System.err.println("There was a major issue connetion to the database.");
            f.printStackTrace();
        }


        if (userCredentialsWork == true)
        {
            if (inboundUserName.equals("root"))
            { 
                //Redirect to rootHome
                System.err.println("Redirect to Root");
                response.sendRedirect(  "rootHome.jsp");
            }
            else if (inboundUserName.equals("client")) 
            {
                //Redirect to clientHome
                System.err.println("Redirect to Client");
                response.sendRedirect(  "client.jsp");
            }
            else if(inboundUserName.equals("dataentryuser")) 
            {
                // redirect to dataEntryHome
                System.err.println("Redirect to dataEntry");
                response.sendRedirect(  "dataEntryHome.jsp");
            }
            else if(inboundUserName.equals("theaccountant"))
            {
                // redirect to accountantHome
                System.err.println("Redirect to accountant");
                response.sendRedirect(  "accountantHome.jsp");
            } 
            else
            {
                System.err.println("Redirect to Error");
                response.sendRedirect("authError.html");
             }

        }
        else
        {
            System.err.println("Redirect to Error");
           response.sendRedirect("authError.html");
        }
    }





}