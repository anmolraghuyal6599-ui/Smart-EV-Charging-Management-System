
package storage;

import java.io.IOException;
import java.util.List;

// Generic interface for storing and retrieving objects
public interface DataStore<T> {

    // Save a list of objects
    void save(List<T> data) throws IOException;

    // Load objects from storage
    List<T> load() throws IOException, ClassNotFoundException;
}
