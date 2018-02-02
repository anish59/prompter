package bbt.com.prompter.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import bbt.com.prompter.R;

/**
 * Created by anish on 02-02-2018.
 */

public class FunctionHelper {
    public static void setPermission(final Context context, @NonNull String[] permissions, PermissionListener permissionListener) {

        if (permissions != null && permissions.length == 0 && permissionListener != null) {
            return;
        }
        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(context.getString(R.string.if_you_deny) + "\n\n" + context.getString(R.string.please_turn_on_permissions))
                .setPermissions(permissions)
                .check();
    }

}
