public class Singleton {
	private static Singleton unique;

	private Singleton() {}

	public static Singleton getInstance() {
		if (unique == null)
			unique = new Singleton();
		return unique;
	}

	public String getDescription() {
		return "I'm a Singleton!";
	}
}
