package matthbo.plugin.ccooldown.config;

import com.google.inject.Inject;
import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.Refs;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.service.config.DefaultConfig;

import java.io.File;

public class Config {

    private CCooldown cc = CCooldown.instance;
    public File configFile = new File(cc.getDataFolder(), "config.cfg");

    public boolean isOn = true;
    public boolean autoOn = false;
    public int time = 20;
    public String warning = "You send the message to quickly!";
    public int messages = 10;

    private String cfgVersion = "VERSION NOT FOUND!";

    public String isOn_LANG = "isOn";
    public String autoOn_LANG = "autoOn";
    public String messages_LANG = "messages";
    public String time_LANG = "time";
    public String warning_LANG = "warning";

    public void initCfg(){
        configFile = new File(cc.getDataFolder(), "config.cfg");
        if(!configFile.exists()){
            cc.getLogger().info("Creating a new config file!");
            makeCfg();
        }
        loadCfg();
    }

    private void loadCfg(){
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
        CommentedConfigurationNode format = null;
        try{
            format = loader.load();
            cfgVersion = format.getNode("cfgVersion").getString();
            if(!cfgVersion.equalsIgnoreCase(Refs.VERSION)){
                try{
                    File dataFolder = cc.getDataFolder();
                    File oldCfgFile = new File(dataFolder, "config_old.cfg");
                    if(oldCfgFile.exists()) oldCfgFile.delete();
                    configFile.renameTo(oldCfgFile);
                    makeCfg();
                    cc.getLogger().warn("New Config File Created!");
                    cc.getLogger().warn("Old Configs Are Saved In 'config_old.yml'!");
                }catch(Exception e) {e.printStackTrace();}
            }
            isOn = format.getNode(isOn_LANG).getBoolean();
            time = format.getNode(time_LANG).getInt();
            warning = format.getNode(warning_LANG).getString();
            autoOn = format.getNode(autoOn_LANG).getBoolean();
            messages = format.getNode(messages_LANG).getInt();
        }catch(Exception e){
            cc.getLogger().error("Couldn't load the config file!");
        }
    }

    private void makeCfg(){
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
        CommentedConfigurationNode format = null;
        try{
            cc.getDataFolder().mkdir();
            configFile.createNewFile();
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode(isOn_LANG).setValue(true);
            format.getNode(time_LANG).setValue(20).setComment("time in seconds");
            format.getNode(warning_LANG).setValue("You send the message to quickly!");
            format.getNode(autoOn_LANG).setValue(false);
            format.getNode(messages_LANG).setValue(10).setComment("the amount of messages needed to turn auto on");
            format.getNode("cfgVersion").setValue(Refs.VERSION).setComment("DO NOT CHANGE!!!");
            loader.save(format);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set(String node, Object value){
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
        CommentedConfigurationNode format = null;
        try {
            loader.createEmptyNode(ConfigurationOptions.defaults());
            format = loader.load();
            format.getNode(node).setValue(value);
            loader.save(format);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reload(){
        initCfg();
    }

}
