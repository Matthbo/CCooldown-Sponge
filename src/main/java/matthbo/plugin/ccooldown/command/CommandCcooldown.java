package matthbo.plugin.ccooldown.command;

import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;

public class CommandCcooldown implements CommandCallable {

    @Override
    public Optional<CommandResult> process(CommandSource sender, String args) throws CommandException {
        if(sender.hasPermission(PermissionList.admin)){
            if(args.length > 0){
                if(args.length == 1 && args[0].equalsIgnoreCase("disable")){
                    if(!config.isOn){sender.sendMessage(pluginMSG + "ChatCooldown is already Disabled!"); return Optional.of(CommandResult.success());}
                    config.isOn = false;
                    config.set(config.isOn_LANG, false);
                    sender.sendMessage(pluginMSG + "ChatCooldown is Disabled!");
                    messageToAll(sender, ChatColor.GOLD +"SlowMode is off!");
                    return Optional.of(CommandResult.success());
                }
                else if(args.length == 1 && args[0].equalsIgnoreCase("enable")){
                    if(config.isOn){sender.sendMessage(pluginMSG + "ChatCooldown is already Enabled!"); return Optional.of(CommandResult.success());}
                    config.isOn = true;
                    config.set(config.isOn_LANG, true);
                    sender.sendMessage(pluginMSG + "ChatCooldown is Enabled!");
                    messageToAll(sender, ChatColor.GOLD +"SlowMode is on!");
                    return Optional.of(CommandResult.success());
                }
                else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                    config.reload();
                    sender.sendMessage(pluginMSG + "Reloaded!");
                    return Optional.of(CommandResult.success());
                }
                else if(args[0].equalsIgnoreCase("auto")){
                    if(args.length == 2 && args[1].equalsIgnoreCase("on")){
                        config.autoOn = true;
                        config.set(config.autoOn_LANG, true);
                        sender.sendMessage(pluginMSG + "AutoOn is On!");
                        return Optional.of(CommandResult.success());
                    }
                    if(args.length == 2 && args[1].equalsIgnoreCase("off")){
                        config.autoOn = false;
                        config.set(config.autoOn_LANG, false);
                        sender.sendMessage(pluginMSG + "AutoOn is Off!");
                        return Optional.of(CommandResult.success());
                    }
                    else{
                        if(config.autoOn) sender.sendMessage(pluginMSG + "AutoOn is On!");
                        if(!config.autoOn) sender.sendMessage(pluginMSG + "AutoOn is Off!");
                        return Optional.of(CommandResult.success());
                    }
                }
            }

            else{
                if(config.isOn){sender.sendMessage(pluginMSG + "ChatCooldown is Enabled!"); return Optional.of(CommandResult.success());}
                if(!config.isOn){sender.sendMessage(pluginMSG + "ChatCooldown is Disabled!"); return Optional.of(CommandResult.success());}
                else sender.sendMessage(pluginUsage + "something went wrong! ;(");
                return Optional.of(CommandResult.success());
            }
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    private final Object desc = "CCooldown Commands";
    private final Object usage = "/<command>";

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Texts.of(desc));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Texts.of(desc));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Texts.of(usage);
    }

    private class PermissionList{

        private static final String prefix = "ccooldown.";

        public static final String bypass = prefix + "bypass";
        public static final String admin = prefix + "admin";

    }
}
