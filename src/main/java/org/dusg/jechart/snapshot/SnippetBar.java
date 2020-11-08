package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.*;
import java.util.List;

public class SnippetBar extends SnippetBase {
    public static void main(String[] args) {
        new SnippetBar().launch();
    }

    @Override
    protected JEChart createChart(Shell shell) {
        JEChart chart;
        chart = new JEChart(shell, SWT.NONE);
        HashMap<String, Object> option = new HashMap<>();
        option.put("title", Map.of("text", "ECharts 入门示例"));
        option.put("tooltip", Collections.emptyMap());
//            option.put("yAxis", Collections.emptyMap());
        option.put("legend", Map.of("data", new String[]{"销量"}));
        option.put("xAxis", Map.of("data", new String[]{"衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋",
                "袜子"}));
//            option.put("series", List.of(
//                    Map.of("name", "销量",
//                            "type", "bar",
//                            "data", List.of(5, 20, 36, 10, 10, 2)
//                    )
//            ));

        chart.setOption(option);
        chart.setyAxis(Collections.emptyMap(), Collections.emptyMap());
        Display.getDefault().timerExec(3000, () -> {
            chart.setGrid(Map.of("left", 130, "right", "30%", "height", "40%", "bottom", 110));
            chart.setSeries(Map.of(
                    "name", "销量",
                    "type", "bar",
                    "data", List.of(5, 20, 36, 10, 10, 20),
                    "yAxisIndex", "0"
                    )
            );
            chart.getxAxis();
        });

        return chart;
    }
}
