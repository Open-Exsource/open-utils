package net.exsource.openutils.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum OperationSystem {

    UNKNOWN(""),
    WINDOWS("Windows,Win"),
    LINUX("Linux,linux,unix,ubuntu,debian,mint"),
    MAC("mac,mac os,osx");

    private final String aliases;

    OperationSystem(final String aliases) {
        this.aliases = aliases;
    }

    public String getAliases() {
        return aliases;
    }

    public List<String> getAliasesAsList() {
        List<String> list = new ArrayList<>();
        if(aliases.contains(",")) {
            String[] array = aliases.split(",");
            for(String tag : array) {
                list.add(tag.trim());
            }
        } else {
            list.add(getAliases());
        }
        return list;
    }

    public static OperationSystem get(@NotNull String name) {
        OperationSystem system = UNKNOWN;
        for(OperationSystem systems : values()) {
            if(systems.getAliasesAsList().size() > 1) {
                for(String alias : systems.getAliasesAsList()) {
                    if(name.contains(alias)) {
                        system = systems;
                        break;
                    }
                }
            } else {
                if(systems.getAliases().contains(name)) {
                    system = systems;
                    break;
                }
            }
        }

        return system;
    }

    public static OperationSystem getCurrent() {
        return get(System.getProperty("os.name"));
    }
}
