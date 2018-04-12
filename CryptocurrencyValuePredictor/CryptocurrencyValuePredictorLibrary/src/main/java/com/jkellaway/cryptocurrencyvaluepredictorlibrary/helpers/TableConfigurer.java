/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
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
    
    /**
     * Configures the JTable to select a single entire row. Also sorts the
     * JTable when the table header is clicked on.
     * @param jTable The JTable to configure.
     */
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
                int index = jTable.convertColumnIndexToModel(jTable.columnAtPoint(e.getPoint()));
                jTable.setAutoCreateRowSorter(true);
                
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
                jTable.setRowSorter(sorter);
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(new RowSorter.SortKey(index, SortOrder.DESCENDING));
                sorter.setSortKeys(sortKeys);
                
                try {
                    sorter.setComparator(index, (String a, String b) -> StringHelper.naturalNumberCompare(a, b));
                } catch (Exception ex) {
                    sorter.setComparator(index, (String a, String b) -> a.compareTo(b));
                }
                sorter.sort();
            }
        });
    }
}