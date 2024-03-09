package com.github.neapovil.dynamicservericon.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class NewCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("dynamicservericon")
                .withPermission("dynamicservericon.command")
                .withArguments(new LiteralArgument("new"))
                .withArguments(new LiteralArgument("fromFile"))
                .withArguments(new StringArgument("fileName").replaceSuggestions(ArgumentSuggestions.stringsAsync(info -> {
                    return CompletableFuture.supplyAsync(() -> {
                        try (Stream<Path> stream = Files.list(plugin.getDataFolder().toPath()))
                        {
                            return stream
                                    .filter(file -> !Files.isDirectory(file))
                                    .map(Path::getFileName)
                                    .map(Path::toString)
                                    .filter(i -> !plugin.configResource.icons.contains(i))
                                    .filter(i -> FilenameUtils.getExtension(i).equals("png"))
                                    .toArray(String[]::new);
                        }
                        catch (IOException e)
                        {
                            return new String[] {};
                        }
                    });
                })))
                .executes((sender, args) -> {
                    final String filename = (String) args.get("fileName");

                    if (!FilenameUtils.getExtension(filename).equals("png"))
                    {
                        return;
                    }

                    if (plugin.configResource.icons.contains(filename))
                    {
                        return;
                    }

                    try
                    {
                        plugin.configResource.icons.add(filename);
                        plugin.save();
                        sender.sendMessage("New icon loaded: " + filename);
                    }
                    catch (IOException e)
                    {
                        plugin.getLogger().severe("Unable to save: " + e.getMessage());
                        sender.sendMessage("<red>Unable to add the new icon: " + filename);
                    }
                })
                .register();
    }
}
