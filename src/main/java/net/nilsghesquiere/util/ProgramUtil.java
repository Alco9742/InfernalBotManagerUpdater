package net.nilsghesquiere.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProgramUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramUtil.class);
	public static String getCapitalizedString(boolean bool){
		String boolString = String.valueOf(bool);
		return boolString.substring(0, 1).toUpperCase() + boolString.substring(1);
	}
	
	public static boolean downloadFileFromUrl(String managerMap, String URL, String filename) {
		if(createDownloadsDir(managerMap)){
			//String managerMap = System.getProperty("user.dir");
			String filePath = managerMap + "\\downloads\\" + filename;
			// Sample Url Location
			String url = URL + filename; 
			URL urlObj = null;
			ReadableByteChannel rbcObj = null;
			FileOutputStream fOutStream  = null;
		
			// Checking If The File Exists At The Specified Location Or Not
			Path filePathObj = Paths.get(filePath);
			boolean fileExists = Files.exists(filePathObj);
			if(!fileExists) {
				File file = new File(filePath);
				try {
					file.createNewFile();
				} catch (IOException e) {
					LOGGER.error("Failure creating file");
					LOGGER.debug(e.getMessage());
				}
			}
			
			try {
				LOGGER.debug(url);
				urlObj = new URL(url);
				rbcObj = Channels.newChannel(urlObj.openStream());
				fOutStream = new FileOutputStream(filePath);
				fOutStream.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
				LOGGER.info(filename  + " download complete");
			} catch (IOException e) {
				LOGGER.error("Problem occured while downloading " + filename);
				LOGGER.debug(e.getMessage());
				return false;
			} finally {
				try {
					if(fOutStream != null){
						fOutStream.close();
					}
					if(rbcObj != null) {
						rbcObj.close();
					}
				} catch (IOException e) {
					LOGGER.error("Problem occured while closing the object");
					LOGGER.debug(e.getMessage());
					return false;
				}				
			}
		} else {
			LOGGER.error("Failure locating backup folder");
			return false;
		}
		return true;
	}
	
	private static boolean createDownloadsDir(String managerMap){
		Path backupDir = Paths.get(managerMap + "\\downloads\\");
		if(!Files.exists(backupDir)){
			try {
				Files.createDirectories(backupDir);
			} catch (IOException e1) {
				//Path exists, do nothing
			}
		}
		return Files.exists(backupDir);
	}
	
	public static boolean createBackupDir(String managerMap){
		Path backupDir = Paths.get(managerMap + "\\backup\\");
		if(!Files.exists(backupDir)){
			try {
				Files.createDirectories(backupDir);
			} catch (IOException e1) {
				//Path exists, do nothing
			}
		}
		return Files.exists(backupDir);
	}
}
