package net.exsource.openutils.io.controller;

import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openutils.io.IOController;
import net.exsource.openutils.tools.Commons;
import net.exsource.openutils.enums.DateFormat;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.exsource.openutils.tools.Commons.cast;

/**
 * This class controls .properties files, or it used {@link IOController#fromArgs(String[], Class)} to
 * convert it in to properties. You can create or edit properties with this class.
 * To use this class you need to create an instance of this class object. The following functions will do
 * that for you {@link IOController#fromArgs(String[], Class)} or you create it by
 * [ PropertiesController controller = new PropertiesController(); ] java standard.
 * If you used the function as indicator you can't use the {@link #save()} function, but if you wish
 * to save the current properties state you can use the {@link #saveAs(String)} or {@link #saveAs(File)}
 * function. This functions will create a new file at the specified parameter which ar given by you.
 * <p>
 * @since 1.0.0
 * @see IOController
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public class PropertiesController extends IOController {

    private static final Pattern COMMENT_LINE = Pattern.compile("^[;|#].*");
    private static final Pattern KEY_VALUE_PATTER = Pattern.compile( "\\s*([^=]*)=(.*)" );
    private final Map<String, Object> resultMap = new LinkedHashMap<>();

    private boolean autoSave;

    public PropertiesController() {
        this.autoSave = false;
    }

    public void load(final InputStream stream) throws IOException {
        logger.debug("Try loading .properties file...");
        if(stream == null)
            throw new FileNotFoundException("inputStream is null");

        try(InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            parseProperties(bufferedReader);
        }
    }

    @Override
    public void load(@NotNull File file) throws IOException {
        this.setResource(file);
        load(new FileInputStream(file));
    }

    @Override
    public void load(String[] args) throws IOException {
        if(args == null)
            throw new NullPointerException("The input args was null...");

        InputStream stream = new ByteArrayInputStream(String.join(System.lineSeparator(), Arrays.asList(args)).getBytes(StandardCharsets.UTF_8));
        load(stream);
    }

    public void load(final String string) throws IOException {
        if(string == null) {
            logger.error("Cannot load a file or raw string which is null!");
            return;
        }

        load(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }

    private void parseProperties(final BufferedReader reader) throws IOException {
        logger.debug("Begin parsing of .properties file...");
        if(reader == null) {
            logger.error("The given Reader was null!");
            return;
        }

        String line;
        while ((line = reader.readLine()) != null) {

            final Matcher commentMatcher = COMMENT_LINE.matcher(line);
            if (commentMatcher.matches()) {
                continue;
            }

            line = line.replaceAll("[^\\\\]#.+", "")
                    .replaceAll("\\\\([;#])", "$1");

            if (StringUtils.isEmpty(line)) {
                continue;
            }

            String key = null;
            String value = null;

            final Matcher keyValueMatcher = KEY_VALUE_PATTER.matcher(line);
            if (keyValueMatcher.matches()) {
                key = keyValueMatcher.group(1).trim();
                value = covertToReadableValue(keyValueMatcher.group(2).trim());
            }

            if(StringUtils.isNotEmpty(key)) {
                if(resultMap.containsKey(key)) {
                    resultMap.replace(key, value);
                    continue;
                }
                resultMap.put(key, value);
            }
        }

        logger.debug("Finished parsing .properties file successfully!");
    }

    public void save() {
        if(!useFile()) {
            logger.debug("Properties ( " + getID() + " ) can't be saved because it's not a file!");
            return;
        }

        File output = getResource();
        if(!output.exists()) {
            logger.error(new FileNotFoundException("Can't find " + output.getName()));
            return;
        }

        int entryIndex = 0;
        StringBuilder text = new StringBuilder();
        text.append("# Updated at: ").append(Commons.parseDateToString(new Date(), DateFormat.DATE_TIME)).append("\n");
        for(String key : resultMap.keySet()) {
            entryIndex++;
            if(entryIndex < resultMap.size()) {
                text.append(key).append(" = ").append(resultMap.get(key)).append("\n");
            } else {
                text.append(key).append(" = ").append(resultMap.get(key));
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            writer.write(text.toString());
            writer.close();
            logger.debug("Properties ( " + getID() + " ) saved!");
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    public void saveAs(@NotNull File file) {
        this.setResource(file);

        try {
            boolean wasCreatingDirectories = false;
            File directory = Commons.getDirectoryFromFile(file);
            if(directory.mkdirs()) {
                wasCreatingDirectories = true;
            }

            if (file.createNewFile()) {
                logger.info("Creating properties file: " + file.getName() + ", and directories: " + wasCreatingDirectories);
            }

            this.save();
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    public void saveAs(@NotNull String file) {
        this.saveAs(new File(file));
    }

    public void add(@NotNull String key, Object value) {
        if(hasKey(key)) {
            logger.debug(ConsoleColor.YELLOW + "Warning:" + ConsoleColor.RESET +
                    " The key: " + ConsoleColor.YELLOW + key + ConsoleColor.RESET + ", was found and can't be duplicated!");
            return;
        }
        resultMap.put(key, value);
        if(autoSave) {
            save();
        }
    }

    public void replace(@NotNull String key, Object value) {
        if(!hasKey(key)) {
            add(key, value);
            return;
        }
        resultMap.replace(key, value);
        if(autoSave) {
            save();
        }
    }

    public void remove(@NotNull String key) {
        if(!hasKey(key)) {
            logger.debug(ConsoleColor.YELLOW + "Warning:" + ConsoleColor.RESET +
                    " The key: " + ConsoleColor.YELLOW + key + ConsoleColor.RESET + ", wasn't found!");
            return;
        }
        resultMap.remove(key);
        if(autoSave) {
            save();
        }
    }

    public void enableAutoSave() {
        this.autoSave = true;
        logger.debug("Properties ( " + getID() + " ) enable auto-save!");
    }

    public void disableAutoSave() {
        this.autoSave = false;
        logger.debug("Properties ( " + getID() + " ) disable auto-save!");
    }

    public boolean usedAutoSave() {
        return autoSave;
    }

    public boolean useFile() {
        return getResource() != null;
    }

    @Override
    public boolean hasKey(@NotNull String key) {
        return resultMap.containsKey(key);
    }

    @Override
    public <T> T getValue(@NotNull String key, Class<T> cast) {
        return hasKey(key) ? cast(resultMap.get(key), cast) : null;
    }

    public Object[] getValueAsArray(@NotNull String key) {
        if(!hasKey(key))
            return new String[]{"error"};

        String rawValue = getValue(key, String.class);
        String transformed = rawValue.trim();
        Object[] array = new String[]{"error"};
        if(transformed.startsWith("[") && transformed.endsWith("]")) {
            transformed = transformed.substring(1, transformed.length() - 1);
            array = filterValue(transformed);
        }
        return array;
    }

    public List<Object> getValueAsList(@NotNull String key) {
        return Arrays.asList(getValueAsArray(key));
    }

    public Map<String, Object> getValueAsMap(@NotNull String key) {
        if(!hasKey(key))
            return new HashMap<>();

        final Map<String, Object> returnedMap = new HashMap<>();
        String line_to_convert = getValue(key, String.class).trim();
        if(line_to_convert.startsWith("{") && line_to_convert.endsWith("}")) {
            line_to_convert = line_to_convert.substring(1, line_to_convert.length() - 1).trim();
            includeEntryInMap(line_to_convert, returnedMap);
        }

        return returnedMap;
    }

    public Map<String, Object> getEntries() {
        return resultMap;
    }

    public List<String> getProperties() {
        List<String> intern = new ArrayList<>();
        for(String key : resultMap.keySet()) {
            intern.add(key + ":" + resultMap.get(key));
        }
        return intern;
    }

    public boolean isEmpty() {
        return resultMap.isEmpty();
    }

    public int getSize() {
        return resultMap.size();
    }

    public void clear() {
        resultMap.clear();
    }

    private void includeEntryInMap(@NotNull String in, final Map<String, Object> out) {
        if(out == null) {
            logger.error("The map to store can't be null!");
            return;
        }

        final List<String> splitList = new ArrayList<>();

        String progress = in.trim();
        int i = 0;

        int start = 0;
        int end = 0;
        boolean isArray = false;
        while (i < progress.length()) {
            int nextSplitPos = progress.charAt(i) == ',' ? i : -1;

            if(progress.charAt(i) == '[' && start == 0) {
                isArray = true;
            } else if(progress.charAt(i) == ']' && start == 0) {
                isArray = false;
            }

            if(!isArray) {
                int string = progress.charAt(i) == '\"' ? i : 0;

                if (start == 0) {
                    start = string;
                } else if (i != start) {
                    end = string;
                }
            }

            if(isArray) {
                nextSplitPos = -1;
            }

            if(nextSplitPos > start && nextSplitPos > end) {
                start = 0;
                String value = progress.substring(0, nextSplitPos).trim();
                splitList.add(value);
                progress = progress.substring(nextSplitPos + 1).trim();
                i = 0;
                continue;
            }

            i++;
        }

        if(!progress.isBlank() || !progress.isEmpty()) {
            if(progress.contains(":")) {
                String value = progress.trim();
                splitList.add(value);
            }
        }

        if(splitList.isEmpty()) {
            logger.error("There was an error at create a property map!");
            return;
        }

        for(String property : splitList) {
            if(!property.contains(":"))
                continue;

            String[] key_value = property.split(":", 2);
            if(out.containsKey(key_value[0]))
                continue;

            out.put(key_value[0], key_value[1]);
        }
    }

    private Object[] filterValue(String in) {
        String[] array = in.split("\",");
        for(int i = 0; i < array.length; i++) {
            array[i] = array[i].replaceAll("\"", "").trim();
        }
        return array;
    }
}
