package net.exsource.openutils.io.ini;

import net.exsource.openlogger.Logger;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Ini {

    private static final Logger logger = Logger.getLogger();

    private static final String NO_SECTION = "_NO_SECTION";
    private static final Pattern SECTION_PATTERN  = Pattern.compile( "\\s*\\[([^]]*)\\]\\s*" );
    private static final Pattern  KEY_VALUE_PATTER = Pattern.compile( "\\s*([^=]*)=(.*)" );
    private static final Pattern COMMENT_LINE = Pattern.compile("^[;|#].*");
    private final Map<String, Map<String, Object>> resultMap = new LinkedHashMap<>();

    public void load(final InputStream stream) throws IOException {
        if(stream == null)
            throw new FileNotFoundException("Ini stream is null");

        MutableObject<String> section = new MutableObject<>(NO_SECTION);
        try (InputStreamReader reader = new InputStreamReader(stream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            parseIni(section, bufferedReader);
        }
    }

    public void load(final File file) throws IOException {
        load(new FileInputStream(file));
    }

    public void load(final String string) throws  IOException {
        load(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }

    private void parseIni(final MutableObject<String> section, final BufferedReader reader) throws IOException {

    }

}
