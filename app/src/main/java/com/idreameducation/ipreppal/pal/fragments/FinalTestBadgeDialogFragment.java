package com.idreameducation.ipreppal.pal.fragments;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.MediaPlayer;
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

import java.io.IOException;

public class FinalTestBadgeDialogFragment extends DialogFragment {

    private Global global;
    private Context context;
    private TextView textViewMessage;
    private TextView continueBt;
    private String topicName;

    public FinalTestBadgeDialogFragment() {
    }

    public static FinalTestBadgeDialogFragment newInstance(String topicName) {
        FinalTestBadgeDialogFragment frag = new FinalTestBadgeDialogFragment();
        Bundle args = new Bundle();
        args.putString("topicName", topicName);
        frag.setArguments(args);
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
        window.setLayout((int) (size.x * 0.50), (int) (size.y * 0.80));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_white_dialog_fragment_shape);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.final_test_badge_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds(view);
        listners();
    }

    private void listners() {
        continueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        topicName = getArguments().getString("topicName", null);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        continueBt = view.findViewById(R.id.continueBt);
        String tName = "<b>"+ topicName + "</b>";
        textViewMessage.setText("आपको अभी-अभी महारत हासिल करने के लिए एक बैज मिला है:\n"+tName);
//        playAudio();
    }


    private void playAudio(){
        AssetFileDescriptor afd = null;
        try {
            afd = getActivity().getApplicationContext().getAssets().openFd("short-crowd-cheerflac-6713.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer player = new MediaPlayer();
        try {
            assert afd != null;
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }
}
