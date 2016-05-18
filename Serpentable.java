
public interface Serpentable {
	void encrypt();
	void decrypt();
	String keySchedule(String k, int round);
	
}
