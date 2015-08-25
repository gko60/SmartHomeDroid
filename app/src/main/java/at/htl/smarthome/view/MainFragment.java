package at.htl.smarthome.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import at.htl.smarthome.OnFragmentInteractionListener;
import at.htl.smarthome.R;
import at.htl.smarthome.async.CurrentWeatherParser;
import at.htl.smarthome.async.ForecastRestImporter;
import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.entity.Sensor;
import at.htl.smarthome.repository.WeatherRepository;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements Observer {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String[] weekDayNames = {"Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    Typeface roboto;
    View fragmentView;
    TextView tvDateTime;
    ImageView ivActual;
    TextView tvDayOfWeekToday;
    ImageView ivToday;
    TextView tvTodayMinTemperature;
    TextView tvTodayMaxTemperature;
    TextView tvDayOfWeekTomorrow;
    ImageView ivTomorrow;
    TextView tvTomorrowMinTemperature;
    TextView tvTomorrowMaxTemperature;
    TextView tvDayOfWeekDayAfterTomorrow;
    ImageView ivDayAfterTomorrow;
    TextView tvDayAfterTomorrowMinTemperature;
    TextView tvDayAfterTomorrowMaxTemperature;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData() {
        new CurrentWeatherParser().execute();
        new ForecastRestImporter().execute();
    }


    /**
     * Liefert den Tagesnamen für das Datum zurück
     *
     * @param date Datum
     * @return String mit Tagesnamen
     */
    private String getDayName(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return weekDayNames[dayOfWeek - 1];
    }


    /*----------------------------------------------------------------------------------*/
    private TextView getTvDateTime() {
        if (tvDateTime == null) {
            tvDateTime = (TextView) fragmentView.findViewById(R.id.tv_date_time);
            tvDateTime.setTypeface(roboto);
        }
        return tvDateTime;
    }

    private ImageView getIvActual() {
        if (ivActual == null) {
            ivActual = (ImageView) fragmentView.findViewById(R.id.iv_actual);
        }
        return ivActual;
    }

    private TextView getTvDayOfWeekToday() {
        if (tvDayOfWeekToday == null) {
            tvDayOfWeekToday = (TextView) fragmentView.findViewById(R.id.tv_day_of_week_today);
        }
        return tvDayOfWeekToday;
    }

    private ImageView getIvToday() {
        if (ivToday == null) {
            ivToday = (ImageView) fragmentView.findViewById(R.id.iv_today);
        }
        return ivToday;
    }

    private TextView getTvTodayMinTemperature() {
        if (tvTodayMinTemperature == null) {
            tvTodayMinTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_min_today);
        }
        return tvTodayMinTemperature;
    }

    private TextView getTvTodayMaxTemperature() {
        if (tvTodayMaxTemperature == null) {
            tvTodayMaxTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_max_today);
        }
        return tvTodayMaxTemperature;
    }

    private TextView getTvDayOfWeekTomorrow() {
        if (tvDayOfWeekTomorrow == null) {
            tvDayOfWeekTomorrow = (TextView) fragmentView.findViewById(R.id.tv_day_of_week_tomorrow);
        }
        return tvDayOfWeekTomorrow;
    }

    private ImageView getIvTomorrow() {
        if (ivTomorrow == null) {
            ivTomorrow = (ImageView) fragmentView.findViewById(R.id.iv_tomorrow);
        }
        return ivTomorrow;
    }

    private TextView getTvTomorrowMinTemperature() {
        if (tvTomorrowMinTemperature == null) {
            tvTomorrowMinTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_min_tomorrow);
        }
        return tvTomorrowMinTemperature;
    }

    private TextView getTvTomorrowMaxTemperature() {
        if (tvTomorrowMaxTemperature == null) {
            tvTomorrowMaxTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_max_tomorrow);
        }
        return tvTomorrowMaxTemperature;
    }

    private TextView getTvDayOfWeekDayAfterTomorrow() {
        if (tvDayOfWeekDayAfterTomorrow == null) {
            tvDayOfWeekDayAfterTomorrow = (TextView) fragmentView.findViewById(R.id.tv_day_of_week_day_after_tomorrow);
        }
        return tvDayOfWeekDayAfterTomorrow;
    }

    private ImageView getIvDayAfterTomorrow() {
        if (ivDayAfterTomorrow == null) {
            ivDayAfterTomorrow = (ImageView) fragmentView.findViewById(R.id.iv_day_after_tomorrow);
        }
        return ivDayAfterTomorrow;
    }

    private TextView getTvDayAfterTomorrowMinTemperature() {
        if (tvDayAfterTomorrowMinTemperature == null) {
            tvDayAfterTomorrowMinTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_min_day_after_tomorrow);
        }
        return tvDayAfterTomorrowMinTemperature;
    }

    private TextView getTvDayAfterTomorrowMaxTemperature() {
        if (tvDayAfterTomorrowMaxTemperature == null) {
            tvDayAfterTomorrowMaxTemperature = (TextView) fragmentView.findViewById(R.id.tv_temperature_max_day_after_tomorrow);
        }
        return tvDayAfterTomorrowMaxTemperature;
    }


    /*----------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        WeatherRepository.getInstance().addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /***
     * Doubklewert mit entsprechenden Nachkommastellen gerundet
     * als Text zurückgeben
     *
     * @param value         double-Wert
     * @param decimalPlaces Anzahl Stellen
     * @return Textdarstellung
     */
    private String getDoubleString(double value, int decimalPlaces) {
        if (decimalPlaces == 0) {
            int number = (int) Math.round(value);
            return String.format(Locale.GERMAN, "%d", number);
        } else {
            String format = "%." + decimalPlaces + "f";
            return String.format(Locale.GERMAN, format, value);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        WeatherRepository weatherRepository = WeatherRepository.getInstance();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String dateTimeString = sdf.format(now);
        getTvDateTime().setText(getDayName(now) + ", " + dateTimeString);
        for (Sensor sensor : WeatherRepository.getInstance().getSensors()) {
            TextView textView = (TextView) fragmentView.findViewWithTag(sensor.getViewTag());
            if (textView != null) {
                textView.setText(getDoubleString(sensor.getValue(), sensor.getDecimalPlaces()) + sensor.getUnit());
            }
        }
        int imgRessource = getActivity().getResources().getIdentifier(WeatherRepository.getInstance().getActualWeatherIconFileName(), "drawable", getActivity().getPackageName());
        getIvActual().setImageResource(imgRessource);
        if (WeatherRepository.getInstance().getDayForecasts().size() > 0) {
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(0);
            Date date = new Date();
            getTvDayOfWeekToday().setText(getDayName(date));
            imgRessource = getActivity().getResources().getIdentifier(dayForecast.getIconFileName(), "drawable", getActivity().getPackageName());
            getIvToday().setImageResource(imgRessource);
            getTvTodayMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °");
            getTvTodayMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °");
        }
        if (WeatherRepository.getInstance().getDayForecasts().size() > 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            getTvDayOfWeekTomorrow().setText(getDayName(tomorrow));
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(1);
            imgRessource = getActivity().getResources().getIdentifier(dayForecast.getIconFileName(), "drawable", getActivity().getPackageName());
            getIvTomorrow().setImageResource(imgRessource);
            getTvTomorrowMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °");
            getTvTomorrowMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °");
        }
        if (WeatherRepository.getInstance().getDayForecasts().size() > 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            Date dayAfterTomorrow = calendar.getTime();
            getTvDayOfWeekDayAfterTomorrow().setText(getDayName(dayAfterTomorrow));
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(2);
            imgRessource = getActivity().getResources().getIdentifier(dayForecast.getIconFileName(), "drawable", getActivity().getPackageName());
            getIvDayAfterTomorrow().setImageResource(imgRessource);
            getTvDayAfterTomorrowMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °");
            getTvDayAfterTomorrowMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °");
        }

    }

}
