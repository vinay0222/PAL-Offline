package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.idreameducation.ipreppal.R;
import com.idreameducation.ipreppal.educationApplication.Global;
import com.idreameducation.ipreppal.util.Util;

public class HomeScreenIntroDialogFragment extends DialogFragment {

    private Global global;
    private Context context;
    private TextView textViewTitle,step1,step2,step3,step4;
    private TextView buttonProceed;

    public HomeScreenIntroDialogFragment() {
    }

    public static HomeScreenIntroDialogFragment newInstance() {
        HomeScreenIntroDialogFragment frag = new HomeScreenIntroDialogFragment();
        return frag;
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.80), (int) (size.y * 0.90));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_white_dialog_fragment_shape);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds(view);
        listners();
    }

    private void listners() {
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        textViewTitle = view.findViewById(R.id.textViewTitle);
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);
        step4 = view.findViewById(R.id.step4);
        buttonProceed = view.findViewById(R.id.buttonProceed);
        if (Util.getSelectedLanguage(context).equalsIgnoreCase("hindi"))
        {
            textViewTitle.setText(Util.getUsername(context) +" iPrep PAL में आपका स्वागत है, ");
            step1.setText("प्रत्येक अध्याय के लिए, आपके सीखने के स्तर को जानने के लिए आपको एक डायग्नोस्टिक परिक्षण करना है");
            step2.setText("उसके बाद वैचारिक स्पष्टता और निपुणता के लिए मनोरंजक एनिमेशन्स के साथ प्रश्नो का अभ्यास करेंगे");
            step3.setText("अंत में पूर्ण महारत के लिए आपको हर अध्याय में अंतिम परिक्षण देना होगा");
            step4.setText("iPrep आपको हर कदम पर सीखने और आगे बढ़ने के लिए मार्गदर्शित करेगा");
            buttonProceed.setText("सीखना शुरू करें");
        }else {
            textViewTitle.setText(Util.getUsername(context) +" Welcome to iPrep PAL, ");
            step1.setText("For each chapter, you have to take a diagnostic test to know your learning level");
            step2.setText("After that practice questions with fun animations for conceptual clarity and dexterity");
            step3.setText("In the end, for complete mastery, you must pass the final test in each chapter.");
            step4.setText("iPrep will guide you every step of the way to learn and grow");
            buttonProceed.setText("Start learning");
        }
    }

}
