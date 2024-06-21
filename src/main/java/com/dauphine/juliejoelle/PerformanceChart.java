package com.dauphine.juliejoelle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
public class PerformanceChart extends JFrame {
    public PerformanceChart(String title, Map<String, List<Double>> fitnessMeans) {
        super(title);
        // Create dataset
        XYSeriesCollection dataset = createDataset(fitnessMeans);
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
        // Add any customization here
        // Add chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        // Save chart as an image
        try {
            ChartUtils.saveChartAsPNG(new File("PerformanceChart.png"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private XYSeriesCollection createDataset(Map<String, List<Double>> fitnessMeans) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (String variant : fitnessMeans.keySet()) {
            XYSeries meanSeries = new XYSeries(variant + " Mean Fitness");
            XYSeries stdDevSeries = new XYSeries(variant + " Std Dev");
            List<Double> means = fitnessMeans.get(variant);
            System.out.println(means);
            for (int i = 0; i < means.size(); i++) {
                meanSeries.add(i + 1, means.get(i));
            }
            dataset.addSeries(meanSeries);
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
            PerformanceChart example = new PerformanceChart("Performance Chart Example", fitnessMeans);
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
