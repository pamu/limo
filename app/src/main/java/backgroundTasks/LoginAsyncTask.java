package backgroundTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by pnagarjuna on 06/05/15.
 */
public class LoginAsyncTask extends AsyncTask<String, String, String> {
    private ProgressDialog dialog;
    private Context mContext;

    public LoginAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog != null && dialog.isShowing()) dialog.cancel();
        
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mContext);
        dialog.setIndeterminate(true);
        dialog.setTitle("Busy");
        dialog.setMessage("logging in ...");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (dialog != null && dialog.isShowing()) dialog.cancel();
    }

    @Override
    protected String doInBackground(String... strings) {
        
        return null;
    }
}
