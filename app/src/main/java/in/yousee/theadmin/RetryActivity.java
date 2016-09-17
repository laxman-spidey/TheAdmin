package in.yousee.theadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import in.yousee.theadmin.util.LogUtil;

/**
 * Created by mittu on 17-09-2016.
 */
public class RetryActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_retry);
        String errorMsg = getIntent().getStringExtra("errorMsg");

        TextView errorMsgTextview = (TextView) findViewById(R.id.errorMsg);
        errorMsgTextview.setText(errorMsg);
        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        LogUtil.print("retry button clicked");
        setResult(RESULT_OK, new Intent("Retry"));
        finish();
    }
}
