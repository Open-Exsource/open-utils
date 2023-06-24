package net.exsource.openutils.io.controller;

import net.exsource.openutils.io.IOController;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.exsource.openutils.tools.Commons.cast;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * This class is the handler for all .ini stuffs. You can use it at any time and
 * all of your classes. Note that you first need to load an ini file {@link #load(File)},
 * before you can start using this class.
 * <p>
 * If you need to use raw strings and string arrays, please note that I'm working on it.
 * Currently, we only support .ini files.
 *
 * @since 1.0.0
 * @see IOController
 * @see MutableObject
 * @see IOUtils
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public class IniController extends IOController {

    private static final String NO_SECTION = "_NO_SECTION";
    private static final Pattern SECTION_PATTERN  = Pattern.compile( "\\s*\\[([^]]*)]\\s*" );
    private static final Pattern KEY_VALUE_PATTER = Pattern.compile( "\\s*([^=]*)=(.*)" );
    private static final Pattern COMMENT_LINE = Pattern.compile("^[;|#].*");
    private final Map<String, Map<String, Object>> resultMap = new LinkedHashMap<>();


    public void load(final InputStream inputStream) throws IOException {
        logger.debug("Try loading .ini file...");
        if (inputStream == null) {
            throw new FileNotFoundException("inputStream is null");
        }
        MutableObject<String> section = new MutableObject<>(NO_SECTION);
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            parseIniFile(section, bufferedReader);
        }
    }

    @Override
    public void load(final File file) throws IOException {
        load(new FileInputStream(file));
    }

    @Override
    public void load(String[] args) throws IOException {
        if(args == null)
            throw new NullPointerException("The input args was null...");

        InputStream stream = new ByteArrayInputStream(String.join(System.lineSeparator(), Arrays.asList(args)).getBytes(StandardCharsets.UTF_8));
        load(stream);
    }

    public void load(final String string) throws  IOException {
        if(string == null) {
            logger.error("Cannot load a file or raw string which is null!");
            return;
        }

        load(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void parseIniFile(final MutableObject<String> section,
                              final BufferedReader bufferedReader) throws IOException {
        logger.debug("Begin parsing of .ini file...");
        if(bufferedReader == null) {
            logger.error("The given Reader was null!");
            return;
        }

        final Map<String, Object> variables = new HashMap<>();
        variables.putAll(System.getenv());
        variables.putAll(new HashMap<String, Object>((Map) System.getProperties()));

        String line;
        StringBuilder multilineValue = new StringBuilder();
        String key = null;
        Map<String, StringSubstitutor> stringStringSubstitutorPerSection = new HashMap<>();
        while ((line = bufferedReader.readLine()) != null ) {

            final Matcher commentMatcher = COMMENT_LINE.matcher(line);
            if (commentMatcher.matches()) {
                continue;
            }

            final Matcher sectionMather = SECTION_PATTERN.matcher(line);
            if (sectionMather.matches()) {
                section.setValue(sectionMather.group(1).trim());
                continue;
            }

            line = line.replaceAll("[^\\\\]#.+", "")
                    .replaceAll("[^\\\\];.+", "")
                    .replaceAll("\\\\([;#])", "$1");

            if (StringUtils.isEmpty(line)) {
                continue;
            }

            final Matcher keyValueMatcher = KEY_VALUE_PATTER.matcher(line);
            if (keyValueMatcher.matches()) {
                key = keyValueMatcher.group(1).trim();
                multilineValue = new StringBuilder(covertToReadableValue(keyValueMatcher.group(2).trim()).replaceAll("\\\\$", "\n"));
                if (line.endsWith("\\")) {
                    continue;
                }
            } else if (line.endsWith("\\")) {
                multilineValue.append(line.replaceAll("\\\\$", "\n"));
                continue;
            } else if (multilineValue.isEmpty()){
                multilineValue.append(line.replaceAll("\\\\$", "\n"));
            }


            if (StringUtils.isNotEmpty(key)) {
                Object normalizedValue = normalizeValue(multilineValue.toString());

                if (normalizedValue instanceof String && line.contains("${")) {
                    StringSubstitutor substitutor = stringStringSubstitutorPerSection.computeIfAbsent(
                            section.getValue(), s -> {
                                StringSubstitutor stringSubstitutor = new StringSubstitutor(
                                        new IniController.DelegateMap(variables, getMapForSection(section)));
                                stringSubstitutor.setEnableSubstitutionInVariables(true);
                                return stringSubstitutor;
                            });

                    normalizedValue = substitutor.replace(normalizedValue.toString());
                }

                getMapForSection(section).put(key, normalizedValue);
            }
            key = null;
            multilineValue = new StringBuilder();
        }
        logger.debug("Finished parsing .ini file successfully!");
    }

    private Map<String, Object> getMapForSection(final MutableObject<String> section) {
        return resultMap.computeIfAbsent(
                section.getValue(), s1 -> new LinkedHashMap<>());
    }

    private Object normalizeValue(final String value) {
        if (NumberUtils.isCreatable(value)) {
            try {
                return NumberFormat.getInstance().parse(value);
            } catch (ParseException e) {
                return value;
            }
        }
        return value;
    }

    @Override
    public boolean hasKey(@NotNull String key) {
        boolean exist = false;
        for(String section : getSections()) {
            for(String keys : getKeys(section)) {
                if(keys.equals(key)) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    public boolean hasKey(final String selection, final String key) {
        return (resultMap.containsKey(selection) && resultMap.get(selection).containsKey(key));
    }

    @Override
    public <T> T getValue(@NotNull String key, Class<T> cast) {
        String firstKey = null;
        String section = null;
        for(String sections : getSections()) {
            for (String keys : getKeys(sections)) {
                if(keys.equals(key)) {
                    firstKey = keys;
                    section = sections;
                    break;
                }
            }
        }
        return getValue(section, firstKey, cast);
    }

    public Object getValue(final String section, final String key) {
        return getValue(section, key, Object.class);
    }

    public <T> T getValue(final String section, final String key, final Class<T> type) {
        return cast(resultMap.getOrDefault(section, new LinkedHashMap<>()).get(key), type);
    }

    public Collection<String> getSections() {
        return resultMap.keySet();
    }

    public Collection<String> getKeys(final String section) {
        return resultMap.getOrDefault(section, new LinkedHashMap<>()).keySet();
    }

    public Map<String, Object> getSection(final String section) {
        Map<String, Object> map = resultMap.get(section);
        if (map == null) {
            return null;
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Object> getSectionSortedByKey(final String section) {
        Map<String, Object> map = resultMap.get(section);
        if (map == null) {
            return null;
        }
        return Collections.unmodifiableMap(new TreeMap<>(map));
    }

    public Map<String, Object> getSectionWithKeysWithPrefix(final String section, final String prefix) {
        return getSectionWithKeysThatMatchFunction(section, entry -> entry.getKey().startsWith(prefix));
    }

    public Map<String, Object> getSectionWithKeysThatMatchFunction(final String section,
                                                                   final Predicate<Map.Entry<String, Object>> filter) {
        final Map<String, Object> stringObjectMap = firstNonNull(resultMap.get(section), new LinkedHashMap<>());
        return stringObjectMap.entrySet().stream()
                .filter(filter)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> getSectionWithKeysWithRegex(final String section, final String regex) {
        final Pattern pattern = Pattern.compile(regex);
        return getSectionWithKeysThatMatchFunction(section, entry -> pattern.matcher(entry.getKey()).matches());
    }

    public void putValue(final String section, final String key, final Object value) {
        resultMap.computeIfAbsent(section, s -> new LinkedHashMap<>()).put(key, value);
    }

    public void store(final OutputStream outputStream, final String comments) throws IOException {
        store(new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)),
                comments);
    }

    public void store(final Writer writer, final String comments) throws IOException {
        try (BufferedWriter bufferedWriter = (writer instanceof BufferedWriter)
                ? (BufferedWriter) writer : new BufferedWriter(writer)) {
            doStore(bufferedWriter, comments);
        }
    }

    private void doStore(final BufferedWriter bufferedWriter, final String comments) throws IOException {
        writeComments(bufferedWriter, comments);
        for (Map.Entry<String, Map<String, Object>> section : resultMap.entrySet()) {
            bufferedWriter.write("[" + section.getKey() + "]");
            bufferedWriter.newLine();
            for (Map.Entry<String, Object> sectionEntry : section.getValue().entrySet()) {
                bufferedWriter.write(sectionEntry.getKey() + " = ");
                bufferedWriter.write(sectionEntry.getValue().toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
        }
    }

    private static void writeComments(final BufferedWriter bw, final String comments)
            throws IOException {
        if (comments !=null ) {
            bw.write("#");
            IOUtils.write(comments, bw);
            bw.newLine();
        }
    }

    public Map<String, Object> removeSection(final String section) {
        return resultMap.remove(section);
    }

    public Object removeSectionKey(final String section, final String key) {
        return resultMap.getOrDefault(section, new LinkedHashMap<>()).remove(key);
    }

    public static final class DelegateMap extends AbstractMap<String, Object> {

        final Map<String, Object>[] sourceMaps;

        @SafeVarargs
        public DelegateMap(final Map<String, Object>... sourceMaps) {
            this.sourceMaps = sourceMaps;
        }

        @Override
        public @NotNull Set<Entry<String, Object>> entrySet() {
            Set<Entry<String, Object>> entrySet = new HashSet<>();
            for (Map<String, Object> map : sourceMaps) {
                entrySet.addAll(map.entrySet());
            }
            return entrySet;
        }
    }
}
