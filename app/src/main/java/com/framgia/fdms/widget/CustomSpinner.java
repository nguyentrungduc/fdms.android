package com.framgia.fdms.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.framgia.fdms.R;

/**
 * Created by anh on 11/14/2017.
 */

public class CustomSpinner extends LinearLayout {
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private AdapterView.OnItemSelectedListener mListener;
    private View mView;

    public CustomSpinner(Context context) {
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner, this, true);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner, this, true);
        setAttribute(context, attrs);
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        mAdapter = adapter;
        Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
        spinner.setAdapter(mAdapter);
    }

    public void setListener(AdapterView.OnItemSelectedListener listener) {
        mListener = listener;
        Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(mListener);
    }

    private void setAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner, 0, 0);
        String titleText = typedArray.getString(R.styleable.CustomSpinner_title);
        typedArray.recycle();
        TextView tvTitle = (TextView) mView.findViewById(R.id.spinner_title);
        tvTitle.setText(titleText);
    }
}
