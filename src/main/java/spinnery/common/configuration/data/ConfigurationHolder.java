package spinnery.common.configuration.data;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ConfigurationHolder<T> {
    T t;
    String namespace;
    String path;

    public ConfigurationHolder(T t) {
        setValue(t);
    }

    public T getValue() {
        return t;
    }

    public void setValue(T t) {
        this.t = t;
    }

    public Text getText() {
        return new TranslatableText("text." + namespace + ".configuration." + path);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
