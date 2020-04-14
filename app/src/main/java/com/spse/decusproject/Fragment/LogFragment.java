package com.spse.decusproject.Fragment;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.decus.R;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import be.greifmatthias.horizontalcalendarstrip.HorizontalCalendar;

public class LogFragment extends Fragment  {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Calendar
        final HorizontalCalendar calendar = getView().findViewById(R.id.hcCalendar);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -35);
        calendar.setSelected(c.getTime());
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        calendar.setSelected(d);

        calendar.setOnChanged(new HorizontalCalendar.onChangeListener() {
            @Override
            public void selectChanged(Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                TextView t=getView().findViewById(R.id.tvTest);
                t.setText(c.getTime().toLocaleString());;
            }

            @Override
            public void labelChanged(Date sourceDate) {

            }
        });

    }
}
