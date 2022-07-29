package md.dev.picocli.converter;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Files;

public class PackageFileToContent implements CommandLine.ITypeConverter<String> {
    @Override
    public String convert(String filePath) throws Exception {
        return Files.readString(new File(filePath).toPath());
    }
}
