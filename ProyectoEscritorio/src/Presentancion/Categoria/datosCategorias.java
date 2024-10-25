/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Categoria;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.servicios.CategoriaServicios;
import logica.Clases.Categoria;

/**
 *
 * @author UnwantedOpinion
 */
public class datosCategorias extends javax.swing.JFrame {

    /**
     * Creates new form datosCategorias
     */
    public datosCategorias() {
        initComponents();
        this.setTitle("Datos de Categorias");
        this.setLocationRelativeTo(null);
        cargarDatos();//llama al método para llenar la tabla
       
        //manejamos el evento de cierre de la ventana
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //codigo que se ejecuta al cerrar la ventana
                manejoCiereVentana();
            }
        });
        
        //deshabilitamos los botones mod y elim
        btnModCategoria.setEnabled(false);
        btnDeshCategoria.setEnabled(false);
        
        //agregamos un listener para la tabla que active los botones al seleccionar una fila
        tblListarCategorias.getSelectionModel().addListSelectionListener(e -> {
            //si hay una fila seleccionada, habilitar los botones
            boolean seleccionValida = tblListarCategorias.getSelectedRow() >= 0;
            btnModCategoria.setEnabled(seleccionValida);
            btnDeshCategoria.setEnabled(seleccionValida);
        });
    }
    
    private void manejoCiereVentana() {
       //cierra la ventana actual (datosCategorias)
       this.dispose();
    }
    
    public void cargarDatos() {
        CategoriaServicios categoriaServicios = new CategoriaServicios();
        ArrayList<Categoria> categorias = categoriaServicios.listarCategorias();

        //obtenemos el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblListarCategorias.getModel();
        
        //limpiamos la tabla antes de agregar los nuevos datos
        modelo.setRowCount(0);

        //agregamos filas a la tabla
        for (Categoria categoria : categorias) {
            modelo.addRow(new Object[]{
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getActivo() == true ? "Sí" : "No"
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarCategorias = new javax.swing.JTable();
        btnAltaCategoria = new javax.swing.JButton();
        btnModCategoria = new javax.swing.JButton();
        btnDeshCategoria = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("TABLA DE CATEGORIAS");

        tblListarCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Descripcion", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblListarCategorias);
        if (tblListarCategorias.getColumnModel().getColumnCount() > 0) {
            tblListarCategorias.getColumnModel().getColumn(0).setMinWidth(50);
            tblListarCategorias.getColumnModel().getColumn(0).setMaxWidth(60);
            tblListarCategorias.getColumnModel().getColumn(1).setMinWidth(200);
            tblListarCategorias.getColumnModel().getColumn(1).setMaxWidth(250);
            tblListarCategorias.getColumnModel().getColumn(3).setMinWidth(30);
            tblListarCategorias.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        btnAltaCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAltaCategoria.setText("Añadir Categoria");
        btnAltaCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaCategoriaActionPerformed(evt);
            }
        });

        btnModCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnModCategoria.setText("Modificar Categoria");
        btnModCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModCategoriaActionPerformed(evt);
            }
        });

        btnDeshCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnDeshCategoria.setText("Deshabilitar Categoria");
        btnDeshCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeshCategoriaActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-update-24.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)
                        .addComponent(btnActualizar)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnAltaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeshCategoria)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnActualizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAltaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeshCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAltaCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaCategoriaActionPerformed
        //crea una nueva instancia de la ventana aniadirCategoria
        aniadirCategoria ventanaAniadirCategoria = new aniadirCategoria();
    
        //hace que la ventana sea visible
        ventanaAniadirCategoria.setVisible(true);
    }//GEN-LAST:event_btnAltaCategoriaActionPerformed

    private void btnModCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModCategoriaActionPerformed
        int filaSeleccionada = tblListarCategorias.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int id = (Integer) tblListarCategorias.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tblListarCategorias.getValueAt(filaSeleccionada, 1);
            String descripcion = (String) tblListarCategorias.getValueAt(filaSeleccionada, 2);
            String activoStr = (String) tblListarCategorias.getValueAt(filaSeleccionada, 3);

            //comparamos con equals() y convertimos a boolean
            Boolean activo = activoStr.equals("Sí");

            int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea modificar esta categoría?", 
                    "Confirmar Modificación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                modificarCategoria ventanaModificacion = new modificarCategoria();
                ventanaModificacion.setId(id);
                ventanaModificacion.setNombre(nombre);
                ventanaModificacion.setDescripcion(descripcion);
                ventanaModificacion.setActivo(activo);
                ventanaModificacion.setVisible(true);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría para modificar.");
        }
    }//GEN-LAST:event_btnModCategoriaActionPerformed

    private void btnDeshCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeshCategoriaActionPerformed
        int selectedRow = tblListarCategorias.getSelectedRow();

        if (selectedRow >= 0) {
            int id = (Integer) tblListarCategorias.getValueAt(selectedRow, 0);

            CategoriaServicios servicios = new CategoriaServicios();

            //verificamos si hay productos asociados con esta categoría
            boolean tieneProductos = servicios.categoriaTieneProductos(id);

            if (tieneProductos) {
                JOptionPane.showMessageDialog(this, 
                        "Esta categoría tiene productos asociados.\n" +
                        "Asegúrese de reasignar estos productos a otra categoría activa antes de continuar.",
                        "Advertencia de Productos Asociados",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            //confirmamos la deshabilitación
            int confirmar = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea deshabilitar esta categoría? Esta acción la hará inactiva.", 
                    "Confirmar Deshabilitación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                boolean exito = servicios.deshabilitarCategoria(id);

                if (exito) {
                    JOptionPane.showMessageDialog(this, 
                            "Categoría deshabilitada exitosamente.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Error al deshabilitar la categoría. Por favor, inténtelo de nuevo.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar una categoría para deshabilitar.", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDeshCategoriaActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        cargarDatos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(datosCategorias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(datosCategorias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(datosCategorias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(datosCategorias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new datosCategorias().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAltaCategoria;
    private javax.swing.JButton btnDeshCategoria;
    private javax.swing.JButton btnModCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListarCategorias;
    // End of variables declaration//GEN-END:variables
}