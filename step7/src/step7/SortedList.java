package step7;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedList extends AbstractList<Movie_rating> {

	private List<Movie_rating> internalList = new ArrayList<Movie_rating>();

	public void addObject(Movie_rating mycompositewritable) {
		internalList.add(mycompositewritable);
		Collections.sort(internalList, new Comparator<Movie_rating>() {

			@Override
			public int compare(Movie_rating o1, Movie_rating o2) {
				// TODO Auto-generated method stub
				return o1.getRating() > o2.getRating() ? -1 : (o1.getRating() < o2.getRating()) ? 1 : 0;
			}
		});
	}

	@Override
	public Movie_rating get(int index) {
		// TODO Auto-generated method stub

		return internalList.get(index);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return internalList.size();
	}

	public void deletelast() {
		internalList.remove(internalList.size() - 1);
	}

}
