import java.util.List;

public class CourseController {

	private CourseDSC courseDSC;

	public CourseController(String dbHost, String dbUserName, String dbPassword) throws Exception {
		courseDSC = new CourseDSC(dbHost, dbUserName, dbPassword);

		try {
			courseDSC.connect();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public List<Course> get() throws Exception {
		//TODO 06: what should this method return? HINT: a relevant call to a courseDSC method
		return courseDSC.getAllCourses();

	}

	public Course get(int id) throws Exception {
		//TODO 07: what should this method return? HINT: a relevant call to a courseDSC method
		return courseDSC.searchCourse(id);

	}

	public int add(Course c) throws Exception {
		// TODO 08: validate argument c, using Validation Framework
		Validator.validate(c);

		// TODO 09: make a relevant call to a courseDSC method
		int newId = courseDSC.addCourse(c.getSubjectName(), c.getNumberOfStudents(), c.getSemester() , c.getDate());


		// TODO 10: this method should return the id of the newly created course
		return newId;
	}

	public Course update(int id) throws Exception {
		// TODO 11: make a relevant call to a courseDSC method

		// TODO 12: this method should return the updated course object
		return courseDSC.updateNumbersOfStudents();
	}

	public int delete(int id) throws Exception {
		// TODO 13: make a relevant call to a courseDSC method

		// TODO 14: this method should return what ever the courseDSC method call (TODO 13) returns
		return courseDSC.removeCourse(id);

	}

	// To perform some quick tests
	public static void main(String [] args) throws Exception {
		// CONSIDER testing each of the above methods here
		// NOTE: this is not a required task, but will help you test your Task 2 requirements
		try {
			// CHANGE the username and password to match yours
			// CHANGE the first param to your database host if you are not using latcs7
			CourseController cc = new CourseController("localhost:3306/coursedb", "root", "root1234");
			System.out.println(cc.get());
			/* UNCOMMENT the following and add the relevant parameters/arguments to do your testing
			
			System.out.println(cc.get(...
				// some id that exists in your course table
				)
			);
			System.out.println(cc.add(new Course(...)));
			System.out.println(cc.update(...
				// some id that exists in your course table
				)
			);
			System.out.println(cc.delete(...
				// some id that exists in your course table
				)
			);
			*/

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
}
