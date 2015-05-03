package matthbo.plugin.ccooldown.config;

import com.google.inject.Inject;
import matthbo.plugin.ccooldown.CCooldown;
import matthbo.plugin.ccooldown.Refs;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.service.config.DefaultConfig;

import java.io.*;

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
    public String cfgVersion_LANG = "cfgVersion";

    public void initCfg(){
        configFile = new File(cc.getDataFolder(), "config.cfg");
        if(!configFile.exists()){
            cc.getLogger().info("Creating a new config file!");
            makeCfg();
        }else loadCfg();
    }

    private void loadCfg(){
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(configFile).build();
        CommentedConfigurationNode format = null;
        try{
            format = loader.load();
            cfgVersion = format.getNode(cfgVersion_LANG).getString();
            if(!cfgVersion.equalsIgnoreCase(Refs.VERSION) || cfgVersion == null){
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
            e.printStackTrace();
            cc.getLogger().error("Couldn't load the config file!");
        }
    }

    private void makeCfg(){
        try{
            if(!cc.getDataFolder().exists())cc.getDataFolder().mkdir();
            configFile.createNewFile();
            OutputStream output = new FileOutputStream(configFile);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            writer.write("isOn="+isOn+"\n");
            writer.write("autoOn="+autoOn+"\n");
            writer.write("# time in seconds\n");
            writer.write("time="+time+"\n");
            writer.write("warning=\"You send the message to quickly!\"\n"); //edit this if warning is different
            writer.write("# the amount of messages needed to turn autoOn on\n");
            writer.write("messages=" + messages + "\n");
            writer.write("\n# DO NOT EDIT!\n");
            writer.write("cfgVersion="+Refs.VERSION+"\n");

            writer.flush();
            output.close();
        }catch (Exception e) {
            e.printStackTrace();
            cc.getLogger().error("Couldn't create a new config file!");
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
