package ru.andreya108.dumcharmarkupinfo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Andrey on 27.10.2015.
 */
public class MmcInfoActivity extends Activity {

    TextView mmcCid;
    TextView vendor;
    TextView partNumber;
    TextView mmcGb;
    TextView mmcDate;
    TextView mmcName;
    TextView mmcSerial;

    MmcUtil mmcu = new MmcUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmc_info);

        mmcCid = (TextView) findViewById(R.id.mmcCid);
        mmcCid.setText(mmcu.getCid());

        mmcDate = (TextView) findViewById(R.id.mmcDate);
        mmcDate.setText(MmcUtil.readMmcFile(MmcUtil.MMC_DATE));

        mmcName = (TextView) findViewById(R.id.mmcName);
        mmcName.setText(MmcUtil.readMmcFile(MmcUtil.MMC_NAME));

        mmcSerial = (TextView) findViewById(R.id.mmcSerial);
        mmcSerial.setText(MmcUtil.readMmcFile(MmcUtil.MMC_SERIAL));

        MmcUtil.MmcChip chip = mmcu.getChip();
        if (chip != null) {
            vendor = (TextView) findViewById(R.id.vendor);
            vendor.setText(chip.vendor);

            partNumber = (TextView) findViewById(R.id.partNumber);
            partNumber.setText(chip.part);

            mmcGb = (TextView) findViewById(R.id.mmcGb);
            mmcGb.setText("" + chip.sizeGb + " Gb");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mmcCid.setTypeface(Typeface.MONOSPACE);
        mmcDate.setTypeface(Typeface.MONOSPACE);
        mmcName.setTypeface(Typeface.MONOSPACE);
        mmcSerial.setTypeface(Typeface.MONOSPACE);
        vendor.setTypeface(Typeface.MONOSPACE);
        partNumber.setTypeface(Typeface.MONOSPACE);
//        dumcharDump.setTypeface(Typeface.MONOSPACE);
    }

}
