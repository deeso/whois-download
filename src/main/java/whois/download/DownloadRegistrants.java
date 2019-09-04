package whois.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.Cookie;

import org.apache.commons.io.FileUtils;

import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;

public class DownloadRegistrants {
	public static final String WHOIS_DL_LINK_FMT = "https://whoisds.com//whois-database/newly-registered-domains/%s/nrd";
	public static final String FILE_NAME_FORMAT = "%02d-%02d-%02d.zip"; 
	static public String createFilename() throws Exception {
		return createFilename(SimpleDate.getString());	
	}
	static public String createFilename(String date) throws Exception {
		int year = SimpleDate.getYearFromString(date);
		int month = SimpleDate.getMonthFromString(date);
		int day = SimpleDate.getDayFromString(date);
		
		if (year == -1 || month == -1 || day == -1) {
			throw new Exception("Invalid date string: "+ date);
		}
		return createFilename(year, month, day);
	}
	
	static public String createFilename(int year, int month, int day) {
		return String.format(FILE_NAME_FORMAT, year, month, day);
	}
			
	static public String generateURLFromString(String date) throws Exception{
		String filename = createFilename(date);
		String encodedString = Base64.getEncoder().encodeToString(filename.getBytes());
		return String.format(WHOIS_DL_LINK_FMT, encodedString);
	}
	
	static public String generateURLFromString() throws Exception{
		return generateURLFromString(SimpleDate.getString());
	}
	
	static public byte[] downloadAndSave() throws Exception {
		return downloadAndSave(false);
	}
	
	static public byte[] downloadAndSave(boolean unzip) throws Exception {
		String date = SimpleDate.getString();
		String saveTo = createFilename(date);
		return downloadAndSave(date, unzip, saveTo);
	}
	static public byte[] downloadAndSave(String date, boolean unzip,  String saveTo) throws Exception {
		byte[] data = downloadWithDate(date);
		System.out.println(String.format("Content size: %d", data.length));
		byte[] content = data;
		if (unzip) {
			content = inflate(data);
			System.out.println(String.format("New Content size: %d", content.length));
		}
		String newSaveto = unzip ? saveTo.replace("zip", "txt") : saveTo;
		saveDownload(content, newSaveto);
		return content;
	}
	
	static public byte[] downloadAndSave(String date, boolean unzip) throws Exception {
		String saveTo = createFilename(date);
		return downloadAndSave(date, unzip, saveTo);		
	}
	static public byte[] downloadAndSave(boolean unzip, String saveTo) throws Exception {
		String date = SimpleDate.getString();
		return downloadAndSave(date, unzip, saveTo);
	}
	
	static public byte[] download() throws Exception {
		return download(generateURLFromString());	
	}
	static public byte[] downloadWithDate(String date) throws Exception {
		return download(generateURLFromString(date));	
	}
	
	static public void saveDownload(byte[] content, String saveTo) throws IOException {
		FileUtils.writeByteArrayToFile(new File(saveTo), content);
	}
	
	static public byte[] download(String url) throws IOException {
		RawResponse resp = Requests.get(url).send();
		byte[] body = resp.readToBytes();
		return body;
	}
	
	static public byte[] inflate(byte[] bytes) throws IOException{
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
        	int len = 0;
        	while ((len = zis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
        	break;
        }
        return baos.toByteArray();
    }
	
	static public void downloadForDates(ArrayList<String> dates) throws Exception {
		HashMap<String, String> urls = new HashMap<String, String>();
		if (dates.size() == 0) {
			String date = SimpleDate.getString();
			urls.put(date, generateURLFromString(date));
		} else {
			for (String date: dates) {
				urls.put(date, generateURLFromString(date));
			}
		}
		
		
		
	}
	
}
