package nidhinkumar.reccs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by M.S. Venugopal on 20-05-2016.
 */
public class Utils {
    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
}
