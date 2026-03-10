package stubs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CreateSequenceFile extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {

    if (args.length != 2) {
      System.out.printf("Usage: CreateSequenceFile <input dir> <output dir>\n");
      return -1;
    }

    Job job = new Job(getConf());
    job.setJarByClass(CreateSequenceFile.class);
    job.setJobName("Create Sequence File");

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    // Set Map-only job (no reducers needed) 
    job.setNumReduceTasks(0); 

    // Set the output format to SequenceFile [cite: 130]
    job.setOutputFormatClass(SequenceFileOutputFormat.class);
    
    // Set output key and value classes [cite: 133, 134, 135]
    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(Text.class);

    // Compress the output using Block compression and the Snappy codec [cite: 149, 150, 151]
    FileOutputFormat.setCompressOutput(job, true);
    SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);
    FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);

    boolean success = job.waitForCompletion(true);
    return success ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new Configuration(), new CreateSequenceFile(), args);
    System.exit(exitCode);
  }
}