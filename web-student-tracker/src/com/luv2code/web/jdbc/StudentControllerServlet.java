package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil; // making a reference to helper class
	
	// making use of JavaEE resource injection, so we can inject datasource (connection pool)
	@Resource (name="jdbc/web_student_tracker")
	private DataSource dataSource;
       
	// now, we initialize the StudentDbUtil class
	// in the Servlet lifecycle, there is a method to override, called Init();.
	// init() method will be called by the JavaEE server or by Tomcat when the servlet is initialized; 
    // so, work that we normally put in the constructor, in the servlet, we put it in the init() method. 
	@Override
	public void init() throws ServletException {
		super.init();
	
	// create our student db util and pass in the conn pool / datasource	
	try{
		
		studentDbUtil = new StudentDbUtil(dataSource);
	}catch(Exception e){throw new ServletException(e);}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			// read the command parameter
			String theCommand = request.getParameter("command");
			if(theCommand ==null){
				theCommand = "LIST";
			}
			switch(theCommand){
			case "LIST":
				listStudents(request,response);
				break;
			
			case "ADD":
				addStudent(request,response);
				break;
				
			case "LOAD":
				loadStudent(request,response);
				break;
				
			case "UPDATE":
				updateStudent(request,response);
				break;
				
			case "DELETE":
				deleteStudent(request,response);
			break;
				
			default:
				listStudents(request,response);
			}
			
		} catch (Exception e) {
			
			throw new ServletException(e);
		}
	
	
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student id from form data
		String theStudentId= request.getParameter("studentId");
		
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		// send them back to "list students' page
		listStudents(request,response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String department = request.getParameter("department");
		String email = request.getParameter("email");
		
		// create a new student obj
		Student theStudent = new Student(id,firstName,lastName,department,email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		// send them back to the list students page
		listStudents(request,response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		// get student from database db util
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		// place student in request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("update-student-form.jsp");
		dispatcher.forward(request, response);
	
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String department=request.getParameter("department");
		String email=request.getParameter("email");
		
		
		// create a new student object
		Student theStudent = new Student(firstName,lastName,department,email);
		
		// add student to database
		studentDbUtil.addStudent(theStudent);
		
		
		// send back to main page ( the student list)
		listStudents(request,response);
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students); // name, value(object)
		
		
		// send to jsp page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("list-students.jsp");
	   dispatcher.forward(request,response);
	}

	

	

}
