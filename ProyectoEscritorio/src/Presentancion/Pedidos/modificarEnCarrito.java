/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Pedidos;

import Presentancion.Pedidos.AddPedido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import logica.Clases.Categoria;
import logica.Clases.Producto;
import logica.servicios.CategoriaServicios;
import logica.servicios.ProductoServicios;

/**
 *
 * @author UnwantedOpinion
 */
public class modificarEnCarrito extends javax.swing.JFrame {

    private AddPedido addPedidoFrame;
    private ActualizarPedido actualizarPedidoFrame;
    private double precioProducto;
    private int selectedRow;
    
    /**
     * Creates new form modificarEnCarrito
     */
    public modificarEnCarrito() {
        
        initComponents();
        cargarCategorias();
        cargarProductos();
        configurarListeners();
        this.setLocationRelativeTo(null);

                setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                    addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            //codigo que se ejecuta al cerrar la ventana
                            manejoCiereVentana();
                        }
                    });
                    txtAreaDescripcion.setEnabled(false);
                    txtSubtotal.setEnabled(false);
        }

    private void manejoCiereVentana() {
           //cierra la ventana actual ()
           this.dispose();
        }
    
    public void setAddPedidoFrame(AddPedido addPedidoFrame) {
        this.addPedidoFrame = addPedidoFrame;
    }
    
    public void setActualizarPedidoFrame(ActualizarPedido actualizarPedidoFrame) {
        this.actualizarPedidoFrame = actualizarPedidoFrame;
    }

    private void cargarProductos() {
        CbNombreProductos.addItem("--Selecciona un producto--");
        ProductoServicios productoServicios = new ProductoServicios();
        List<Producto> productos = productoServicios.listarProductos();
        for (Producto producto : productos) {
            CbNombreProductos.addItem(producto.getNombre());
        }
    }
    
    private void cargarCategorias() {
        CbNombreCategorias.addItem("--Selecciona una categoría--");
        CategoriaServicios categoriaServicios = new CategoriaServicios();
        List<Categoria> categorias = categoriaServicios.listarCategorias();
        for (Categoria categoria : categorias) {
            CbNombreCategorias.addItem(categoria.getNombre());
        }
    }
    
    private void configurarListeners() {
        CbNombreProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDescripcionYPrecio();
            }
        });

        CbNombreCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoriaSeleccionada = (String) CbNombreCategorias.getSelectedItem();
                if (!categoriaSeleccionada.equals("--Selecciona una categoría--")) {
                    //llamamos al método para cargar los productos de la categoría seleccionada
                    cargarProductosPorCategoria(categoriaSeleccionada, "");
                }
            }
        });

        txtCantidad.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }
        });
    }
    
    public void setDatos(String producto, float precioUnidad, int cantidad, float subtotal, int row) {
        //selecciona la categoría del producto en cbNombreCategorias
        ProductoServicios productoServicios = new ProductoServicios();
        Producto prod = productoServicios.buscarProductoPorNombre(producto);

        if (prod != null) {
            //selecciona la categoría en cbNombreCategorias
            Categoria categoria = prod.getCategoria();
            CbNombreCategorias.setSelectedItem(categoria.getNombre());

            //cargar los productos de la misma categoría
            cargarProductosPorCategoria(categoria.getNombre(), producto);
        }

        txtCantidad.setText(String.valueOf(cantidad));
        txtSubtotal.setText(String.valueOf(subtotal));
        this.selectedRow = row;
        this.precioProducto = precioUnidad;

        if (prod != null) {
            txtAreaDescripcion.setText(prod.getDescripcion());
        } else {
            txtAreaDescripcion.setText("Descripción no disponible");
        }
    }
    
    private void cargarProductosPorCategoria(String categoriaNombre, String productoSeleccionado) {
        CbNombreProductos.removeAllItems();
    
        ProductoServicios productoServicios = new ProductoServicios();
        CategoriaServicios categoriaServicios = new CategoriaServicios();

        Categoria categoria = categoriaServicios.buscarCategoriaPorNombre(categoriaNombre);
        if (categoria != null) {
            List<Producto> productos = productoServicios.listarProductosPorCategoria(categoriaNombre);

            if (!productoSeleccionado.isEmpty()) {
                //añadimos primero el producto seleccionado
                CbNombreProductos.addItem(productoSeleccionado);
            } else {
                CbNombreProductos.addItem("--Selecciona un producto--");
            }

            //añadimos los demás productos de la categoría, excluyendo el producto seleccionado
            for (Producto producto : productos) {
                if (!producto.getNombre().equals(productoSeleccionado)) {
                    CbNombreProductos.addItem(producto.getNombre());
                }
            }
        }
    }

    
    private void actualizarDescripcionYPrecio() {
        String nombreProducto = (String) CbNombreProductos.getSelectedItem();
        ProductoServicios productoServicios = new ProductoServicios();
        Producto producto = productoServicios.buscarProductoPorNombre(nombreProducto);

        if (producto != null) {
            txtAreaDescripcion.setText(producto.getDescripcion());
            precioProducto = producto.getPrecioVenta();
            actualizarSubtotal();
        }
    }
    
    private void actualizarSubtotal() {
        String cantidadText = txtCantidad.getText();
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(cantidadText);
            if (cantidad < 1) {
                cantidad = 0;
            }
        } catch (NumberFormatException e) {
            cantidad = 0;
        }
        txtSubtotal.setText(String.format("%.2f", precioProducto * cantidad));
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
        jLabel7 = new javax.swing.JLabel();
        CbNombreProductos = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        SPanelDescripcion = new javax.swing.JScrollPane();
        txtAreaDescripcion = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        CbNombreCategorias = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("MODIFICAR PEDIDO");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Producto:");

        CbNombreProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbNombreProductosActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Descripcion:");

        txtAreaDescripcion.setColumns(20);
        txtAreaDescripcion.setRows(5);
        SPanelDescripcion.setViewportView(txtAreaDescripcion);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Cantidad:");

        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Subtotal:");

        txtSubtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubtotalActionPerformed(evt);
            }
        });

        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-checkmark-32.png"))); // NOI18N
        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        CbNombreCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbNombreCategoriasActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Categoria:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(107, 107, 107))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(52, 52, 52)
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CbNombreProductos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SPanelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CbNombreCategorias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(31, 31, 31))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CbNombreCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CbNombreProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(SPanelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CbNombreProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbNombreProductosActionPerformed
        actualizarDescripcionYPrecio();
    }//GEN-LAST:event_CbNombreProductosActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void txtSubtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubtotalActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        //obtenemos nombre del producto seleccionado
        String producto = (String) CbNombreProductos.getSelectedItem();
    
        //validamos el nombre del producto
        if (producto == null || producto.equals("--Selecciona un producto--")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un producto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //obtenemos y validamos la cantidad
        String cantidadText = txtCantidad.getText().trim();
        int cantidad = 0;
        if (cantidadText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo de cantidad no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            cantidad = Integer.parseInt(cantidadText);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //obtenemos y validamos el subtotal
        String subtotalText = txtSubtotal.getText().trim();
        float subtotal = 0;
        if (subtotalText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El subtotal no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            subtotal = Float.parseFloat(subtotalText.replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El subtotal no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //obtenemos el precio por unidad o de venta del producto
        float precioUnidad = (float) precioProducto;
        
        if (addPedidoFrame != null) {
            addPedidoFrame.actualizarFila(selectedRow, producto, precioUnidad, cantidad, subtotal);
        }
    
        if (actualizarPedidoFrame != null) {
            actualizarPedidoFrame.actualizarFila(selectedRow, producto, precioUnidad, cantidad, subtotal);
        }
        
        this.dispose();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas cancelar?",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION);

        if(confirmar == JOptionPane.YES_OPTION) {
            //cerramos la ventana actual
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void CbNombreCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbNombreCategoriasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbNombreCategoriasActionPerformed

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
            java.util.logging.Logger.getLogger(modificarEnCarrito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(modificarEnCarrito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(modificarEnCarrito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(modificarEnCarrito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new modificarEnCarrito().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbNombreCategorias;
    private javax.swing.JComboBox<String> CbNombreProductos;
    private javax.swing.JScrollPane SPanelDescripcion;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextArea txtAreaDescripcion;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtSubtotal;
    // End of variables declaration//GEN-END:variables
}
