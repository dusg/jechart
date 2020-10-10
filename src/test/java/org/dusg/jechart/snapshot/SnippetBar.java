package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SnippetBar {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 136");
        shell.setLayout(new FillLayout());
        JEChart browser;
        try {
            browser = new JEChart(shell, SWT.NONE);
            HashMap<String, Object> option = new HashMap<>();
            option.put("title", Map.of("text", "ECharts 入门示例"));
            option.put("tooltip", Collections.emptyMap());
            option.put("yAxis", Collections.emptyMap());
            option.put("legend", Map.of("data", new String[]{"销量"}));
            option.put("xAxis", Map.of("data", new String[]{"衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"}));
            option.put("series", new Object[]{Map.of("name", "销量", "type", "bar", "data", new Object[]{5, 20, 36, 10, 10
                    , 20})});
            browser.setOption(option);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
