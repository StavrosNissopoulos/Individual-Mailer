package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MaskedPassword {

	public static String readAPassword() {
	      RunThread thread = new RunThread();
	      Thread mask = new Thread(thread);
	      mask.start();

	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      String password = "";

	      try {
	         password = br.readLine();
	      } catch (IOException e) {
	        e.printStackTrace(System.out);
	      }
	      thread.stopMasking();
	      return password;
	   }
}
