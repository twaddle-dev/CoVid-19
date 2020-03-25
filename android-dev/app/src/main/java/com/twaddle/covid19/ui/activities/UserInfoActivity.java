package com.twaddle.covid19.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twaddle.covid19.R;
import com.twaddle.covid19.databinding.ActivityUserInfoBinding;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_info );
        com.twaddle.covid19.databinding.ActivityUserInfoBinding binding = ActivityUserInfoBinding.inflate( getLayoutInflater() );

        EditText et_name = binding.etName;
        EditText et_age = binding.etAge;
        Button btn_next = binding.btnUserInfoNext;
        btn_next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( UserInfoActivity.this, MainActivity.class );
                startActivity( intent );
            }
        } );

    }
}
