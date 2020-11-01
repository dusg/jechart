package org.dusg.jechart;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        try {
            extraPath = Files.createTempDirectory("jechart");
            String jarFile = JEChart.class.getProtectionDomain().getCodeSource().getLocation().getPath();
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
