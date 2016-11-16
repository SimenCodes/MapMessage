package codes.simen.mapmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {
    public static final String TAG = "MessageReceiver";

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            final Bundle extras = intent.getExtras();
            Log.d(TAG, "onReceive: " + extras);
            Object[] pdus = (Object[]) extras.get("pdus");
            String format = extras.getString("format", "3gpp");

            String from = "";
            String fullMessage = "";
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage message;
                    if (Build.VERSION.SDK_INT >= 23)
                        message = SmsMessage.createFromPdu((byte[]) pdu, format);
                    else
                        message = SmsMessage.createFromPdu((byte[]) pdu);
                    String fromAddress = message.getOriginatingAddress();
                    String messageBody = message.getMessageBody();
                    fullMessage = fullMessage + messageBody;
                    from = fromAddress;
                }
            }

            fullMessage = fullMessage.trim();

            context.startActivity(new Intent(context, MapActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("from", from)
                    .putExtra("message", fullMessage)
            );

        }
    }
}
