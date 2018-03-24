package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ReadFixFile {

    public static void main(String[] args) {
	new ReadFixFile()
		.read("G:\\Disco\\Estudo\\Bovespa\\prodreplay-derivatives-28apr2014\\prod_replay_derivatives.fix");

    }

    private void read(String fName) {

	try {
	    File fixFile = new File(fName);
	    Scanner sc = new Scanner(fixFile);
	    int n = 0;
	    while (sc.hasNext()) {
		String line = sc.nextLine();

		String msgs = line.replaceAll("8=FIX", "\n8=FIX");
		FileOutputStream fos = new FileOutputStream(new File(fixFile.getPath() + "adj_" + n++ + ".fix"));
		fos.write(msgs.getBytes());
		fos.close();
	    }
	    sc.close();

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
