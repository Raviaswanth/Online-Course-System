public class Subject {

	// code is the unique id
	private String code;
	private String name;
	private boolean hasPrerequisites; // defaults to false

	// constructor
	public Subject(String code,String name, boolean hasPrerequisites) {
		this.code = code;
		this.name = name;
		this.hasPrerequisites = hasPrerequisites;
	}

	// constructor
	public Subject(String code, String name) {
		this(code, name, false);
	}

	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}

	public boolean getHasPrerequisites()
	{
		return this.hasPrerequisites;
	}

	public String toString() {
		return "[ code: " + this.code 
			+ ", name: " + this.name 
			+ ", hasPrerequisites: " + this.hasPrerequisites
			+ " ]";
	}

	// To perform some quick tests
	public static void main(String [] args)
	{
		Subject s1 = new Subject("CSE3OAD", "OBJECT-ORIENTED APPLICATION DEVELOPMENT",  true);
		System.out.println(s1);

		Subject s2 = new Subject("CSE1PE", "PROGRAMMING ENVIRONMENT", false);
		System.out.println(s2);
	}
}