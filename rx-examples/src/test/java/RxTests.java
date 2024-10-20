import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import util.Color;

import java.io.FileNotFoundException;

import static util.ColorUtil.print;
import static util.ColorUtil.printThread;

public class RxTests {

    private static final String MOVIES1_DB = "movies1";

    private static final String MOVIES2_DB = "movies2";

    /**
     * Example 1: Creating and subscribing observable from iterable.
     */
    @Test
    public void loadMoviesAsList() throws FileNotFoundException {

        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesFromList(MOVIES1_DB)
                .subscribe(movie -> print(movie, Color.BLUE));
    }

    /**
     * Example 2: Creating and subscribing observable from custom emitter.
     */
    @Test
    public void loadMoviesAsStream() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .subscribe(movie -> print(movie, Color.BLUE));
    }

    /**
     * Example 3: Handling errors.
     */
    @Test
    public void loadMoviesAsStreamAndHandleError() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream("<:").
                subscribe(movie -> print(movie, Color.BLUE), error -> print("Error",Color.RED));

    }

    /**
     * Example 4: Signaling end of a stream.
     */
    @Test
    public void loadMoviesAsStreamAndFinishWithMessage() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .take(10)
                .subscribe(movie -> print(movie, Color.BLUE),
                        error -> print("Error",Color.RED),
                        () -> print("Finished",Color.GREEN));


    }

    /**
     * Example 5: Filtering stream data.
     */
    @Test
    public void displayLongMovies() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .filter(movie -> movie.getLength()>150)
                .subscribe(movie -> print(movie, Color.BLUE),
                        error -> print("Error",Color.RED),
                        () -> print("Finished",Color.GREEN));
    }

    /**
     * Example 6: Transforming stream data.
     */
    @Test
    public void displaySortedMoviesTitles() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .map(Movie::getTitle)
                .take(10)
                .sorted()
                .subscribe(movie -> print(movie, Color.BLUE),
                        error -> print("Error",Color.RED),
                        () -> print("Finished",Color.GREEN));
        //Sorted zbiera wszystkie filmy i je sortuje
        //kolejność take i sorted ma znaczenie
    }

    /**
     * Example 7: Monads are like burritos.
     */
    @Test
    public void displayActorsForMovies() {

    }

    /**
     * Example 8: Combining observables.
     */
    @Test
    public void loadMoviesFromManySources() {

    }

    /**
     * Example 9: Playing with threads (subscribeOn).
     */
    @Test
    public void loadMoviesInBackground() {

    }

    /**
     * Example 10: Playing with threads (observeOn).
     */
    @Test
    public void switchThreadsDuringMoviesProcessing() {

    }

    /**
     * Example 11: Combining parallel streams.
     */
    @Test
    public void loadMoviesFromManySourcesParallel() {
        // Static merge solution



        // FlatMap solution:
        final MovieDescriptor movie1Descriptor = new MovieDescriptor(MOVIES1_DB, Color.GREEN);
        final MovieDescriptor movie2Descriptor = new MovieDescriptor(MOVIES2_DB, Color.BLUE);

    }

    /**
     * Example 12: Zip operator.
     */
    @Test
    public void loadMoviesWithDelay() {

    }

    /**
     * Example 13: Backpressure.
     */
    @Test
    public void trackMoviesLoadingWithBackpressure() {

    }

    /**
     * Example 14: Cold and hot observables.
     */
    @Test
    public void oneMovieStreamManyDifferentSubscribers() {

    }

    /**
     * Example 15: Caching observables (hot-cold hybrid).
     */
    @Test
    public void cacheMoviesInfo() {

    }

    private void displayProgress(Movie movie) throws InterruptedException {
        print((movie.getIndex() / 500.0 * 100) + "%", Color.GREEN);
        Thread.sleep(50);
    }

    private class MovieDescriptor {
        private final String movieDbFilename;

        private final Color debugColor;

        private MovieDescriptor(String movieDbFilename, Color debugColor) {
            this.movieDbFilename = movieDbFilename;
            this.debugColor = debugColor;
        }

        public Color getDebugColor() {
            return debugColor;
        }

        public String getMovieDbFilename() {
            return movieDbFilename;
        }
    }
}
