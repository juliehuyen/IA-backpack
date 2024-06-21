package com.dauphine.juliejoelle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

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
        YIntervalSeriesCollection dataset = createDataset(fitnessMeans, fitnessStdDevs);

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

        // Customize renderer for lines and error bars
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        XYErrorRenderer errorRenderer = new XYErrorRenderer();


        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            lineRenderer.setSeriesStroke(i, new BasicStroke(2.0f));
            lineRenderer.setSeriesShapesVisible(i, true);
            lineRenderer.setSeriesShape(i, new Rectangle(-3, -3, 6, 6));
            errorRenderer.setSeriesPaint(i, lineRenderer.getSeriesPaint(i));
        }

        plot.setRenderer(0, lineRenderer);
        plot.setRenderer(1, errorRenderer);

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

    private YIntervalSeriesCollection createDataset(Map<String, List<Double>> fitnessMeans, Map<String, List<Double>> fitnessStdDevs) {
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();

        for (String variant : fitnessMeans.keySet()) {
            YIntervalSeries series = new YIntervalSeries(variant + " Mean Fitness");

            List<Double> means = fitnessMeans.get(variant);
            List<Double> stdDevs = fitnessStdDevs.get(variant);

            for (int i = 0; i < means.size(); i++) {
                double mean = means.get(i);
                double stdDev = stdDevs.get(i);
                series.add(i + 1, mean, mean - stdDev, mean + stdDev);
            }

            dataset.addSeries(series);
        }

        return dataset;
    }

    public static void main(String[] args) {
        // Example usage:
        // You would replace this with your actual fitness data for different variants
        Map<String, List<Double>> fitnessMeans = Map.of(
                "Variant 1", List.of(10.0, 20.0, 30.0, 40.0, 50.0),
                "Variant 2", List.of(15.0, 25.0, 35.0, 45.0, 55.0)
        );

        Map<String, List<Double>> fitnessStdDevs = Map.of(
                "Variant 1", List.of(5.0, 4.0, 3.0, 2.0, 1.0),
                "Variant 2", List.of(4.0, 3.0, 2.0, 1.0, 0.5)
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
