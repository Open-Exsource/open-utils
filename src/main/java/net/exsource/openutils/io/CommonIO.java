package net.exsource.openutils.io;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.tools.DateFormat;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class CommonIO {

    private static final Logger logger = Logger.getLogger();

    /* =================================================================================
     *
     *                               Date Functions
     *
     * ================================================================================= */

    /**
     * This function converted your date object to a string.
     * You can choose the used pattern, the pattern ar from {@link SimpleDateFormat}.
     * If you need help by the pattern visit this website:
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">click here</a>
     *
     * @param date your date object can be null but than the function will set it to now.
     * @param pattern the simpleDateFormat pattern. By default "dd.MM.yyyy".
     * @return String - the generated date as string.
     *
     * @see SimpleDateFormat
     * @see Date
     */
    public static String parseDateToString(Date date, String pattern) {
        if(pattern == null || pattern.isEmpty() || pattern.isBlank())
            pattern = "dd.MM.yyyy";

        if(date == null)
            date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * This function converted your date object to a string.
     * You can choose a pattern from the {@link DateFormat} class.
     * This function is calling the mother function {@link #parseDateToString(Date, String)}.
     *
     * @param date your date object can be null but than the function will set it to now.
     * @param format this used our default formats, for more pattern use the {@link #parseDateToString(Date, String)} function.
     * @return String - the generated date as string.
     *
     * @see SimpleDateFormat
     * @see Date
     */
    public static String parseDateToString(Date date, DateFormat format) {
        return parseDateToString(date, format.getPattern());
    }

    /* =================================================================================
     *
     *                               File Functions
     *
     * ================================================================================= */

    /**
     * This function fixed path variables, this means it make the path readable for the java
     * language. This is helpful by using more than one operating system like dos and unix.
     *
     * @param path the string which need the file path layout.
     * @return String - the converted path.
     */
    public static String convertToPath(@NotNull String path) {
        return path
                .replace("\\/", File.separator)
                .replace("\\\\", File.separator)
                .replace("\\", File.separator)
                .replace("/", File.separator);
    }

    /**
     * This function gives the correct file directory without the file name.
     * This means it will return the directory which stored the file.
     *
     * @param file the file object to become the absolute path.
     * @return File - the directory as work friendly object.
     *
     * @see File
     */
    public static File getDirectoryFromFile(@NotNull File file) {
        if(file.exists())
            return new File("dummy");
        return getDirectoryFromFile(file.getAbsolutePath());
    }

    /**
     * This function gives the correct file directory without the file name.
     * This means it will return the directory which stored the file.
     *
     * @param absolutePath the absolute string path to the file.
     * @return File - the directory as work friendly object.
     *
     * @see File
     */
    public static File getDirectoryFromFile(@NotNull String absolutePath) {
        String fixedPath = convertToPath(absolutePath);
        String directory = fixedPath;

        if(directory.contains(File.separator))
            directory = directory.substring(0, fixedPath.lastIndexOf(File.separatorChar));

        if(directory.endsWith(File.separator))
            return new File(directory);

        return new File(directory + File.separatorChar);
    }

    /**
     * This function filtered the file name form the path and cut the path away.
     *
     * @param absolutePath the correct and absolute path to the file.
     * @return String - the correct file name with extension.
     */
    public static String getFileName(@NotNull String absolutePath) {
        String fixedPath = convertToPath(absolutePath);
        return fixedPath.substring(fixedPath.lastIndexOf(File.separatorChar) + 1);
    }

    /**
     * This function converted the given file name and removed the file type.
     *
     * @param pathValue the value can be the absolutePath, filename or the normal path.
     * @return String - only the file name.
     */
    public static String getOnlyFileName(@NotNull String pathValue) {
        if(pathValue.isEmpty() || pathValue.isBlank())
            return "";

        if(pathValue.contains(String.valueOf(File.separatorChar)))
            pathValue = getFileName(pathValue);

        int dotPos = pathValue.lastIndexOf(".");
        if(dotPos != -1)
            pathValue = pathValue.substring(0, dotPos);

        return pathValue;
    }

    /**
     * This function converted the given file and removed the file name.
     * This is useful to become the file type.
     *
     * @param pathValue the value can be the absolutePath, filename or the normal path.
     * @return String - only the file type.
     */
    public static String getFileType(@NotNull String pathValue) {
        if(pathValue.isEmpty() || pathValue.isBlank())
            return "";

        if(pathValue.contains(String.valueOf(File.separatorChar)))
            pathValue = getFileName(pathValue);

        String[] converted = pathValue.split("\\.");
        if(converted.length < 2)
            return "";

        return converted[converted.length - 1];
    }

    /**
     * This function created form a raw resource an {@link ByteBuffer}.
     * We used for the progress the following functions {@link #toByteArray(InputStream, int)}
     * and {@link #resurceToInputStream(String)}
     * Warning a buffer size which is out of bounds for the given resource,
     * can be ended in an error.
     *
     * @param resource the resource is any file you want.
     * @param bufferSize the buffer size you need.
     * @return ByteBuffer - the finished {@link ByteBuffer} object.
     * @see IOException
     * @see ByteBuffer
     */
    public static ByteBuffer resourceToByteBuffer(@NotNull String resource, int bufferSize) {
        ByteBuffer data = null;
        try {
            InputStream stream = resurceToInputStream(resource);
            if(stream == null) {
                logger.error(new FileNotFoundException("File " + resource + " not found!"));
                return null;
            }
            byte[] bytes = toByteArray(stream, bufferSize);
            data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
            data.flip();
        } catch (IOException exception) {
            logger.error(exception);
        }
        return data;
    }

    /**
     * This function created form a raw resource an {@link ByteBuffer}.
     * It will use his parent function {@link #resourceToByteBuffer(String, int)} and
     * he used the {@link #calculateBufferCapacity(String)} for the buffer size.
     * This means it will calculate from the resource itself.
     *
     * @param resource the resource is any file you want.
     * @return ByteBuffer - the finished {@link ByteBuffer} object.
     * @see IOException
     * @see ByteBuffer
     */
    public static ByteBuffer resourceToByteBuffer(@NotNull String resource) {
        return resourceToByteBuffer(resource, calculateBufferCapacity(resource));
    }

    /**
     * This function convert a raw file resource to an {@link InputStream}.
     * The stream is for example used at {@link #resourceToByteBuffer(String, int)}.
     *
     * @param resource the resource which need to be converted.
     * @return InputStream - the created stream from the given resource.
     * @throws IOException if the resource not found.
     * @see IOException
     * @see InputStream
     */
    public static InputStream resurceToInputStream(@NotNull String resource) throws IOException {
        InputStream stream;
        File file = new File(resource);

        if(file.exists() && file.isFile())
            stream = new FileInputStream(file);
        else
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

        return stream;
    }

    /**
     * This function creates a byte array from {@link InputStream}.
     * This is helpful for functions like {@link #resourceToByteBuffer(String)}.
     *
     * @param stream the input stream which need to convert.
     * @param size the byte array size.
     * @return byte[] - the bytes from the stream as array.
     * @see IOException
     * @see InputStream
     */
    public static byte[] toByteArray(@NotNull InputStream stream, int size) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[size];

        try {
            while ((read = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            buffer.flush();
        } catch (IOException exception) {
            logger.error(exception);
        }

        return buffer.toByteArray();
    }

    /**
     * This function generates the capacity from a raw resource.
     * This is a better function to that step. Use at {@link #resourceToByteBuffer(String)}.
     *
     * @param path the file path to the resource.
     * @return Integer - the generated capacity.
     * @see InputStream
     */
    public static int calculateBufferCapacity(@NotNull String path) {
        int capacity = 0;
        try {
            InputStream stream = new FileInputStream(path);
            capacity = stream.available();
            stream.close();
        } catch (IOException exception) {
            logger.error(exception);
        }
        return capacity;
    }

    /**
     * This function gets the directory from the current running java program.
     *
     * @param indicator a class from the packed .jar file as indicator.
     * @return File - directory as file. Can be null if no class indicator was found.
     * @see URISyntaxException
     * @see File
     */
    public static File getProgramDirectory(Class<?> indicator) {
        File result = null;
        try {
            result = new File(indicator.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException exception) {
            logger.error(exception);
        }
        return result;
    }

    /**
     * This function gets the parent directory from the current running java program.
     *
     * @param indicator a class from the packed .jar file as indicator.
     * @return File - directory as file. Can be null if no class indicator was found.
     * @see NullPointerException
     * @see File
     */
    public static File getProgramParentDirectory(Class<?> indicator) {
        File result = null;
        try {
            result = new File(getProgramDirectory(indicator).getParentFile().getPath());
        } catch (NullPointerException exception) {
            logger.error(exception);
        }
        return result;
    }

}
