import java.math.BigInteger;

public class Receive implements Serpentable {

	private String input; // 16 letters
	private String key; // 32 letterss
	// private String[] keys;
	private String a;
	private String b;
	private String c;
	private String d;
	private String[] sbox1 = { "0000", "0001", "0010", "0100", "1000", "0011", "0101", "1001", "0110", "1010", "1100",
			"1110", "0111", "1011", "1101", "1111" };
	private String[] sbox2 = new String[16];
	private String[] sbox3 = new String[16];
	private String[] sbox4 = new String[16];
	private String[] sbox5 = new String[16];
	private String[] sbox6 = new String[16];
	private String[] sbox7 = new String[16];
	private String[] sbox8 = new String[16];
	private int length;

	public Receive(String message, String k) {
		input = message;
		/*
		System.out.println(input);
		input = "00010001100110101000101001011000100000000000000000000000010100001101100001000001110110000100010101000010001001001000010000000110";
		String output = "";

		System.out.println(input); 
		for (int w = 0; w < input.length(); w += 8) {
			int ch = Integer.parseInt(input.substring(w, w + 8), 2);
			String str = new Character((char) ch).toString();
			output += str;
		}
		input = output;
		System.out.println(input); 
		input = message; */
		key = k;
		// while (input.length() % 4 != 0) {
		// input += "a";
		// }
		// length = input.length() / 4;
		for (int i = 0; i < 16; i++) {

			sbox2[i] = sbox1[less16(i + 2)];
			sbox3[i] = sbox1[less16(i + 3)];
			sbox4[i] = sbox1[less16(i + 4)];
			sbox5[i] = sbox1[less16(i + 5)];
			sbox6[i] = sbox1[less16(i + 6)];
			sbox7[i] = sbox1[less16(i + 7)];
			sbox8[i] = sbox1[less16(i + 8)];
		}

	}

	public int less16(int n) {
		if (n > 15)
			return n - 15;
		return n;
	}

	public String[] choose(int r) {
		if (r == 1 || r == 9 || r == 17 || r == 25)
			return sbox1;
		if (r == 2 || r == 10 || r == 18 || r == 26)
			return sbox2;
		if (r == 3 || r == 11 || r == 19 || r == 27)
			return sbox3;
		if (r == 4 || r == 12 || r == 20 || r == 28)
			return sbox4;
		if (r == 5 || r == 13 || r == 21 || r == 29)
			return sbox5;
		if (r == 6 || r == 14 || r == 22 || r == 30)
			return sbox6;
		if (r == 7 || r == 15 || r == 23 || r == 31)
			return sbox7;
		else
			return sbox8;
	}

	public void ipermute() {
		input = new BigInteger(input.getBytes()).toString(2);

		// System.out.println(input.length());
		while (input.length() <= 127)
			input = "0" + input;
		String[] inputarray = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			inputarray[i] = input.substring(i, i + 1);
		}
		for (int f = 0; f < input.length(); f++) {
			String replace = inputarray[f];
			inputarray[f] = inputarray[(f * 4) % 127];
			inputarray[(f * 4) % 127] = replace;
		}
		input = "";
		for (int g = 0; g < inputarray.length; g++) {
			input += inputarray[g];
		}
	}

	public void sbox(int round) {
		if (round != 33) {
			for (int h = 0; h < input.length(); h += 4) { // sbox baby
				for (int s = 0; s < sbox1.length; s++) {
					if (input.substring(h, h + 4).equals(choose(round)[s]))
						input = input.substring(0, h) + (sbox1[s]) + input.substring(h + 4);
				}
			}
		}
		String k = keySchedule(key, round);
		String[] keyarray = new String[k.length()]; // creates arrays
		// System.out.println(k.length());
		for (int i = 0; i < keyarray.length; i++) // makes it easier
			keyarray[i] = k.substring(i, i + 1); // to compare
		String[] inputarray = new String[input.length()];
		for (int j = 0; j < input.length(); j++) {
			inputarray[j] = input.substring(j, j + 1);
		}
		for (int g = 0; g < inputarray.length; g++) { // xors key and input
			if (inputarray[g].equals("0") && keyarray[g].equals("0"))
				inputarray[g] = "0";
			if (inputarray[g].equals("1") && keyarray[g].equals("0"))
				inputarray[g] = "1";
			if (inputarray[g].equals("0") && keyarray[g].equals("1"))
				inputarray[g] = "1";
			if (inputarray[g].equals("1") && keyarray[g].equals("1"))
				inputarray[g] = "0";
		}
		input = "";
		for (int e = 0; e < inputarray.length; e++) {
			input += inputarray[e];
		}

	}

	public void fpermute() {
		String[] inputarray = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			inputarray[i] = input.substring(i, i + 1);
		}
		for (int f = 0; f < input.length(); f++) {
			String replace = inputarray[f];
			inputarray[f] = inputarray[f * 32 % 127];
			inputarray[f * 32 % 127] = replace;
		}
		input = "";
		for (int g = 0; g < inputarray.length; g++) {
			input += inputarray[g];
		}

	}

	public void lintrans() {
		String[] inputarray = new String[input.length()];
		for (int j = 0; j < input.length(); j++) {
			inputarray[j] = input.substring(j, j + 1);
		}
		input = c + a + d + b;
	}

	@Override
	public void decrypt() {
		// System.out.println(input.length());
		ipermute();

		length = input.length() / 4;
		// System.out.println(input.length());
		a = input.substring(0, length);
		b = input.substring(length, 2 * length);
		c = input.substring(2 * length, 3 * length);
		d = input.substring(3 * length, 4 * length);
		sbox(33);
		for (int y = 32; y > 0; y--) {
			lintrans();
			sbox(y);

		}

		fpermute();

		String[] inputarray = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			inputarray[i] = input.substring(i, i + 1);
		}
		System.out.println(input);
		String output = "";
		for (int w = 0; w < input.length(); w += 8) {
			int z = Integer.parseInt(input.substring(w, w + 8), 2);
			output += (char) z;
		}
		input = output;

	}

	@Override
	public void encrypt() {
		Transmit test = new Transmit(input, key);
		test.encrypt();
	}

	@Override
	public String keySchedule(String k, int round) {

		String out = k;
		round = round - 1;
		if (round < 17)
			out = out.substring(round, round + 16);
		else if (round == 33)
			out = out.substring(0, 16);
		else
			out = out.substring(round) + out.substring(0, round - 16);
		out = new BigInteger(out.getBytes()).toString(2);
		while (out.length() <= 127)
			out = "0" + out;

		return out;

		/*
		 * byte[] q = new byte[8]; for (int i = 0; i < q.length; i++) { q[i] =
		 * Byte.valueOf(k.substring(4 * i, 4 * i + 4)); } String phi1 = "ž7y¹";
		 * byte phi = Byte.valueOf(phi1); String[] u = new String[140]; for (int
		 * a = 0; a < 132; a++) { if (a < 1) for (int j = 0; j < 32; j++) { u[a]
		 * = "" + (q[j] ^ q[j + 3] ^ q[j + 5] ^ q[j + 7] ^ phi); u[a] =
		 * u[a].substring(11) + u[a].substring(0, 11); }
		 * 
		 * } return k;
		 */
	}

	@Override
	public String toString() {
		return input;
	}
}
