package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetBar {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 136");
        shell.setLayout(new FillLayout());
        Browser browser;
        try {
            browser = new Browser(shell, SWT.NONE);
        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: " + e.getMessage());
            display.dispose();
            return;
        }

        browser.setUrl("file://" + JEChart.getExtraPath() + "/web/index.html");
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
