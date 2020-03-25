package com.twaddle.covid19.dagger;


import com.twaddle.covid19.services.LocationTrack;
import com.twaddle.covid19.ui.activities.MainActivity;
import com.twaddle.covid19.ui.activities.UserInfoActivity;
import com.twaddle.covid19.ui.fragments.Tab2;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {
    void inject(MainActivity activity);
    void inject(UserInfoActivity userInfoActivity);
    void inject(LocationTrack locationTrack);
    void inject(Tab2 tab2);
}
