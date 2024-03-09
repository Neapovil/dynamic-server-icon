package com.github.neapovil.dynamicservericon.command;

import java.io.IOException;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class RemoveCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("dynamicservericon")
                .withPermission("dynamicservericon.command")
                .withArguments(new LiteralArgument("remove"))
                .withArguments(new StringArgument("icon").replaceSuggestions(ArgumentSuggestions.strings(plugin.configResource.icons)))
                .executes((sender, args) -> {
                    final String icon = (String) args.get("icon");

                    if (!plugin.configResource.icons.contains(icon))
                    {
                        return;
                    }

                    try
                    {
                        plugin.configResource.icons.removeIf(icon::equalsIgnoreCase);

                        if (icon.equalsIgnoreCase(plugin.configResource.current))
                        {
                            plugin.configResource.loadDefaultIcon();
                        }

                        plugin.save();

                        sender.sendMessage("Icon removed: " + icon);
                    }
                    catch (IOException e)
                    {
                        plugin.getLogger().severe("Unable to remove icon: " + e.getMessage());
                        sender.sendRichMessage("<red>Unable to remove icon: " + icon);
                    }
                })
                .register();
    }
}
