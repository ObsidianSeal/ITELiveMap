package fyi.ite.ITELiveMap;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public final class ITELiveMap extends JavaPlugin {

    private static final Logger log = Bukkit.getLogger();
    public FirebaseDatabase database;
    public static DatabaseReference reference;

    @Override
    public void onEnable() {
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

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("mc/map");

        getServer().getPluginManager().registerEvents(new Update(), this);
        log.info("HELLO. ITELiveMap NOW ENABLED.");
    }

    @Override
    public void onDisable() {
        log.info("FAREWELL. ITELiveMap NOW DISABLED.");
    }

    public static void setMapMaterial(Location location, Block block) {
        reference.child(location.getBlockX() + "," + location.getBlockZ()).setValueAsync(block.getType());
    }

}
