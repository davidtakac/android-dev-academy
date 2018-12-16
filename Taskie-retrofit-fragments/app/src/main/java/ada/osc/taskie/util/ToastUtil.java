package ada.osc.taskie.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void shortToastWithString(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
