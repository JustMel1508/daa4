package com.aisha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class PlotGenerator {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1000;

    private static final int LEFT = 150;
    private static final int RIGHT = 100;
    private static final int TOP = 120;
    private static final int BOTTOM = 190;

    public void generateAll() throws IOException {

        File csvFile =
                new File("results/results.csv");

        if (!csvFile.exists()) {
            throw new FileNotFoundException(
                    "results/results.csv was not found. Run the experiment first."
            );
        }

        File plotDirectory =
                new File("results/plots");

        if (!plotDirectory.exists()) {
            plotDirectory.mkdirs();
        }

        List<ResultRow> rows =
                readCsv(csvFile);

        createRecursiveOperationsPlot(rows);

        createDpTimePlot(rows);

        createSearchOperationsPlot(rows);

        createSearchRatioPlot(rows);

        System.out.println();
        System.out.println(
                "Plots generated successfully."
        );

        System.out.println(
                "Open the folder:"
        );

        System.out.println(
                "results/plots"
        );
    }

    // =========================================================
    // PLOT 1
    // =========================================================

    private void createRecursiveOperationsPlot(
            List<ResultRow> rows
    ) throws IOException {

        List<Series> series =
                new ArrayList<>();

        series.add(
                buildSeries(
                        rows,
                        "Recursive - equal",
                        "Recursive",
                        "equal",
                        false
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "Recursive - different",
                        "Recursive",
                        "different",
                        false
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "Recursive - random",
                        "Recursive",
                        "random",
                        false
                )
        );

        drawChart(
                "Recursive Edit Distance: Growth of Recursive Calls",
                "Input size n (characters)",
                "Recursive calls (log scale)",
                "Notice: the number of recursive calls grows very quickly when characters differ.",
                series,
                true,
                new File(
                        "results/plots/01_recursive_operations.png"
                )
        );
    }

    // =========================================================
    // PLOT 2
    // =========================================================

    private void createDpTimePlot(
            List<ResultRow> rows
    ) throws IOException {

        List<Series> series =
                new ArrayList<>();

        series.add(
                buildSeries(
                        rows,
                        "Table - random",
                        "Table",
                        "random",
                        true
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "Two Rows - random",
                        "TwoRows",
                        "random",
                        true
                )
        );

        drawChart(
                "Dynamic Programming Edit Distance: Runtime",
                "Input size n (characters)",
                "Median execution time (ms)",
                "Notice: both DP implementations grow with n, while Two Rows uses much less extra memory.",
                series,
                false,
                new File(
                        "results/plots/02_dp_runtime.png"
                )
        );
    }

    // =========================================================
    // PLOT 3
    // =========================================================

    private void createSearchOperationsPlot(
            List<ResultRow> rows
    ) throws IOException {

        List<Series> series =
                new ArrayList<>();

        series.add(
                buildSeries(
                        rows,
                        "Naive - worst case",
                        "Naive",
                        "repeated_worst",
                        false
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "KMP - worst case",
                        "KMP",
                        "repeated_worst",
                        false
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "Naive - random",
                        "Naive",
                        "random",
                        false
                )
        );

        series.add(
                buildSeries(
                        rows,
                        "KMP - random",
                        "KMP",
                        "random",
                        false
                )
        );

        drawChart(
                "Naive vs KMP: Character Comparisons",
                "Text size n (characters)",
                "Character comparisons",
                "Notice: on repeated input, Naive repeats many comparisons while KMP reuses prefix information.",
                series,
                false,
                new File(
                        "results/plots/03_search_operations.png"
                )
        );
    }

    // =========================================================
    // PLOT 4 - RATIO
    // =========================================================

    private void createSearchRatioPlot(
            List<ResultRow> rows
    ) throws IOException {

        Series naive =
                buildSeries(
                        rows,
                        "Naive",
                        "Naive",
                        "repeated_worst",
                        false
                );

        Series kmp =
                buildSeries(
                        rows,
                        "KMP",
                        "KMP",
                        "repeated_worst",
                        false
                );

        Series ratio =
                new Series(
                        "Naive / KMP operation ratio"
                );

        for (Point naivePoint : naive.points) {

            Point kmpPoint =
                    findPointByX(
                            kmp,
                            naivePoint.x
                    );

            if (kmpPoint != null
                    && kmpPoint.y != 0) {

                ratio.points.add(
                        new Point(
                                naivePoint.x,
                                naivePoint.y
                                        / kmpPoint.y
                        )
                );
            }
        }

        drawChart(
                "String Search: Naive-to-KMP Work Ratio",
                "Text size n (characters)",
                "Naive operations / KMP operations (ratio)",
                "Notice: values above 1 mean Naive performs more measured work than KMP.",
                List.of(ratio),
                false,
                new File(
                        "results/plots/04_search_ratio.png"
                )
        );
    }

    // =========================================================
    // BUILD SERIES
    // =========================================================

    private Series buildSeries(
            List<ResultRow> rows,
            String seriesName,
            String algorithm,
            String input,
            boolean useTime
    ) {

        Series series =
                new Series(seriesName);

        Map<Integer, List<Double>> grouped =
                new TreeMap<>();

        for (ResultRow row : rows) {

            if (!row.algorithm.equals(algorithm)) {
                continue;
            }

            if (!row.input.equals(input)) {
                continue;
            }

            double value;

            if (useTime) {
                value = row.timeMs;
            } else {
                value = row.operations;
            }

            grouped
                    .computeIfAbsent(
                            row.n,
                            key -> new ArrayList<>()
                    )
                    .add(value);
        }

        for (
                Map.Entry<Integer, List<Double>>
                        entry : grouped.entrySet()
        ) {

            double median =
                    median(entry.getValue());

            series.points.add(
                    new Point(
                            entry.getKey(),
                            median
                    )
            );
        }

        return series;
    }

    // =========================================================
    // DRAW CHART
    // =========================================================

    private void drawChart(
            String title,
            String xLabel,
            String yLabel,
            String takeaway,
            List<Series> seriesList,
            boolean logY,
            File output
    ) throws IOException {

        BufferedImage image =
                new BufferedImage(
                        WIDTH,
                        HEIGHT,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g =
                image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Background
        g.setColor(Color.WHITE);

        g.fillRect(
                0,
                0,
                WIDTH,
                HEIGHT
        );

        // Title
        g.setColor(Color.BLACK);

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        34
                )
        );

        FontMetrics titleMetrics =
                g.getFontMetrics();

        int titleX =
                (WIDTH
                        - titleMetrics.stringWidth(title))
                        / 2;

        g.drawString(
                title,
                titleX,
                55
        );

        double minX =
                Double.MAX_VALUE;

        double maxX =
                -Double.MAX_VALUE;

        double minY =
                Double.MAX_VALUE;

        double maxY =
                -Double.MAX_VALUE;

        for (Series series : seriesList) {

            for (Point point : series.points) {

                minX =
                        Math.min(
                                minX,
                                point.x
                        );

                maxX =
                        Math.max(
                                maxX,
                                point.x
                        );

                double displayedY =
                        logY
                                ? safeLog10(point.y)
                                : point.y;

                minY =
                        Math.min(
                                minY,
                                displayedY
                        );

                maxY =
                        Math.max(
                                maxY,
                                displayedY
                        );
            }
        }

        if (minX == Double.MAX_VALUE) {
            g.dispose();

            throw new IllegalStateException(
                    "No data available for plot: "
                            + title
            );
        }

        if (maxX == minX) {
            maxX = minX + 1;
        }

        if (maxY == minY) {
            maxY = minY + 1;
        }

        double yPadding =
                (maxY - minY) * 0.10;

        if (yPadding == 0) {
            yPadding = 1;
        }

        minY -= yPadding;
        maxY += yPadding;

        int chartLeft =
                LEFT;

        int chartRight =
                WIDTH - RIGHT;

        int chartTop =
                TOP;

        int chartBottom =
                HEIGHT - BOTTOM;

        // Grid
        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        18
                )
        );

        int ticks = 6;

        for (int i = 0; i <= ticks; i++) {

            int y =
                    chartBottom
                            - i
                            * (chartBottom - chartTop)
                            / ticks;

            g.setColor(
                    new Color(
                            225,
                            225,
                            225
                    )
            );

            g.drawLine(
                    chartLeft,
                    y,
                    chartRight,
                    y
            );

            double value =
                    minY
                            + (maxY - minY)
                            * i
                            / ticks;

            String label;

            if (logY) {

                double realValue =
                        Math.pow(
                                10,
                                value
                        );

                label =
                        formatNumber(
                                realValue
                        );

            } else {

                label =
                        formatNumber(
                                value
                        );
            }

            g.setColor(Color.DARK_GRAY);

            FontMetrics metrics =
                    g.getFontMetrics();

            g.drawString(
                    label,
                    chartLeft
                            - metrics.stringWidth(label)
                            - 15,
                    y + 6
            );
        }

        for (int i = 0; i <= ticks; i++) {

            int x =
                    chartLeft
                            + i
                            * (chartRight - chartLeft)
                            / ticks;

            g.setColor(
                    new Color(
                            235,
                            235,
                            235
                    )
            );

            g.drawLine(
                    x,
                    chartTop,
                    x,
                    chartBottom
            );

            double value =
                    minX
                            + (maxX - minX)
                            * i
                            / ticks;

            String label =
                    String.valueOf(
                            (int) Math.round(value)
                    );

            g.setColor(Color.DARK_GRAY);

            FontMetrics metrics =
                    g.getFontMetrics();

            g.drawString(
                    label,
                    x
                            - metrics.stringWidth(label)
                            / 2,
                    chartBottom + 30
            );
        }

        // Axes
        g.setColor(Color.BLACK);

        g.setStroke(
                new BasicStroke(2.5f)
        );

        g.drawLine(
                chartLeft,
                chartTop,
                chartLeft,
                chartBottom
        );

        g.drawLine(
                chartLeft,
                chartBottom,
                chartRight,
                chartBottom
        );

        // Axis labels
        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        FontMetrics axisMetrics =
                g.getFontMetrics();

        g.drawString(
                xLabel,
                (WIDTH
                        - axisMetrics.stringWidth(xLabel))
                        / 2,
                chartBottom + 75
        );

        drawRotatedText(
                g,
                yLabel,
                40,
                (chartTop + chartBottom) / 2
        );

        // Series colors
        Color[] colors = {
                new Color(31, 119, 180),
                new Color(214, 39, 40),
                new Color(44, 160, 44),
                new Color(148, 103, 189),
                new Color(255, 127, 14)
        };

        int seriesIndex = 0;

        for (Series series : seriesList) {

            Color color =
                    colors[
                            seriesIndex
                                    % colors.length
                            ];

            g.setColor(color);

            g.setStroke(
                    new BasicStroke(
                            4.0f
                    )
            );

            Point previous = null;

            for (Point point : series.points) {

                double shownY =
                        logY
                                ? safeLog10(point.y)
                                : point.y;

                int px =
                        mapX(
                                point.x,
                                minX,
                                maxX,
                                chartLeft,
                                chartRight
                        );

                int py =
                        mapY(
                                shownY,
                                minY,
                                maxY,
                                chartTop,
                                chartBottom
                        );

                if (previous != null) {

                    double previousShownY =
                            logY
                                    ? safeLog10(previous.y)
                                    : previous.y;

                    int previousX =
                            mapX(
                                    previous.x,
                                    minX,
                                    maxX,
                                    chartLeft,
                                    chartRight
                            );

                    int previousY =
                            mapY(
                                    previousShownY,
                                    minY,
                                    maxY,
                                    chartTop,
                                    chartBottom
                            );

                    g.draw(
                            new Line2D.Double(
                                    previousX,
                                    previousY,
                                    px,
                                    py
                            )
                    );
                }

                g.fillOval(
                        px - 7,
                        py - 7,
                        14,
                        14
                );

                previous = point;
            }

            seriesIndex++;
        }

        // Legend
        int legendX =
                chartRight - 320;

        int legendY =
                chartTop + 15;

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        19
                )
        );

        seriesIndex = 0;

        for (Series series : seriesList) {

            Color color =
                    colors[
                            seriesIndex
                                    % colors.length
                            ];

            int y =
                    legendY
                            + seriesIndex * 34;

            g.setColor(color);

            g.fillRect(
                    legendX,
                    y - 13,
                    24,
                    8
            );

            g.setColor(Color.BLACK);

            g.drawString(
                    series.name,
                    legendX + 38,
                    y
            );

            seriesIndex++;
        }

        // Takeaway
        g.setColor(
                new Color(
                        35,
                        35,
                        35
                )
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.ITALIC,
                        19
                )
        );

        drawCenteredWrappedText(
                g,
                takeaway,
                WIDTH / 2,
                HEIGHT - 55,
                WIDTH - 200
        );

        g.dispose();

        ImageIO.write(
                image,
                "png",
                output
        );
    }

    // =========================================================
    // CSV READING
    // =========================================================

    private List<ResultRow> readCsv(
            File file
    ) throws IOException {

        List<ResultRow> rows =
                new ArrayList<>();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file)
                        )
        ) {

            String line =
                    reader.readLine();

            while (
                    (line = reader.readLine())
                            != null
            ) {

                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",");

                if (parts.length < 8) {
                    continue;
                }

                ResultRow row =
                        new ResultRow();

                row.family =
                        parts[0];

                row.algorithm =
                        parts[1];

                row.input =
                        parts[2];

                row.n =
                        Integer.parseInt(
                                parts[3]
                        );

                row.m =
                        Integer.parseInt(
                                parts[4]
                        );

                row.run =
                        Integer.parseInt(
                                parts[5]
                        );

                row.timeMs =
                        Double.parseDouble(
                                parts[6]
                        );

                row.operations =
                        Double.parseDouble(
                                parts[7]
                        );

                rows.add(row);
            }
        }

        return rows;
    }

    // =========================================================
    // MEDIAN
    // =========================================================

    private double median(
            List<Double> values
    ) {

        List<Double> copy =
                new ArrayList<>(values);

        Collections.sort(copy);

        int middle =
                copy.size() / 2;

        if (copy.size() % 2 == 1) {

            return copy.get(middle);

        } else {

            return (
                    copy.get(middle - 1)
                            + copy.get(middle)
            ) / 2.0;
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private Point findPointByX(
            Series series,
            double x
    ) {

        for (Point point : series.points) {

            if (point.x == x) {
                return point;
            }
        }

        return null;
    }

    private double safeLog10(
            double value
    ) {

        if (value <= 0) {
            return 0;
        }

        return Math.log10(value);
    }

    private int mapX(
            double value,
            double min,
            double max,
            int left,
            int right
    ) {

        return (int) (
                left
                        + (value - min)
                        / (max - min)
                        * (right - left)
        );
    }

    private int mapY(
            double value,
            double min,
            double max,
            int top,
            int bottom
    ) {

        return (int) (
                bottom
                        - (value - min)
                        / (max - min)
                        * (bottom - top)
        );
    }

    private String formatNumber(
            double value
    ) {

        if (Math.abs(value) >= 1_000_000) {

            return String.format(
                    Locale.US,
                    "%.1fM",
                    value / 1_000_000.0
            );
        }

        if (Math.abs(value) >= 1_000) {

            return String.format(
                    Locale.US,
                    "%.1fk",
                    value / 1_000.0
            );
        }

        if (Math.abs(value) >= 10) {

            return String.format(
                    Locale.US,
                    "%.0f",
                    value
            );
        }

        return String.format(
                Locale.US,
                "%.2f",
                value
        );
    }

    private void drawRotatedText(
            Graphics2D g,
            String text,
            int x,
            int centerY
    ) {

        FontMetrics metrics =
                g.getFontMetrics();

        int width =
                metrics.stringWidth(text);

        Graphics2D copy =
                (Graphics2D) g.create();

        copy.rotate(
                -Math.PI / 2
        );

        copy.drawString(
                text,
                -centerY - width / 2,
                x
        );

        copy.dispose();
    }

    private void drawCenteredWrappedText(
            Graphics2D g,
            String text,
            int centerX,
            int y,
            int maximumWidth
    ) {

        FontMetrics metrics =
                g.getFontMetrics();

        if (
                metrics.stringWidth(text)
                        <= maximumWidth
        ) {

            g.drawString(
                    text,
                    centerX
                            - metrics.stringWidth(text)
                            / 2,
                    y
            );

            return;
        }

        String[] words =
                text.split(" ");

        StringBuilder line =
                new StringBuilder();

        List<String> lines =
                new ArrayList<>();

        for (String word : words) {

            String candidate =
                    line.isEmpty()
                            ? word
                            : line + " " + word;

            if (
                    metrics.stringWidth(candidate)
                            > maximumWidth
            ) {

                lines.add(
                        line.toString()
                );

                line =
                        new StringBuilder(word);

            } else {

                line =
                        new StringBuilder(candidate);
            }
        }

        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        int currentY =
                y - (lines.size() - 1) * 23;

        for (String currentLine : lines) {

            g.drawString(
                    currentLine,
                    centerX
                            - metrics.stringWidth(
                            currentLine
                    ) / 2,
                    currentY
            );

            currentY += 25;
        }
    }

    // =========================================================
    // DATA CLASSES
    // =========================================================

    private static class ResultRow {

        String family;
        String algorithm;
        String input;

        int n;
        int m;
        int run;

        double timeMs;
        double operations;
    }

    private static class Series {

        String name;

        List<Point> points =
                new ArrayList<>();

        Series(String name) {
            this.name = name;
        }
    }

    private static class Point {

        double x;
        double y;

        Point(
                double x,
                double y
        ) {

            this.x = x;
            this.y = y;
        }
    }
}