package com.twaddle.covid19.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.twaddle.covid19.databinding.FragmentElderlyHelpBinding;
import com.twaddle.covid19.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class Tab1 extends Fragment {
    private static final String TAG = "Tab1";
    private FragmentHomeBinding binding;
    private Button corona;
    private LinearLayout ll_content;
    private TextView tv_title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate( inflater , container , false );
        corona = binding.btnCorona;
        tv_title = binding.tvTitleTrack;
        ll_content = binding.llContent;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        tv_title.setVisibility( View.GONE );
        corona.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                corona.setVisibility( View.INVISIBLE );
                ll_content.setVisibility( View.GONE );
                tv_title.setVisibility( View.VISIBLE );
            }
        } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
