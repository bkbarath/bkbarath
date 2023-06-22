import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Gui {
    private JPanel jp;
    private JTextField pathtext;
    private JButton enterButton;
    private JTable table1;
    private JTextField agetext;
    private JComboBox comboBox1;
    private JButton calculateButton;
    private JTextField rectext;
    private JButton searchButton;
    private JTable table2;
    private JTable table3;
    private JPanel jp1;

    public Gui() {
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream file = new FileInputStream("C:\\" + pathtext.getText() + ".xlsx");
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0);

                    int rowCount = sheet.getLastRowNum();
                    int colCount = 4;
                    String[] column = {"DealerName", "Price", "Rating", "ProductName"};
                    String[][] data = new String[rowCount + 1][4];
                    for (int i = 0; i <= rowCount; i++) {
                        Row row = sheet.getRow(i);
                        for (int j = 0; j < colCount; j++) {
                            Cell cell = row.getCell(j);
                            data[i][j] = cell.toString();
                        }
                    }
                    table1.setModel(new DefaultTableModel(data, column));


                    calculateButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int price = Integer.parseInt(agetext.getText());
                            double rating = Double.parseDouble(comboBox1.getSelectedItem().toString());
                            double[] selectprice = new double[rowCount + 1];
                            double[] selectrate = new double[rowCount + 1];
                            double[] knnvalue = new double[rowCount + 1];
                            String[] name = new String[rowCount + 1];
                            String[] pname = new String[rowCount + 1];
                            int[] orderid = new int[rowCount + 1];
                            String[][] data = new String[rowCount + 1][rowCount + 1];
                            for (int i = 0; i < rowCount + 1; i++) {
                                Row row = sheet.getRow(i);
                                selectprice[i] = row.getCell(1).getNumericCellValue();
                                selectrate[i] = row.getCell(2).getNumericCellValue();
                                knnvalue[i] = Math.sqrt(((selectprice[i] - price) * (selectprice[i] - price)) + ((selectrate[i] - rating) * (selectrate[i] - rating)));
                                double cal = knnvalue[i];
                                name[i] = row.getCell(0).getStringCellValue();
                                pname[i] = row.getCell(3).getStringCellValue();
                                orderid[i] = i + 1;
                                data[i][0] = String.valueOf(orderid[i]);
                                data[i][1] = name[i];
                                data[i][2] = pname[i];
                                data[i][3] = String.valueOf(cal);
                                data[i][4]=String.valueOf(selectprice[i]);
                            }
                            String[] column2 = {"OrderId", "Name", "ProductName", "Distance","Price"};

                            table2.setModel(new DefaultTableModel(data, column2));

                        }
                    });
                    searchButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] column3 = {"OrderId", "ProductName", "Distance"};
                            int n = Integer.parseInt(rectext.getText());

                            TableRowSorter<TableModel>sot=new TableRowSorter<TableModel>(table2.getModel());
                            sot.setComparator(3, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    if(o1 instanceof Double &&o2 instanceof Double){
                                        return Double.compare((Double)o1,(Double)o2);
                                    }
                                    else if(o1 instanceof String && o2 instanceof  String){
                                        Double d1=Double.parseDouble((String)o1);
                                        Double d2=Double.parseDouble((String)o2);
                                        return Double.compare(d1,d2);
                                    }
                                    else {
                                        return 0;
                                    }
                                }
                            });
                            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                            sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
                            sot.setSortKeys(sortKeys);
                            table2.setRowSorter(sot);

                            String[][] rowData = new String[n][3];

                            jp1.setLayout(new BorderLayout());
                            XYSeriesCollection set=new XYSeriesCollection();
                            XYSeries series=new XYSeries("Coll");

                            for(int i=0;i<Math.min(n,rowCount);i++){
                                int id=Integer.parseInt(table2.getValueAt(i,0).toString());
                                String pname=table2.getValueAt(i,2).toString();
                                double disstan=Double.parseDouble(table2.getValueAt(i,3).toString());
                                rowData[i][0]=String.valueOf(id);
                                rowData[i][1]=pname;
                                rowData[i][2]=String.valueOf(disstan);

                            }
                            table3.setModel(new DefaultTableModel(rowData, column3));
                            for(int i=0;i<rowCount+1;i++){
                                double category = Double.parseDouble(table1.getValueAt(i, 1).toString());
                                double sa= Double.parseDouble(table2.getValueAt(i,4 ).toString());
                                series.add(category,sa);
                            }
                            set.addSeries(series);
                            JFreeChart chart= ChartFactory.createScatterPlot("K-NN","Price",
                                    "Ratings",set);
                            XYPlot plot=(XYPlot)chart.getPlot();
                            plot.getRangeAxis().setRange(0.0,250);
                            plot.getDomainAxis().setRange(0.0,300);
                            ChartPanel chartPanel = new ChartPanel(chart);
                            chartPanel.setPreferredSize(new Dimension(200,300));
                            jp1.add(chartPanel,BorderLayout.CENTER);
                        }
                    });
                    workbook.close();
                    file.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("des");
        frame.setPreferredSize(new Dimension(1500,865));
        frame.setContentPane(new Gui().jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}