package org.dusg.jechart;

import org.eclipse.swt.widgets.Composite;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractChart extends JEChart {

    public static final String SERIES = "series";

    public AbstractChart(Composite parent, int style) {
        super(parent, style);
        initOption();
    }

    public void setTextColor(String color) {
        getTextStyle().put("color", color);
        applyOption();
    }

    private Map<String, Object> getTextStyle() {
        return (Map<String, Object>) option.computeIfAbsent("textStyle", k -> new HashMap<>());
    }

    protected void initOption() {
    }

    protected void setType(String type) {
        getSeries().put("type", type);
        applyOption();
    }

    protected Map<String, Object> getSeries() {
        Object series = option.computeIfAbsent(SERIES, k -> new HashMap<String, Object>());
        return (Map<String, Object>) series;
    }

    public void setLabelLineColor(String color) {
        getLineStyle().put("color", color);
        applyOption();
    }

    private Map<String, Object> getLineStyle() {
        Object lineStyle = ((Map<String, Object>) getSeries().computeIfAbsent("labelLine",
                k -> new HashMap<>()))
                .computeIfAbsent("lineStyle", k -> new HashMap<>());
        return (Map<String, Object>) lineStyle;
    }

    public void setItemColor(String color) {
        getNormalStyle().put("color", color);
        applyOption();
    }

    protected Map<String, Object> getItemStyle() {
        return (Map<String, Object>) getSeries().computeIfAbsent("itemStyle", k -> new HashMap<String,
                Object>());
    }

    protected Map<String, Object> getEmphasisStyle() {
        Map<String, Object> style = (Map<String, Object>) getItemStyle().computeIfAbsent("emphasis",
                k -> new HashMap<String, Object>());
        return style;
    }
    protected Map<String, Object> getNormalStyle() {
        return getItemStyle();
    }
}
