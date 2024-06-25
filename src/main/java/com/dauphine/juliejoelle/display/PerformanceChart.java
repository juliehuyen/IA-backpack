package com.dauphine.juliejoelle.display;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PerformanceChart extends JFrame {

    public PerformanceChart(String title, Map<String, List<Double>> fitnessMeans, Map<String, List<Double>> fitnessStdDevs) {
        super(title);

        // Create dataset
        DefaultXYDataset dataset = createDataset(fitnessMeans);
        DefaultXYDataset areaDataset = createAreaDataset(fitnessMeans, fitnessStdDevs);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Performance Chart",
                "Generation",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize the chart
        XYPlot plot = chart.getXYPlot();

        // Customize renderer for lines
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            lineRenderer.setSeriesStroke(i, new BasicStroke(2.0f)); // Thicker lines for means
            lineRenderer.setSeriesShapesVisible(i, true);
            lineRenderer.setSeriesShape(i, new Rectangle(-3, -3, 6, 6));
        }
        plot.setRenderer(0, lineRenderer);

        // Customize renderer for difference areas (transparent)
        XYDifferenceRenderer diffRenderer = new XYDifferenceRenderer(new Color(0f,0f,0f,.5f ), new Color(0f,0f,0f,.5f ),false); // Only lines, no fill
        diffRenderer.setSeriesPaint(0, Color.RED); // Red for Variant 1 StdDev
        diffRenderer.setSeriesPaint(1, Color.RED); // Red for Variant 1 StdDev
        diffRenderer.setSeriesPaint(2, Color.BLUE); // Blue for Variant 2 StdDev
        diffRenderer.setSeriesPaint(3, Color.BLUE); // Blue for Variant 2 StdDev
        diffRenderer.setSeriesPaint(4, Color.GREEN); // Green for Variant 3 StdDev
        diffRenderer.setSeriesPaint(5, Color.GREEN); // Green for Variant 3 StdDev
        diffRenderer.setSeriesPaint(6, Color.YELLOW); // Yellow for Variant 4 StdDev
        diffRenderer.setSeriesPaint(7, Color.YELLOW); // Yellow for Variant 4 StdDev

        plot.setRenderer(1, diffRenderer);

        // Add the area dataset
        plot.setDataset(1, areaDataset);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Customize the range axis (Y-axis)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Add chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1024, 768));
        setContentPane(chartPanel);

        // Save chart as an image
        try {
            ChartUtils.saveChartAsPNG(new File("PerformanceChart.png"), chart, 1024, 768);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DefaultXYDataset createDataset(Map<String, List<Double>> fitnessMeans) {
        DefaultXYDataset dataset = new DefaultXYDataset();

        for (String variant : fitnessMeans.keySet()) {
            List<Double> means = fitnessMeans.get(variant);
            double[][] data = new double[2][means.size()];
            for (int i = 0; i < means.size(); i++) {
                data[0][i] = i + 1;
                data[1][i] = means.get(i);
            }
            dataset.addSeries(variant + " Mean Fitness", data);
        }

        return dataset;
    }

    private DefaultXYDataset createAreaDataset(Map<String, List<Double>> fitnessMeans, Map<String, List<Double>> fitnessStdDevs) {
        DefaultXYDataset dataset = new DefaultXYDataset();

        for (String variant : fitnessMeans.keySet()) {
            List<Double> means = fitnessMeans.get(variant);
            List<Double> stdDevs = fitnessStdDevs.get(variant);

            double[][] upperData = new double[2][means.size()];
            double[][] lowerData = new double[2][means.size()];

            for (int i = 0; i < means.size(); i++) {
                upperData[0][i] = i + 1;
                upperData[1][i] = means.get(i) + stdDevs.get(i);
                lowerData[0][i] = i + 1;
                lowerData[1][i] = means.get(i) - stdDevs.get(i);
            }

            dataset.addSeries(variant + " StdDev Upper", upperData);
            dataset.addSeries(variant + " StdDev Lower", lowerData);
        }

        return dataset;
    }

    public static void main(String[] args) {
        // Example usage:
        // You would replace this with your actual fitness data for different variants
        Map<String, List<Double>> fitnessMeans = Map.of(
                "Variant 1", List.of(3600.0, 3700.0, 3800.0, 3900.0, 3950.0),
                "Variant 2", List.of(3650.0, 3750.0, 3850.0, 3900.0, 3950.0),
                "Variant 3", List.of(3550.0, 3650.0, 3750.0, 3850.0, 3950.0),
                "Variant 4", List.of(3500.0, 3600.0, 3700.0, 3800.0, 3900.0)
        );

        Map<String, List<Double>> fitnessStdDevs = Map.of(
                "Variant 1", List.of(50.0, 40.0, 30.0, 20.0, 10.0),
                "Variant 2", List.of(45.0, 35.0, 25.0, 15.0, 5.0),
                "Variant 3", List.of(55.0, 45.0, 35.0, 25.0, 15.0),
                "Variant 4", List.of(60.0, 50.0, 40.0, 30.0, 20.0)
        );

        SwingUtilities.invokeLater(() -> {
            PerformanceChart example = new PerformanceChart("Performance Chart Example", fitnessMeans, fitnessStdDevs);
            example.setSize(1024, 768);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
