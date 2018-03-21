package bbt.com.prompter.dialogs;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import bbt.com.prompter.R;
import bbt.com.prompter.helper.AppConstants;
import bbt.com.prompter.helper.DeliverReceiver;
import bbt.com.prompter.helper.FunctionHelper;
import bbt.com.prompter.helper.SentReceiver;

/**
 * Created by anish on 13-03-2018.
 */

public class SendMessageDialog extends Dialog {
    private Context context;
    private android.widget.TextView btnOther;
    private android.widget.TextView btnSim1;
    private android.widget.TextView btnSim2;
    private String contactName;
    private String msg;
    private String contactNo;
    private TextView txtName;
    private TextView txtMsg;
    private android.widget.ImageView imgClose;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public SendMessageDialog(@NonNull Context context, String name, String msg, String number) {
        super(context);
        this.context = context;
        this.contactName = name;
        this.msg = msg;

        this.contactNo = FunctionHelper.filterNumber(number);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sms_layout, null, false);
        this.imgClose = (ImageView) view.findViewById(R.id.imgClose);
        this.txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        this.txtName = (TextView) view.findViewById(R.id.txtName);
        this.btnSim2 = (TextView) view.findViewById(R.id.btnSim2);
        this.btnSim1 = (TextView) view.findViewById(R.id.btnSim1);
        this.btnOther = (TextView) view.findViewById(R.id.btnOther);
        setDialogProps(view);
        show();

        txtName.setText(contactName);
        txtMsg.setText(msg);

        setSimDetails();

        initListener();
    }

    private void initListener() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int subSimOneId = (int) btnSim1.getTag();
                sendMessage(subSimOneId);
            }
        });

        btnSim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int subSimTwoId = (int) btnSim2.getTag();
                sendMessage(subSimTwoId);
            }
        });
    }

    public void sendMessage(int subSimId) {
        Log.e("ContactNo", contactNo);
        try {


            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, new Random().nextInt()
                    , new Intent(SENT), PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, new Random().nextInt()
                    , new Intent(DELIVERED), PendingIntent.FLAG_UPDATE_CURRENT);

            context.registerReceiver(new SentReceiver(), new IntentFilter(SENT));

            context.registerReceiver(new DeliverReceiver(), new IntentFilter(DELIVERED));

            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subSimId);
            smsManager.sendTextMessage(contactNo, null, msg, sentPI, deliveredPI);
            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void setSimDetails() {
        String simOneName = "", simTwoName = "";


        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        if (subscriptionInfoList != null && subscriptionInfoList.size() > 0) {
            for (int i = 0; i < subscriptionInfoList.size(); i++) {
                if (simOneName.equals("")) {
                    simOneName = subscriptionInfoList.get(i).getCarrierName().toString();
                    btnSim1.setVisibility(View.VISIBLE);
                    btnSim1.setText(simOneName);
                    btnSim1.setTag(subscriptionInfoList.get(i).getSubscriptionId());
                } else {
                    simTwoName = subscriptionInfoList.get(i).getCarrierName().toString();
                    btnSim2.setVisibility(View.VISIBLE);
                    btnSim2.setText(simTwoName);
                    btnSim2.setTag(subscriptionInfoList.get(i).getSubscriptionId());
                    break;// so two names are already acquired now we need to break the loop and proceed
                }
            }
            if (btnSim1.getVisibility() == View.GONE && btnSim2.getVisibility() == View.GONE) {
                btnOther.setText(R.string.send);
            }

        }
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
