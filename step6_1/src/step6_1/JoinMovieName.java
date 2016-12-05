package step6_1;

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

public class JoinMovieName {
	public static class MovieJoinMapper extends Mapper<Object, Text, Text, Text> {
		private Text outkey = new Text();
		private Text outvalue = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer str = new StringTokenizer(value.toString(), "\n");
			while (str.hasMoreTokens()) {
				String[] tokens = str.nextToken().split("\\s++");
				outkey.set(tokens[1]);
				outvalue.set("A" + tokens[0] + "::" + tokens[2]);

				context.write(outkey, outvalue);
			}
		}
	}

	public static class NameJoinMapper extends Mapper<Object, Text, Text, Text> {
		private Text outkey = new Text();
		private Text outvalue = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer str = new StringTokenizer(value.toString(), "\n");
			while (str.hasMoreTokens()) {
				String[] tokens = str.nextToken().split("::");
				outkey.set(tokens[0]);
				outvalue.set("B" + tokens[1]);

				context.write(outkey, outvalue);
			}
		}
	}

	public static class MovieNameJoinReducer extends Reducer<Text, Text, Text, Text> {
		private Text tmp = new Text();
		private ArrayList<String> listA = new ArrayList<String>();
		private ArrayList<String> listB = new ArrayList<String>();

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			listA.clear();
			listB.clear();

			while (values.iterator().hasNext()) {
				tmp = values.iterator().next();
				if (Character.toString((char) tmp.charAt(0)).equals("A")) {
					listA.add(tmp.toString().substring(1));
				} else if (Character.toString((char) tmp.charAt(0)).equals("B")) {
					listB.add(tmp.toString().substring(1));
				}
			}
			executeJoin(context, key);
		}

		private void executeJoin(Context context, Text key) throws IOException, InterruptedException {
			if (!listA.isEmpty() && !listB.isEmpty()) {
				for (String A : listA) {
					for (String B : listB) {
						context.write(key, new Text(A + "::" + B));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		
		conf.set("mapreduce.output.textoutputformat.separator", "::");
		
		Job job = Job.getInstance(conf, "Join");
		job.setJarByClass(JoinMovieName.class);

		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, MovieJoinMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, NameJoinMapper.class);

		job.setReducerClass(MovieNameJoinReducer.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job, new Path(args[2]));

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
