package step6;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class MyCompositeWritable implements Writable{
	private int movie;
	private float rating;
	
	public MyCompositeWritable(){
		
	}
	
	public MyCompositeWritable(int movie, float rating){
		this.rating = rating;
		this.movie = movie;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		movie = in.readInt();
		rating = in.readFloat();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeFloat(rating);
		out.writeInt(movie);
	}

	public int getMovie() {
		return movie;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
}
