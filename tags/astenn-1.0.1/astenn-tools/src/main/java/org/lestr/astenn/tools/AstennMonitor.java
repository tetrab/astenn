/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AstennMonitor.java
 *
 * Created on 10 sept. 2010, 11:24:00
 */

package org.lestr.astenn.tools;

import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.plugin.IPersistenceDriver;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PIBONNIN
 */
public class AstennMonitor extends javax.swing.JFrame {

    /** Creates new form AstennMonitor */
    public AstennMonitor() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pluginsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jToolBar1.setRollover(true);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("META-INF/Messages"); // NOI18N
        jButton1.setText(bundle.getString("Refresh")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        pluginsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Interface", "Implementation"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(pluginsTable);
        pluginsTable.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("Interface")); // NOI18N
        pluginsTable.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("Implementation")); // NOI18N

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(bundle.getString("Plugins"), jPanel1); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("Servers"), jPanel2); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        refresh();

    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        refresh();

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AstennMonitor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable pluginsTable;
    // End of variables declaration//GEN-END:variables


    private void refresh() {

        IPersistenceDriver persistenceDriver = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver();

        DefaultTableModel model = (DefaultTableModel) pluginsTable.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);

        if (persistenceDriver != null)
            for (String interfaceName : persistenceDriver.getPluginInterfacesNames())
                for (String pluginImplementationAddress : persistenceDriver.getPluginImplementationsAddresses(interfaceName))
                    if(pluginImplementationAddress != null)
                        model.addRow(new Object[]{interfaceName, pluginImplementationAddress.toString()});

        pluginsTable.setModel(model);

    }// END Method refresh


}// END Class AstennMonitor