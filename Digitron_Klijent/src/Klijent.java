
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Klijent implements Runnable {
	static Socket soketZaKomunikaciju = null;
	static BufferedReader serverInput = null;
	static PrintStream serverOutput = null;
	static BufferedReader unosSaTastature = null;

	
	
	public static void main(String[] args) {

		try {
			soketZaKomunikaciju = new Socket("localhost", 7000);

			serverInput = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			serverOutput = new PrintStream(soketZaKomunikaciju.getOutputStream());
			unosSaTastature = new BufferedReader(new InputStreamReader(System.in));

			new Thread(new Klijent()).start();

			String input;

			while (true) {
				input = serverInput.readLine();
				System.out.println(input);

				if (input.startsWith("Dovidjenja"))
					break;
				if (input.startsWith("Vasi logovi")) {
					PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter("logoviKorisnika.txt")));
					pw1.write(serverInput.readLine());

					pw1.close();
					System.out.println("su spakovani u fajl logoviKorisnika.txt.\n");
				}
			}
			soketZaKomunikaciju.close();

		} catch (IOException e) {
			System.out.println("Server is down");
		}
	}

	@Override
	public void run() {
		try {
			String message;
			while (true) {
				message = unosSaTastature.readLine();
				serverOutput.println(message);
				if (message.startsWith("quit"))
					break;
			}
		} catch (IOException e) {

		}
	}
}
