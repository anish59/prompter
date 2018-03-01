package bbt.com.prompter.helper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bbt.com.prompter.R;

/**
 * Created by anish on 09-01-2018.
 */

public class UiHelper {
    public static void initToolbar(final AppCompatActivity activity, Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);

    }

    public static void showToast(String promptMsg, Activity activity, boolean isLengthLong) {
        int drawableInt = R.drawable.wtf;
        setCustomPrompt(promptMsg, activity, isLengthLong, drawableInt);
    }

    public static void showToastWithImage(String promptMsg, int drawableInt, Activity activity, boolean isLengthLong) {
        setCustomPrompt(promptMsg, activity, isLengthLong, drawableInt);
    }

    public static void setCustomPrompt(String promptMsg, Activity activity, boolean isLengthLong, int drawableInt) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null);

        ImageView image = (ImageView) layout.findViewById(R.id.imgPrompt);
        image.setImageResource(drawableInt);
        TextView text = (TextView) layout.findViewById(R.id.txtPrompt);
        text.setText(promptMsg);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if (isLengthLong) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setView(layout);
        toast.show();
    }
}
