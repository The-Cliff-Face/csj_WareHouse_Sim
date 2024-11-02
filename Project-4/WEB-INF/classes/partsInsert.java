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
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "partsInsert", value="/PartSub")
public class partsInsert extends HttpServlet
{

    private Connection connection;
    private Statement statement;
    private int mySQLUpdateValue;
    private String message = "";


    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {

        String inboundNum = request.getParameter("partNum");
        String inbound_P_Name = request.getParameter("partName");
        String inboundColor = request.getParameter("partColor");
        String inboundWeight = request.getParameter("partWeight");
        String inboundCity = request.getParameter("partCity");

        PreparedStatement pstatement;

        message = "insert into parts values (?, ?, ?, ?, ?);";

        try
        {
            getDBConnection();
            pstatement = connection.prepareStatement(message);
        
            pstatement.setString(1,inboundNum);
            pstatement.setString(2, inbound_P_Name);
            pstatement.setString(3,inboundColor);
            pstatement.setString(4, inboundWeight);
            pstatement.setString(5, inboundCity);

            mySQLUpdateValue = pstatement.executeUpdate();

            if (mySQLUpdateValue == 1)
            {
            message = "New Shipments record: (" +
            inboundNum+", " +inbound_P_Name+", "+inboundColor+", "+inboundWeight+", "+inboundCity+
            ") - successfully entered into the database.";
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
