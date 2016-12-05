package step7;

public class Movie_rating {

	private int movieID;
	private float rating;
	private String movieName;
	
	public Movie_rating(){
		
	}
		
	public Movie_rating(int movieID, float rating, String movieName){
		this.rating = rating;
		this.movieID = movieID;
		this.movieName = movieName;
	}

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	
}
