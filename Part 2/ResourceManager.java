

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {
    
    public static void save(Serializable data, String saveFile) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(saveFile)))) {
             oos.writeObject(data);
        }
    }

    public static Object load(String saveFile) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(saveFile)))) {
            return ois.readObject();
        }
    }
}
