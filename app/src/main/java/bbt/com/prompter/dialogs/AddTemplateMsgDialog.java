package bbt.com.prompter.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import bbt.com.prompter.R;

/**
 * Created by anish on 12-02-2018.
 */

public class AddTemplateMsgDialog extends Dialog {
    private Context context;
    private android.widget.EditText txtTemplateMsg;
    private android.widget.Button btnNotifyTime;
    private EditText txtSelectTime;
    private Button btnCancel;
    private Button btnDone;

    public AddTemplateMsgDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
        initListener();
        show();
    }

    private void initListener() {
        txtSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_msg, null, false);
        this.btnDone = (Button) view.findViewById(R.id.btnDone);
        this.btnCancel = (Button) view.findViewById(R.id.btnCancel);
        this.txtSelectTime = (EditText) view.findViewById(R.id.txtSelectTime);
        this.txtTemplateMsg = (EditText) view.findViewById(R.id.txtTemplateMsg);
        setDialogProps(view);
    }


    private void setDialogProps(View view) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        this.setCanceledOnTouchOutside(false);
    }


}
