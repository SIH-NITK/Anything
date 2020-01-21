package com.example.sih;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Constants {


    public static void showToast(Activity activity, String message, int toastLength) {
        View layout = activity.getLayoutInflater().inflate(R.layout.toast_custom, (ViewGroup) activity.findViewById(R.id.custom_toast_layout_id));
        ((TextView) layout.findViewById(R.id.text)).setText(message);
        Toast toast = new Toast(activity);
        toast.setDuration(toastLength);
        toast.setView(layout);
        toast.show();
    }

}
