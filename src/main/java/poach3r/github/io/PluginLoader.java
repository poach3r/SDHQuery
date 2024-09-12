package poach3r.github.io;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import poach3r.github.io.Commands.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {
    private final List<Plugin> plugins;
    private final List<String> enabledPlugins;

    public PluginLoader(List<String> enabledPlugins) {
        plugins = new ArrayList<>();
        this.enabledPlugins = enabledPlugins;
    }

    public void load() throws IOException {
        GroovyShell shell = new GroovyShell();
        try {
            for (String enabledPlugin : enabledPlugins) {
                plugins.add(new Plugin(shell.parse(new File(enabledPlugin))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}

