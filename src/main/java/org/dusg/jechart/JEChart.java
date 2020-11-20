package org.dusg.jechart;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JEChart {
    public static final String X_AXIS = "xAxis";
    private static Path extraPath;
    protected final Browser browser;
    protected Map<String, Object> option = new HashMap<>();
    protected boolean loadComplete = false;
    private final JEChart.getOptionFunction getOptionFunction;

    public static Path getExtraPath() {
        return extraPath;
    }

    static private void extraResources() {
        try {
            extraPath = Files.createTempDirectory("jechart");
            String jarFile =
                    JEChart.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (Files.isDirectory(Paths.get(jarFile))) {
                // this for debug
                extraPath = Paths.get(jarFile);
                return;
            }
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry jarEntry = enumEntries.nextElement();
                if (!jarEntry.getName().startsWith("web")) {
                    continue;
                }
                Path dstFile = Paths.get(extraPath.toString(), jarEntry.getName());

                if (jarEntry.isDirectory()) {
                    Files.createDirectories(dstFile);
                    continue;
                }
                FileUtils.copyInputStreamToFile(jar.getInputStream(jarEntry), dstFile.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static {
        extraResources();
    }

    public JEChart(Composite parent, int style) {
//        browser = new Browser(parent, style | SWT.CHROMIUM);
        browser = new Browser(parent, style);
        browser.setUrl("file://" + JEChart.getExtraPath() + "/web/index.html");
        browser.addProgressListener(ProgressListener.completedAdapter(progressEvent -> {
            setLoadComplete(true);
            jsSetOption();
        }));
        getOptionFunction = new getOptionFunction(browser);

        browser.addDisposeListener(disposeEvent -> {
            getOptionFunction.dispose();
        });
        browser.addMenuDetectListener(e -> e.doit = false);
    }

    @Deprecated
    public Object getOption() {
        return option;
    }

    public void setOption(Map<String, Object> option) {
        this.option = option;
        if (isLoadComplete()) {
            jsSetOption();
        }
    }

    @SafeVarargs
    public final void setxAxis(Map<String, Object>... xAxis) {
        if (xAxis.length == 1)
            option.put(X_AXIS, xAxis[0]);
        else
            option.put(X_AXIS, xAxis);
        if (isLoadComplete()) {
            jsSetOption();
        }
    }

    public List<Object> getxAxis() {
        return new Gson().fromJson(getOption(X_AXIS), List.class);
    }

    @SafeVarargs
    public final void setyAxis(Map<String, Object>... yAxis) {
        option.put("yAxis", yAxis);
        if (isLoadComplete()) {
            jsSetOption();
        }
    }


    public void setGrid(Map<String, Object> grid) {
        option.put("grid", grid);
        if (isLoadComplete()) {
            jsSetOption();
        }
    }

    @SafeVarargs
    public final void setSeries(Map<String, Object>... series) {
        option.put("series", series);
        if (isLoadComplete()) {
            jsSetOption();
        }
    }

    public void setBackgroundColor(String color) {
        option.put("backgroundColor", color);
        execute(()-> browser.execute(String.format("body.style.background = '%s'", color)));
        applyOption();
    }

    public void showLoading() {
        execute(()-> {
            browser.execute("chart.showLoading();");
        });
    }

    public void hideLoading() {
        execute(() -> {
            browser.execute("chart.hideLoading();");
        });
    }

    protected void execute(Runnable runnable) {
        if (isLoadComplete()) {
            runnable.run();
            return;
        }
        Display.getDefault().asyncExec(()->execute(runnable));
    }

    protected String getOption(String name) {
        return (String) browser.evaluate(String.format("return JSON.stringify(chart.getOption().%s)", name));
    }
    protected void jsSetOption() {
        browser.execute(String.format("chart.setOption(%s)", javaToJs(option)));
        option.clear();
    }

    protected String javaToJs(Object option) {
        if (option instanceof String) {
            return String.format("'%s'", option);
        }
        if (option instanceof Number) {
            Number number = (Number) option;
            return number.toString();
        }
        if (option instanceof Boolean) {
            Boolean aBoolean = (Boolean) option;
            return aBoolean.toString();
        }
        if (option instanceof Object[] || option instanceof List) {
            Object[] objects;
            if (option instanceof Object[])
                objects = (Object[]) option;
            else
                objects = ((List<?>) option).toArray();

            StringBuilder builder = new StringBuilder();
            builder.append("[");
            for (Object object : objects) {
                builder.append(javaToJs(object));
                builder.append(",");
            }
            builder.append("]");
            return builder.toString();
        }
        if (option instanceof Map) {
            Map map = (Map) option;
            StringBuilder builder = new StringBuilder();
            builder.append("{");

            map.forEach((key, value) -> {
                builder.append(javaToJs(key));
                builder.append(":");
                builder.append(javaToJs(value));
                builder.append(",");
            });
            builder.append("}");
            return builder.toString();
        }
        return "undefined";
    }

    protected boolean isLoadComplete() {
        return loadComplete;
    }

    protected void setLoadComplete(boolean loadComplete) {
        this.loadComplete = loadComplete;
    }

    protected void applyOption() {
        setOption(option);
    }

    class getOptionFunction extends BrowserFunction {

        public getOptionFunction(Browser browser) {
            super(browser, "getOption");
        }

        @Override
        public Object function(Object[] arguments) {
            return getOption();
        }
    }
}
