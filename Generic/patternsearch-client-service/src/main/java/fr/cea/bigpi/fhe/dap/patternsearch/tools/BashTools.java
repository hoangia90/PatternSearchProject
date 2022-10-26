package fr.cea.bigpi.fhe.dap.patternsearch.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates the execution of shell commands.
 *
 */
public class BashTools {
	
	private static final Logger log = LoggerFactory.getLogger(BashTools.class);

	public static boolean Exec(
			boolean isShowLog,
			boolean isWithBash,
			String script1, 
			String script2, 
			String script3, 
			String script4,
			String script5,
			String script6,
			String script7,
			String script8,
			String script9)
	{
		if(isShowLog) log.info("Starting exec inputs paramaters");
		Process childExec = null;
		String bash = "/bin/bash";
		
		log.info(String.format("%s %s %s %s %s %s %s %s %s %s %s\n", "Start Exec String Params:", 
				isWithBash ? bash : "", script1, script2, script3, 
				script4, script5, script6, 
				script7, script8, script9));
		
		try {
			if(script2.length() == 0)
				childExec = new ProcessBuilder(script1).start();
			else if(script3.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2).start();
				else 
					childExec = new ProcessBuilder(script1, script2).start();
			}
			else if(script4.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3).start();
				else 
					childExec = new ProcessBuilder(script1, script2, script3).start();
			}
			else if(script5.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4).start();
				else
					childExec = new ProcessBuilder(script1, script2, script3, script4).start();
			}
			else if(script6.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4, script5).start();
				else
					childExec = new ProcessBuilder(script1, script2, script3, script4, script5).start();
			}
			else if(script7.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4, script5, script6).start();
				else 
					childExec = new ProcessBuilder(script1, script2, script3, script4, script5, script6).start();
			}
			else if(script8.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4, script5, script6, script7).start();
				else 
					childExec = new ProcessBuilder(script1, script2, script3, script4, script5, script6, script7).start();
			}
			else if(script8.length() == 0) {
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4, script5, script6, script7, script8).start();
				else 
					childExec = new ProcessBuilder(script1, script2, script3, script4, script5, script6, script7, script8).start();
			}
			else { 
				if(isWithBash)
					childExec = new ProcessBuilder(bash, script1, script2, script3, script4, script5, script6, script7, script8, script9).start();
				else 
					childExec = new ProcessBuilder(script1, script2, script3, script4, script5, script6, script7, script8, script9).start();
			}
			int code = childExec.waitFor();			
			
			if(isShowLog) 
			{
				BufferedReader output = new BufferedReader(
						new InputStreamReader(childExec.getInputStream()));
				String line = null;
				while ((line = output.readLine()) != null)
					log.info("Exec output > " + line);				
			}
			log.info(String.format("%s %d %s %s %s %s %s %s %s %s %s %s %s\n", "End with code :", code, " Exec String Params:", 
					isWithBash ? bash : "", script1, script2, script3, 
					script4, script5, script6, 
					script7, script8, script9));
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace();
			log.error("SecurityException ERROR exec script  : " + e.toString());
			return false;
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			log.error("InterruptedException ERROR exec script  : " + e.toString());
			return false;
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("IOException ERROR exec script : " + e.toString());
			return false;
		}  					
		return true;
	}

	public static boolean Exec(			
			boolean isWithBash,
			String script1, 
			String script2, 
			String script3, 
			String script4,
			String script5,
			String script6,
			String script7,
			String script8,
			String script9) {		
		return Exec(false, isWithBash, script1, script2, script3, script4, script5, script6, script7, script8, script9);		
	}
	
	public static boolean Exec(boolean isWithBash, String script1, String script2, String script3, String script4, 
								String script5, String script6, String script7, String script8)
	{
		return Exec(isWithBash, script1, script2, script3, script4, script5, script6, script7, script8, "");
	}

	public static boolean Exec( boolean isWithBash, String script1, String script2, String script3, String script4, String script5, String script6, String script7)
	{
		return Exec(isWithBash, script1, script2, script3, script4, script5, script6, script7, "", "");
	}
	public static boolean Exec(boolean isWithBash,  String script1, String script2, String script3, String script4, String script5, String script6)
	{
		return Exec(isWithBash, script1, script2, script3, script4, script5, script6, "", "", "");
	}
	public static boolean Exec(boolean isWithBash, String script1, String script2, String script3, String script4, String script5)
	{
		return Exec(isWithBash, script1, script2, script3, script4, script5, "", "", "", "");
	}
	public static boolean Exec(boolean isWithBash, String script1, String script2, String script3, String script4)
	{
		return Exec(isWithBash, script1, script2, script3, script4, "", "", "", "", "");
	}
	public static boolean Exec(boolean isWithBash, String script1, String script2, String script3)
	{
		return Exec(isWithBash, script1, script2, script3, "", "", "", "", "", "");
	}
	public static boolean Exec(boolean isWithBash, String script1, String script2)
	{
		return Exec(isWithBash, script1, script2, "", "", "", "", "", "", "");
	}	
}
