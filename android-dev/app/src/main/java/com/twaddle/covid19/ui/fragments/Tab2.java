package com.twaddle.covid19.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twaddle.covid19.R;
import com.twaddle.covid19.databinding.FragmentElderlyHelpBinding;
import com.twaddle.covid19.model.Elder;
import com.twaddle.covid19.model.UserDetails;
import com.twaddle.covid19.model.WantHelpElder;
import com.twaddle.covid19.network.RetrofitApiInterface;
import com.twaddle.covid19.ui.App;
import com.twaddle.covid19.ui.adapters.EldersListAdapter;
import com.twaddle.covid19.utils.PrefManager;

import java.util.ArrayList;

import javax.inject.Inject;

import douglasspgyn.com.github.circularcountdown.CircularCascadeCountdown;
import douglasspgyn.com.github.circularcountdown.CircularCountdown;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tab2 extends Fragment {

    @Inject
    Retrofit retrofit;

    private static final String TAG = "Tab2";
    private FragmentElderlyHelpBinding binding;
    private CircularCountdown cascadeCountdownMin;
    private CircularCountdown cascadeCountdownSec;
    private String purchaseItem;
    private PrefManager prefManager;
    private TextView tv_showHelperDetails;
    private  Button btn_giveHelp, btn_wantHelp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        App.getApp().getDataComponent().inject( this );
        prefManager = new PrefManager( getActivity() );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentElderlyHelpBinding.inflate( inflater , container , false );
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        btn_giveHelp = binding.btnGiveHelp;
        btn_wantHelp = binding.btnWantHelp;
        tv_showHelperDetails = binding.tvHelperDetails;
        cascadeCountdownMin = binding.circularCountdownMin;
        cascadeCountdownSec = binding.circularCountdownSec;
        btn_wantHelp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWantHelpSinglePickerDialog( getActivity() );
            }
        } );
        btn_giveHelp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiveHelpTripDetailsDialog( getActivity() );
            }
        } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showGiveHelpTripDetailsDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( "Enter details" );
        View customLayout = getLayoutInflater().inflate( R.layout.dialog_give_help_details , null );
        builder.setView( customLayout );
        final Spinner spinnerItems = customLayout.findViewById( R.id.spinner_items );
        final EditText et_dropLocation = customLayout.findViewById( R.id.et_dropLocation );
        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i( TAG, "onClick: spinner value" + String.valueOf(spinnerItems.getSelectedItem() ));
                Log.i( TAG, "onClick: edittext= " + et_dropLocation.getText().toString() );
                dialogInterface.dismiss();
                showGiveHelpListofEldersDialog( context );
            }
        } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showGiveHelpListofEldersDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( "Enter details" );
        View customLayout = getLayoutInflater().inflate( R.layout.dialog_list_of_elders, null );
        builder.setView( customLayout );
        final RecyclerView recyclerView = customLayout.findViewById( R.id.recycler_viewlist_elders );
        EldersListAdapter adapter = new EldersListAdapter( getElderList() ,getActivity() );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        recyclerView.setAdapter( adapter );
        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i( TAG, "onClick: done" );
                dialogInterface.dismiss();
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
                sendRequestWhoWantHelp();
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

    private void sendRequestWhoWantHelp() {
        WantHelpElder wantHelpElder = new WantHelpElder( prefManager.getHomeLatitude() , prefManager.getHomeLongitude() , purchaseItem );
        RetrofitApiInterface apiInterface = retrofit.create( RetrofitApiInterface.class);
        Call<UserDetails> call = apiInterface.sendWantHelpRequest(wantHelpElder);
        call.enqueue( new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(response.isSuccessful()){
                    Log.i( this.getClass().getSimpleName(), "onResponse: " + response );
                    tv_showHelperDetails.setVisibility( View.VISIBLE );
                    tv_showHelperDetails.setText( response.body().toString() );
                    cascadeCountdownMin.setVisibility( View.GONE );
                    cascadeCountdownSec.setVisibility( View.GONE );
                    btn_giveHelp.setVisibility( View.INVISIBLE );
                    btn_wantHelp.setVisibility( View.INVISIBLE );
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.i( TAG, "onFailure: " + t );
                try{
                    Thread.sleep( 1000 );
                }catch (InterruptedException e){}
                tv_showHelperDetails.setVisibility( View.VISIBLE );
                tv_showHelperDetails.setText( "Sorry!! No person is available right Now");
                cascadeCountdownMin.setVisibility( View.GONE );
                cascadeCountdownSec.setVisibility( View.GONE );
                btn_giveHelp.setVisibility( View.INVISIBLE );
                btn_wantHelp.setVisibility( View.INVISIBLE );
            }
        } );
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
                        String[] item = getResources().getStringArray( R.array.purchase_items );
                        Log.i( TAG, "onClick: " + item[which] );
                        purchaseItem = item[which];
                        dialog.dismiss();
                        showWantHelpConfirmDialog( context );
                    }
                } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Elder> getElderList(){
        ArrayList<Elder> arrayList = new ArrayList<>();
        arrayList.add( new Elder( "Narendra Modi" , "300m Away" , "Medicine" ) );
        arrayList.add( new Elder( "Narendra Modi" , "300m Away" , "Medicine" ) );
        arrayList.add( new Elder( "Narendra Modi" , "300m Away" , "Medicine" ) );
        arrayList.add( new Elder( "Narendra Modi" , "300m Away" , "Medicine" ) );
        arrayList.add( new Elder( "Narendra Modi" , "300m Away" , "Medicine" ) );
        return arrayList;
    }
}
