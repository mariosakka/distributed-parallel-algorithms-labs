package stubs;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCoMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  private static final IntWritable ONE = new IntWritable(1);

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    
    // Convert the line to lowercase and split on non-word characters
    String line = value.toString().toLowerCase();
    String[] words = line.split("\\W+");
    
    // Loop through the words to emit adjacent pairs
    // Note: We stop at words.length - 1 because we are grabbing pairs (i and i+1)
    for (int i = 0; i < words.length - 1; i++) {
      
      String word1 = words[i];
      String word2 = words[i+1];
      
      // Ensure neither word is empty before emitting
      if (word1.length() > 0 && word2.length() > 0) {
        // Create a new string formatted as "word1, word2"
        String pair = word1 + ", " + word2;
        
        // Emit the text pair as the key and 1 as the value
        context.write(new Text(pair), ONE);
      }
    }
  }
}