package org.dusg.jechart.snapshot;

import org.dusg.jechart.JEChart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class SnippetBase {
    protected abstract JEChart createChart(Shell shell);

    public void launch() {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Snippet 136");
        shell.setLayout(new FillLayout());

        createChart(shell);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
