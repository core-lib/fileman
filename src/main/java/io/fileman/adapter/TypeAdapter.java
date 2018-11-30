package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.ResolveContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TypeAdapter extends Adapter {

    @Override
    public String column() {
        return "Type";
    }

    @Override
    public String key() {
        return "type";
    }

    @Override
    public Object resolve(File file, ResolveContext context) throws IOException {
        Path path = Paths.get(file.toURI());
        String type = Files.probeContentType(path);
        return type != null ? type : "";
    }
}
