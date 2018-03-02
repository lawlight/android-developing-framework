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
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends AppCompatActivity {

    @Bind(R.id.chart)
    LineChartView mChartView;

    String[] date = {"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23"};//X轴的标注
    int[] score = {90, 100, 70, 120, 110, 103, 105, 95, 95};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<PointValue> limitPointValues = new ArrayList<>();

    private List<AxisValue> mAxisXValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
        limitPointValues.add(new PointValue(0,60));
        limitPointValues.add(new PointValue(score.length - 1, 150));
    }

    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.RED);  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        //line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        //line.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(false);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        //line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);

        //lines.add(new Line(limitPointValues).setColor(Color.TRANSPARENT));
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        //axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        //axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        //axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        //axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        //Axis axisY = new Axis();  //Y轴
        Axis axisY = new Axis();

        //axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setMaxLabelChars(4);//max label length, for example 60
        List<AxisValue> values = new ArrayList<>();
        for(int i = 60; i <= 150; i+= 10){
            AxisValue value = new AxisValue(i);
            String label = i+"";
            value.setLabel(label);
            values.add(value);
        }
        axisY.setValues(values);

        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        mChartView.setZoomEnabled(false);
//        mChartView.setInteractive(true);
//        mChartView.setZoomType(ZoomType.HORIZONTAL);
//        mChartView.setMaxZoom((float) 2);//最大方法比例
//        mChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
          mChartView.setLineChartData(data);
//        /**注：下面的7，10只是代表一个数字去类比而已
//         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
//         */
//        Viewport v = new Viewport(mChartView.getMaximumViewport());
//        v.left = 0;
//        v.right= 7;
//        mChartView.setCurrentViewport(v);

        resetViewport();
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        mChartView.setViewportCalculationEnabled(false);
        final Viewport v = new Viewport(mChartView.getMaximumViewport());
        v.bottom = 60;
        v.top = 150;
        v.left = 0;
        v.right = score.length - 1;
        mChartView.setMaximumViewport(v);
        mChartView.setCurrentViewport(v);
    }

}
