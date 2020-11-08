package org.dusg.jechart;

import org.eclipse.swt.widgets.Composite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PieChart extends AbstractChart {

    public PieChart(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected void initOption() {
        super.initOption();
        setType("pie");
    }

    public void setName(String name) {
        getSeries().put("name", name);
        applyOption();
    }

    public void setRadius(float radius) {
        getSeries().put("radius", radius);
        applyOption();
    }

    public void setRadius(String radius) {
        getSeries().put("radius", radius);
        applyOption();
    }

    public void setData(List<Map<String, Object>> data) {
        getSeries().put("data", data);
        applyOption();
    }

    @SafeVarargs
    public final void setData(Map<String, Object>... data) {
        getSeries().put("data", data);
        applyOption();
    }

    public void setRoseType(String roseType) {
        getSeries().put("roseType", roseType);
        applyOption();
    }

    public void setHoverShadowBlur(float shadowBlur) {
        getEmphasisStyle().put("shadowBlur", shadowBlur);
        applyOption();
    }

    public void setShadowBlur(float shadowBlur) {
        getNormalStyle().put("shadowBlur", shadowBlur);
        applyOption();
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        getNormalStyle().put("shadowOffsetX", shadowOffsetX);
        applyOption();
    }

    public void setHoverShadowOffsetX(float shadowOffsetX) {
        getEmphasisStyle().put("shadowOffsetX", shadowOffsetX);
        applyOption();
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        getNormalStyle().put("shadowOffsetY", shadowOffsetY);
        applyOption();
    }

    public void setHoverShadowOffsetY(float shadowOffsetY) {
        getEmphasisStyle().put("shadowOffsetY", shadowOffsetY);
        applyOption();
    }

    public void setShadowColor(String color) {
        getNormalStyle().put("shadowColor", color);
        applyOption();
    }

    public void setHoverShadowColor(String color) {
        getEmphasisStyle().put("shadowColor", color);
        applyOption();
    }

    public void setVisualMap(Map<String, Object> visualMap) {
        option.put("visualMap", visualMap);
        applyOption();
    }
}
