package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;
    DogApiBreedFetcher fetcher = new DogApiBreedFetcher();
    BreedFetcherForLocalTesting fetcherForLocalTesting = new BreedFetcherForLocalTesting();
    public static HashMap cache = new HashMap();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        if  (fetcher instanceof DogApiBreedFetcher) {
            this.fetcher = (DogApiBreedFetcher) fetcher;
            this.fetcherForLocalTesting = null;
        }
        else if  (fetcher instanceof BreedFetcherForLocalTesting) {
            this.fetcherForLocalTesting = (BreedFetcherForLocalTesting) fetcher;
            this.fetcher = null;
        }
    }


    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        ArrayList<String> subBreeds = new ArrayList<>();
        try {
            if (cache.get(breed) != null) {
                return (List<String>) cache.get(breed);
            } else if (fetcher != null) {
                for (String s : fetcher.getSubBreeds(breed)) {
                    subBreeds.add(s);
                }
                cache.put(breed, subBreeds);
            } else {

                for (String s : fetcherForLocalTesting.getSubBreeds(breed)) {
                    subBreeds.add(s);
                }
                cache.put(breed, subBreeds);
            }
        } catch (BreedFetcher.BreedNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        callsMade++;

            // return statement included so that the starter code can compile and run.
            return subBreeds;
        }
    public int getCallsMade() {
        return callsMade;
    }
}