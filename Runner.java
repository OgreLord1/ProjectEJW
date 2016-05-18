
public class Runner {
	public static void main(String[] args) {

		Transmit test = new Transmit("aonflepakdiwmfia", "thisisakeythathasthirtythreelett");
		test.encrypt();
		System.out.println(test);
		Receive test2 = new Receive(test.toString(), "thisisakeythathasthirtythreelett");
		test2.decrypt();
		System.out.println(test2);

	}
}
