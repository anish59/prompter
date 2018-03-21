package bbt.com.prompter.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import bbt.com.prompter.R;
import bbt.com.prompter.helper.AppAlarmHelper;
import bbt.com.prompter.helper.DateHelper;
import bbt.com.prompter.helper.UiHelper;
import bbt.com.prompter.model.ContactTable;

/**
 * Created by anish on 12-02-2018.
 */

public class AddTemplateMsgDialog extends Dialog {
    private Context context;
    private android.widget.EditText edtTemplateMsg;
    private EditText edtSelectTime;
    private Button btnCancel;
    private Button btnDone;
    private android.widget.CheckBox chkDailyNotification;
    private Activity activity;
    private String name, phoneNo;
    private String imgUri;
    private boolean isEditMode = false;
    private long createdTime;
    private long contactId;
    private android.widget.TextView txtReminderFor;
    private DataUpdatedListener dataUpdatedListener;

    public AddTemplateMsgDialog(Activity activity, String name, String phoneNo, String imgUri) {
        super(activity);
        this.context = activity;
        this.activity = activity;
        this.name = name;
        this.phoneNo = phoneNo;
        this.imgUri = imgUri;
        init(name);
        initListener();
        show();
        isEditMode = false;

    }

    public AddTemplateMsgDialog(Activity activity, String phoneNo, DataUpdatedListener dataUpdatedListener) {
        super(activity);
        this.activity = activity;
        this.context = activity;
        this.phoneNo = phoneNo;
        this.dataUpdatedListener = dataUpdatedListener;
        isEditMode = true;
        init("");
        initListener();
        setData(phoneNo);
        show();
    }

    private void setData(String phoneNo) {
        ContactTable contactTable;
        contactTable = ContactTable.getContactWithNumber(phoneNo, context);
        if (contactTable == null) {
            return;
        }

        txtReminderFor.setText(String.format("Get reminder for %s !", contactTable.getName()));
        edtSelectTime.setText(contactTable.getNotifyTime());
        edtTemplateMsg.setText(contactTable.getTemplate());
        createdTime = contactTable.getCreatedDateInt();
        contactId = contactTable.getContactId();
        chkDailyNotification.setChecked(contactTable.getIsDaily() == 1);

        imgUri = contactTable.getImgUri();
        name = contactTable.getName();
        this.contactId = contactTable.getContactId();

    }

    private void initListener() {


        edtSelectTime.setOnClickListener(new View.OnClickListener() {
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

                if (TextUtils.isEmpty(edtSelectTime.getText().toString())) {
                    UiHelper.showToast("Please enter Date Time!", activity, true);
                    return;
                }
                if (TextUtils.isEmpty(edtTemplateMsg.getText().toString())) {
                    UiHelper.showToast("please write some template msg", activity, true);
                    return;
                }

                insertOrUpdateRecord();
                dismiss();
            }
        });

        chkDailyNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }


    private void insertOrUpdateRecord() {
        ContactTable contactTable = new ContactTable();
        contactTable.setIsDaily(chkDailyNotification.isChecked() ? 1 : 0);
        contactTable.setNotifyTime(edtSelectTime.getText().toString().trim());
        contactTable.setTemplate(edtTemplateMsg.getText().toString().trim());

        contactTable.setNumber(phoneNo);
        contactTable.setName(name);
        contactTable.setImgUri(imgUri);
        Calendar calendar = DateHelper.stringToCalendar(contactTable.getNotifyTime(), DateHelper.dd_MMM_yyyy_hh_mm_a);

        if (!isEditMode) {
            createdTime = Long.parseLong(DateHelper.formatDate(new Date(), DateHelper.YYYY_MMDD_HHMMSS));
            contactTable.setCreatedDateInt(createdTime);
            contactTable.setUpdatedDateInt(createdTime); // both will be created time as it is a new fresh entry

            ContactTable.addContact(contactTable, context);

            // first query wrt to phone to get inserted contactId inOrder to set alarm
            contactId = ContactTable.getContactWithNumber(phoneNo, context).getContactId();
            setNotification(calendar.getTimeInMillis(), (int) contactId);

        } else {
            long updatedTime = Long.parseLong(DateHelper.formatDate(new Date(), DateHelper.YYYY_MMDD_HHMMSS));
            contactTable.setCreatedDateInt(createdTime);
            contactTable.setUpdatedDateInt(updatedTime); // updatedTime will change everyTime we update Data and CreatedTime will stay same

            contactTable.setContactId(contactId);
            ContactTable.updateContact(contactTable, context);
            dataUpdatedListener.onUpdated();

            setNotification(calendar.getTimeInMillis(), (int) contactId);
        }

    }

    private void setNotification(long timeInMillis, int contactId) {
        AppAlarmHelper appAlarmHelper = new AppAlarmHelper();//setting Alarm
        if (chkDailyNotification.isChecked()) {
            appAlarmHelper.setAlarm(context
                    , contactId
                    , true, timeInMillis);

        } else {
            appAlarmHelper.setAlarm(context, contactId, false, timeInMillis);
        }
    }

    private void chooseDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            String selectDateTime = "";

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                selectDateTime = dayOfMonth + "-" + month + "-" + year;
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectDateTime = selectDateTime + "-" + hourOfDay + "-" + minute;
                        Log.e("DTFormat", "" + selectDateTime);
                        String dateTime = DateHelper.formatDate(selectDateTime, DateHelper.dd_MM_yyyy_HH_mm, DateHelper.dd_MMM_yyyy_hh_mm_a);
                        edtSelectTime.setText(dateTime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void init(String name) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_msg, null, false);
        this.txtReminderFor = (TextView) view.findViewById(R.id.txtReminderFor);
        this.chkDailyNotification = (CheckBox) view.findViewById(R.id.chkDailyNotification);
        this.btnDone = (Button) view.findViewById(R.id.btnDone);
        this.btnCancel = (Button) view.findViewById(R.id.btnCancel);
        this.edtSelectTime = (EditText) view.findViewById(R.id.edtSelectTime);
        this.edtTemplateMsg = (EditText) view.findViewById(R.id.edtTemplateMsg);
        edtSelectTime.setFocusable(false);
        setDialogProps(view);

        txtReminderFor.setText(String.format("Get reminder for %s !", name));
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

    public interface DataUpdatedListener {
        void onUpdated();
    }


}
