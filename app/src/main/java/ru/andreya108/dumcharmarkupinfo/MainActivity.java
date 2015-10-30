package ru.andreya108.dumcharmarkupinfo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    DialogFragment aboutDialog;
    DialogFragment errorDialog;
    DumcharUtil dumcharUtil;
    TextView detectedMarkup;
    TextView partitionSizes;
    TextView dumcharDump;
    ListView list;

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

        long emmcSize = dumcharUtil.getEmmcSizeGb();
        MmcUtil mmcu = new MmcUtil();

        if (mmcu.getChip() != null) {
            emmcSize = mmcu.getChip().sizeGb;

            if (emmcSize != dumcharUtil.getEmmcSizeGb()) {
                String text = getString(R.string.wrong_markup);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(this, text, duration);
                toast.show();
            }
        }

        detectedMarkup = (TextView) findViewById(R.id.detectedMarkup);
        detectedMarkup.setText(dumcharUtil.getMarkup() + " " + emmcSize + "Gb");

//        partitionSizes = (TextView) findViewById(R.id.partitionSizes);
//        partitionSizes.setText(dumcharUtil.getPartSizes());

        PartitionSizesAdapter adapter=new PartitionSizesAdapter(this, dumcharUtil.getPartSizes());
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        dumcharDump = (TextView) findViewById(R.id.dumcharDump);
        dumcharDump.setText(dumcharUtil.getDumcharInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();

//        partitionSizes.setTypeface(Typeface.MONOSPACE);
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
        if (id == R.id.action_mmc_info) {

            Intent intent = new Intent(this, MmcInfoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_about) {

            aboutDialog.show(getFragmentManager(), "aboutDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
