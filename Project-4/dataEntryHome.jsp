<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String message = (String) session.getAttribute("message");
if (message == null) message = "";
%>

<!DOCTYPE html>
<html lang="='eng">

<script type="text/javascript">
    function eraseData()
    {
        document.getElementById("SupplierSub").innerHTML=" ";
        document.getElementById("PartSub").innerHTML=" ";
        document.getElementById("JobSub").innerHTML=" ";
        document.getElementById("ShipSub").innerHTML=" ";
        document.getElementById("data").innerHTML=" ";

    }
</script>


<head>
    <title> CNT4714 - Spring Project 4 </title>


</head>
<body bgcolor="black" text="='white" lang="=EN-US">

<div class=Section1>
    <p class=MsoHeader  style='text-align:center'><span
        style='font-size:14.0pt; color:white;'>
        Welcome To the Spring 2024 Project 4 Enterprise System 
    </p>

</div>

<div>
    <p class=MsoDetails  style='text-align:center'><b><span
        style='font-size:18.0pt; color:yellow;'>
        Data Entry Application
    </p>
</div>
<hr>
<div>
    <p class=rootHomeSum  style="text-align: center;"><span 
    style="font-size: 10.0pt; color:white;">
        You are connected to the Project 4 Enterprise System Database as a
        <span style="color: red;"> data-entry</span> user. 
        <br> Enter data values into the forms below to add a new record to the associated database table
    </p>
</div>
<hr>
<center>
    <!-- Snippet for Suppliers Field-->
    <p style="color: white;">
        Suppliers Record Insert
    </p>
    <form action="SupplierSub" method="post">
    <div>
            <label for="suppliersSnum;" style="color: white;"> sNum</label>
            <input type="text" name="supplierSnum" id="supplierSnum">
     <br>
            <label for="suppliersSname" style="color: white;"> sName</label>
            <input type="text" name="suppliersSname" id="suppliersSname">
     <br>
            <label for="suppliersStatus" style="color: white;"> status</label>
            <input type="text" name="suppliersStatus" id="suppliersStatus">
     <br>
            <label for="suppliersCity" style="color: white;"> City</label>
            <input type="text" name="suppliersCity" id="suppliersCity">
        </div>
    <div>
            <input type="submit" value="Enter Supplier Records" style="color: greenyellow; background-color: grey;">
            <input type="reset" value="Clear Data and Results"onclick ="javascript:eraseData();" style="color: orange; background-color: grey;">
    </div>
    <hr>
    <p style="color: white;">Parts Record Insert</p>
</form>
<!-- Snippet for Parts Record Field-->
<form action="PartSub" method="post">
    <div>
        <label for="partsNum;" style="color: white;"> pNum</label>
        <input type="text" name="partNum" id="partNum">
    <br>
        <label for="partsName" style="color: white;"> pName</label>
        <input type="text" name="partName" id="partName">
    <br>
        <label for="partColor" style="color: white;"> color</label>
        <input type="text" name="partColor" id="partColor">
    <br>
        <label for="partWeight" style="color: white;"> weight</label>
        <input type="text" name="partWeight" id="partWeight">
        <br>
        <label for="partCity" style="color: white;"> city</label>
        <input type="text" name="partCity" id="partCity">
    </div>
    <div>
        <input type="submit" value="Enter Parts Records" style="color: greenyellow; background-color: grey;">
        <input type="reset" value="Clear Data and Results"onclick ="javascript:eraseData() ;" style="color: orange; background-color: grey;">
    </div>  
    <hr>
</form>

<!-- Snippet for Jobs Record Field-->
<p style="color: white;">Jobs Record Insert</p>
<form action="JobSub" method="post">
    <div>
        <label for="jNum;" style="color: white;"> jNum</label>
        <input type="text" name="partNum" id="partNum">
    <br>
        <label for="jName" style="color: white;"> jName</label>
        <input type="text" name="jName" id="jName">
    <br>
        <label for="numWorkers" style="color: white;"> nWorkers</label>
        <input type="text" name="numWorkers" id="numWorkers">
    <br>
        <label for="jCity" style="color: white;"> City</label>
        <input type="text" name="jCity" id="jCity">
        <br>

    </div>
    <div>
        <input type="submit" value="Enter Jobs Records" style="color: greenyellow; background-color: grey;">
        <input type="reset" value="Clear Data and Results"onclick ="javascript:eraseData();" style="color: orange; background-color: grey;">
    </div>
    <hr>

</form>
<!-- Snippet for Shipments Record Field-->
<p style="color: white;">Shipment Record Insert</p>
<form action="ShipSub" method="post">
    <div>
        <label for="shipNum;" style="color: white;"> sNum</label>
        <input type="text" name="shipNum" id="shipNum">
    <br>
        <label for="shipPNum" style="color: white;"> pNum</label>
        <input type="text" name="shipPNum" id="shipPNum">
    <br>
        <label for="shipJNum" style="color: white;"> jNum</label>
        <input type="text" name="shipJNum" id="shipJNum">
    <br>
        <label for="jQuantity" style="color: white;"> Quantity</label>
        <input type="text" name="jQuantity" id="jQuantity">
        <br>
    </div>
    <div>
        <input type="submit" value="Enter Shipments Records" style="color: greenyellow; background-color: grey;">
        <input type="reset" value="Clear Data and Results" onclick ="javascript:eraseData();" style="color: orange; background-color: grey;">
    </div>
</form>
<hr>
<b class="main" style="color: white;"> Execution Results</b><br><br>
<table id="data" style="background-color: azure; color: black;">
    <p style="color: aquamarine;"> <%=message%> </p>

</table>
</p>
</center>