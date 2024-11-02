

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Properties;
import java.io.*;


@WebServlet(name = "rootServlet", value="/rootServlet")

public class rootServlet extends HttpServlet{

    private Connection connection;
    private Statement statement;
    private int mySQLUpdateValue;
    private int[] updateReturnValues;
    String message = "";
    private int changedRows;


    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException 
    {

        String sqlStatement = request.getParameter("sqlStatement");

        try
        {
            getDBConnection();
            Statement statement = connection.createStatement();
            sqlStatement = sqlStatement.trim();
            String sqlType = sqlStatement.substring(0,6);

            if(sqlType.equalsIgnoreCase("select"))
            {
                ResultSet resultset = statement.executeQuery(sqlStatement);
                message = getHtmlRows(resultset);
            }
            else if(sqlType.equalsIgnoreCase("delete")) 
            {
                // Specifically handles the 'DELETE' case, to avoid potential business logic triggering
                mySQLUpdateValue = statement.executeUpdate(sqlStatement);
                message = "Statement Successful! "+mySQLUpdateValue+"row(s) affected. Business Logic Not Triggered.";
            }
            else if(sqlType.equalsIgnoreCase("insert")) 
            {
                // Handles 'INSERT' queries for parsing out the quantity, since this is easy to do
                // This block handles only if we are inserting into shipments, to accomodate handing business logic
                String sqlInsDest = sqlStatement.substring(12,20);
                if(sqlInsDest == "shipments")
                {
                    int startIndex = sqlStatement.lastIndexOf(' ');
                    int endIndex = sqlStatement.lastIndexOf(')');
                    String parsedString = sqlStatement.substring(startIndex+1, endIndex).trim();
                    int parsQuant = Integer.parseInt(parsedString);
                    mySQLUpdateValue = statement.executeUpdate(sqlStatement);
                    message = "executed Update";
                    if(parsQuant >= 100)
                    {
                        changedRows = businessLogicTriggered();
                        message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic WAS Triggered with "+changedRows+" affected!";
                    }
                    else message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic Not Triggered!";
                }
                else
                {
                    // If we are inserting to a table other than 'shipments', we can just fire it off and not check for logic
                    mySQLUpdateValue = statement.executeUpdate(sqlStatement);
                    message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic Not Triggered!";
                }
            }
            else if(sqlType.equalsIgnoreCase("update")) 
            { 
                //Handles 'UPDATE' queries for more intense parsing
                // First checks to make sure we are updating shipments. Otherwise, we can not worry about business logic
                String sqlUpdateTypeParser = sqlStatement.substring(7,16);
                mySQLUpdateValue = statement.executeUpdate(sqlStatement);
                // message = sqlUpdateTypeParser;

                // If we are updating shipments, we need to perform the update, then trigger logic to see
                // if the target went over 100
                if(sqlUpdateTypeParser.equalsIgnoreCase("shipments")) 
                {
                    // Triggers business logic for the test case. This becomes difficult if we do not allow a DB Trigger to assist us
                    // since parsing for the last criteria is impossible to implement with perfect accuracy and without throwing exceptions
                    changedRows = businessLogicTriggered();
                    message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic WAS Triggered with "+changedRows+" affected!";
                }
                else
                {

                    message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic Not Triggered!";
                }

            }
            else
            {
                mySQLUpdateValue = statement.executeUpdate(sqlStatement);
                message = "Statement Successful! "+mySQLUpdateValue+" row(s) affected. Business Logic Not Triggered!";




            }


            statement.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
            message = e.getMessage();
        }

        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("sqlStatement", sqlStatement);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/rootHome.jsp");
        dispatcher.forward(request, response);
        
    }

    private int businessLogicTriggered()
    {
        // Uses BRUTE FORCE METHOD
        int rowsAffected = 0;

        try
        {
            String busMessage = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments WHERE quantity >= 100);";

        PreparedStatement statement = connection.prepareStatement(busMessage);
        rowsAffected = statement.executeUpdate();
        } catch(SQLException z)
        {
            z.printStackTrace();
            message = z.getMessage();
        }
        return rowsAffected;
    }

    // I had issues making the class import properly with packages, so it was moved into here
    public static synchronized String getHtmlRows( ResultSet results) throws SQLException
    {
        StringBuffer htmlRows = new StringBuffer();
        ResultSetMetaData metadata = results.getMetaData();
        int numColumns = metadata.getColumnCount();

        htmlRows.append("<tr>");

        for (int i=1; i<= numColumns; i++)
        {
            htmlRows.append("<th>"
             + metadata.getColumnName(i)+"</th>");
        }
        htmlRows.append("<tr>");

        int zebraCounter = 0;
        while (results.next())
        {
            htmlRows.append("<tr");

            if (zebraCounter %2==0) 
            {
                htmlRows.append(" class=\"even\">");
            }
            else 
            {
                htmlRows.append(" class=\"odd\">");
            }
            
            zebraCounter++;

            for (int i = 1; i <= numColumns; i++)
            {
                htmlRows.append("<td>" + results.getString(i) + "</td>");
            }

            htmlRows.append("</tr>");
        }
        return htmlRows.toString();
    }   

    private void getDBConnection() throws IOException
    {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource datasource = null;

        try{
            filein = new FileInputStream("/Library/Tomcat10120/webapps/Project-4/WEB-INF/lib/root.properties");
            properties.load(filein);
            datasource = new MysqlDataSource();
            datasource.setURL(properties.getProperty("MYSQL_DB_URL"));
            datasource.setUser((properties.getProperty("MYSQL_DB_USERNAME")));
            datasource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = datasource.getConnection();
            statement = connection .createStatement();
       } catch (SQLException e)
       {
        e.printStackTrace();
       }
    } 
    
}
