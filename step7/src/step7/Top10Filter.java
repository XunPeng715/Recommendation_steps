package step7;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Top10Filter {
	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
		private Text k = new Text();
		private Text v = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer str = new StringTokenizer(value.toString(), "\n");
			while (str.hasMoreTokens()) {
				String[] tokens = str.nextToken().split("::");
				String userID = tokens[1];
				String movieID = tokens[0];
				String ratings = tokens[2];
				String movieName = tokens[3];

				k.set(userID);
				v.set(movieID + "::" + movieName + "::" + ratings);
				context.write(k, v);
			}
		}
	}

	public static class compareReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			// sortedlist.clear();
			SortedList sortedlist = new SortedList();

			for (Text val : values) {
				// 按照value的rating降序排序
				String[] tokens = val.toString().split("::");
				int movieID = Integer.valueOf(tokens[0]);
				String moviename = String.valueOf(tokens[1]);
				float rating = Float.valueOf(tokens[2]);

				Movie_rating movie_rating = new Movie_rating(movieID, rating, moviename);

				sortedlist.addObject(movie_rating);
				if (sortedlist.size() > 2) {
					sortedlist.deletelast();
				}
			}

			for (Movie_rating mr : sortedlist) {
				int movieID = mr.getMovieID();
				String movieName = mr.getMovieName();
				float rating = mr.getRating();
				
				String v1 = String.valueOf(movieID);
				
				String v2 = String.valueOf(rating);
				Text v = new Text(v1 + " " + movieName + " " + v2);
				context.write(key, v);
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Filter");
		job.setJarByClass(Top10Filter.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setReducerClass(compareReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
