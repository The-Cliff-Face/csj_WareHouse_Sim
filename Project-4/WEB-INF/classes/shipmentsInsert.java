import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import java.util.Properties;
import java.util.function.ToDoubleBiFunction;
import java.io.FileInputStream;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "shipmentsInsert", value="/ShipSub")
public class shipmentsInsert extends HttpServlet
{

    private Connection connection;
    private Statement statement;
    private int mySQLUpdateValue;
    private String message = "";


    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {

        String inboundNum = request.getParameter("shipNum");
        String inbound_P_Num = request.getParameter("shipPNum");
        String inboundJNum = request.getParameter("shipJNum");
        String inboundQuantity = request.getParameter("jQuantity");
        PreparedStatement pstatement;

        message = "insert into shipments values (?, ?, ?, ?);";

        try
        {
            getDBConnection();
            pstatement = connection.prepareStatement(message);
        
            pstatement.setString(1,inboundNum);
            pstatement.setString(2, inbound_P_Num);
            pstatement.setString(3,inboundJNum);
            pstatement.setString(4, inboundQuantity);

            mySQLUpdateValue = pstatement.executeUpdate();

            // Business Logic Check
            int quantity = Integer.parseInt(inboundQuantity);

            if (mySQLUpdateValue == 1 && quantity >= 100)
            {
                businessLogicTriggered();
                message = "New Shipments record: (" +
                inboundNum+", " +inbound_P_Num+", "+inboundJNum+", "+inboundQuantity+") "+
                "- successfully entered into the database. Business Logic Triggered!";
            }
            else
            if (mySQLUpdateValue ==1)
            {
                message = "New Shipments record: (" +
                inboundNum+", " +inbound_P_Num+", "+inboundJNum+", "+inboundQuantity+") "+
                "- successfully entered into the database. No Business Logic";
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            message = e.getMessage();
        }

        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataEntryHome.jsp");
        dispatcher.forward(request, response);
    }

    // Function for when we need to trigger business logic, and increment status (Brute Force Method)
    private void businessLogicTriggered()
    {
        // Uses BRUTE FORCE METHOD
        try
        {
            String busMessage = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT snum FROM shipments WHERE quantity >- 100);";

        PreparedStatement statement = connection.prepareStatement(busMessage);
        mySQLUpdateValue = statement.executeUpdate();
        } catch(SQLException z)
        {
            z.printStackTrace();
            message = z.getMessage();
        }
    }

    // Sets up the DB connection
    private void getDBConnection() throws IOException
    {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource datasource = null;

        try{
            filein = new FileInputStream("/Library/Tomcat10120/webapps/Project-4/WEB-INF/lib/dataentryuser.properties");
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
            message = e.getMessage();
       }
    } 
    
}
