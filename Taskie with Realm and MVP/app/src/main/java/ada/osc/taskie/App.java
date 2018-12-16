package ada.osc.taskie;

import android.app.Application;
import android.content.Context;

import ada.osc.taskie.database.category.CategoryDao;
import ada.osc.taskie.database.category.CategoryDaoImpl;
import ada.osc.taskie.database.task.TaskDao;
import ada.osc.taskie.database.task.TaskDaoImpl;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.util.Constants;
import ada.osc.taskie.util.SharedPrefsUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static TaskDao taskDao;
    private static CategoryDao categoryDao;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        initRealm();
        final Realm realm = Realm.getDefaultInstance();

        taskDao = new TaskDaoImpl(realm);
        categoryDao = new CategoryDaoImpl(realm);

        boolean previouslyStarted = SharedPrefsUtil.getFromPrefs(context, Constants.PREVIOUSLY_STARTED, false);
        if(!previouslyStarted){
            categoryDao.copyToRealm(new Category(Constants.NONE_CATEGORY_NAME));
            SharedPrefsUtil.saveToPrefs(context, Constants.PREVIOUSLY_STARTED, true);
        }
    }

    public static TaskDao getTaskDao(){
        return taskDao;
    }

    public static CategoryDao getCategoryDao(){
        return categoryDao;
    }

    public static Context getContext(){
        return context;
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Constants.REALM_APP_NAME)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
