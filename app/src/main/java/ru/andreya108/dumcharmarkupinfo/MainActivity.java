package ru.andreya108.dumcharmarkupinfo;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

    DialogFragment aboutDialog;
    DialogFragment errorDialog;
    DumcharUtil dumcharUtil;
    TextView detectedMarkup;
    TextView partitionSizes;
    TextView dumcharDump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aboutDialog = new AboutDialog();
        errorDialog = new ErrorDialog();

        dumcharUtil = new DumcharUtil(this);
        if ( dumcharUtil.readDumchar() == false)
        {
            errorDialog.show(getFragmentManager(), "errorDialog");
            finish();
            return;
        }

        detectedMarkup = (TextView) findViewById(R.id.detectedMarkup);
        detectedMarkup.setText(dumcharUtil.getMarkup() + " " + dumcharUtil.getEmmcSizeGb() + "Gb");

        partitionSizes = (TextView) findViewById(R.id.partitionSizes);
        partitionSizes.setText(dumcharUtil.getPartSizes());

        dumcharDump = (TextView) findViewById(R.id.dumcharDump);
        dumcharDump.setText(dumcharUtil.getDumcharInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();

        partitionSizes.setTypeface(Typeface.MONOSPACE);
        dumcharDump.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            aboutDialog.show(getFragmentManager(), "aboutDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
