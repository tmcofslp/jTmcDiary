package diary;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
public class DiaryMain 
{
	static DiaryController controller;
	
	public static final String DB_PATH = "/media/veracrypt2/diary/diary.db";
	
	public static void main(String[] args) 
	{
		CommandLine cmdLine;
		String dbpath = DB_PATH;
		
		CommandLineParser parser = new DefaultParser();
		Option helpOption = Option.builder("h")
                .longOpt("help")
                .required(false)
                .desc("shows this message")
                .build();



		Option dbOption = Option.builder("d")
                .longOpt("database")
                .numberOfArgs(1)
                .required(false)
                .type(String.class)
                .desc("path to database file")
                .build();
		Options options = new Options();
		options.addOption(helpOption);
		
		options.addOption(dbOption);

		
		try {
			cmdLine = parser.parse(options, args);
		} catch (Exception Ex) {
			// TODO gibt args.toString was gescheites raus?
			System.err.println("error parsing command line: " + args.toString());
			Ex.printStackTrace();
			return;
			
		}
		if (cmdLine.hasOption("help")) {
		 HelpFormatter formatter = new HelpFormatter();
		 formatter.printHelp("TmcDiary", options);
		} else {
			if(cmdLine.hasOption("database")) {
				
				
				try {
					dbpath = cmdLine.getParsedOptionValue("database").toString();
				} catch (ParseException pex) {
					System.err.println("could not read argument option database");
					pex.printStackTrace();
					return;
				}
				
				
				
			}
			
			if(Files.isRegularFile(Paths.get(dbpath))) {
				try { // weil DBController.getInstance(dbpath); wirft FileNotFoundException
					
					@SuppressWarnings("unused")
					DBController dbc = DBController.getInstance(dbpath);			
				
					EventQueue.invokeLater(new Runnable() 
					{
						public void run() 
						{
								//TODO DB_Path as argument
								
								try { // (new DiaryController) can throw FileNotFoundException !?!?
									controller = new DiaryController();
									controller.ShowView();
								}catch (FileNotFoundException fnfex) {
									System.err.println("Database file \"" + DBController.GetDBPath()+ "\" not found!");
								}
							
						}
					});	
				} catch (FileNotFoundException ex) {
					System.err.println("Database file \"" + DBController.GetDBPath() + "\" not found!");
				}
				catch (Exception e) {
					System.err.println("general exception");
					e.printStackTrace();
				}
			} else {
				System.err.println("Database file " + dbpath + " does not exist!");
				return;
			}
			
		}
		return;
	}
}
