import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import util.Color;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

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
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .filter(movie -> movie.getLength()>150)
                .flatMap(movie -> Observable.fromIterable(movieReader.readActors(movie)))
                .distinct()
                .sorted()
                .subscribe(movie -> print(movie, Color.BLUE));
    }

    /**
     * Example 8: Combining observables.
     */
    @Test
    public void loadMoviesFromManySources() {
        MovieReader movieReader = new MovieReader();
        Observable<Movie> movies1 = movieReader.getMoviesAsStream(MOVIES1_DB)
                .doOnNext(movie -> print(movie, Color.RED));
        Observable<Movie> movies2 = movieReader.getMoviesAsStream(MOVIES2_DB)
                .doOnNext(movie -> print(movie, Color.GREEN));
        Observable.merge(movies1, movies2)
                .subscribe(movie -> print(movie,Color.BLUE));

    }

    /**
     * Example 9: Playing with threads (subscribeOn).
     */
    @Test
    public void loadMoviesInBackground() throws InterruptedException {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .subscribeOn(Schedulers.newThread())
                .subscribe(movie -> printThread(movie,Color.BLUE));
        printThread("Po wszystkim", Color.MAGENTA);
        Thread.sleep(10000);
    }

    /**
     * Example 10: Playing with threads (observeOn).
     */
    @Test
    public void switchThreadsDuringMoviesProcessing() throws InterruptedException {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .map(Movie::getIndex)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(movie -> printThread(movie,Color.RED))
                .observeOn(Schedulers.newThread())
                .doOnNext(movie -> printThread(movie,Color.GREEN))
                .blockingSubscribe(movie -> printThread(movie,Color.BLUE));



    }

    /**
     * Example 11: Combining parallel streams.
     */
    @Test
    public void loadMoviesFromManySourcesParallel() {
        // Static merge solution
        MovieReader movieReader = new MovieReader();
//        Observable<Movie> movies1 = movieReader.getMoviesAsStream(MOVIES1_DB)
//                .doOnNext(movie -> print(movie, Color.RED))
//                .subscribeOn(Schedulers.newThread());
//        Observable<Movie> movies2 = movieReader.getMoviesAsStream(MOVIES2_DB)
//                .doOnNext(movie -> print(movie, Color.GREEN))
//                .subscribeOn(Schedulers.newThread());
//        Observable.merge(movies1, movies2)
//                .blockingSubscribe(movie -> print(movie,Color.BLUE));


        // FlatMap solution:
        final MovieDescriptor movie1Descriptor = new MovieDescriptor(MOVIES1_DB, Color.RED);
        final MovieDescriptor movie2Descriptor = new MovieDescriptor(MOVIES2_DB, Color.GREEN);
        Observable.just(movie1Descriptor, movie2Descriptor)
                .flatMap(movieDescriptor -> movieReader.getMoviesAsStream(movieDescriptor.getMovieDbFilename()))
                .doOnNext(movie -> print(movie,movie1Descriptor.getDebugColor()))
                .subscribeOn(Schedulers.newThread())
                .blockingSubscribe(movie -> print(movie,Color.BLUE));
    }

    /**
     * Example 12: Zip operator.
     */
    @Test
    public void loadMoviesWithDelay() {
        MovieReader movieReader = new MovieReader();
        Observable<Movie> movies1 = movieReader.getMoviesAsStream(MOVIES1_DB)
                .take(10)
                .subscribeOn(Schedulers.newThread());
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        Observable.zip(movies1,interval, (movie,tick)->movie)
                .blockingSubscribe(movie -> print(movie,Color.BLUE));
    }

    /**
     * Example 13: Backpressure.
     */
    @Test
    public void trackMoviesLoadingWithBackpressure() {
        MovieReader movieReader = new MovieReader();
        movieReader.getMoviesAsStream(MOVIES1_DB)
                .doOnNext(movie -> print(movie,Color.RED))
                .doOnNext(movie -> Thread.sleep(10))
                .subscribeOn(Schedulers.newThread())
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io(),true,1)
                .doOnNext(this::displayProgress)
                .blockingSubscribe();
    }

    /**
     * Example 14: Cold and hot observables.
     */
    @Test
    public void oneMovieStreamManyDifferentSubscribers() {
        MovieReader movieReader = new MovieReader();
        //cold observable
        Observable<Movie> movies1 = movieReader.getMoviesAsStream(MOVIES1_DB);

        //hot observable

        ConnectableObservable<Movie> hotObservable = movies1.publish();
        hotObservable
                .take(30)
                .subscribe(movie -> print(movie,Color.RED));
        hotObservable
                .filter(movie -> movie.getRating().equals("R"))
                .subscribe(movie -> print(movie,Color.BLUE));
        hotObservable.connect();
        hotObservable
                .take(10)
                .subscribe(movie -> print(movie,Color.GREEN));
    }

    /**
     * Example 15: Caching observables (hot-cold hybrid).
     */
    @Test
    public void cacheMoviesInfo() {
        MovieReader movieReader = new MovieReader();
        Observable<Movie> movies1 = movieReader.getMoviesAsStream(MOVIES1_DB).cache();
        movies1.subscribe(movie -> print(movie,Color.RED));
        Long moviesCount = movies1.count().blockingGet();
        System.out.println(moviesCount);
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
