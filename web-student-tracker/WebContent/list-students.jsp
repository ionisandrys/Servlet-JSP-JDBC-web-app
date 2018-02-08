<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import = "java.util.*, com.luv2code.web.jdbc.*"%>
<!DOCTYPE html>
<html>
<head><title>Student Tracker App</title></head>
<h2 style="color:#80ff00">Welcome !</h2> <br/>

<link type = "text/css" rel = "stylesheet" href = "css/style.css">

<%
// get the students from the request object (sent by servlet)
 List<Student> theStudents = (List<Student>) request.getAttribute("STUDENT_LIST");

%>




<body>
<div id = "wrapper">
  <img src="caltech.jpg" alt="Caltech students" width="700" height="360" align="center" ><br/><br/>
	<div id = "header">
	  <h2 align = "center">Caltech University</h2>
	</div>
	</div>
	
	<div id = "container">
	<div id = "content">
	<!-- Add the button  -->
	<input type = "button" value = "Add student" 
			onclick="window.location.href='add-student-form.jsp';
	return false;"
	class = "add-student-button" /> <!--  from style.css -->
	
	<table>
		<tr>
		<th>First name</th>
		<th>Last name</th>
		<th>Department</th>
		<th>Email</th>
		<th>Action</th>
		<th>Picture Link</th>
		</tr>
		
	<c:forEach var = "tempStudent" items="${STUDENT_LIST }">
	
	<!--  Set up a link for each student -->
	<c:url var = "tempLink" value = "StudentControllerServlet">
	<c:param name = "command" value = "LOAD"/>
	<c:param name = "studentId" value = "${tempStudent.id }"/>
	</c:url>
	
	<!--  set up a link to delete a student -->
	
	<c:url var = "deleteLink" value = "StudentControllerServlet">
	<c:param name = "command" value = "DELETE"/>
	<c:param name = "studentId" value = "${tempStudent.id }"/>
	</c:url>
	
	
	<tr>
	
	<td> ${ tempStudent.firstName} </td>
	<td> ${ tempStudent.lastName} </td>
	<td> ${tempStudent.department} </td>
	<td> ${tempStudent.email }</td>
	
	<td>
		<a href = "${tempLink }">Update</a>
		|			<!--  make use of little javascript in jsp -->
		<a href="${deleteLink }" 
		onclick="if (!(confirm('Are you sure you want to delete this student ????')))return false">Delete</a>
		</td>
		
	<td> <img src="Phys.jpg" alt=""  height=22 width=22 onclick ="window.location.href='http://bigbangtheory.wikia.com/wiki/List_of_The_Big_Bang_Theory_characters'"></img> </td>
	
	</tr>
	
	</c:forEach>
	</table>
	
	</div>
	
	</div>

</body>
</html>