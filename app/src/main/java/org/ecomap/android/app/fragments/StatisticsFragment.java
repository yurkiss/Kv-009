package org.ecomap.android.app.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.ecomap.android.app.R;
import org.ecomap.android.app.data.EcoMapContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private PieChart pieChart;
    private TextView one_day;
    private TextView one_week;
    private TextView one_month;
    private TextView one_year;
    private TextView one_reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getString(R.string.nav_titles_statistics));
        getActivity().invalidateOptionsMenu();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_layout, container, false);

        //getActivity().invalidateOptionsMenu();

        one_day = (TextView) view.findViewById(R.id.one_day);
        one_week = (TextView) view.findViewById(R.id.one_week);
        one_month = (TextView) view.findViewById(R.id.one_month);
        one_year = (TextView) view.findViewById(R.id.one_year);
        one_reset = (TextView) view.findViewById(R.id.one_reset);

        one_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                underlineButton(v);
                setData(1);
            }
        });
        one_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underlineButton(v);
                setData(7);
            }
        });
        one_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underlineButton(v);
                setData(30);
            }
        });
        one_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underlineButton(v);
                setData(365);
            }
        });
        one_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underlineButton(v);
                setData(0);
            }
        });

        pieChart = (PieChart) view.findViewById(R.id.pie_chart);

        //Setting piechart
        pieChart.setDescription("");
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(30f);
        pieChart.setDrawSliceText(false);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setWordWrapEnabled(true);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setXEntrySpace(20);

        //Starting chart - all problems
        setData(0);

        return view;
    }

    private void underlineButton(View view) {

        //reset all
        one_day.setPaintFlags(one_day.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        one_week.setPaintFlags(one_week.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        one_month.setPaintFlags(one_month.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        one_year.setPaintFlags(one_year.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        one_reset.setPaintFlags(one_reset.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

        switch (view.getId()) {
            case R.id.one_day:
                one_day.setPaintFlags(one_day.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                break;
            case R.id.one_week:
                one_week.setPaintFlags(one_week.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                break;
            case R.id.one_month:
                one_month.setPaintFlags(one_month.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                break;
            case R.id.one_year:
                one_year.setPaintFlags(one_year.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                break;
            case R.id.one_reset:
                one_reset.setPaintFlags(one_reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                break;

        }

    }

    private void setData(int time) {

        //Animating loading of chart
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        //Date format cause we're getting string from DB
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        //Current date
        Calendar currentDate = Calendar.getInstance();

        //Current time
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentWeek = currentDate.get(Calendar.WEEK_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);

        //Problem time
        int problemDay;
        int problemWeek;
        int problemMonth;
        int problemYear;

        // ints for counting each type of problems
        int woods_problem_count = 0;
        int trash_problem_count = 0;
        int building_problem_count = 0;
        int water_problem_count = 0;
        int lifeforms_problem_count = 0;
        int poaching_problem_count = 0;
        int other_problem_count = 0;
        float whole_result = 0;

        //Getting cursor from DB
        Cursor cursor = getActivity().getContentResolver()
                .query(EcoMapContract.ProblemsEntry.CONTENT_URI, null, null, null, null);

        //Cursor iteration
        while (cursor.moveToNext()) {

            //Each problem string
            String problemDateFromBase = cursor.getString(cursor.getColumnIndex(EcoMapContract.ProblemsEntry.COLUMN_DATE));

            try {
                //Getting each problem's time
                currentDate.setTime(dateFormat.parse(problemDateFromBase));
                problemDay = currentDate.get(Calendar.DAY_OF_MONTH);
                problemWeek = currentDate.get(Calendar.WEEK_OF_MONTH);
                problemMonth = currentDate.get(Calendar.MONTH);
                problemYear = currentDate.get(Calendar.YEAR);

                //Switch for 1day 1week 1month 1year sorting
                switch (time) {
                    case 1:
                        if (problemYear == currentYear && problemMonth == currentMonth && problemDay == currentDay) {
                            break;
                        } else continue;
                    case 7:
                        if (problemYear == currentYear && problemMonth == currentMonth && problemWeek == currentWeek) {
                            break;
                        } else continue;
                    case 30:
                        if (problemYear == currentYear && problemMonth == currentMonth) {
                            break;
                        } else continue;
                    case 365:
                        if (problemYear == currentYear) {
                            break;
                        } else continue;
                    default:
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //If problem is not filtered, we're incrementing whole result
            ++whole_result;

            //Getting type of problem and incrementing each type
            switch (cursor.getInt(cursor.getColumnIndex(EcoMapContract.ProblemsEntry.COLUMN_PROBLEM_TYPE_ID))) {
                case 1:
                    ++woods_problem_count;
                    break;
                case 2:
                    ++trash_problem_count;
                    break;
                case 3:
                    ++building_problem_count;
                    break;
                case 4:
                    ++water_problem_count;
                    break;
                case 5:
                    ++lifeforms_problem_count;
                    break;
                case 6:
                    ++poaching_problem_count;
                    break;
                default:
                    ++other_problem_count;
                    break;
            }
        }

        cursor.close();

        //Creating dataset
        ArrayList<Entry> problemsResults = new ArrayList<>();
        ArrayList<String> problemsNames = new ArrayList<>();

        if (woods_problem_count != 0) {
            problemsResults.add(new Entry((woods_problem_count / whole_result) * 100, 0));
            problemsNames.add(getString(R.string.problem_type_string_1) + ": " + woods_problem_count);
        }
        if (trash_problem_count != 0) {
            problemsResults.add(new Entry((trash_problem_count / whole_result) * 100, 1));
            problemsNames.add(getString(R.string.problem_type_string_2) + ": " + trash_problem_count);
        }
        if (building_problem_count != 0) {
            problemsResults.add(new Entry((building_problem_count / whole_result) * 100, 2));
            problemsNames.add(getString(R.string.problem_type_string_3) + ": " + building_problem_count);
        }
        if (water_problem_count != 0) {
            problemsResults.add(new Entry((water_problem_count / whole_result) * 100, 3));
            problemsNames.add(getString(R.string.problem_type_string_4) + ": " + water_problem_count);
        }
        if (lifeforms_problem_count != 0) {
            problemsResults.add(new Entry((lifeforms_problem_count / whole_result) * 100, 4));
            problemsNames.add(getString(R.string.problem_type_string_5) + ": " + lifeforms_problem_count);
        }
        if (poaching_problem_count != 0) {
            problemsResults.add(new Entry((poaching_problem_count / whole_result) * 100, 5));
            problemsNames.add(getString(R.string.problem_type_string_6) + ": " + poaching_problem_count);
        }
        if (other_problem_count != 0) {
            problemsResults.add(new Entry((other_problem_count / whole_result) * 100, 6));
            problemsNames.add(getString(R.string.problem_type_string_7) + ": " + other_problem_count);
        }

        // Setting dataset
        PieDataSet dataSet = new PieDataSet(problemsResults, getString(R.string.all_sort) + ": " + (int) whole_result);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS) colors.add(c);

        dataSet.setColors(colors);

        // set Data
        PieData data = new PieData(problemsNames, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}