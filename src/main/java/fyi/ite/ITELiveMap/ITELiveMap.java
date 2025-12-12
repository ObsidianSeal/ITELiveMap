package fyi.ite.ITELiveMap;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public class ITELiveMap extends JavaPlugin {

    public ConsoleCommandSender console;
    public FirebaseDatabase database;
    public static DatabaseReference reference;

    @Override
    public void onEnable() {
        // FIREBASE INITIALIZATION
        FirebaseOptions options;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("ite-fyi-firebase-adminsdk-fbsvc-de0a1244a0.json"))))
                    .setDatabaseUrl("https://ite-fyi-default-rtdb.firebaseio.com/")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseApp.initializeApp(options);

        // SET VARIABLES
        console = Bukkit.getConsoleSender();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("mc/map");

        // INITIALIZE PLUGIN
        getServer().getPluginManager().registerEvents(new Update(), this);
        console.sendMessage("ITELiveMap enabled!");
    }

    @Override
    public void onDisable() {
        console.sendMessage("ITELiveMap disabled!");
    }

    public static void sendMapRegion(String location, String blocks) {
        reference.child(location).setValueAsync(blocks);
    }

}
