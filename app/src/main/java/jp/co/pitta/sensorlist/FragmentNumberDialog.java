package jp.co.pitta.sensorlist;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


public class FragmentNumberDialog extends DialogFragment {

    private static final String ARG_numDials = "numDials";
    private static final String ARG_initValue = "initValue";

    private int numDials;
    private int currentValue;
    private String mUnit;
    private String mTitle;

    private NumberPicker[] numPickers;
    private OnNumberDialogDoneListener mListener;

    public static FragmentNumberDialog newInstance(int numDials, int initValue, String unit, String title) {
        FragmentNumberDialog numdialog = new FragmentNumberDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_numDials, numDials);
        args.putInt(ARG_initValue, initValue);
        args.putString("unit", unit);
        args.putString("title", title);

        numdialog.setArguments(args);
        return numdialog;
    }

    public FragmentNumberDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numDials = getArguments().getInt(ARG_numDials);
            currentValue = getArguments().getInt(ARG_initValue);
            numPickers = new NumberPicker[numDials];
            mUnit = getArguments().getString("unit");
            mTitle = getArguments().getString("title");
        }
        if (savedInstanceState != null) {
            currentValue = savedInstanceState.getInt("CurrentValue");

        }

        try {
            mListener = (OnNumberDialogDoneListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnNumberDialogDoneListener");
        }
    }

    private int getDigit(int d, int i) {
        String temp = Integer.toString(d);
        if (temp.length() <= i) return 0;
        int r = Character.getNumericValue(temp.charAt(temp.length() - i - 1));
        return r;
    }

    private int getValue() {
        int value = 0;
        int mult = 1;
        for (int i = 1; i < numDials; i++) {
            value += numPickers[i].getValue() * mult;
            mult *= 10;
        }
        return value;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout linLayoutH = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutH.setLayoutParams(params);

        TextView textView = new TextView(getActivity());
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);

        for (int i = numDials - 1; i >= 1; i--) {
            numPickers[i] = new NumberPicker(getActivity());
            numPickers[i].setMaxValue(9);
            numPickers[i].setMinValue(0);
            numPickers[i].setValue(getDigit(currentValue, i - 1));
            linLayoutH.addView(numPickers[i]);
        }

        numPickers[0] = new NumberPicker(getActivity());
        numPickers[0].setMinValue(0);
        numPickers[0].setMaxValue(1);

        String[] str;
        str = new String[]{"msec", "sec"};

        numPickers[0].setDisplayedValues(str);

        if (mUnit.equals("sec")) {
            numPickers[0].setValue(1);
        } else {
            numPickers[0].setValue(0);
        }
        linLayoutH.addView(numPickers[0]);

        LinearLayout linLayoutV = new LinearLayout(getActivity());
        linLayoutV.setOrientation(LinearLayout.VERTICAL);
        linLayoutV.addView(textView);
        linLayoutV.addView(linLayoutH);
        Button okButton = new Button(getActivity());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentValue = getValue();
                if (mListener != null) {
                    if (mTitle.equals("samplingTime")) {
                        mListener.onDone("samplingTime", Integer.toString(numPickers[0].getValue()), currentValue);
                    } else {
                        mListener.onDone("delayTime", Integer.toString(numPickers[0].getValue()), currentValue);
                    }
                }
                ;
                dismiss();
            }
        });


        params.gravity = Gravity.CENTER_HORIZONTAL;
        okButton.setLayoutParams(params);
        okButton.setText("Done");
        linLayoutV.addView(okButton);
        return linLayoutV;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentValue", getValue());

    }

    public interface OnNumberDialogDoneListener {
        public void onDone(String title, String unit, int time);
    }

}
