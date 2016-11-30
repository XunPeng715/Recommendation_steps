package step6;

public class Movie_rating {

	private int movie;
	private float rating;
	
	public Movie_rating(){
		
	}
		
	public Movie_rating(int movie, float rating){
		this.rating = rating;
		this.movie = movie;
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
