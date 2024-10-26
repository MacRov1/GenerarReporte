/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentancion.Vendedores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.servicios.VendedorServicios;
import logica.Clases.Vendedor;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author UnwantedOpinion
 */
public class datosVendedores extends javax.swing.JFrame {

    private Timer timer;
    private TableRowSorter<DefaultTableModel> sorter;
    
     public datosVendedores() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Datos de Vendedores");
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
        btnModVendedor.setEnabled(false);
        btnDeshVendedor.setEnabled(false);
        
        //agregamos un listener para la tabla que active los botones al seleccionar una fila
        tblListarVendedores.getSelectionModel().addListSelectionListener(e -> {
            //si hay una fila seleccionada, habilitar los botones
            boolean seleccionValida = tblListarVendedores.getSelectedRow() >= 0;
            btnModVendedor.setEnabled(seleccionValida);
            btnDeshVendedor.setEnabled(seleccionValida);
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
        VendedorServicios vendedorServicios = new VendedorServicios();
        ArrayList<Vendedor> vendedores = vendedorServicios.listarVendedores();

        //obtenemos el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblListarVendedores.getModel();
        
        //configuramos el TableRowSorter
        sorter = new TableRowSorter<>(modelo);
        tblListarVendedores.setRowSorter(sorter);

        //creamos un renderizador que centre el contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblListarVendedores.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        //limpiamos la tabla antes de agregar los nuevos datos
        modelo.setRowCount(0);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        //agregamos filas a la tabla
        for (Vendedor vendedor : vendedores) {
            String fechaContratacionFormateada = "";
            if (vendedor.getFechaContratacion() != null) {
                //formateamos la fecha a cadena
                fechaContratacionFormateada = formatoFecha.format(vendedor.getFechaContratacion());
            }
            modelo.addRow(new Object[]{
                vendedor.getId(),
                vendedor.getNomUsuario(),
                vendedor.getContrasenia(),
                vendedor.getNombre(),
                vendedor.getCedula(),
                vendedor.getCorreo(),
                vendedor.getTelefono(),
                vendedor.getDireccion(),
                fechaContratacionFormateada,
                vendedor.getActivo()  == true ? "Sí" : "No"
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarVendedores = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAltaVendedor1 = new javax.swing.JButton();
        btnModVendedor = new javax.swing.JButton();
        btnDeshVendedor = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtBBusqueda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbFiltros = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblListarVendedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre Usuario", "Contraseña", "Nombre", "Cedula", "Correo Electronico", "Telefono", "Dirección", "Fecha de Contratacion", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblListarVendedores);
        if (tblListarVendedores.getColumnModel().getColumnCount() > 0) {
            tblListarVendedores.getColumnModel().getColumn(0).setMinWidth(50);
            tblListarVendedores.getColumnModel().getColumn(0).setMaxWidth(60);
            tblListarVendedores.getColumnModel().getColumn(9).setMinWidth(30);
            tblListarVendedores.getColumnModel().getColumn(9).setMaxWidth(50);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("TABLA DE VENDEDORES");

        btnAltaVendedor1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-plus-32.png"))); // NOI18N
        btnAltaVendedor1.setText("Añadir Vendedor");
        btnAltaVendedor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaVendedor1ActionPerformed(evt);
            }
        });

        btnModVendedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-modify-32.png"))); // NOI18N
        btnModVendedor.setText("Modificar Vendedor");
        btnModVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModVendedorActionPerformed(evt);
            }
        });

        btnDeshVendedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentancion/Iconos/icons8-cancel-32.png"))); // NOI18N
        btnDeshVendedor.setText("Deshabilitar Vendedor");
        btnDeshVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeshVendedorActionPerformed(evt);
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

        cbFiltros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "ID", "Nombre Usuario", "Contraseña", "Nombre", "Cedula", "Correo Electronico", "Telefono", "Direccion", "Fecha de Contratacion", "Activo" }));
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 169, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(138, 138, 138))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(217, 217, 217))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnAltaVendedor1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(btnModVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(btnDeshVendedor)
                                .addGap(128, 128, 128))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAltaVendedor1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeshVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAltaVendedor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaVendedor1ActionPerformed
        //crea una nueva instancia de la ventana aniadirVendedor
        aniadirVendedor ventanaAniadirVendedor = new aniadirVendedor();
    
        //hace que la ventana sea visible
        ventanaAniadirVendedor.setVisible(true);
    }//GEN-LAST:event_btnAltaVendedor1ActionPerformed

    private void btnModVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModVendedorActionPerformed
        int filaSeleccionada = tblListarVendedores.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int id = (Integer) tblListarVendedores.getValueAt(filaSeleccionada, 0);
            String nomUsuario = (String) tblListarVendedores.getValueAt(filaSeleccionada, 1);
            String contrasenia = (String) tblListarVendedores.getValueAt(filaSeleccionada, 2);
            String nombre = (String) tblListarVendedores.getValueAt(filaSeleccionada, 3);
            int cedula = (Integer) tblListarVendedores.getValueAt(filaSeleccionada, 4);
            String correo = (String) tblListarVendedores.getValueAt(filaSeleccionada, 5);
            String telefono = (String) tblListarVendedores.getValueAt(filaSeleccionada, 6);
            String direccion = (String) tblListarVendedores.getValueAt(filaSeleccionada, 7);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea modificar este vendedor?", 
                "Confirmar Modificación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            modificarVendedor ventanaModificacion = new modificarVendedor();
            ventanaModificacion.setId(id);
            ventanaModificacion.setNombreUsuario(nomUsuario);
            ventanaModificacion.setContrasenia(contrasenia);
            ventanaModificacion.setNombre(nombre);
            ventanaModificacion.setCedula(cedula);
            ventanaModificacion.setCorreo(correo);
            ventanaModificacion.setTelefono(telefono);
            ventanaModificacion.setDireccion(direccion);
            ventanaModificacion.setVisible(true);

        }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un vendedor para modificar.");
        }
    }//GEN-LAST:event_btnModVendedorActionPerformed

    private void btnDeshVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeshVendedorActionPerformed
        int selectedRow = tblListarVendedores.getSelectedRow();

        if (selectedRow >= 0) {
            int id = (Integer) tblListarVendedores.getValueAt(selectedRow, 0);

            VendedorServicios servicios = new VendedorServicios();

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea deshabilitar este vendedor?", 
                    "Confirmar Deshabilitación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean exito = servicios.deshabilitarVendedor(id);

                if (exito) {
                    JOptionPane.showMessageDialog(this, 
                            "Vendedor deshabilitado exitosamente.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Error al deshabilitar el vendedor.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un vendedor para deshabilitar.", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDeshVendedorActionPerformed

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
            java.util.logging.Logger.getLogger(datosVendedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(datosVendedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(datosVendedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(datosVendedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new datosVendedores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltaVendedor1;
    private javax.swing.JButton btnDeshVendedor;
    private javax.swing.JButton btnModVendedor;
    private javax.swing.JComboBox<String> cbFiltros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListarVendedores;
    private javax.swing.JTextField txtBBusqueda;
    // End of variables declaration//GEN-END:variables
}
