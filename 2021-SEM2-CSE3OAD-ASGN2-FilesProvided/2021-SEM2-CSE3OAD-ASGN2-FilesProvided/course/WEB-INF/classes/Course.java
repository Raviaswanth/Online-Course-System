import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Course {

	public static final int MINIMUM_QUANTITY = 1;

	// TODO 01: add which annotation? Does it have any params?
	private int id;

	// TODO 02: add which annotation? Does it have any params?
	private Subject subject;

	// TODO 03: add which annotation? Does it have any params?
	private LocalDate date;

	// TODO 04: add which annotation? Does it have any params?
	private int numberOfStudents;

	// TODO 05: add which annotation? Does it have any params?
	private CourseDSC.SEMESTER semester;

	// constructor
	public Course(int id, Subject subject, LocalDate date, int numberOfStudents, CourseDSC.SEMESTER semester)
			throws Exception {
		this.id = id;
		this.subject = subject;
		this.date = date != null ? date : LocalDate.now();
		this.numberOfStudents = numberOfStudents;
		this.semester = semester;
	}

	// constructor
	public Course(int id, Subject subject, int numberOfStudents, CourseDSC.SEMESTER semester) throws Exception {
		this(id, subject, LocalDate.now(), numberOfStudents, semester);
	}

	public Course(Subject subject, LocalDate date, int numberOfStudents, CourseDSC.SEMESTER semester) throws Exception {
		this(0, subject, date, numberOfStudents, semester);
	}

	// constructor
	public Course(int id, Subject subject, CourseDSC.SEMESTER semester) throws Exception {
		this(id, subject, LocalDate.now(), MINIMUM_QUANTITY, semester);
	}

	public int getId() {
		return this.id;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public String getSubjectName() {
		return this.subject.getName();
	}

	public String getSubjectCode() {
		return this.subject.getCode();
	}

	public LocalDate getDate() {
		return this.date;
	}

	public String getDateStr() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CourseDSC.DATE_FORMAT);

		return this.date.format(dtf);
	}

	public String getDaysAgo() {
		return CourseDSC.calcDaysAgoStr(date);
	}

	public int getNumberOfStudents() {
		return this.numberOfStudents;
	}

	public void updateNumbersOfStudents() throws Exception {
		if (this.numberOfStudents > MINIMUM_QUANTITY)
			this.numberOfStudents--;
		else
			throw new Exception("[ERROR] Quantity value cannot be less than 1");
	}

	public CourseDSC.SEMESTER getSemester() {
		return this.semester;
	}

	public String toString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CourseDSC.DATE_FORMAT);
		// String daysAgo = CourseDSC.calcDaysAgoStr(date);

		return "[ id: " + this.id + ", subject: " + this.subject.getName() + ", prerequisites: "
				+ subject.getHasPrerequisites() + ", date: " + this.date.format(dtf) // + " (" + daysAgo + ")"
				+ ", numberOfStudents: " + this.numberOfStudents + ", semester: " + this.semester + " ]";
	}

	// To perform some quick tests
	public static void main(String[] args) throws Exception {
		Subject s1 = new Subject("CSE3OAD", "OBJECT-ORIENTED APPLICATION DEVELOPMENT", true);
		System.out.println(s1);

		Subject s2 = new Subject("CSE1PE", "PROGRAMMING ENVIRONMENT", false);
		System.out.println(s2);

		Course g1 = new Course(1, s1, CourseDSC.SEMESTER.SE1);
		System.out.println(g1);

		Course g2 = new Course(2, s2, 10, CourseDSC.SEMESTER.SE2);
		System.out.println(g2);
	}
}