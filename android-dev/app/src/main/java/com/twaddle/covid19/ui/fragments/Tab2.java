package com.twaddle.covid19.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.twaddle.covid19.R;
import com.twaddle.covid19.databinding.FragmentElderlyHelpBinding;

import douglasspgyn.com.github.circularcountdown.CircularCascadeCountdown;
import douglasspgyn.com.github.circularcountdown.CircularCountdown;

public class Tab2 extends Fragment {
    private static final String TAG = "Tab2";
    private FragmentElderlyHelpBinding binding;
    private CircularCountdown cascadeCountdownMin;
    private CircularCountdown cascadeCountdownSec;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentElderlyHelpBinding.inflate( inflater , container , false );
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        final Button btn_giveHelp = binding.btnGiveHelp;
        final Button btn_wantHelp = binding.btnWantHelp;
        cascadeCountdownMin = binding.circularCountdownMin;
        cascadeCountdownSec = binding.circularCountdownSec;

        btn_wantHelp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWantHelpSinglePickerDialog( getActivity() );
            }
        } );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showGiveHelpDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( "Enter details" );
        View customLayout = getLayoutInflater().inflate( R.layout.dialog_give_help_details , null );
        builder.setView( customLayout );

        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showWantHelpConfirmDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( "Confirm" );
        builder.setMessage( " Your address and contact details will be shared to the person who agrees to purchase your" +
                " item to deliver it to you" );
        builder.setPositiveButton( "Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CircularCascadeCountdown countdown = new CircularCascadeCountdown(5*CircularCountdown.MINUTE_CONVERTER ,cascadeCountdownSec , cascadeCountdownMin );
                countdown.start();
            }
        } );
        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // set single choice items
    private void showWantHelpSinglePickerDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context);
        builder.setTitle( "Item" );
        final String[] items = getResources().getStringArray( R.array.purchase_items );
        builder.setSingleChoiceItems( items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String[] fuel_type = getResources().getStringArray( R.array.purchase_items );
                        Log.i( TAG, "onClick: " + fuel_type[which] );
                        dialog.dismiss();
                        showWantHelpConfirmDialog( context );
                    }
                } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
