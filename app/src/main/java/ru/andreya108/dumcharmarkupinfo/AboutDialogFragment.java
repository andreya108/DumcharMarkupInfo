package ru.andreya108.dumcharmarkupinfo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andrey on 30.09.2015.
 */
public class AboutDialogFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about, null);
        TextView version = (TextView ) v.findViewById(R.id.version);
        version.setText( String.format(getResources().getString(R.string.version), BuildConfig.VERSION_NAME) );
        return v;
    }
}
