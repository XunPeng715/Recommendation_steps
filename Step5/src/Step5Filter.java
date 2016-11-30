import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Step5Filter {

	public static class FilterMapper1 extends Mapper<Object, Text, Text, Text> {
		private Text k = new Text();
		private Text v = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer str = new StringTokenizer(value.toString(), "\n");
			while (str.hasMoreTokens()) {
				String[] tokens = str.nextToken().split(",");
				String user = tokens[0];
				String itemID = tokens[1];
				k.set(user + " " + itemID);
				v.set("A");
				context.write(k, v);
			}
		}
	}

	public static class Mapper2 extends Mapper<Object, Text, Text, Text> {
		private Text k = new Text();
		private Text v = new Text(); 
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer str = new StringTokenizer(value.toString(), "\n");
			while (str.hasMoreTokens()) {
				String[] tokens = str.nextToken().split("\\s+");
				String user = tokens[0];
				String itemID = tokens[1];
				String rating = tokens[2];
				k.set(user + " " + itemID);
				v.set(rating);
				context.write(k, v);
			}
		}
	}

	public static class JoinReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			ArrayList<Text> list = new ArrayList<Text>();
			for(Text val : values){
				list.add(val);
			}
			if(list.size() == 1){
				context.write(key, list.get(0));
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Filter");
		job.setJarByClass(Step5Filter.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class,
				FilterMapper1.class);
	    MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class,
	            Mapper2.class);
	    job.setReducerClass(JoinReducer.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
		
		TextOutputFormat.setOutputPath(job, new Path(args[2]));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.exit(job.waitForCompletion(true) ? 0 : 2);
	}
}
