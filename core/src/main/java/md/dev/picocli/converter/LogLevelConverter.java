package md.dev.picocli.converter;

import picocli.CommandLine;

import ch.qos.logback.classic.Level;

public class LogLevelConverter implements CommandLine.ITypeConverter<Level> {
    @Override
    public Level convert(String value) {
        return Level.valueOf(value);
    }
}
