package ada.osc.taskie.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ada.osc.taskie.ui.login.LoginActivity;
import ada.osc.taskie.ui.register.RegisterActivity;
import ada.osc.taskie.util.SharedPrefsUtil;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean previouslyStarted = Boolean
                .parseBoolean(SharedPrefsUtil.getPreferencesField(this, SharedPrefsUtil.PREVIOUSLY_STARTED, Boolean.FALSE.toString()));

        if(previouslyStarted){
            startLoginActivity();
        } else {
            SharedPrefsUtil.storePreferencesField(this, SharedPrefsUtil.PREVIOUSLY_STARTED, Boolean.TRUE.toString());
            startRegisterActivity();
        }
    }

    private void startRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
