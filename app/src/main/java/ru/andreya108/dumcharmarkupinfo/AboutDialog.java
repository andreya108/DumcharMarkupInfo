package ru.andreya108.dumcharmarkupinfo;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andrey on 30.09.2015.
 */
public class AboutDialog extends DialogFragment implements View.OnClickListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.about, null);
        TextView version = (TextView ) v.findViewById(R.id.version);
        getDialog().setTitle(getResources().getString(R.string.action_about));
        version.setText( String.format(getResources().getString(R.string.version), BuildConfig.VERSION_NAME) );

        v.findViewById(R.id.aboutDialogBtn).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
