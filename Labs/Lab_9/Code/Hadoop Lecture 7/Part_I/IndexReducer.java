package stubs;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * On input, the reducer receives a word as the key and a set
 * of locations in the form "play name@line number" for the values. 
 * The reducer builds a readable string in the valueList variable that
 * contains an index of all the locations of the word. 
 */
public class IndexReducer extends Reducer<Text, Text, Text, Text> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {

    StringBuilder valueList = new StringBuilder();
    boolean first = true;

    // Iterate through all the locations for this word
    for (Text value : values) {
      if (!first) {
        valueList.append(","); // Add a comma separator between values
      }
      valueList.append(value.toString());
      first = false;
    }

    // Emit the word and its comma-separated list of locations
    context.write(key, new Text(valueList.toString()));
  }
}