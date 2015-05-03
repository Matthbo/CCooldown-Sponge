package matthbo.plugin.ccooldown.command;

import com.google.common.base.Optional;
import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.config.Config;
import matthbo.plugin.ccooldown.Refs;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public class CommandCcooldown implements CommandCallable {

    private CCooldown cc = CCooldown.instance;
    private Config config = cc.getConfig();

    private Text sendToAll(String msg){
        return Texts.builder(Refs.pluginMSG).color(TextColors.DARK_PURPLE)
                .append(Texts.builder(msg).color(TextColors.GOLD).build()).build();
    }

    private void messageToAll(String msg) {
        Object[] allPlayers = cc.getGame().getServer().getOnlinePlayers().toArray();

        for (int j = 0; j < allPlayers.length; j++) {
            Player target = (Player)allPlayers[j];
            target.sendMessage(sendToAll(msg));
            cc.getLogger().info(msg);
        }
    }

    private Text sendMSG(String msg){
        return Texts.builder(Refs.pluginMSG).color(TextColors.DARK_PURPLE)
                .append(Texts.builder(msg).color(TextColors.RESET).build()).build();
    }

    @Override
    public Optional<CommandResult> process(CommandSource sender, String argsuments) throws CommandException {
        if(sender.hasPermission(PermissionList.admin)){
            String[] args = argsuments.split(" ");
            if(args.length > 0 && !args[0].equalsIgnoreCase("")){
                if(args.length == 1 && args[0].equalsIgnoreCase("disable")){
                    if(!config.isOn){sender.sendMessage(sendMSG("ChatCooldown is already Disabled!")); return Optional.of(CommandResult.success());}
                    config.isOn = false;
                    config.set(config.isOn_LANG, false);
                    sender.sendMessage(sendMSG("ChatCooldown is Disabled!"));
                    messageToAll("SlowMode is off!");
                    return Optional.of(CommandResult.success());
                }
                else if(args.length == 1 && args[0].equalsIgnoreCase("enable")){
                    if(config.isOn){sender.sendMessage(sendMSG("ChatCooldown is already Enabled!")); return Optional.of(CommandResult.success());}
                    config.isOn = true;
                    config.set(config.isOn_LANG, true);
                    sender.sendMessage(sendMSG("ChatCooldown is Enabled!"));
                    messageToAll("SlowMode is on!");
                    return Optional.of(CommandResult.success());
                }
                else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                    config.reload();
                    sender.sendMessage(sendMSG("Reloaded!"));
                    return Optional.of(CommandResult.success());
                }
                else if(args[0].equalsIgnoreCase("auto")){
                    if(args.length == 2 && args[1].equalsIgnoreCase("on")){
                        config.autoOn = true;
                        config.set(config.autoOn_LANG, true);
                        sender.sendMessage(sendMSG("AutoOn is On!"));
                        return Optional.of(CommandResult.success());
                    }
                    if(args.length == 2 && args[1].equalsIgnoreCase("off")){
                        config.autoOn = false;
                        config.set(config.autoOn_LANG, false);
                        sender.sendMessage(sendMSG("AutoOn is Off!"));
                        return Optional.of(CommandResult.success());
                    }
                    else{
                        if(config.autoOn) sender.sendMessage(sendMSG("AutoOn is On!"));
                        if(!config.autoOn) sender.sendMessage(sendMSG("AutoOn is Off!"));
                        return Optional.of(CommandResult.success());
                    }
                }
            }

            else{
                if(config.isOn){sender.sendMessage(sendMSG("ChatCooldown is Enabled!")); return Optional.of(CommandResult.success());}
                if(!config.isOn){sender.sendMessage(sendMSG("ChatCooldown is Disabled!")); return Optional.of(CommandResult.success());}
                else sender.sendMessage(sendMSG("something went wrong! ;("));
                return Optional.of(CommandResult.success());
            }
        }
        return Optional.of(CommandResult.success());
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        if(source.hasPermission(PermissionList.admin)) {
            List<String> suggest = new ArrayList<>();
            suggest.add("enable");
            suggest.add("disable");
            suggest.add("auto on");
            suggest.add("auto off");
            return suggest;
        }
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

    public class PermissionList{

        private static final String prefix = "ccooldown.";

        public static final String bypass = prefix + "bypass";
        public static final String admin = prefix + "admin";

    }
}
