import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformDates
{
	
	/**
	 * Transforms dates in a file from one format to another.
	 * 
	 * For example, "On 9/6/78, a really cool thing happened." and a destination
	 * format of "EEEE, MMMM dd, yyyy". The expected output would be "On
	 * Wednesday, September 06, 1978, a really cool thing happened.
	 * 
	 * "
	 * 
	 * @param file
	 *            The file whose dates will be transformed.
	 * @paramsourcePattern A {@link SimpleDateFormat}-compatible date format
	 *                     that identifies the format of dates within the file
	 *                     to transform.
	 * @paramdestinationPattern A {@link SimpleDateFormat}-compatible date
	 *                          format that identifies the destination format of
	 *                          transformed dates.
	 */
	public static void transformDates(final File file, String sourcePattern,
			String destinationPattern) throws FileNotFoundException
	{
		StringBuilder outStr = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			SimpleDateFormat sourceSDF = new SimpleDateFormat(sourcePattern);
			sourceSDF.setLenient(false); // strict mode
			
			SimpleDateFormat destinationSDF = new SimpleDateFormat(destinationPattern);
			
			// Regex pattern matching expression for MM/dd/yyyy
			Pattern patternSourceDate = Pattern.compile("(0?[1-9]|1[012])[/](0?[1-9]|[12][0-9]|3[01])[/](19|20)\\d\\d");

			String currentLine;
			Matcher matcher;

			while ((currentLine = br.readLine()) != null)
			{
				
				matcher = patternSourceDate.matcher(currentLine);
				
				while (matcher.find())
				{
					try
					{
						
						Date date = sourceSDF.parse(matcher.group()); // throws ParseException if date is invalid 
						
						String replaceStr = destinationSDF.format(date); // converts to destination format
						
						/*
						 * replace number format with date format
						 */
						currentLine = currentLine.replaceFirst(matcher.group(),	replaceStr); 

					} 
					catch (ParseException e)
					{
						continue; // on exception ignore
					}
					

				}
				outStr.append(currentLine + System.lineSeparator());
			}
			
			System.out.println(outStr);

		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		/*
		 * Write to output.txt file
		 */
		try(PrintWriter out = new PrintWriter(new File ("output.txt")))
		{
			out.write(outStr.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * MAIN PROGRAM EXECUTION
	 * PROGRAM OUTPUT >> output.txt
	 */
	public static void main(String[] args)
	{

		File file = new File("input.txt");
		try
		{
			transformDates(file, "MM/dd/yyyy", "EEEE, MMMM dd, yyyy");
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

	}
}
