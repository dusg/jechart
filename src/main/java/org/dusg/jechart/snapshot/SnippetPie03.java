package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.dusg.jechart.PieChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import java.util.List;
import java.util.Map;

public class SnippetPie03 extends SnippetBase {
    public static void main(String[] args) {
        new SnippetPie03().launch();
    }

    @Override
    protected JEChart createChart(Shell shell) {
        PieChart pieChart = new PieChart(shell, SWT.NONE);
        pieChart.setName("访问来源");
        pieChart.setRadius("55%");
        pieChart.setData(
                Map.of("value", 235, "name", "视频广告"),
                Map.of("value", 274, "name", "联盟广告"),
                Map.of("value", 310, "name", "邮件营销"),
                Map.of("value", 335, "name", "直接访问"),
                Map.of("value", 400, "name", "搜索引擎")
        );
        pieChart.setRoseType("angle");
        pieChart.setShadowBlur(20);
        pieChart.setShadowOffsetX(0);
        pieChart.setShadowOffsetY(0);
        pieChart.setShadowColor("rgba(0,0,0,0.5)");
        pieChart.setBackgroundColor("#2c343c");
        pieChart.setTextColor("rgba(255, 255, 255, 0.3)");
        pieChart.setLabelLineColor("rgba(255, 255, 255, 0.3)");
        pieChart.setItemColor("#c23531");
        pieChart.setVisualMap(Map.of(
                "show", false,
                "min", 80,
                "max", 600,
                "inRange", Map.of(
                        "colorLightness", List.of(0, 1)
                )
        ));
        return pieChart;
    }
}
