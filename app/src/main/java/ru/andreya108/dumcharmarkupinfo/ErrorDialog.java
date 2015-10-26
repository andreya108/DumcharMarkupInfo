package ru.andreya108.dumcharmarkupinfo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andrey on 30.09.2015.
 */
public class ErrorDialog extends DialogFragment implements View.OnClickListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.error, null);
        getDialog().setTitle(getResources().getString(R.string.error_title));

        v.findViewById(R.id.errorDialogBtn).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
