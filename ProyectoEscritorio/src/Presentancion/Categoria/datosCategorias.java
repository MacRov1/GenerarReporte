/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import logica.servicios.CategoriaServicios;
import logica.Clases.Categoria;

/**
 *
 * @author UnwantedOpinion
 */
public class datosCategorias extends javax.swing.JFrame {

    private Timer timer;
    private TableRowSorter<DefaultTableModel> sorter;
    
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
        
        // Agregar un listener para la tabla que activa los botones al seleccionar una fila
        tblListarCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Solo procesar si la selección ha cambiado
                int filaSeleccionada = tblListarCategorias.getSelectedRow();
                boolean seleccionValida = filaSeleccionada >= 0;

                // Habilitar o deshabilitar botones según la selección
                btnModCategoria.setEnabled(seleccionValida);
                btnDeshCategoria.setEnabled(seleccionValida);
            }
        });
        
        // Inicializar el Timer
        timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solo actualiza si el filtro "Todos" está seleccionado y no hay texto en el JTextField
                if (cbFiltros.getSelectedIndex() == 0 && txtBBusqueda.getText().trim().isEmpty()) {
                    cargarDatos(); // Llama al método para recargar datos
                }
            }
        });
        timer.start(); // Iniciar el timer

        // Agregar filtros
        agregarFiltros();
    }
    
    private void agregarFiltros() {
        txtBBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                pausarActualizacion(); // Pausa el timer al buscar
                aplicarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                pausarActualizacion(); // Pausa el timer al buscar
                aplicarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                pausarActualizacion(); // Pausa el timer al buscar
                aplicarFiltro();
            }
        });

        cbFiltros.addActionListener(e -> {
            pausarActualizacion(); // Pausa el timer al cambiar el filtro
            aplicarFiltro();
        });
    }

    private void pausarActualizacion() {
        timer.stop(); // Detener el timer
    }

    private void reanudarActualizacion() {
        // Comprobar las condiciones antes de reiniciar el timer
        if (cbFiltros.getSelectedIndex() == 0 && txtBBusqueda.getText().trim().isEmpty()) {
            timer.start(); // Reiniciar el timer solo si las condiciones son adecuadas
        }
    }
    
    // Este método debe estar fuera de los Listeners para que sea accesible desde ambos
    private void aplicarFiltro() {
        String texto = txtBBusqueda.getText().trim();
        int filtroSeleccionado = cbFiltros.getSelectedIndex();  // Índice de la opción seleccionada en el ComboBox

        if (filtroSeleccionado == 0) {
            // Buscar en todas las columnas de manera sensible a mayúsculas y minúsculas
            sorter.setRowFilter(RowFilter.regexFilter(texto));
        } else {
            // Buscar en una columna específica de manera sensible a mayúsculas y minúsculas
            int columna = filtroSeleccionado - 1;  // Restar 1 porque el índice "Todos" es 0
            sorter.setRowFilter(RowFilter.regexFilter(texto, columna));
        }
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
        
        //configuramos el TableRowSorter
        sorter = new TableRowSorter<>(modelo);
        tblListarCategorias.setRowSorter(sorter);

        //creamos un renderizador que centre el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblListarCategorias.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
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

        jLabel2 = new javax.swing.JLabel();
        txtBBusqueda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbFiltros = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarCategorias = new javax.swing.JTable();
        btnAltaCategoria = new javax.swing.JButton();
        btnModCategoria = new javax.swing.JButton();
        btnDeshCategoria = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Buscar:");

        txtBBusqueda.setToolTipText("");
        txtBBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBBusquedaActionPerformed(evt);
            }
        });

        jLabel3.setText("Filtrar:");

        cbFiltros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "ID", "Nombre", "Descripcion", "Activo" }));
        cbFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltrosActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnAltaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeshCategoria)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAltaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeshCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
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

    private void txtBBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBBusquedaActionPerformed

    private void cbFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFiltrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFiltrosActionPerformed

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
    private javax.swing.JButton btnAltaCategoria;
    private javax.swing.JButton btnDeshCategoria;
    private javax.swing.JButton btnModCategoria;
    private javax.swing.JComboBox<String> cbFiltros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListarCategorias;
    private javax.swing.JTextField txtBBusqueda;
    // End of variables declaration//GEN-END:variables
}
