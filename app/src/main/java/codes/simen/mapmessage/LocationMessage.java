package codes.simen.mapmessage;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by simen on 16.11.16.
 */
public class LocationMessage {
    private static final String TAG = "LocationMessage";

    public final String message;
    public final LatLng location;
    public final int radius;
    public final int color;
    public final String from;

    public LocationMessage(String message, LatLng location, int radius, int color, String from) {
        this.message = message;
        this.location = location;
        this.radius = radius;
        this.color = color;
        this.from = from;
    }

    public LocationMessage(String from, String message) {
        Log.d(TAG, "LocationMessage() called with: from = [" + from + "], message = [" + message + "]");
        final Pattern pattern = Pattern.compile("-?\\d+\\.\\d+,-?\\d+\\.\\d+,\\d+,\\d+");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String match = matcher.group();
            Log.d(TAG, "LocationMessage: " + match);
            final String[] split = match.split(",", 5);
            this.location = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            this.radius = Integer.parseInt(split[2]);
            this.color = Integer.parseInt(split[3]);
            this.message = message;
            this.from = from;
        } else {
            Log.e(TAG, "LocationMessage: Unknown");
            throw new IllegalArgumentException("Went wrong");
        }
    }

    public boolean isArea() {
        return radius > 0;
    }
}
