package io.fileman.adapter;

import io.fileman.Action;
import io.fileman.Adapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TypeAdapter extends Adapter {

    @Override
    public String name() {
        return "Type";
    }

    @Override
    public String key() {
        return "type";
    }

    @Override
    public Object resolve(Action action) throws IOException {
        Path path = Paths.get(action.getFile().toURI());
        String type = Files.probeContentType(path);
        return type != null ? type : "";
    }
}
