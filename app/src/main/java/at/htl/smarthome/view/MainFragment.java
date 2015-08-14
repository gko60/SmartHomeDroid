package at.htl.smarthome.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import at.htl.smarthome.OnFragmentInteractionListener;
import at.htl.smarthome.R;
import at.htl.smarthome.async.CurrentWeatherParser;
import at.htl.smarthome.async.ForecastRestImporter;
import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.repository.WeatherRepository;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements Observer, View.OnClickListener {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View fragmentView;
    TextView tvDateTime;
    ImageView ivActual;
    TextView tvTemperatureOut;
    TextView tvAirPressure;
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

    /*----------------------------------------------------------------------------------*/
    private TextView getTvDateTime() {
        if (tvDateTime == null) {
            tvDateTime = (TextView) fragmentView.findViewById(R.id.tv_date_time);
        }
        return tvDateTime;
    }

    private ImageView getIvActual() {
        if (ivActual == null) {
            ivActual = (ImageView) fragmentView.findViewById(R.id.iv_actual);
        }
        return ivActual;
    }

    private TextView getTvTemperatureOut() {
        if (tvTemperatureOut == null) {
            tvTemperatureOut = (TextView) fragmentView.findViewById(R.id.tv_temperature_out);
        }
        return tvTemperatureOut;
    }

    private TextView getTvAirPressure() {
        if (tvAirPressure == null) {
            tvAirPressure = (TextView) fragmentView.findViewById(R.id.tv_air_pressure);
        }
        return tvAirPressure;
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

    /*----------------------------------------------------------------------------------*/

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
        View buttonRefresh = fragmentView.findViewById(R.id.button_refresh);
        buttonRefresh.setOnClickListener(this);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd. MM.yyyy HH:mm:ss");
        String dateTimeString = sdf.format(new Date());
        getTvDateTime().setText(dateTimeString);
        getTvTemperatureOut().setText(getDoubleString(WeatherRepository.getInstance().getTemperatureOut(), 1) + " °C");
        getTvAirPressure().setText(getDoubleString(WeatherRepository.getInstance().getAirPressure(), 1) + " hPa");

        if (WeatherRepository.getInstance().getDayForecasts().size() > 0) {
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(0);
            getIvToday().setImageResource(dayForecast.getIcon());
            getTvTodayMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °C");
            getTvTodayMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °C");
        }
        if (WeatherRepository.getInstance().getDayForecasts().size() > 1) {
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(1);
            getIvTomorrow().setImageResource(dayForecast.getIcon());
            getTvTomorrowMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °C");
            getTvTomorrowMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °C");
        }
        if (WeatherRepository.getInstance().getDayForecasts().size() > 2) {
            DayForecast dayForecast = WeatherRepository.getInstance().getDayForecasts().get(2);
            getIvDayAfterTomorrow().setImageResource(dayForecast.getIcon());
            getTvDayAfterTomorrowMaxTemperature().setText(getDoubleString(dayForecast.getMaxTemperature(), 1) + " °C");
            getTvDayAfterTomorrowMinTemperature().setText(getDoubleString(dayForecast.getMinTemperature(), 1) + " °C");
        }


//        TextView tvDayOfWeekToday;
//        ImageView ivToday;
//        TextView tvTodayMinTemperature;
//        TextView tvTodayMaxTemperature;

    }

    @Override
    public void onClick(View view) {
        new CurrentWeatherParser().execute();
        new ForecastRestImporter(getActivity()).execute();
    }
}
