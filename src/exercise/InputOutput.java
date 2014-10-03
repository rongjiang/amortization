package exercise;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Class that handles read/write to either Console or System.in/System.out
 */
public class InputOutput {

	Console console;
	BufferedReader reader;
	PrintWriter writer;
	
	/**
	 * If console is null, read/write from standard input/output
	 * 
	 * @param console
	 */
	public InputOutput(Console console) {
		this.console = console;
		if (console == null) {
			reader = new BufferedReader(new InputStreamReader(System.in));
			writer = new PrintWriter(System.out);
		} else {
			writer = console.writer();
		}
	}
	
	/**
	 * Reads a line from input with a user prompt
	 * 
	 * @param userPrompt
	 * @return
	 * @throws IOException
	 */
	public String readLine(String userPrompt) throws IOException {
		if (console != null) {
			return console.readLine(userPrompt).trim();
		} else {
			writer.printf(userPrompt);
			writer.flush();
			return reader.readLine().trim();
		}
	}
	
	/**
	 * Writes to output
	 * 
	 * @param formatString
	 * @param args
	 */
	public void print(String formatString, Object... args) {
		writer.printf(formatString, args);
		writer.flush();
	}
	
	/**
	 * Flushes the writer
	 */
	public void flush() {
		writer.flush();
	}
	
	/**
	 * @return current writer for this class
	 */
	public PrintWriter writer() {
		return writer;
	}
}
