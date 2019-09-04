package whois.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {
	
	static Namespace ns = null;
	static boolean initted = false;
	static ArgumentParser parser;

	
	static String PRINT_URL = "print_url";
	static String SAVETO = "saveto";
	static String UNZIP = "unzip";
	static String DATE = "date";
	// Algorithms

	
	public static void init() {
		if (ns != null)
			return;
	    parser = ArgumentParsers.newFor("whois-download").build()
	            .defaultHelp(true)
	            .description("download whois registrant domains.");
	    parser.addArgument("-"+SAVETO).type(String.class)
	            .help("specify the output file to compare");
	    parser.addArgument("-"+DATE).nargs("*").type(String.class)
        .help("specify the output file to compare");
	    parser.addArgument("-"+PRINT_URL).action(Arguments.storeTrue())
	    		.help("print url only");
	    parser.addArgument("-"+UNZIP).action(Arguments.storeTrue())
		.help("unzip data before writing output");

	}

	
	public static Namespace parseArgs(String[] args) {
		init();
	    try {
	        ns = parser.parseArgs(args);
	        return ns;
	    } catch (ArgumentParserException e) {
	        parser.handleError(e);
	        System.exit(1);
	    }
	    return null;
	    
	}
	
	public static ArrayList<String> getDates() {
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<String> in_dates = (ArrayList<String>) ns.get(DATE);
		if (in_dates == null)
			in_dates = new ArrayList<String>();
		
		for (String s: in_dates) {
			if (s != null && s.length() > 0 && SimpleDate.validString(s)) {
				dates.add(s);
			}
		}
		
		if (dates.size() == 0) {
			dates.add(SimpleDate.getString());
		}
		return dates;
	}
	
	public static void performPrintUrl() throws Exception {
		ArrayList<String> dates = getDates();
		HashMap<String, String> urls = new HashMap<String, String>();
		for (String d: dates) {
			urls.put(d, DownloadRegistrants.generateURLFromString(d));
		}
		
		for (Entry<String, String> x: urls.entrySet()) {
			System.out.println(String.format("Date: %s Url: %s", x.getKey(), x.getValue()));
		}
		
	}
	
	public static void performDownload() throws Exception {
		ArrayList<String> dates = getDates();
		HashMap<String, String> urls = new HashMap<String, String>();
		for (String d: dates) {
			urls.put(d, DownloadRegistrants.generateURLFromString(d));
		}
		
		boolean unzip = ns.getBoolean(UNZIP);
		
		for (String d: dates) {
			DownloadRegistrants.downloadAndSave(d, unzip);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		ns = parseArgs(args);
		if (ns.getBoolean(PRINT_URL)) {
			performPrintUrl();
		} else {
			performDownload();
		}
	}

}
