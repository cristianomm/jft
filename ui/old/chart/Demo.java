package com.cmm.jft.ui.chart;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import com.sun.tools.visualvm.charts.ChartFactory;
//import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
//import com.sun.tools.visualvm.charts.SimpleXYChartSupport;

public class Demo extends JPanel {
//
//    private static final long SLEEP_TIME = 500;
//    private static final int VALUES_LIMIT = 150;
//    private static final int ITEMS_COUNT = 8;
//    private SimpleXYChartSupport support;
//
//    public Demo() {
//        createModels();
//        setLayout(new BorderLayout());
//        add(support.getChart(), BorderLayout.CENTER);
//    }
//
//    private void createModels() {
//        SimpleXYChartDescriptor descriptor =
//                SimpleXYChartDescriptor.decimal(0, 1000, 1000, 1d, true, VALUES_LIMIT);
//
//        for (int i = 0; i < ITEMS_COUNT; i++) {
//            descriptor.addLineFillItems("Item " + i);
//        }
//
//        descriptor.setDetailsItems(new String[]{"Detail 1", "Detail 2", "Detail 3"});
//        descriptor.setChartTitle("<html><font size='+1'><b>Demo Chart</b></font></html>");
//        descriptor.setXAxisDescription("<html>X Axis <i>[time]</i></html>");
//        descriptor.setYAxisDescription("<html>Y Axis <i>[units]</i></html>");
//
//        support = ChartFactory.createSimpleXYChart(descriptor);
//
//        new Generator(support).start();
//    }
//
//    private static class Generator extends Thread {
//
//        private SimpleXYChartSupport support;
//
//        public void run() {
//            while (true) {
//                try {
//                    long[] values = new long[ITEMS_COUNT];
//                    for (int i = 0; i < values.length; i++) {
//                        values[i] = (long) (1000 * Math.random());
//                    }
//                    support.addValues(System.currentTimeMillis(), values);
//                    support.updateDetails(new String[]{1000 * Math.random() + "",
//                                1000 * Math.random() + "",
//                                1000 * Math.random() + ""});
//                    Thread.sleep(SLEEP_TIME);
//                } catch (Exception e) {
//                    e.printStackTrace(System.err);
//                }
//            }
//        }
//
//        private Generator(SimpleXYChartSupport support) {
//            this.support = support;
//        }
//    }
//    
//    
//    public static void main(String args[]) {
//	
//	JFrame f = new JFrame();
//	f.add(new Demo());
//	f.setVisible(true);
//	
//	
//	
//    }

}