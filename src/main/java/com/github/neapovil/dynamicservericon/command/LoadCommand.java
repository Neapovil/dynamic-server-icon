package com.github.neapovil.dynamicservericon.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class LoadCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("dynamicservericon")
                .withPermission("dynamicservericon.command")
                .withArguments(new LiteralArgument("load"))
                .withArguments(new StringArgument("icon").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    return plugin.configResource.icons.toArray(String[]::new);
                })))
                .executes((sender, args) -> {
                    final String icon = (String) args.get("icon");

                    if (!plugin.configResource.icons.contains(icon))
                    {
                        return;
                    }

                    try
                    {
                        plugin.configResource.load(icon);
                        plugin.save();
                        sender.sendMessage("Server icon changed to: " + icon);
                    }
                    catch (Exception e)
                    {
                        plugin.getLogger().severe("Unable to load server icon: " + e.getMessage());
                        sender.sendRichMessage("<red>Unable to update server icon to: " + icon);
                    }
                })
                .register();
    }
}
