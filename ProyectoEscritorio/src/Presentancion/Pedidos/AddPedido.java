/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Pedidos;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import logica.Clases.DetallePedido;
import logica.Clases.Pedido;
import logica.Clases.Producto;
import logica.Fabrica;
import logica.Interfaces.IControladorPedido;
import logica.servicios.ClienteServicios;
import logica.servicios.PedidosServicios;
import logica.servicios.VendedorServicios;
import logica.servicios.ProductoServicios;

/**
 *
 * @author Mateo
 */
public class AddPedido extends javax.swing.JFrame {

    private PedidosServicios pedidosServicios;
    private VendedorServicios vendedorServicios;
    private ClienteServicios clienteServicios;
    private ProductoServicios productoServicios;
    private IControladorPedido ICP;
    

    public AddPedido() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.vendedorServicios = new VendedorServicios();
        this.clienteServicios = new ClienteServicios();
        this.productoServicios = new ProductoServicios();
        this.pedidosServicios = new PedidosServicios();
        this.ICP = Fabrica.getInstance().getIControladorPedido();
    
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //codigo que se ejecuta al cerrar la ventana
                    manejoCiereVentana();
                }
            });
            txtPrecioTotal.setEnabled(false);
            cargarNombres();
            
            //deshabilitamos los botones quitar, modificar y limpiar
            btnQuitar.setEnabled(false);
            btnModificar.setEnabled(false);
            btnLimpiarTabla.setEnabled(false);

                JtableCarrito.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    btnQuitar.setEnabled(JtableCarrito.getSelectedRow() != -1);
                    btnModificar.setEnabled(JtableCarrito.getSelectedRow() != -1);
                }
            });
            
            ((DefaultTableModel) JtableCarrito.getModel()).addTableModelListener(e -> {
                btnLimpiarTabla.setEnabled(JtableCarrito.getRowCount() > 0);
            });
            
            //añadimos un TableModelListener a la tabla para actualizar el total
            ((DefaultTableModel) JtableCarrito.getModel()).addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    // Solo actualiza si la tabla cambia
                    actualizarPrecioTotal();
                }
            });
        }
    
        private void manejoCiereVentana() {
           //limpiamos la tabla y cerramos la ventana actual ()
           ((DefaultTableModel) JtableCarrito.getModel()).setRowCount(0);
           this.dispose();
        }
        
        private void cargarNombres() {
        CbNombreVendedor.addItem("--Selecciona un vendedor--");
        CbNombreCliente.addItem("--Selecciona un cliente--");
        List<String> nombresVendedores = vendedorServicios.obtenerNombresVendedoresActivos();
        List<String> nombresClientes = clienteServicios.obtenerNombresClientesActivos();

        for (String nombre : nombresVendedores) {
            CbNombreVendedor.addItem(nombre);
        }

        for (String nombre : nombresClientes) {
            CbNombreCliente.addItem(nombre);
        }
    }
    
    //funcion que se usa en añadirAlCarrito para traer los datos acá
    public void agregarProductoATabla(String producto, float precioUnidad, int cantidad, float subtotal) {
        DefaultTableModel model = (DefaultTableModel) JtableCarrito.getModel();
        boolean productoExistente = false;

        //itera sobre las filas existentes en la tabla para verificar si el producto ya está agregado
        for (int i = 0; i < model.getRowCount(); i++) {
            String productoEnTabla = (String) model.getValueAt(i, 0);
            if (productoEnTabla.equals(producto)) {
                //si el producto ya existe, actualiza la cantidad y el subtotal
                int cantidadActual = (int) model.getValueAt(i, 2);
                float subtotalActual = (float) model.getValueAt(i, 3);

                //sumamos la cantidad nueva a la existente
                int nuevaCantidad = cantidadActual + cantidad;
                float nuevoSubtotal = subtotalActual + subtotal;

                //actualizamos la fila con la nueva cantidad y subtotal
                model.setValueAt(nuevaCantidad, i, 2);//actualiza la cantidad
                model.setValueAt(nuevoSubtotal, i, 3);//actualiza el subtotal

                productoExistente = true;
                break;
            }
        }

        //si el producto no existe en la tabla, lo agregamos como una nueva fila
        if (!productoExistente) {
            model.addRow(new Object[]{producto, precioUnidad, cantidad, subtotal});
        }
    }

    //auxiliar para actualizar el precio total
    private void actualizarPrecioTotal() {
        DefaultTableModel model = (DefaultTableModel) JtableCarrito.getModel();
        float total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Float subtotal = (Float) model.getValueAt(i, 3); // Columna "Subtotal"
            if (subtotal != null) {
                total += subtotal;
            }
        }

        txtPrecioTotal.setText(String.format("%.2f", total));
    }
    
    public void actualizarFila(int row, String producto, float precioUnidad, int cantidad, float subtotal) {
        DefaultTableModel model = (DefaultTableModel) JtableCarrito.getModel();

        model.setValueAt(producto, row, 0);
        model.setValueAt(precioUnidad, row, 1);
        model.setValueAt(cantidad, row, 2);
        model.setValueAt(subtotal, row, 3);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CbNombreVendedor = new javax.swing.JComboBox<>();
        CbNombreCliente = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JtableCarrito = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtPrecioTotal = new javax.swing.JTextField();
        btnAñadir = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        btnLimpiarTabla = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("AÑADIR PEDIDO");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Vendedor:");

        CbNombreVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbNombreVendedorActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Cliente:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Carrito:");

        JtableCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Precio Unidad", "Cantidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(JtableCarrito);
        if (JtableCarrito.getColumnModel().getColumnCount() > 0) {
            JtableCarrito.getColumnModel().getColumn(0).setMinWidth(120);
            JtableCarrito.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Precio Total:");

        txtPrecioTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioTotalActionPerformed(evt);
            }
        });

        btnAñadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAñadir.setText("Añadir");
        btnAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAñadirActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnLimpiarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-broom-32 (1).png"))); // NOI18N
        btnLimpiarTabla.setText("Limpiar");
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CbNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CbNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnAñadir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnQuitar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpiarTabla))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(202, 202, 202))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(188, 188, 188)))
                        .addGap(109, 109, 109))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(CbNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(CbNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAñadir)
                    .addComponent(btnModificar)
                    .addComponent(btnQuitar)
                    .addComponent(btnLimpiarTabla))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPrecioTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioTotalActionPerformed

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        añadirAlCarrito añadirCarritoFrame = new añadirAlCarrito();
        añadirCarritoFrame.setAddPedidoFrame(this);
        añadirCarritoFrame.setVisible(true);
    }//GEN-LAST:event_btnAñadirActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int selectedRow = JtableCarrito.getSelectedRow();
        if (selectedRow != -1) {
            String producto = (String) JtableCarrito.getValueAt(selectedRow, 0);
            float precioUnidad = (Float) JtableCarrito.getValueAt(selectedRow, 1);
            int cantidad = (Integer) JtableCarrito.getValueAt(selectedRow, 2);
            float subtotal = (Float) JtableCarrito.getValueAt(selectedRow, 3);

            modificarEnCarrito modificarFrame = new modificarEnCarrito();
            modificarFrame.setAddPedidoFrame(this);
            modificarFrame.setDatos(producto, precioUnidad, cantidad, subtotal, selectedRow);
            modificarFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar.");
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        int filaSeleccionada = JtableCarrito.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar el producto seleccionado?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                ((DefaultTableModel) JtableCarrito.getModel()).removeRow(filaSeleccionada);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecciona una fila para eliminar.",
                "Selección Requerida",
                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTablaActionPerformed
        ((DefaultTableModel) JtableCarrito.getModel()).setRowCount(0);
        btnLimpiarTabla.setEnabled(false);
    }//GEN-LAST:event_btnLimpiarTablaActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
         try {
            //verifica si la tabla tiene al menos una fila
            if (JtableCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío. Añade al menos un producto antes de confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
             
            if (CbNombreVendedor.getSelectedIndex() == 0 || CbNombreCliente.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un vendedor y un cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //obtenemos ID del vendedor y del cliente seleccionados
            String nombreVendedor = (String) CbNombreVendedor.getSelectedItem();
            String nombreCliente = (String) CbNombreCliente.getSelectedItem();

            int idVendedor = vendedorServicios.obtenerIdVendedorPorNombre(nombreVendedor);
            int idCliente = clienteServicios.obtenerIdClientePorNombre(nombreCliente);

            //creamos un nuevo objeto Pedido
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setFechaPedido(new java.util.Date());
            nuevoPedido.setEstado(Pedido.Estado.EN_PREPARACION);
            nuevoPedido.setIdVendedor(idVendedor);
            nuevoPedido.setIdCliente(idCliente);

            //creamos los detalles del pedido
            for (int i = 0; i < JtableCarrito.getRowCount(); i++) {
                // Obtener el nombre del producto
                String nombreProducto = (String) JtableCarrito.getValueAt(i, 0);
                Producto producto = productoServicios.buscarProductoPorNombre(nombreProducto);
                if (producto == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado: " + nombreProducto, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                float precioVenta = (float) JtableCarrito.getValueAt(i, 1);
                int cantidad = (int) JtableCarrito.getValueAt(i, 2);

                DetallePedido detalle = new DetallePedido(cantidad, precioVenta, producto, nuevoPedido.getIdentificador());
                nuevoPedido.agregarDetalle(detalle);
            }

            //agregamos el pedido a la base de datos
            boolean resultado = pedidosServicios.agregarPedido(nuevoPedido);

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Pedido añadido exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al añadir el pedido. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error al confirmar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas cancelar? Los cambios no guardados se perderán.",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION);

        if(confirmar == JOptionPane.YES_OPTION) {
            //cerramos la ventana actual
            ((DefaultTableModel) JtableCarrito.getModel()).setRowCount(0);
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void CbNombreVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbNombreVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbNombreVendedorActionPerformed

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
            java.util.logging.Logger.getLogger(AddPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddPedido().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbNombreCliente;
    private javax.swing.JComboBox<String> CbNombreVendedor;
    private javax.swing.JTable JtableCarrito;
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtPrecioTotal;
    // End of variables declaration//GEN-END:variables
}
