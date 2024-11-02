
// IMPORTANT NOTE:
/*
This file is only used as reference with the others.
I had issues making the pages display properly when everything was imported into a package.
As a result, all files had to be kept 'loose' to ensure they were working properly.
This file has been kept as template for the other files to reference when needed.
 */

import java.sql.*;

public class ResultSetToHTMLFormatterClass {

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

    
}
