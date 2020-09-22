package org.dusg.jechart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JEChart {
    private static Path extraPath;

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

}
