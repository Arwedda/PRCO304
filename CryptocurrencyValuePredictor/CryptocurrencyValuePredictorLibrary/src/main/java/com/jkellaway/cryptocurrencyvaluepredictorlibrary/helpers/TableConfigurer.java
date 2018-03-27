/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jkell
 */
public class TableConfigurer {
    public static void configureTable(JTable jTable) {
        //Selecting single entire row
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setColumnSelectionAllowed(false);
        jTable.setRowSelectionAllowed(true);
        jTable.setDefaultEditor(Object.class, null);
        
        //Sorting by column when table header clicked
        jTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
        /*        int columnIndex = jTable.convertColumnIndexToModel(jTable.columnAtPoint(e.getPoint()));
                Object columnClass = jTable.getColumnClass(columnIndex);
                jTable.setAutoCreateRowSorter(true);
                
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
                jTable.setRowSorter(sorter);
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();

                boolean isDouble = false;
                try {
                    //If the column is a Double (stored as String to show extra 0s)
                    Double.parseDouble(String.valueOf(jTable.getValueAt(0, columnIndex)));
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING));
                    sorter.setSortKeys(sortKeys);
                    sorter.setComparator(columnIndex, (Double a, Double b) -> a.compareTo(b));
                    isDouble = true;
                } catch (NumberFormatException ex) {
                    try {
                        //If the column is a Double but needs " USD" trimmed off...
                        String val = String.valueOf(jTable.getValueAt(0, columnIndex));
                        val = val.substring(0, val.length() - 4);
                        Double.parseDouble(val);
                        jTable.setValueAt(val, 0, columnIndex);
                        jTable.getRowCount();
                        jTable.getModel().getRowCount();

                        for (int i = 1; i < jTable.getRowCount(); i++) {
                            val = String.valueOf(jTable.getValueAt(i, columnIndex));
                            val = val.substring(0, val.length() - 4);
                            jTable.setValueAt(val, i, columnIndex);
                        }
                        sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING));
                        sorter.setSortKeys(sortKeys);
                        sorter.setComparator(columnIndex, (Double a, Double b) -> a.compareTo(b));
                        isDouble = true;
                    } catch (NumberFormatException exc) {
                        //Isn't a Double
                    }
                } catch (Exception ex) {
                    
                }
                
                if (!isDouble) {
                    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
                    sorter.setSortKeys(sortKeys);
                    try {
                        //If the column is a Double (stored as String to show extra 0s)
                        Integer.parseInt((String)jTable.getValueAt(0, columnIndex));
                        sorter.setComparator(columnIndex, (Integer a, Integer b) -> a.compareTo(b));
                    } catch (NumberFormatException ex) {
                        //Not an Integer, treat as String
                        sorter.setComparator(columnIndex, (String a, String b) -> a.compareTo(b));
                    }
                }
                sorter.sort();
                if (isDouble) {
                    
                }*/
            }
        });
    }
}
