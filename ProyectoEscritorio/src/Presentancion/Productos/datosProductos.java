/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Productos;

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
import logica.Clases.Producto;
import logica.servicios.ProductoServicios;


/**
 *
 * @author UnwantedOpinion
 */
public class datosProductos extends javax.swing.JFrame {

    private Timer timer;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public datosProductos() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Datos de Productos");
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
        btnModProducto.setEnabled(false);
        btnDeshProducto.setEnabled(false);
        
        //agregamos un listener para la tabla que active los botones al seleccionar una fila
        tblListarProductos.getSelectionModel().addListSelectionListener(e -> {
            //si hay una fila seleccionada, habilitar los botones
            boolean seleccionValida = tblListarProductos.getSelectedRow() >= 0;
            btnModProducto.setEnabled(seleccionValida);
            btnDeshProducto.setEnabled(seleccionValida);
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
       //cierra la ventana actual (datosVendedores)
       this.dispose();
    }
    
    public void cargarDatos() {
        ProductoServicios productoServicios = new ProductoServicios();
        ArrayList<Producto> productos = productoServicios.listarProductos();

        //obtenemos el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblListarProductos.getModel();
        
        //configuramos el TableRowSorter
        sorter = new TableRowSorter<>(modelo);
        tblListarProductos.setRowSorter(sorter);

        //creamos un renderizador que centre el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblListarProductos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        //limpiamos la tabla antes de agregar los nuevos datos
        modelo.setRowCount(0);

        //agregamos filas a la tabla
        for (Producto producto : productos) {
            modelo.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getSKU(),
                producto.getPrecioVenta(),
                producto.getStock(),
                producto.getCategoria().getNombre(),
                producto.getActivo() == true ? "Sí" : "No"
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
        tblListarProductos = new javax.swing.JTable();
        btnAltaProducto = new javax.swing.JButton();
        btnModProducto = new javax.swing.JButton();
        btnDeshProducto = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtBBusqueda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbFiltros = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("TABLA DE PRODUCTOS");

        tblListarProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Descripcion", "SKU", "Precio", "Stock", "Categoria", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblListarProductos);
        if (tblListarProductos.getColumnModel().getColumnCount() > 0) {
            tblListarProductos.getColumnModel().getColumn(0).setMinWidth(50);
            tblListarProductos.getColumnModel().getColumn(0).setMaxWidth(60);
            tblListarProductos.getColumnModel().getColumn(2).setMinWidth(250);
            tblListarProductos.getColumnModel().getColumn(2).setMaxWidth(260);
            tblListarProductos.getColumnModel().getColumn(3).setMinWidth(100);
            tblListarProductos.getColumnModel().getColumn(3).setMaxWidth(110);
            tblListarProductos.getColumnModel().getColumn(4).setMinWidth(50);
            tblListarProductos.getColumnModel().getColumn(4).setMaxWidth(60);
            tblListarProductos.getColumnModel().getColumn(5).setMinWidth(60);
            tblListarProductos.getColumnModel().getColumn(5).setMaxWidth(70);
            tblListarProductos.getColumnModel().getColumn(7).setMinWidth(50);
            tblListarProductos.getColumnModel().getColumn(7).setPreferredWidth(30);
        }

        btnAltaProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAltaProducto.setText("Añadir Producto");
        btnAltaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaProductoActionPerformed(evt);
            }
        });

        btnModProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnModProducto.setText("Modificar Producto");
        btnModProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModProductoActionPerformed(evt);
            }
        });

        btnDeshProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnDeshProducto.setText("Deshabilitar Producto");
        btnDeshProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeshProductoActionPerformed(evt);
            }
        });

        jLabel2.setText("Buscar:");

        txtBBusqueda.setToolTipText("");
        txtBBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBBusquedaActionPerformed(evt);
            }
        });

        jLabel3.setText("Filtrar:");

        cbFiltros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "ID", "Nombre", "Descripcion", "SKU", "Precio", "Stock", "Categoria", "Activo" }));
        cbFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(btnAltaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(btnModProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnDeshProducto)
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(268, 268, 268))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(149, 149, 149))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAltaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeshProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAltaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaProductoActionPerformed
        //crea una nueva instancia de la ventana aniadirProducto
        aniadirProducto ventanaAniadirProducto = new aniadirProducto();
    
        //hace que la ventana sea visible
        ventanaAniadirProducto.setVisible(true);
    }//GEN-LAST:event_btnAltaProductoActionPerformed

    private void btnModProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModProductoActionPerformed
        int filaSeleccionada = tblListarProductos.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int id = (Integer) tblListarProductos.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tblListarProductos.getValueAt(filaSeleccionada, 1);
            String descripcion = (String) tblListarProductos.getValueAt(filaSeleccionada, 2);
            String sku = (String) tblListarProductos.getValueAt(filaSeleccionada, 3);
            float precioVenta = (Float) tblListarProductos.getValueAt(filaSeleccionada, 4);
            int stock = (Integer) tblListarProductos.getValueAt(filaSeleccionada, 5);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea modificar este vendedor?", 
                "Confirmar Modificación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            modificarProducto ventanaModificacion = new modificarProducto();
            ventanaModificacion.setId(id);
            ventanaModificacion.setNombre(nombre);
            ventanaModificacion.setDescripcion(descripcion);
            ventanaModificacion.setSKU(sku);
            ventanaModificacion.setPrecioVenta(precioVenta);
            ventanaModificacion.setStock(stock);
            ventanaModificacion.setVisible(true);
        }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para modificar.");
        }
    }//GEN-LAST:event_btnModProductoActionPerformed

    private void btnDeshProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeshProductoActionPerformed
        int selectedRow = tblListarProductos.getSelectedRow();

        if (selectedRow >= 0) {
            int id = (Integer) tblListarProductos.getValueAt(selectedRow, 0);

            ProductoServicios productoServicios = new ProductoServicios();

            //confirmamos la deshabilitación
            int confirmar = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea deshabilitar este producto?", 
                    "Confirmar Deshabilitación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                boolean exito = productoServicios.deshabilitarProducto(id);

                if (exito) {
                    JOptionPane.showMessageDialog(this, 
                            "Producto deshabilitado exitosamente.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Error al deshabilitar el producto.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un producto para deshabilitar.", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDeshProductoActionPerformed

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
            java.util.logging.Logger.getLogger(datosProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(datosProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(datosProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(datosProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new datosProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltaProducto;
    private javax.swing.JButton btnDeshProducto;
    private javax.swing.JButton btnModProducto;
    private javax.swing.JComboBox<String> cbFiltros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListarProductos;
    private javax.swing.JTextField txtBBusqueda;
    // End of variables declaration//GEN-END:variables
}
