import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

class ReadJpgData {
	
	public static void main(String[] args) throws ImageProcessingException, IOException {
		// List<List<String>> rows;
		
		// Creates CSV File with header
		FileWriter csvFile = new FileWriter("csv.csv");
		csvFile.append("Name");
		csvFile.append(",");
		csvFile.append("DoW");
		csvFile.append(",");
		csvFile.append("Month");
		csvFile.append(",");
		csvFile.append("Day");
		csvFile.append(",");
		csvFile.append("Time");
		csvFile.append(",");
		csvFile.append("Loc");
		csvFile.append(",");
		csvFile.append("Year");
		csvFile.append("\n");
		
		// Selector for directory
		File dir = new File("img/");
		File[] directoryListing = dir.listFiles();
		String dow = "", month="", day="", time="", loc="", year="";

		// Reads JPG and stores values into CSV file
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.getName().endsWith("JPG")) {
					Metadata metadata = ImageMetadataReader.readMetadata(child);
					ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);	
					Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
					
					String[] s_date = date.toString().split(" ");
					dow = s_date[0];
					month = s_date[1];
					day = s_date[2];
					time = s_date[3];
					loc = s_date[4];
					year = s_date[5];
					
					List<String> temp = Arrays.asList(child.getName(), dow, month, day, time, loc, year);
					csvFile.append(String.join(",", temp));
					csvFile.append("\n");
					
					// System.out.println(child.getName() + " : " + date);
				}
			}
		}
		csvFile.flush();
		csvFile.close();
		System.out.println("Done!");
	}
}