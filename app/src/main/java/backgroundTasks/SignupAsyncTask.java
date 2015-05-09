package backgroundTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import constants.Constants;

public class SignupAsyncTask extends AsyncTask<String, Integer, Integer> {
    private ProgressDialog dialog;
    private Context mContext;

    public SignupAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        try {
            JSONObject object = new JSONObject();
            object.put("email", strings[0]);
            object.put("password", strings[1]);
            String json = object.toString();

            URL url = new URL(Constants.SIGNUP_SERVICE_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(json);
            writer.flush();
            int code = httpURLConnection.getResponseCode();
            return code;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        } catch (JSONException e) {
            e.printStackTrace();
            return -3;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(mContext);
        dialog.setTitle("Busy");
        dialog.setMessage("Fetching information from cloud ...");
    }
}