
package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Handles saving and loading objects using file storage
public class FileDataStore<T> implements DataStore<T> {

    private final String fileName;

    // Constructor
    public FileDataStore(String fileName) {
        this.fileName = fileName;
    }

    // Save objects to file
    @Override
    public void save(List<T> data) throws IOException {

        // Create parent directories if they do not exist
        Path path = Paths.get(fileName).toAbsolutePath();
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream(fileName))) {

            output.writeObject(data);
        }
    }

    // Load objects from file
    @Override
    @SuppressWarnings("unchecked")
    public List<T> load() throws IOException, ClassNotFoundException {

        try (ObjectInputStream input =
                new ObjectInputStream(new FileInputStream(fileName))) {

            return (List<T>) input.readObject();

        } catch (FileNotFoundException e) {

            // Return an empty list if storage file does not exist yet
            return new ArrayList<>();
        }
    }
}
