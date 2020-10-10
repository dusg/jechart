package org.dusg.jechart;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JEChart {
    private static Path extraPath;
    private final Browser browser;
    private Object option = new HashMap<String, Object>();
    protected boolean loadComplete = false;
    private final JEChart.getOptionFunction getOptionFunction;

    public static Path getExtraPath() {
        return extraPath;
    }

    static private void extraResources() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            extraPath = Files.createTempDirectory("jechart");
            String jarFile = JEChart.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (Files.isDirectory(Paths.get(jarFile))) {
                extraPath = Paths.get(jarFile);
                return;
            }
            processBuilder.directory(extraPath.toFile());
            processBuilder.command("jar", "xf", jarFile, "web");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static {
        extraResources();
    }

    public JEChart(Composite parent, int style) {
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
    }

    private void jsSetOption() {
        javaToJs(option);
        browser.execute(String.format("chart.setOption(%s)", javaToJs(getOption())));
    }

    private String javaToJs(Object option) {
        if (option instanceof String) {
            return String.format("'%s'", option);
        }
        if (option instanceof Number) {
            Number number = (Number) option;
            return number.toString();
        }
        if (option instanceof Object[]) {
            Object[] objects = (Object[]) option;
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

    public Object getOption() {
        return option;
    }

    public void setOption(Object option) {
        this.option = option;
        if (isLoadComplete()) {
            jsSetOption();
        }
    }

    public boolean isLoadComplete() {
        return loadComplete;
    }

    protected void setLoadComplete(boolean loadComplete) {
        this.loadComplete = loadComplete;
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
