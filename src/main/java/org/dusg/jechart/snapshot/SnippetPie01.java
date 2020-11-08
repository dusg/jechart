package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.dusg.jechart.PieChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import java.util.Map;

public class SnippetPie01 extends SnippetBase{
    public static void main(String[] args) {
        new SnippetPie01().launch();
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
        return pieChart;
    }
}
