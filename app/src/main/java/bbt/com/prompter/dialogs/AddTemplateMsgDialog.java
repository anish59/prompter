package bbt.com.prompter.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import bbt.com.prompter.R;

/**
 * Created by anish on 12-02-2018.
 */

public class AddTemplateMsgDialog extends Dialog {
    private Context context;
    private android.widget.EditText txtTemplateMsg;
    private EditText txtSelectTime;
    private Button btnCancel;
    private Button btnDone;
    private android.widget.CheckBox chkDailyNotification;

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
                chooseDateAndTime();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        chkDailyNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }

    private void chooseDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            String seleteDateTime = "";

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                seleteDateTime = dayOfMonth + "-" + month + "-" + year;
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        seleteDateTime = seleteDateTime + " : " + hourOfDay + "" + minute;
                        txtSelectTime.setText(seleteDateTime);

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_msg, null, false);
        this.chkDailyNotification = (CheckBox) view.findViewById(R.id.chkDailyNotification);
        this.btnDone = (Button) view.findViewById(R.id.btnDone);
        this.btnCancel = (Button) view.findViewById(R.id.btnCancel);
        this.txtSelectTime = (EditText) view.findViewById(R.id.txtSelectTime);
        this.txtTemplateMsg = (EditText) view.findViewById(R.id.txtTemplateMsg);
        txtSelectTime.setFocusable(false);
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
