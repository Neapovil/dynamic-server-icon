package com.github.neapovil.dynamicservericon;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

public final class DynamicServerIcon extends JavaPlugin implements Listener
{
    private static DynamicServerIcon instance;
    private CachedServerIcon cachedServerIcon;

    @Override
    public void onEnable()
    {
        instance = this;

        try
        {
            final BufferedImage bufferedimage = ImageIO.read(this.getResource("server-icon.png"));
            this.cachedServerIcon = this.getServer().loadServerIcon(bufferedimage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public static DynamicServerIcon instance()
    {
        return instance;
    }

    @EventHandler
    private void serverListPing(ServerListPingEvent event)
    {
        event.setServerIcon(this.cachedServerIcon);
    }
}
