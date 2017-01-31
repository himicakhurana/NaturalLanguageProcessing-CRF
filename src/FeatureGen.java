import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;

public class FeatureGen {
	public static HashSet<String> locSet = new HashSet<>();
	public static HashSet<String> eventSet = new HashSet<>();
	public static HashSet<String> timeSet = new HashSet<>();
	public static HashSet<String> measureSet = new HashSet<>();

	public static void main(String[] args) {
		generateSets();
		FileInputStream fis;
		File fout = new File("train.txt");
		HashSet<String> dirSet = new HashSet<String>(Arrays.asList("north", "south", "east", "west"));
		try {

			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			fis = new FileInputStream("out.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			boolean b = false;
			String line = null;
			while ((line = br.readLine()) != null) {
				String output = line.split(" ")[0].trim();
				String prevPunc = "0";
				String label = "Irrelevant";
				if (b) {
					prevPunc = "4";
					b = false;
				}
				line = line.substring(output.length());
				if (locSet.contains(output.toLowerCase())) {
					label = "Location";
				}
				if (measureSet.contains(output.toLowerCase())) {
					label = "Measurement";
				}
				if (eventSet.contains(output.toLowerCase())) {
					label = "Event";
				}
				if (timeSet.contains(output.toLowerCase())) {
					label = "Time";
				}
				char suffix = output.charAt(output.length() - 1);
				char prefix = output.charAt(0);
				String opLow = output.toLowerCase();
				String timeFormat = Pattern.matches("(1[012]|[1-9]):[0-5][0-9]", opLow)
						|| Pattern.matches("(1[012]|[1-9]):[0-5][0-9](\\s)", opLow)
								| Pattern.matches("(1[012]|[1-9]):[0-5][0-9]", opLow) ? "0" : "1";
				String dir = "0";
				for (String d : dirSet) {
					dir = opLow.contains(d) ? "1" : "0";
				}
				String cap = Character.isUpperCase(prefix) ? "0" : "1";
				String numeric = Pattern.matches("[0-9]", output) ? "0" : "1";
				output = output + " " + output.length() + " " + cap + " " + prefix + " " + suffix + " " + prevPunc + " "
						+ dir + " " + timeFormat +" "+ numeric;
				line = output+ line;
				b = Pattern.matches("\\p{Punct}", output);
				if (b & output.equals(".")) {
					b = false;
				}

				bw.write(line + " " + label);
				bw.newLine();
			}
			bw.close();

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void generateSets() {
		// TODO Auto-generated method stub
		String[] files = { "event.txt", "location.txt", "time.txt", "measure.txt" };
		for (String filename : files) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(filename);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (filename.equals("event.txt")) {
						eventSet.add(line.trim().toLowerCase());
					}
					if (filename.equals("location.txt")) {
						locSet.add(line.trim().toLowerCase());
					}
					if (filename.equals("time.txt")) {

						timeSet.add(line.trim().toLowerCase());
					}
					if (filename.equals("measure.txt")) {
						measureSet.add(line.trim().toLowerCase());
					}
				}

				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
