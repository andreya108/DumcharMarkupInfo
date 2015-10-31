package ru.andreya108.dumcharmarkupinfo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Andrey on 30.10.2015.
 */
public class MarkupFragment extends Fragment {

        DialogFragment errorDialog;
        DumcharUtil dumcharUtil;
        TextView detectedMarkup;
        TextView dumcharDump;
        ListView list;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_markup, container, false);


            dumcharUtil = new DumcharUtil(container.getContext());
            if ( dumcharUtil.readDumchar() == false)
            {
                errorDialog.show(getFragmentManager(), "errorDialog");
                getActivity().finish();
            }

            long emmcSize = dumcharUtil.getEmmcSizeGb();
            MmcUtil mmcu = new MmcUtil(getResources());

            if (mmcu.getChip() != null) {
                emmcSize = mmcu.getChip().sizeGb;

                if (emmcSize != dumcharUtil.getEmmcSizeGb()) {
                    String text = getString(R.string.wrong_markup);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
            }

            detectedMarkup = (TextView) rootView.findViewById(R.id.detectedMarkup);
            detectedMarkup.setText( String.format(getResources().getString(R.string.markup_info), dumcharUtil.getMarkup(), emmcSize) );

            PartitionSizesAdapter adapter=new PartitionSizesAdapter(inflater, dumcharUtil.getPartSizes());
            list=(ListView)rootView.findViewById(R.id.listView);
            list.setAdapter(adapter);

            dumcharDump = (TextView) rootView.findViewById(R.id.dumcharDump);
            dumcharDump.setText(dumcharUtil.getDumcharInfo());

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            dumcharDump.setTypeface(Typeface.MONOSPACE);
        }

    }
