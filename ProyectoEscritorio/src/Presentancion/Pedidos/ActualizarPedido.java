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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import logica.Clases.DetallePedido;
import logica.Clases.Pedido;
import logica.Clases.Pedido.Estado;
import logica.Clases.Producto;
import logica.Fabrica;
import logica.Interfaces.IControladorPedido;
import logica.servicios.ClienteServicios;
import logica.servicios.DetallePedidoServicios;
import logica.servicios.PedidosServicios;
import logica.servicios.VendedorServicios;
import logica.servicios.ProductoServicios;

public class ActualizarPedido extends javax.swing.JFrame {

    private PedidosServicios pedidosServicios;
    private VendedorServicios vendedorServicios;
    private ClienteServicios clienteServicios;
    private ProductoServicios productoServicios;
    private DetallePedidoServicios detallePedidos;
    private IControladorPedido ICP;
    private Pedido pedidoSeleccionado;

    public ActualizarPedido() {
        initComponents();
        this.setTitle("Gestion de Pedidos");
        this.setLocationRelativeTo(null);
        this.vendedorServicios = new VendedorServicios();
        this.clienteServicios = new ClienteServicios();
        this.productoServicios = new ProductoServicios();
        this.pedidosServicios = new PedidosServicios();
        this.detallePedidos = new DetallePedidoServicios();
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
        
        //ActionListener para gestionar el cambio de estado
        CbEstado.addActionListener(e -> {
            //obtenemos el estado seleccionado
            String estadoSeleccionado = (String) CbEstado.getSelectedItem();

            //desactivamos si el estado es "Cancelado" o "Entregado"
            if (estadoSeleccionado.equals("Cancelado") || estadoSeleccionado.equals("Entregado")) {
                //deseleccionamos la fila si había una seleccionada
                JtableCarrito.clearSelection();

                CbNombreVendedor.setEnabled(false);
                CbNombreCliente.setEnabled(false);
                JtableCarrito.setEnabled(false);
                btnAñadir.setEnabled(false);
                btnLimpiarTabla.setEnabled(false);
            } else if (!estadoSeleccionado.equals("--Selecciona un estado--")) {
                //activamos para cualquier otro estado
                CbNombreVendedor.setEnabled(true);
                CbNombreCliente.setEnabled(true);
                JtableCarrito.setEnabled(true);
                btnAñadir.setEnabled(true);

                //activamos el botón limpiar tabla solo si hay más de una fila
                btnLimpiarTabla.setEnabled(JtableCarrito.getRowCount() > 0);
            } else {
                btnLimpiarTabla.setEnabled(false);
            }
        });

        //deshabilitamos los botones quitar y modificar
        btnQuitar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnLimpiarTabla.setEnabled(false);

        //ListSelectionListener para actualizar botones de quitar y modificar
        JtableCarrito.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = JtableCarrito.getSelectedRow() != -1;
                btnQuitar.setEnabled(rowSelected);
                btnModificar.setEnabled(rowSelected);

                //activamos el botón limpiar tabla si hay más de una fila
                btnLimpiarTabla.setEnabled(JtableCarrito.getRowCount() > 0);
            }
        });

        //TableModelListener para habilitar o deshabilitar el botón limpiar tabla
        ((DefaultTableModel) JtableCarrito.getModel()).addTableModelListener(e -> {
            //desactivamos el botón limpiar tabla si se queda vacío
            btnLimpiarTabla.setEnabled(JtableCarrito.getRowCount() > 0 &&
                                       !CbEstado.getSelectedItem().equals("Cancelado") &&
                                       !CbEstado.getSelectedItem().equals("Entregado") &&
                                       !CbEstado.getSelectedItem().equals("--Selecciona un estado--"));
        });

        //lógica adicional para manejar el botón limpiar tabla si es necesario
        btnLimpiarTabla.addActionListener(e -> {
            //limpiamos la tabla
            ((DefaultTableModel) JtableCarrito.getModel()).setRowCount(0);

            //desactivamos el botón limpiar tabla tras limpiar
            btnLimpiarTabla.setEnabled(false);
        });

        //añadimos un TableModelListener a la tabla para actualizar el total
        ((DefaultTableModel) JtableCarrito.getModel()).addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //solo actualiza si la tabla cambia
                actualizarPrecioTotal();
            }
        });

    }

    private void manejoCiereVentana() {
        //cierra la ventana actual ()
        this.dispose();
    }

    //cargamos los nombres a los combobox
    private void cargarNombres() {
        CbNombreVendedor.addItem("--Selecciona un vendedor--");
        CbNombreCliente.addItem("--Selecciona un cliente--");
        
        List<String> nombresVendedores = vendedorServicios.obtenerNombresVendedores();
        List<String> nombresClientes = clienteServicios.obtenerNombresClientes();

        for (String nombre : nombresVendedores) {
            CbNombreVendedor.addItem(nombre);
        }

        for (String nombre : nombresClientes) {
            CbNombreCliente.addItem(nombre);
        }
    }
    
    //cargamos los estados (entregado y cancelado se hacen en terminar pedido)
    private void cargarEstado(Estado estado) {
        CbEstado.removeAllItems();
        CbEstado.addItem("--Selecciona un estado--");

        //agregamos las opciones según el estado del pedido
        if (estado == Estado.EN_PREPARACION) {
        CbEstado.addItem("En Preparacion");
        CbEstado.addItem("En Viaje");
        } else if (estado == Estado.EN_VIAJE) {
            CbEstado.addItem("En Preparacion");
            CbEstado.addItem("En Viaje");
        } else if (estado == Estado.ENTREGADO || estado == Estado.CANCELADO) {
            // Si el estado es entregado o cancelado, mostramos todas las opciones
            CbEstado.addItem("En Preparacion");
            CbEstado.addItem("En Viaje");
            CbEstado.addItem("Entregado");
            CbEstado.addItem("Cancelado");
        }
        //siempre establecer el estado actual del pedido
        String estadoFormateado = "";
        switch (estado) {
            case EN_PREPARACION:
                estadoFormateado = "En Preparacion";
                break;
            case EN_VIAJE:
                estadoFormateado = "En Viaje";
                break;
            case ENTREGADO:
                estadoFormateado = "Entregado";
                break;
            case CANCELADO:
                estadoFormateado = "Cancelado";
                break;
            default:
                estadoFormateado = "--Selecciona un estado--";
                break;
        }
        CbEstado.setSelectedItem(estadoFormateado);
    }
    
    //traemos el objeto pedido desde GestorPedidoUI
    public void setPedido(Pedido pedido) {
        this.pedidoSeleccionado = pedido;
        cargarDatosPedido();
    }
    
    
    private void cargarDatosPedido() {
        // Cargamos los datos del pedido seleccionado en los comboboxes
        CbNombreVendedor.setSelectedItem(vendedorServicios.getNombreVendedorPorId(pedidoSeleccionado.getIdVendedor()));
        CbNombreCliente.setSelectedItem(clienteServicios.getNombreClientePorId(pedidoSeleccionado.getIdCliente()));

        Estado estado = pedidoSeleccionado.getEstado();

        cargarEstado(estado);

        // Configuramos el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) JtableCarrito.getModel();
        model.setColumnIdentifiers(new String[]{"Nombre", "Precio Unidad", "Cantidad", "Subtotal"});
        model.setRowCount(0); // Limpiamos la tabla

        // Cargamos los productos en la tabla
        for (DetallePedido detalle : detallePedidos.obtenerDetallesPedido(pedidoSeleccionado.getIdentificador())) {
            model.addRow(new Object[]{
                detalle.getProducto().getNombre(),
                detalle.getPrecioVenta(),
                detalle.getCantidad(),
                detalle.subTotal()
            });
        }

        // Automáticamente actualiza el precio total
        actualizarPrecioTotal();
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

    //funcion que se usa en modifcarEnCarrito para traer los datos acá
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
        btnAñadir = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        btnLimpiarTabla = new javax.swing.JButton();
        txtPrecioTotal = new javax.swing.JTextField();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        CbEstado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ACTUALIZAR PEDIDO");

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

        txtPrecioTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioTotalActionPerformed(evt);
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

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Precio Total:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Estado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addComponent(btnAñadir)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnModificar))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(42, 42, 42)
                                                        .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btnQuitar)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnLimpiarTabla))))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CbNombreCliente, 0, 210, Short.MAX_VALUE)
                                    .addComponent(CbNombreVendedor, 0, 210, Short.MAX_VALUE)
                                    .addComponent(CbEstado, 0, 210, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(jLabel1)))
                .addContainerGap(29, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(CbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAñadir)
                    .addComponent(btnModificar)
                    .addComponent(btnQuitar)
                    .addComponent(btnLimpiarTabla))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CbNombreVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbNombreVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbNombreVendedorActionPerformed

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAñadirActionPerformed
        añadirAlCarrito añadirCarritoFrame = new añadirAlCarrito();
        añadirCarritoFrame.setActualizarPedidoFrame(this);
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
            modificarFrame.setActualizarPedidoFrame(this);
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

    private void txtPrecioTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioTotalActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        //validaciones básicas
        if (JtableCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Añade al menos un producto antes de confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (CbNombreVendedor.getSelectedIndex() == 0 || CbNombreCliente.getSelectedIndex() == 0 || CbEstado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //obtenemos el ID del vendedor y del cliente seleccionados
        String nombreVendedor = (String) CbNombreVendedor.getSelectedItem();
        String nombreCliente = (String) CbNombreCliente.getSelectedItem();
        
        int idVendedor = vendedorServicios.obtenerIdVendedorPorNombre(nombreVendedor);
        int idCliente = clienteServicios.obtenerIdClientePorNombre(nombreCliente);
        
        //tambien obtenemos el estado
        Pedido.Estado estado = Pedido.Estado.valueOf((String) CbEstado.getSelectedItem());

        //actualizamos el objeto pedido
        pedidoSeleccionado.setIdVendedor(idVendedor);
        pedidoSeleccionado.setIdCliente(idCliente);
        pedidoSeleccionado.setEstado(estado);

        //obtenemos los detalles (carrito) actuales del pedido
        DefaultTableModel model = (DefaultTableModel) JtableCarrito.getModel();
        List<DetallePedido> detalles = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String nombreProducto = (String) model.getValueAt(i, 0);
            Producto producto = productoServicios.buscarProductoPorNombre(nombreProducto);
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado: " + nombreProducto, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            float precioVenta = (float) model.getValueAt(i, 1);
            int cantidad = (int) model.getValueAt(i, 2);
            float subtotal = (float) model.getValueAt(i, 3);

            DetallePedido detalle = new DetallePedido(cantidad, precioVenta, producto, pedidoSeleccionado.getIdentificador());
            detalles.add(detalle);
        }

        //actualizamos en la base de datos
        boolean resultado = pedidosServicios.actualizarPedido(pedidoSeleccionado);
        
        if (resultado) {
            //si se pudo actualizar el pedido, tambie actualizamos los detalles del pedido en la base de datos
            boolean resultadoDetalles = detallePedidos.actualizarDetallesPedido(pedidoSeleccionado.getIdentificador(), detalles);
            if (resultadoDetalles) {
                JOptionPane.showMessageDialog(this, "Pedido actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar los detalles del pedido. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el pedido. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas cancelar? Los cambios no guardados se perderán.",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            //cerramos la ventana actual
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

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
            java.util.logging.Logger.getLogger(ActualizarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ActualizarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ActualizarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ActualizarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ActualizarPedido().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbEstado;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtPrecioTotal;
    // End of variables declaration//GEN-END:variables
}
