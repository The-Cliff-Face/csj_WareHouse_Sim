

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;

import java.io.IOException;
import java.io.*;
import java.util.Properties;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class accountantServlet extends HttpServlet{

    private Connection connection;
    private Statement statement;


    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        String message = "";
        String command = "";

        try
        {

            getDBConnection();
            String selection = request.getParameter("radioOp");

            int commandVal = Integer.parseInt(selection);

            if (commandVal == 1)
            {
                command = "{call `Get_The_Maximum_Status_Of_All_Suppliers`()}";
            }
            else if (commandVal == 2)
            {
                command = "{call `Get_The_Sum_Of_All_Parts_Weights`()}";
            }
            else if (commandVal == 3)
            {
                command = "{call `Get_The_Total_Number_Of_Shipments`()}";
            }
            else if (commandVal == 4)
            {   
                command = "{call `Get_The_Name_Of_The_Job_With_The_Most_Workers`}";
            }
            else if (commandVal == 5)
            {
                command = "{call `List_The_Name_And_Status_Of_All_Suppliers`}";
            }
            else 
            {
                command = "{call ERROR()}";
            }

            CallableStatement statement = connection.prepareCall(command);
            boolean returnValue = statement.execute();

            if (returnValue)
            {
                ResultSet results = statement.getResultSet();
                message = getHtmlRows(results);
            }
            else
            {
                message = "Error Executing RPC!";
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            message=e.getMessage();
        }


        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("sqlStatement", statement);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/accountantHome.jsp");
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
            filein = new FileInputStream("/Library/Tomcat10120/webapps/Project-4/WEB-INF/lib/theaccountant.properties");
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
