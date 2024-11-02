

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


@WebServlet(name = "clientServlet", value="/clientServlet")

public class clientServlet extends HttpServlet{

    private Connection connection;
    private Statement statement;
    private int mySQLUpdateValue;
    private int[] updateReturnValues;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException 
    {

        String sqlStatement = request.getParameter("sqlStatement");
        String message = "";

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
            else
            {
                // Since client can't do anything but select, everything else goes here
                 mySQLUpdateValue = statement.executeUpdate(sqlStatement);
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
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/client.jsp");
        dispatcher.forward(request, response);
        
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
            filein = new FileInputStream("/Library/Tomcat10120/webapps/Project-4/WEB-INF/lib/client.properties");
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
