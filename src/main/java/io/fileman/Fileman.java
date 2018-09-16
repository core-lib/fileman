package io.fileman;

import java.util.List;
import java.util.Map;

/**
 * 文件
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class Fileman {
    private String uri;
    private String path;
    private boolean folder;
    private Map<String, Object> properties;
    private List<Fileman> children;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<Fileman> getChildren() {
        return children;
    }

    public void setChildren(List<Fileman> children) {
        this.children = children;
    }
}
