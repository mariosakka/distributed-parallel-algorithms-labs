package stubs;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IndexMapper extends Mapper<Text, Text, Text, Text> {

  @Override
  public void map(Text key, Text value, Context context) throws IOException,
      InterruptedException {

    // Retrieve the file name from the context
    FileSplit fileSplit = (FileSplit) context.getInputSplit();
    Path path = fileSplit.getPath();
    String fileName = path.getName();

    // The key is the line number, the value is the line of text
    String lineNumber = key.toString();
    String line = value.toString();

    // Split the line into words. We use a regex to split on non-word characters.
    String[] words = line.split("\\W+");

    for (String word : words) {
      if (word.length() > 0) {
        // Create the location string: filename@linenumber
        String location = fileName + "@" + lineNumber;
        
        // Emit the word as the key, and the location as the value
        context.write(new Text(word.toLowerCase()), new Text(location));
      }
    }
  }
}