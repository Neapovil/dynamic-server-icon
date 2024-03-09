package com.github.neapovil.dynamicservericon;

import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import com.github.neapovil.dynamicservericon.command.LoadCommand;
import com.github.neapovil.dynamicservericon.command.NewCommand;
import com.github.neapovil.dynamicservericon.command.RemoveCommand;
import com.github.neapovil.dynamicservericon.resource.ConfigResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class DynamicServerIcon extends JavaPlugin implements Listener
{
    private static DynamicServerIcon instance;
    public CachedServerIcon cachedServerIcon;
    public ConfigResource configResource;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable()
    {
        instance = this;

        this.saveResource("config.json", false);

        try
        {
            this.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.configResource.loadCurrent();

        this.getServer().getPluginManager().registerEvents(this, this);

        new LoadCommand().register();
        new NewCommand().register();
        new RemoveCommand().register();
    }

    public static DynamicServerIcon instance()
    {
        return instance;
    }

    public void load() throws IOException
    {
        final String string = Files.readString(this.getDataFolder().toPath().resolve("config.json"));
        this.configResource = this.gson.fromJson(string, ConfigResource.class);
    }

    public void save() throws IOException
    {
        final String string = this.gson.toJson(this.configResource);
        Files.write(this.getDataFolder().toPath().resolve("config.json"), string.getBytes());
    }

    @EventHandler
    private void serverListPing(ServerListPingEvent event)
    {
        event.setServerIcon(this.cachedServerIcon);
    }
}
