package ada.osc.taskie.database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;

/* An ORMLite special: Configures database manually, skipping annotation work.
 * This noticeably enhances performance. Creates a ormlite_config.txt in /res/raw.
 * The reason I created another /res/raw folder is because this class doesn't know
 * where the ACTUAL /res/raw is located, so I had to put one at the root of my project.
 * That way it saves the config file which I can copy to the ACTUAL /app/src/res/raw folder.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Task.class, Category.class};

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt", classes);
    }
}
