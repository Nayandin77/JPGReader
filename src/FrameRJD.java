import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class FrameRJD {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameRJD window = new FrameRJD();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public FrameRJD() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSelectFolder = new JButton("Select Folder");
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select Images Folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);

			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {			      
			      File dir = chooser.getSelectedFile();
			      File[] directoryListing = dir.listFiles();
			      try { 
			    	  File csv = processJpg(directoryListing);
			    	  
			          JFileChooser fcPick = new JFileChooser();
			          fcPick.setDialogTitle("Save as...");
			          fcPick.setSelectedFile(csv);
			    	  int res = fcPick.showSaveDialog(null);		
			    	  if (res == JFileChooser.APPROVE_OPTION) {
			    		  csv.renameTo(fcPick.getSelectedFile());
			    	  } else {
			    		  System.out.println("User did not want to save file.");
			    	  }
			      } 
			      catch (ImageProcessingException e1) { e1.printStackTrace(); } 
			      catch (IOException e1) { e1.printStackTrace(); }
			    } else { // If nothing selected
			      System.out.println("No Selection ");
			    }
			}
		});
		btnSelectFolder.setBounds(169, 144, 117, 29);
		frame.getContentPane().add(btnSelectFolder);
		
		JLabel lblNewLabel = new JLabel("Jpg Image Reader");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 21));
		lblNewLabel.setBounds(6, 6, 190, 29);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblCreatedByDennis = new JLabel("Created by Dennis Nayandin");
		lblCreatedByDennis.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblCreatedByDennis.setBounds(16, 36, 178, 16);
		frame.getContentPane().add(lblCreatedByDennis);
		
		JLabel label_info = new JLabel("<html>How it works: Click on 'Select Folder' and choose the "
				+ "folder that has all the jpg images you want to be processed. Then the program will "
				+ "prompt you to save the csv file onto your machine.</html>"
				);
		label_info.setVerticalAlignment(SwingConstants.TOP);
		label_info.setToolTipText("");
		label_info.setHorizontalAlignment(SwingConstants.TRAILING);
		label_info.setBounds(6, 64, 438, 77);
		frame.getContentPane().add(label_info);
	}
	
	public File processJpg(File[] directoryListing) throws IOException, ImageProcessingException {
		// Creates CSV File with header
		File csvFile = new File("csv.csv");
		FileWriter csvFileW = new FileWriter(csvFile);
		csvFileW.append("Name");
		csvFileW.append(",");
		csvFileW.append("DoW");
		csvFileW.append(",");
		csvFileW.append("Month");
		csvFileW.append(",");
		csvFileW.append("Day");
		csvFileW.append(",");
		csvFileW.append("Time");
		csvFileW.append(",");
		csvFileW.append("Loc");
		csvFileW.append(",");
		csvFileW.append("Year");
		csvFileW.append("\n");
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
					csvFileW.append(String.join(",", temp));
					csvFileW.append("\n");
				}
			}
		}
		csvFileW.flush();
		csvFileW.close();
		return csvFile;
	}
}
