import java.util.List;

public class SubjectController {

	private CourseDSC courseDSC;

	public SubjectController(String dbHost, String dbUserName, String dbPassword) throws Exception {
		courseDSC = new CourseDSC(dbHost, dbUserName, dbPassword);

		try {
			courseDSC.connect();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public List<Subject> get() throws Exception {
		//TODO 15: what should this method return? HINT: a relevant call to a courseDSC method
		return courseDSC.getAllSubjects();

	}

	// To perform some quick tests
	public static void main(String [] args) throws Exception {
		// CONSIDER testing each of the above methods here
		// NOTE: this is not a required task, but will help you test your Task 2 requirements
		try {
			// CHANGE the username and password to match yours
			// CHANGE the first param to your database host if you are not using latcs7
			SubjectController sc = new SubjectController("localhost:3306/coursedb", "root", "root1234");
			System.out.println(sc.get());
			/* UNCOMMENT the following and add the relevant parameters/arguments to do your testing
			System.out.println(sc.get());

			*/

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
}