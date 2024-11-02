<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
String message = (String) session.getAttribute("message");
if (message == null) message = "";
%>
<!DOCTYPE html>
<html lang="='eng">
<head>
    <title> CNT4714 - Spring Project 4 </title>
    <style>

        .label{
            display: inline-block;
            color: black;
            background-color: white;
            border: 1px;
        }
    </style>

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
        style='font-size:18.0pt; color:yellow;'>
        A Servlet/JSP-base Multi-Tiered Enterprise Application 
        using a Tomcat Container 
    </p>
</div>
<hr>
<div>
    <p class=rootHomeSum  style="text-align: center;"><span 
    style="font-size: 12.0pt; color:white;">
        You are connected to the Project 4 Enterprise System Database as a
        <span style="color: red;"> accountant-level</span> user. 
        <br> Please seslect the operation you would like to perform from the list below.
    </p>
</div>
<!-- Block for button queries-->
<center>
    <form action="AccServ" method="post" style="background-color:rgb(135, 108, 89);">
        </div>
            <input type="radio" id="option1" name="radioOp" value="1">
            <label for="option1" style="color: white;">Get Maximum Status Value of all All Suppliers (Returns a Maximum value)</label> 
       
        <br><br>
            <input type="radio" id="option2" name="radioOp" value="2">
            <label for="option2" style="color: white;">Get the Total weight of all parts (Returns a Sum)</label>

        <br><br>
            <input type="radio" id="option3" name="radioOp" value="3">
            <label for="option3" style="color: white;">Get the Total Number of Shipments (Returns current number of shipments in total)</label>
        <br><br>
            <input type="radio" id="option1" name="radioOp" value="4">
            <label for="option4" style="color: white;">Get the name and number of workers of the job with the most workers (Returns two values)</label>
        
        <br><br>
            <input type="radio" id="option5" name="radioOp" value="5">
            <label for="option5" style="color: white;">List the Name and Status of Every Supplier (Returns a list of suppliers names with status)</label>
      
        <br><br>
        <div>
            <button style="color: greenyellow;background-color: darkgrey;">Execute Command</button>
            <button style="background-color: darkgray; color: orangered;">Clear Result</button>
        </div>
    </form>
    <div><p style="color: white;">All Execution results will appear below the following line</p></div>
<hr><br>
<p style="color: white;">Execution Results:</p>
<table id="data" style="background-color: azure; color: black;">
    <p style="color: aquamarine;"> <%=message%> </p>

</table>

</center>