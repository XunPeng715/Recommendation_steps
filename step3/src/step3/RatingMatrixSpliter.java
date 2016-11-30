package step3;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RatingMatrixSpliter {
	
	public static class SpliterMapper extends Mapper<Object, Text, IntWritable, Text>{
		private IntWritable k = new IntWritable();
		private Text v = new Text();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
			StringTokenizer itr = new StringTokenizer(value.toString(), "\n");
			while(itr.hasMoreTokens()){
				String[] string = itr.nextToken().split("\\s+");
				String user = string[0];
				String[] itemIDandRating = string[1].split(",");
				int size = itemIDandRating.length;
				for(int i = 0; i < size; i++){
					String[] itemandrating = itemIDandRating[i].split(":");
					String item = itemandrating[0];
					String rating = itemandrating[1];
					k.set(Integer.valueOf(item));
					v.set(user + ":" + rating);
					context.write(k, v);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "rating matrix");
		job.setJarByClass(RatingMatrixSpliter.class);
		job.setMapperClass(SpliterMapper.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
	
}
