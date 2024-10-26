/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Clientes;

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
import logica.Clases.Cliente;
import logica.servicios.ClienteServicios;

/**
 *
 * @author UnwantedOpinion
 */
public class ClientesPrincipal extends javax.swing.JFrame {

    private Timer timer;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public ClientesPrincipal() {
        initComponents();
        actualizarTablaClientes();
        this.setTitle("Datos de Clientes");
        this.setLocationRelativeTo(null); // Centra la ventana
    
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //codigo que se ejecuta al cerrar la ventana
                    manejoCiereVentana();
                }
            });
            
            btnDeshabcliente.setEnabled(false);
            btnModificarCliente.setEnabled(false);
            
            // Agregar un listener para la tabla que activa los botones al seleccionar una fila
            tablaClientes2.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) { // Solo procesar si la selección ha cambiado
                    int filaSeleccionada = tablaClientes2.getSelectedRow();
                    boolean seleccionValida = filaSeleccionada >= 0;

                    // Habilitar o deshabilitar botones según la selección
                    btnModificarCliente.setEnabled(seleccionValida);
                    btnDeshabcliente.setEnabled(seleccionValida);
                }
            });
            
            // Inicializar el Timer
            timer = new Timer(4000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Solo actualiza si el filtro "Todos" está seleccionado y no hay texto en el JTextField
                    if (cbFiltros.getSelectedIndex() == 0 && txtBBusqueda.getText().trim().isEmpty()) {
                        actualizarTablaClientes(); // Llama al método para recargar datos
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
           //cierra la ventana actual (aniadirVendedor)
           this.dispose();
    }

    private void actualizarTablaClientes() {
        ClienteServicios clienteServicios = new ClienteServicios();
        ArrayList<Cliente> clientes = clienteServicios.getClientes();

        //obtenemos el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes2.getModel();
        
        //configuramos el TableRowSorter
        sorter = new TableRowSorter<>(modelo);
        tablaClientes2.setRowSorter(sorter);

        //creamos un renderizador que centre el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaClientes2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        //limpiamos la tabla antes de agregar los nuevos datos
        modelo.setRowCount(0);

        //agregamos filas a la tabla
        for (Cliente cliente : clientes) {
            modelo.addRow(new Object[]{
                cliente.getNum_rut(),
                cliente.getNom_empresa(),
                cliente.getTelefono(),
                cliente.getCorreo_electronico(),
                (cliente.getActivo() != null && cliente.getActivo()) ? "Sí" : "No"
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

        jPanel1 = new javax.swing.JPanel();
        btnModificarCliente = new javax.swing.JButton();
        btnDeshabcliente = new javax.swing.JButton();
        btnAltaCliente2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtBBusqueda = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbFiltros = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(574, 500));

        btnModificarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnModificarCliente.setText("Modificar Cliente");
        btnModificarCliente.setEnabled(false);
        btnModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarClienteActionPerformed(evt);
            }
        });

        btnDeshabcliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnDeshabcliente.setText("Deshabilitar Cliente");
        btnDeshabcliente.setEnabled(false);
        btnDeshabcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeshabclienteActionPerformed(evt);
            }
        });

        btnAltaCliente2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAltaCliente2.setText("Añadir Cliente");
        btnAltaCliente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaCliente2ActionPerformed(evt);
            }
        });

        tablaClientes2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RUT", "Nombre", "Telefono", "Correo", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaClientes2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tablaClientes2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tablaClientes2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tablaClientes2PropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tablaClientes2);
        if (tablaClientes2.getColumnModel().getColumnCount() > 0) {
            tablaClientes2.getColumnModel().getColumn(4).setMinWidth(30);
            tablaClientes2.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        jLabel3.setText("Buscar:");

        txtBBusqueda.setToolTipText("");
        txtBBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBBusquedaActionPerformed(evt);
            }
        });

        jLabel4.setText("Filtrar:");

        cbFiltros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "RUT", "Nombre", "Telefono", "Correo", "Activo" }));
        cbFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnAltaCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnDeshabcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAltaCliente2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeshabcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("LISTADO DE CLIENTES");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(231, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(224, 224, 224))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(490, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(27, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(27, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaClientes2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tablaClientes2PropertyChange

    }//GEN-LAST:event_tablaClientes2PropertyChange

    private void tablaClientes2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tablaClientes2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClientes2AncestorAdded

    private void btnAltaCliente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaCliente2ActionPerformed
        //crea una nueva instancia de la ventana ClientesAlta
        ClientesAlta ventanaAltaCliente = new ClientesAlta();

        //hace que la ventana sea visible
        ventanaAltaCliente.setVisible(true);
    }//GEN-LAST:event_btnAltaCliente2ActionPerformed

    private void btnDeshabclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeshabclienteActionPerformed
        int selectedRow = tablaClientes2.getSelectedRow();

        if (selectedRow != -1) {
            //obtenemos el RUT del cliente seleccionado
            String rutCliente = (String) tablaClientes2.getValueAt(selectedRow, 0);

            //mostramos un cuadro de confirmación
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Estás seguro de que deseas deshabilitar el cliente?", 
                    "Confirmar deshabilitación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                //llamamos al método para deshabilitar al cliente
                ClienteServicios servicio = new ClienteServicios();
                if (servicio.deshabilitarCliente(rutCliente)) {
                    JOptionPane.showMessageDialog(this, 
                            "Cliente deshabilitado exitosamente.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "No se pudo deshabilitar el cliente.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un cliente para deshabilitar.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeshabclienteActionPerformed

    private void btnModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarClienteActionPerformed
        // Verificar que se haya seleccionado una fila en la tabla
        int filaSeleccionada = tablaClientes2.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el RUT de la columna correspondiente ( columna 0)
        String rut = (String) tablaClientes2.getValueAt(filaSeleccionada, 0);
        // Asignar el RUT a la variable estática
        ClientesModificar.rutCliente = rut;

        try {
            ClientesModificar frameClientesModificar = new ClientesModificar();
            frameClientesModificar.setLocationRelativeTo(null);
            frameClientesModificar.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al abrir el formulario: " + e.getMessage());
        }
    }//GEN-LAST:event_btnModificarClienteActionPerformed

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
            java.util.logging.Logger.getLogger(ClientesPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientesPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientesPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientesPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientesPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltaCliente2;
    private javax.swing.JButton btnDeshabcliente;
    private javax.swing.JButton btnModificarCliente;
    private javax.swing.JComboBox<String> cbFiltros;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaClientes2;
    private javax.swing.JTextField txtBBusqueda;
    // End of variables declaration//GEN-END:variables
}
