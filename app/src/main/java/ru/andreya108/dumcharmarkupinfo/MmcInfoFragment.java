package ru.andreya108.dumcharmarkupinfo;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andrey on 27.10.2015.
 */
public class MmcInfoFragment extends Fragment {

    TextView mmcCid;
    TextView vendor;
    TextView partNumber;
    TextView mmcGb;
    TextView mmcUser;
    TextView mmcDate;
    TextView mmcName;
    TextView mmcSerial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MmcUtil mmcu = new MmcUtil(getResources());

        View rootView = inflater.inflate(R.layout.fragment_mmcinfo, container, false);

        mmcCid = (TextView) rootView.findViewById(R.id.mmcCid);
        mmcCid.setText(mmcu.getCid());

        mmcDate = (TextView) rootView.findViewById(R.id.mmcDate);
        mmcDate.setText(MmcUtil.readMmcFile(MmcUtil.MMC_DATE));

        mmcName = (TextView) rootView.findViewById(R.id.mmcName);
        mmcName.setText(MmcUtil.readMmcFile(MmcUtil.MMC_NAME));

        mmcSerial = (TextView) rootView.findViewById(R.id.mmcSerial);
        mmcSerial.setText(MmcUtil.readMmcFile(MmcUtil.MMC_SERIAL));

        MmcChip chip = mmcu.getChip();
        if (chip != null) {
            vendor = (TextView) rootView.findViewById(R.id.vendor);
            vendor.setText(chip.vendor);

            partNumber = (TextView) rootView.findViewById(R.id.partNumber);
            partNumber.setText(chip.part);

            mmcGb = (TextView) rootView.findViewById(R.id.mmcGb);
            mmcGb.setText( String.format(getResources().getString(R.string.total_size), chip.sizeGb, chip.totalSizeMb) );
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mmcCid.setTypeface(Typeface.MONOSPACE);
        mmcDate.setTypeface(Typeface.MONOSPACE);
        mmcName.setTypeface(Typeface.MONOSPACE);
        mmcSerial.setTypeface(Typeface.MONOSPACE);
        vendor.setTypeface(Typeface.MONOSPACE);
        partNumber.setTypeface(Typeface.MONOSPACE);
    }

}
