<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String sqlStatement = (String) session.getAttribute("sqlStatement");
if (sqlStatement == null) sqlStatement = "select * from suppliers";
String message = (String) session.getAttribute("message");
if (message == null) message = "";
%>
<!DOCTYPE html>
<html lang="='eng">

<script type="text/javascript">
    function eraseData()
    {
        document.getElementById("cmd").innerHTML=" ";
    }
</script>

<script type="text/javascript">
    function eraseText()
    {
        document.getElementById("data").innerHTML="";
    }
</script>

    <head>
        <title> CNT4714 - Spring Project 4 </title>
    </head>
    <body bgcolor="black" text="='white" lang="=EN-US">

    <div class=Section1>
        <p class=MsoHeader  style='text-align:center'><span
            style='font-size:24.0pt; color:white;'>
            Welcome To the Spring 2024 Project 4 Enterprise System 
        </p>
        <br>
    </div>

    <div>
        <p class=MsoDetails  style='text-align:center'><b><span
            style='font-size:24.0pt; color:yellow;'>
            A Servlet/JSP-base Multi-Tiered Enterprise Application 
            using a Tomcat Container 
        </p>
    </div>
    <hr>
    <div>
        <div>
            <p class=rootHomeSum  style="text-align: center;"><span 
            style="font-size: 16.0pt; color:white;">
                You are connected to the Project 4 Enterprise System Database as a
                <span style="color: red;"> ROOT-LEVEL</span> user. 
                <br> Please enter any SQL queries or update commands in the box below.
            </p>
        </div>
        <center>

            <form action="RootUserApp" method="post">
                <textarea id="cmd" name="sqlStatement" cols="60" rows="10"><%=sqlStatement%></textarea> <br>
                <input type="submit" value="Execute Command">
                <input type="reset" value="ResetForm" onclick="javascript:eraseText();">
                <input type="button" value="Clear Results" onclick="javascript:eraseData();">
            </form>

            <p style="color: white;">All execution results will appear below this line.</p>
            <hr>
            <p>
            <b class="main" style="color: white;"> Execution Results</b><br><br>
                <table id="data" style="background-color: azure; color:black;">
                    <p style="color: aquamarine;"> <%=message%> </p>

                </table>
            </p>
        </center>
    </div>
    </body>
</html>