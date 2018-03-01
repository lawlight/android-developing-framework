package hz.com.androidlib.hellocharts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends AppCompatActivity {

    @Bind(R.id.chart)
    LineChartView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);


        List values = new ArrayList<PointValue>();//折线上的点
        values.add(new PointValue(0, 90));
        values.add(new PointValue(1, 100));
        values.add(new PointValue(2, 95));
        values.add(new PointValue(3, 120));
        values.add(new PointValue(4, 110));
        values.add(new PointValue(5, 100));

        Line line = new Line(values).setColor(Color.RED);//声明线并设置颜色
        line.setCubic(true);//设置是平滑的还是直的
        line.setHasPoints(false);

        List lines = new ArrayList<Line>();
        lines.add(line);

        mChartView.setInteractive(true);//设置图表是可以交互的（拖拽，缩放等效果的前提）
        //mChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);//设置缩放方向
        mChartView.setZoomEnabled(false);
        LineChartData data = new LineChartData();

        data.setLines(lines);

        Axis axisX = new Axis();//x轴
        Axis axisY = new Axis();//y轴

        axisX.setName("时间");
        axisY.setName("频率");

        List yValues = new ArrayList<AxisValue>();
        yValues.add(new AxisValue(70f));
        yValues.add(new AxisValue(80f));
        yValues.add(new AxisValue(90f));
        yValues.add(new AxisValue(100f));
        yValues.add(new AxisValue(110f));
        yValues.add(new AxisValue(120f));
        yValues.add(new AxisValue(130f));
        yValues.add(new AxisValue(140f));
        yValues.add(new AxisValue(150f));
        axisY.setValues(yValues);

        data.setBaseValue(0);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChartView.setLineChartData(data);//给图表设置数据

    }
}
