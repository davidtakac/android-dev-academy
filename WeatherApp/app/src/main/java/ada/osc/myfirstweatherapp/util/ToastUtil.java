package ada.osc.myfirstweatherapp.util;

import android.content.Context;
import android.widget.Toast;

import ada.osc.myfirstweatherapp.R;

public class ToastUtil {
    public static void showToastWithString(Context context, String toShow){
        Toast.makeText(context, toShow, Toast.LENGTH_SHORT).show();
    }
}
