package hz.com.androidlib.mpcharts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;

public class MPLineActivity extends AppCompatActivity {

    @Bind(R.id.chart1)
    LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpline);
        ButterKnife.bind(this);

        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        setData(8, 100);
    }

    /**
     * 设置模拟数据
     * @param count 模拟的个数
     * @param range 数据的范围
     */
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");
            // set the line to be drawn like this "- - - - - -"
            //set1.enableDashedLine(10f, 5f, 0f);
            set1.setColor(Color.RED);
            set1.setDrawCircles(false);
            set1.setLineWidth(2f);
            set1.setValueTextSize(9f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            mChart.setData(data);
        }
    }

}
