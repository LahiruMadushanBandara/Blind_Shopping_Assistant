

package com.beacon.shopping.assistant.action;

import android.content.Context;
import android.net.Uri;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.NotificationAction;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lahiru on 04/12/2017.
 */
public class UrlAction extends NoneAction {

    public UrlAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        try {
            Uri.parse(param);

            if (param.startsWith("http://")) {
                new CallUrl<>(param).start();
            }

            if (param.startsWith("https://")) {
                new CallUrl<HttpsURLConnection>(param).start();
            }
        } catch (Exception e) {
            return context.getString(R.string.action_urlaction_error);
        }
        return super.execute(context);
    }

    private class CallUrl<T extends HttpURLConnection> extends Thread {

        final String url;

        public CallUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                T urlConnection = (T) new URL(url).openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                // nothing to do.
            }
        }
    }

    private void readStream(InputStream in) throws IOException  {
        // nothing to do.
        in.close();
    }

    @Override
    public boolean isParamRequired() {
        return true;
    }

    @Override
    public String toString() {
        return "UrlAction, url: " + param;
    }
}
