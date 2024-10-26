package Presentacion;

import logica.Fabrica;

import Presentancion.Productos.datosProductos;
import Presentancion.Categoria.datosCategorias;
import Presentancion.Vendedores.datosVendedores;
import Presentancion.Clientes.ClientesPrincipal;
import Presentancion.Pedidos.ActualizarPedido;
import Presentancion.Pedidos.AddPedido;
import Presentancion.Proveedor.datosProveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

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
import logica.Clases.Pedido;
import logica.Interfaces.IControladorCliente;
import logica.Interfaces.IControladorPedido;
import logica.Interfaces.IControladorUsuario;
import logica.Interfaces.IControladorVendedor;
import logica.servicios.PedidosServicios;
import logica.servicios.VendedorServicios;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LucasCiceri
 */
public class GestorPedidosUI extends javax.swing.JFrame {
    
    private IControladorUsuario ICU;
    private IControladorPedido ICP;
    private IControladorVendedor ICV;
    private IControladorCliente ICC;
    private ActualizarPedido actualizarPedido = new ActualizarPedido();
    private AddPedido agregarPedido = new AddPedido();
    //agregamos un contador para controlar los clics para el filtrado por columna (no el combobox)
    private int clickCount = 0;
    private TableRowSorter<DefaultTableModel> sorter;
    private Timer timer;

    public GestorPedidosUI() {
        initComponents();
        this.ICU = Fabrica.getInstance().getIControladorUsuario();
        this.ICP = Fabrica.getInstance().getIControladorPedido();
        this.ICV = Fabrica.getInstance().getIControladorVendedor();
        this.ICC = Fabrica.getInstance().getIControladorCliente();
        this.setTitle("Gestion de Pedidos");
        this.setLocationRelativeTo(null); // Centra la ventana
        this.cargarDatosDePedidos();
        this.agregarFiltros();
        txtBBusqueda.setToolTipText(null);
        
        // Deshabilitamos los botones mod y elim
        btnActualizarPedido.setEnabled(false);
        btnTerminarPedido.setEnabled(false);
        
        timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solo actualiza si el filtro "Todos" está seleccionado y no hay texto en el JTextField
                if (cbFiltros.getSelectedIndex() == 0 && txtBBusqueda.getText().trim().isEmpty()) {
                    recargarDatosDelPedido();
                }
            }
        });
        timer.start(); // Iniciar el timer
        agregarFiltros();
        
        // Agregar un listener para la tabla que activa los botones al seleccionar una fila
        tblPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Solo procesar si la selección ha cambiado
                int filaSeleccionada = tblPedidos.getSelectedRow();
                boolean seleccionValida = filaSeleccionada >= 0;

                // Habilitar o deshabilitar botones según la selección
                btnActualizarPedido.setEnabled(seleccionValida);
                btnTerminarPedido.setEnabled(seleccionValida);
            }
        });
    }
    
    private void cargarDatosDePedidos() {
        ArrayList<Pedido> pedidosDeBaseDeDatos = this.ICP.getPedidos();
        DefaultTableModel modelo = (DefaultTableModel) this.tblPedidos.getModel();

        //configuramos el TableRowSorter
        sorter = new TableRowSorter<>(modelo);
        tblPedidos.setRowSorter(sorter);

        //creamos un renderizador que centre el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblPedidos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");

        for (Pedido pedido : pedidosDeBaseDeDatos) {
            String nombreVendedor = ICV.obtenerNombreVendedorPorId(pedido.getIdVendedor());
            String nombreCliente = ICC.obtenerNombreClientePorId(pedido.getIdCliente());

            String estadoFormateado = "";
            switch (pedido.getEstado()) {
                case EN_PREPARACION:
                    estadoFormateado = "En Preparación";
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
                    estadoFormateado = "Desconocido";
                    break;
            }

            String fechaFormateada = "";
            if (pedido.getFechaPedido() != null) {
                fechaFormateada = fechaFormato.format(pedido.getFechaPedido());
            }

            Object[] nuevaRow = {
                pedido.getIdentificador(),
                fechaFormateada,
                estadoFormateado,
                nombreVendedor,
                nombreCliente
            };
            modelo.addRow(nuevaRow);
        }
    }
    
    private void agregarFiltros() {
        txtBBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                pausarActualizacion();  // Pausa el timer al buscar
                aplicarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                pausarActualizacion();  // Pausa el timer al buscar
                aplicarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                pausarActualizacion();  // Pausa el timer al buscar
                aplicarFiltro();
            }
        });

        cbFiltros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausarActualizacion();  // Pausa el timer al cambiar el filtro
                aplicarFiltro();
            }
        });
    }

    // Método para pausar el timer
    private void pausarActualizacion() {
        timer.stop();  // Detener el timer
        reanudarActualizacion(); // Verificar si se puede reanudar
    }

    private void reanudarActualizacion() {
        // Comprobar las condiciones antes de reiniciar el timer
        if (cbFiltros.getSelectedIndex() == 0 || txtBBusqueda.getText().trim().isEmpty()) {
            timer.start();  // Reiniciar el timer solo si las condiciones son adecuadas
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

    
    private void limpiarTablaUsuarios() {
        DefaultTableModel modelo = (DefaultTableModel) this.tblPedidos.getModel();
        modelo.setRowCount(0);
    }

    public void recargarDatosDelPedido() {
        limpiarTablaUsuarios();
        cargarDatosDePedidos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDatosProveedores = new javax.swing.JButton();
        btnDatosClientes = new javax.swing.JButton();
        btnDatosVendedores = new javax.swing.JButton();
        btnDatosProductos = new javax.swing.JButton();
        btnDatosCategoria = new javax.swing.JButton();
        btnAddPedido = new javax.swing.JButton();
        btnActualizarPedido = new javax.swing.JButton();
        btnTerminarPedido = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPedidos = new javax.swing.JTable();
        txtBBusqueda = new javax.swing.JTextField();
        cbFiltros = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnDatosProveedores.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDatosProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-supply-32.png"))); // NOI18N
        btnDatosProveedores.setText("Proveedores");
        btnDatosProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosProveedoresActionPerformed(evt);
            }
        });

        btnDatosClientes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDatosClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-customers-32.png"))); // NOI18N
        btnDatosClientes.setText("Clientes");
        btnDatosClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosClientesActionPerformed(evt);
            }
        });

        btnDatosVendedores.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDatosVendedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-resume-32.png"))); // NOI18N
        btnDatosVendedores.setText("Vendedores");
        btnDatosVendedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosVendedoresActionPerformed(evt);
            }
        });

        btnDatosProductos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDatosProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-inventory-32.png"))); // NOI18N
        btnDatosProductos.setText("Productos");
        btnDatosProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosProductosActionPerformed(evt);
            }
        });

        btnDatosCategoria.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDatosCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-label-32.png"))); // NOI18N
        btnDatosCategoria.setText("Categorias");
        btnDatosCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosCategoriaActionPerformed(evt);
            }
        });

        btnAddPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAddPedido.setText("Añadir Pedido");
        btnAddPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPedidoActionPerformed(evt);
            }
        });

        btnActualizarPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizarPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnActualizarPedido.setText("Modificar Pedido");
        btnActualizarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarPedidoActionPerformed(evt);
            }
        });

        btnTerminarPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTerminarPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnTerminarPedido.setText("Terminar Pedido");
        btnTerminarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminarPedidoActionPerformed(evt);
            }
        });

        tblPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Estado", "Vendedor", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPedidos);
        if (tblPedidos.getColumnModel().getColumnCount() > 0) {
            tblPedidos.getColumnModel().getColumn(0).setMinWidth(50);
            tblPedidos.getColumnModel().getColumn(0).setMaxWidth(60);
            tblPedidos.getColumnModel().getColumn(1).setMinWidth(50);
            tblPedidos.getColumnModel().getColumn(1).setPreferredWidth(30);
        }

        txtBBusqueda.setToolTipText("");
        txtBBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBBusquedaActionPerformed(evt);
            }
        });

        cbFiltros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "ID", "Fecha", "Estado", "Vendedor", "Cliente" }));
        cbFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltrosActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar:");

        jLabel2.setText("Filtrar:");
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDatosVendedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDatosClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDatosProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDatosProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDatosCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddPedido)
                                .addGap(55, 55, 55)
                                .addComponent(btnActualizarPedido)
                                .addGap(53, 53, 53)
                                .addComponent(btnTerminarPedido))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 20, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDatosClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatosVendedores, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatosProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatosProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatosCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnActualizarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTerminarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDatosProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosProveedoresActionPerformed
        datosProveedor ventanaDatosProveedores = new datosProveedor();
        ventanaDatosProveedores.setVisible(true);
    }//GEN-LAST:event_btnDatosProveedoresActionPerformed

    private void btnDatosClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosClientesActionPerformed
        //crea una nueva instancia de la ventana ClientesPrincipal
        ClientesPrincipal ventanaClientesPrincipal = new ClientesPrincipal();
    
        //hace que la ventana sea visible
        ventanaClientesPrincipal.setVisible(true);
    }//GEN-LAST:event_btnDatosClientesActionPerformed

    private void btnDatosVendedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosVendedoresActionPerformed
        //crea una nueva instancia de la ventana datosVendedores
        datosVendedores ventanaDatosVendedor = new datosVendedores();
    
        //hace que la ventana sea visible
        ventanaDatosVendedor.setVisible(true);
    }//GEN-LAST:event_btnDatosVendedoresActionPerformed

    private void btnDatosProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosProductosActionPerformed
        //crea una nueva instancia de la ventana datosProductos
        datosProductos ventanaDatosProductos = new datosProductos();
    
        //hace que la ventana sea visible
        ventanaDatosProductos.setVisible(true);
    }//GEN-LAST:event_btnDatosProductosActionPerformed

    private void btnDatosCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosCategoriaActionPerformed
        //crea una nueva instancia de la ventana datosCategorias
        datosCategorias ventanaDatosCategoria = new datosCategorias();
    
        //hace que la ventana sea visible
        ventanaDatosCategoria.setVisible(true);
    }//GEN-LAST:event_btnDatosCategoriaActionPerformed

    private void btnAddPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPedidoActionPerformed
        this.agregarPedido.setVisible(true);
    }//GEN-LAST:event_btnAddPedidoActionPerformed

    private void btnActualizarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarPedidoActionPerformed
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow != -1) {
            //obtenemos el ID del pedido desde la tabla
            int idPedido = (int) tblPedidos.getValueAt(selectedRow, 0);

            //obtenemos el pedido desde PedidosServicios
            PedidosServicios pedidosServicios = new PedidosServicios();
            Pedido pedido = pedidosServicios.obtenerPedidoPorId(idPedido);

            if (pedido != null) {
                //crea y muestra la ventana de actualización
                ActualizarPedido actualizarPedidoFrame = new ActualizarPedido();
                actualizarPedidoFrame.setPedido(pedido);
                actualizarPedidoFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el pedido.");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido para modificar.");
        }
    }//GEN-LAST:event_btnActualizarPedidoActionPerformed

    private void btnTerminarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminarPedidoActionPerformed
        //obtenemos la fila seleccionada
        int filaSeleccionada = tblPedidos.getSelectedRow();

        if (filaSeleccionada != -1) {
            //obtenemos el ID del pedido desde la primera columna
            int idPedido = (int) tblPedidos.getValueAt(filaSeleccionada, 0);

            //mostramos cuadro de diálogo para seleccionar el nuevo estado
            String[] estados = {"CANCELADO", "ENTREGADO"};
            String estadoSeleccionado = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona el nuevo estado del pedido:",
                    "Cambiar Estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    estados,
                    estados[0]);

            if (estadoSeleccionado != null) {
                //llamamos al servicio para actualizar el estado del pedido
                boolean exito = ICP.actualizarEstadoPedido(idPedido, estadoSeleccionado);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "Estado del pedido actualizado correctamente.");
                    recargarDatosDelPedido();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el estado del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un pedido para actualizar el estado.");
        }
    }//GEN-LAST:event_btnTerminarPedidoActionPerformed

    private void cbFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFiltrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFiltrosActionPerformed

    private void txtBBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBBusquedaActionPerformed

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
            java.util.logging.Logger.getLogger(GestorPedidosUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestorPedidosUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestorPedidosUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestorPedidosUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestorPedidosUI().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarPedido;
    private javax.swing.JButton btnAddPedido;
    private javax.swing.JButton btnDatosCategoria;
    private javax.swing.JButton btnDatosClientes;
    private javax.swing.JButton btnDatosProductos;
    private javax.swing.JButton btnDatosProveedores;
    private javax.swing.JButton btnDatosVendedores;
    private javax.swing.JButton btnTerminarPedido;
    private javax.swing.JComboBox<String> cbFiltros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTable tblPedidos;
    private javax.swing.JTextField txtBBusqueda;
    // End of variables declaration//GEN-END:variables

}
