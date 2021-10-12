import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class CourseDSC {

	// the date format we will be using across the application
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	/*
		SE1, // Semester 1
		SE2, // Semester 2
		SUM, // Semester Summer
	
		note: Enums are implicitly public static final
	*/
	public enum SEMESTER{
		SE1,
		SE2,
		SUM};

	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;
	
	private String dbUserName;
	private String dbPassword;
	private String dbURL;
	
	public CourseDSC(String dbHost, String dbUserName, String dbPassword) {
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		// in order to allow the DSC to be compatible with either
		//	- latcs7 MySQL server (Bundoora and Bendigo Campuses only)
		//	- your own MySQL server (or any other)
		// dbHost argument will include both the host and the database
		// example:
		//	localhost:3306/your-database-name
		//	where localhost:3306 is the database host address
		//	and your-database-name is the your application's database name
		dbURL = "jdbc:mysql://" + dbHost;
	}

	public void connect() throws SQLException {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
			statement = connection.createStatement();
  		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}		
	}

	public static void disconnect() throws SQLException {
		if(preparedStatement != null) preparedStatement.close();
		if(statement != null) statement.close();
		if(connection != null) connection.close();
	}

	public Subject searchSubject(String code) throws Exception {
		String queryString = "SELECT * FROM subject WHERE code = ?";
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setString(1, code);
		ResultSet rs = preparedStatement.executeQuery();

		Subject subject = null;

		if (rs.next()) { // i.e. the subject exists
			String name = rs.getString(2);
			boolean hasPrerequisites = rs.getBoolean(3);
			subject = new Subject(code,name, hasPrerequisites);
		}

		return subject;
	}

	public Course searchCourse(int id) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM course WHERE id = ?";
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();

		Course course = null;

		if (rs.next()) { // i.e. the course exists
			String subjectCode = rs.getString(2);
			Subject subject = searchSubject(subjectCode);
			if (subject == null) {
				System.err.println("[WARNING] Subject: '" + subjectCode + "'' does not exist!");
			}
			LocalDate date = LocalDate.parse(rs.getString(3), dtf);
			int quantity = rs.getInt(4);
			CourseDSC.SEMESTER semester = SEMESTER.valueOf(rs.getString(5));

			course = new Course(id, subject, date, quantity, semester);

		}

		return course;
	}

	public List<Subject> getAllSubjects() throws Exception {
		String queryString = "SELECT * FROM subject";
		preparedStatement = connection.prepareStatement(queryString);
		ResultSet rs = preparedStatement.executeQuery();
		System.out.println(rs);

		List<Subject> subjects = new ArrayList<Subject>();

		while (rs.next()) { // i.e. subjects exist
			String code = rs.getString(1);
			String name = rs.getString(1);
			boolean hasPrerequisites = rs.getBoolean(3);
			subjects.add(new Subject(code, name, hasPrerequisites));
		}

		return subjects;
	}

	public List<Course> getAllCourses() throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM course";
		ResultSet rs = statement.executeQuery(queryString);

		List<Course> courses = new ArrayList<Course>();

		while (rs.next()) { // 
			int id = rs.getInt(1);
			String subjectCode = rs.getString(2);
			Subject subject = searchSubject(subjectCode);
			if (subject == null) {
				System.err.println("[WARNING] Subject: '" + subjectCode + "'' does not exist!");
				continue;
			}
			LocalDate date = LocalDate.parse(rs.getString(3), dtf);
			int quantity = rs.getInt(4);
			SEMESTER semester = SEMESTER.valueOf(rs.getString(5));

			courses.add(new Course(id, subject, date, quantity, semester));
		}

		return courses;
	}


	public int addCourse(String code, int quantity, SEMESTER semester, LocalDate date) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		//LocalDate date = LocalDate.now();
		String dateStr = date.format(dtf);
		
		String command = "INSERT INTO course VALUES(?, ?, ?, ?, ?)";
		preparedStatement = connection.prepareStatement(command);

		preparedStatement.setInt(1, 0);
		preparedStatement.setString(2, code);
		preparedStatement.setString(3, dateStr);
		preparedStatement.setInt(4, quantity);
		preparedStatement.setString(5, semester.toString());

		preparedStatement.executeUpdate();

		ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
		rs.next();
		int newId = rs.getInt(1);

		return newId;		
	}

	public Course updateNumbersOfStudents(int id, int numberOfStds) throws Exception {
		Course g = searchCourse(id);
		if (numberOfStds == 0) 
			throw new Exception("There is at least one student enrolled: " + g.getSubject() + "( - use DELETE instead.");

		String queryString = 
			"UPDATE course " +
			"SET numberOfStudents = '" + numberOfStds +"' " +
			" WHERE numberOfStudents >= 1 " + 
			"AND id = " + id + ";";
		System.out.println(queryString);
		if (statement.executeUpdate(queryString) > 0)
			return searchCourse(id);
		else throw new Exception("Update was executed but did not effect any records!");
	}

	public int removeCourse(int id) throws Exception {
		String queryString = "SELECT COUNT(*) FROM course WHERE id = ?";
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();

		// are there any results
		boolean pre = rs.next();
		if(!pre) { // no, throw error
			throw new RuntimeException("The course does not exist!");
		}

		// there are results, proceed with delete
		return statement.executeUpdate("DELETE FROM course WHERE id = " + id);
	}

	// STATIC HELPERS -------------------------------------------------------

	public static long calcDaysAgo(LocalDate date) {
    	return Math.abs(Duration.between(LocalDate.now().atStartOfDay(), date.atStartOfDay()).toDays());
	}

	public static String calcDaysAgoStr(LocalDate date) {
    	String formattedDaysAgo;
    	long diff = calcDaysAgo(date);

    	if (diff == 0)
    		formattedDaysAgo = "today";
    	else if (diff == 1)
    		formattedDaysAgo = "yesterday";
    	else formattedDaysAgo = diff + " days ago";	

    	return formattedDaysAgo;			
	}	
	

	// To perform some quick tests	
	public static void main(String[] args) throws Exception {
		CourseDSC myCourseDSC = new CourseDSC("localhost:3306/coursedb", "root", "root1234");

		myCourseDSC.connect();

		System.out.println("\nSYSTEM:------\n"+ myCourseDSC);

		System.out.println("\n\nshowing all of each:");
		//System.out.println(myCourseDSC.getAllSubjects());
		System.out.println(myCourseDSC.getAllCourses());

		int addedId = myCourseDSC.addCourse("CSE3OAD", 40, SEMESTER.SE1,LocalDate.now());
		System.out.println("added: " + addedId);
		System.out.println("deleting " + (addedId - 1) + ": " + (myCourseDSC.removeCourse(addedId - 1) > 0 ? "DONE" : "FAILED"));
		//System.out.println("using " + (addedId) + ": " + myCourseDSC.useGrocery(addedId));
		System.out.println(myCourseDSC.searchCourse(addedId));

		myCourseDSC.disconnect();
	}
}