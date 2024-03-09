package com.github.neapovil.dynamicservericon.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.neapovil.dynamicservericon.DynamicServerIcon;

public final class ConfigResource
{
    public final List<String> icons = new ArrayList<>();
    public String current;

    public void loadDefaultIcon()
    {
        final DynamicServerIcon plugin = DynamicServerIcon.instance();
        try
        {
            final BufferedImage bufferedimage = ImageIO.read(plugin.getResource("server-icon.png"));
            plugin.cachedServerIcon = plugin.getServer().loadServerIcon(bufferedimage);
            this.current = "server-icon.png";
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadCurrent()
    {
        if (this.current.equals("server-icon.png"))
        {
            this.loadDefaultIcon();
        }
        else
        {
            final DynamicServerIcon plugin = DynamicServerIcon.instance();
            try
            {
                final BufferedImage bufferedimage = ImageIO.read(new File(plugin.getDataFolder(), this.current));
                plugin.cachedServerIcon = plugin.getServer().loadServerIcon(bufferedimage);
            }
            catch (Exception e)
            {
                plugin.getLogger().severe("Unable to load current server icon. Falling back to default icon.");
                this.loadDefaultIcon();
            }
        }
    }

    public void load(String icon) throws Exception
    {
        final DynamicServerIcon plugin = DynamicServerIcon.instance();
        final BufferedImage bufferedimage = ImageIO.read(new File(plugin.getDataFolder(), icon));
        plugin.cachedServerIcon = plugin.getServer().loadServerIcon(bufferedimage);
        this.current = icon;
    }
}
