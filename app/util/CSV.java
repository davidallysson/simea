package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CSV {
	public static File export(Map<String, String> data, String filename) {
		
		String csv = data.get("data[saveMe]");

		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("public/" + filename), "UTF-8"));
			bw.write(csv);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (UnsupportedEncodingException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		File downloadMe = new File("public/" + filename);

		return downloadMe;
	}
}
