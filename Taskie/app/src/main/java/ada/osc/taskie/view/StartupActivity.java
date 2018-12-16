package ada.osc.taskie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ada.osc.taskie.util.SharedPrefsUtil;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean previouslyStarted = SharedPrefsUtil.getFromPrefs(this, SharedPrefsUtil.PREVIOUSLY_STARTED, false);
        if (previouslyStarted) {
            startLoginActivity();
        } else {
            SharedPrefsUtil.saveToPrefs(this, SharedPrefsUtil.PREVIOUSLY_STARTED, true);
            startRegisterActivity();
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
