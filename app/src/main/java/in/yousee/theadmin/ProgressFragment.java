package in.yousee.theadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProgressFragment extends ProgressDialog {

    ContentLoadingProgressBar progressBar;

    public ProgressFragment(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progress);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        progressBar.show();


    }
}
