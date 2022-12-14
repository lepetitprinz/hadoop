package compression;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MaxTemperatureWithMapOutputCompression {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: compression.MaxTemperatureWithMapOutputCompression " +
                    "<input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        // Whether to compress map outputs
        conf.setBoolean(Job.MAP_OUTPUT_COMPRESS, true);
        // The compression codec to use for map outputs
        conf.setClass(Job.MAP_OUTPUT_COMPRESS_CODEC, GzipCodec.class, Comparable.class);

        Job job = new Job(conf);
        job.setJarByClass(MaxTemperature.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setCombinerClass(MaxTemperatureReducer.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
