package net.exsource.openutils.tools;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.util.ConsoleColor;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unused")
public class Commons {

    private static final Logger logger = Logger.getLogger();

    /* =================================================================================
     *
     *                               Date Functions
     *
     * ================================================================================= */

    /**
     * This function converted your date object to a string.
     * You can choose the used pattern, the pattern ar from {@link SimpleDateFormat}.
     * If you need help by the pattern, visit this website:
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
     * This function fixed path variables, this means it makes the path readable for the java
     * language. This is helpful by using more than one operating system like dos and unix.
     *
     * @param path the string which needs the file path layout.
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
     * @return File - the directory as work-friendly object.
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
     * @return File - the directory as work-friendly object.
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
     * This function filtered the file name forms the path and cut the path away.
     *
     * @param absolutePath the correct and absolute path to the file.
     * @return String - the correct file name with an extension.
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
     * This means it will be calculated from the resource itself.
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
     * This function converts a raw file resource to an {@link InputStream}.
     * The stream is for example used at {@link #resourceToByteBuffer(String, int)}.
     *
     * @param resource the resource which needs to be converted.
     * @return InputStream - the created stream from the given resource.
     * @throws IOException if the resource isn't found.
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
     * @param stream the input stream which needs to convert.
     * @param size the byte array size.
     * @return byte[] - the bytes from the stream as an array.
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

    /**
     * This function saved a resource from the internal resource folder into a given output folder.
     * Note that this function is currently not 100% tested! This means there can be throw exceptions
     *  that are not handled yet. This function worked with {@link InputStream}'s and {@link OutputStream}'s
     * for handling files. Note that files with commits can be corrupted after overriding because the
     * commits will be added randomly.
     * @param resource the file which is existed internal.
     * @param out the specified output path, if this wasn't found, the function created the folder.
     * @param override this means that's this file will be overridden after any call this function.
     * @apiNote this function is already in development! Last update: 26.06.2023
     * @see File
     * @see FileOutputStream
     */
    public static void saveResource(@NotNull String resource, @NotNull String out, boolean override) {
        if(resource.startsWith("/"))
            resource = resource.substring(1);

        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if(url == null) {
            logger.warn("Can't find [ " + ConsoleColor.YELLOW + resource + ConsoleColor.RESET + " ]");
            return;
        }

        InputStream inStream;
        FileOutputStream outputStream;

        try {
            File file = new File(url.toURI());
            String filename;
            if(resource.contains("/"))
                filename = resource.substring(resource.lastIndexOf("/"));
            else
                filename = resource;

            File outPath = new File(out);
            if(!outPath.exists() && outPath.mkdirs())
                logger.debug("Created missing directories [ " + ConsoleColor.CYAN + outPath.getPath() + ConsoleColor.RESET + " ]");

            if(!file.exists()) {
                logger.warn("Can't find file " + ConsoleColor.YELLOW + file.getAbsolutePath() + ConsoleColor.RESET);
                return;
            }

            if(!outPath.exists()) {
                logger.warn("Can't find out path " + ConsoleColor.YELLOW + outPath.getAbsolutePath() + ConsoleColor.RESET);
                return;
            }

            File output = new File(outPath.getPath() + "/" + filename);
            if(output.exists() && !override)
                return;

            inStream = resurceToInputStream(resource);
            outputStream = new FileOutputStream(output);
            int character;
            while ((character = inStream.read()) != -1) {
                outputStream.write(character);
            }

            inStream.close();
            outputStream.close();
        } catch (URISyntaxException | IOException exception) {
            logger.error(exception);
        }
    }

    /* =================================================================================
     *
     *                               Cast Functions
     *
     * ================================================================================= */

    /**
     * This function will try to cast the given object to the given class you set.
     * If this does not work, it returns the object back.
     * The first check is if the object is null.
     * Is it null we can't work with this.
     *
     * @param object will be cast to other class.
     * @param type the caster class.
     * @return T - the finished cast object.
     * @param <T> - the placeholder for the cast class object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object object, final Class<T> type) {
        if(object == null)
            return null;

        if(type.isInstance(object))
            return (T) object;

        if(String.class.equals(object))
            return (T) object.toString();

        if(ClassUtils.isPrimitiveWrapper(object.getClass())
                && object.getClass().equals(ClassUtils.primitiveToWrapper(type)))
            return (T) object;

        if(object instanceof Number) {
            if(Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type))
                return (T) (Long) ((Number) object).longValue();

            if(Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type))
                return (T) (Integer) ((Number) object).intValue();

            if(Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type))
                return (T) (Double) ((Number) object).doubleValue();

            if(Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type))
                return (T) (Float) ((Number) object).floatValue();

            if(Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type))
                return (T) (Short) ((Number) object).shortValue();

            if(Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type))
                return (T) (Byte) ((Number) object).byteValue();
        }

        if(Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type))
            return (T) Boolean.valueOf(object.toString());

        if(Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type))
            return (T) Character.valueOf(object.toString().charAt(0));

        return (T) object;
    }

    /* =================================================================================
     *
     *                              String Functions
     *
     * ================================================================================= */

    /**
     * This function generates a random string from the given {@link StringPattern} enum.
     * The enum contains many patterns for a specified algorithm. You can choose any of this pattern.
     * The UUID pattern used the default {@link UUID} class for generation. All other patterns were created
     * by the api developers.
     * @param pattern the generation pattern.
     * @param length the string output path.
     * @return String - the generated string from this function.
     * @apiNote Note that this function can't return null. This function can be used in all of yor classes,
     * it is helpfully for generated unique ids or names.
     * @see StringPattern
     * @see StandardCharsets
     */
    public static String generateString(StringPattern pattern, int length) {
        String result;
        if(length <= 0) {
            logger.warn("You will create a randomized string with a length equals or less 0?");
            length = 8;
        }

        if(pattern == null) {
            byte[] bytes = new byte[length];
            new Random().nextBytes(bytes);
            result = new String(bytes, StandardCharsets.UTF_8);
            logger.warn("Create a randomized string from old java code because the pattern is null!");
        } else {
            byte[] bytes = new byte[256];
            new Random().nextBytes(bytes);
            String randomString = new String(bytes, StandardCharsets.UTF_8);
            String replacer;

            if(pattern.equals(StringPattern.UUID_FORMAT))
                result = UUID.randomUUID().toString();
            else {
                replacer = randomString.replaceAll(pattern.getPattern(), "");
                result = randomFormatted(replacer, length);
            }
        }

        return result;
    }

    /**
     * This private function generates the string from the input string.
     * The input can be ABC... etc. The function worked with for-loops to catch
     * single chars from the input string to create a random result.
     * This function is used by {@link #generateString(StringPattern, int)}
     * @param in the given converted pattern string.
     * @param length the allowed sting build length.
     * @return String - the finished created random string.
     * @see StringBuffer
     */
    private static String randomFormatted(String in, int length) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < in.length(); i++) {
            if(Character.isLetter(in.charAt(i)) && (length > 0)
                    || Character.isDigit(in.charAt(i)) && (length > 0)) {
                builder.append(in.charAt(i));
                length--;
            }
        }
        return builder.toString();
    }

    /* =================================================================================
     *
     *                               Read Functions
     *
     * ================================================================================= */

    /**
     * This function read a document and split it in string lines.
     * This lines will be added to a {@link List<String>} with the {@link List#add(Object)} function.
     * Please note that it is not important to declarative which file type it is.
     * <p>
     * @param document the document you need to read.
     * @return List<String> - the string list of the single lines.
     */
    public static List<String> readDocument(@NotNull String document) {
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(document));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException exception) {
                logger.error(exception);
            }
        } catch (FileNotFoundException exception) {
            logger.error(exception);
        }

        return lines;
    }
}
