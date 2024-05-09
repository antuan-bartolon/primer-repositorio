package controlador;

import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vistas.*;

public class Controlador {

    // INSTANCIAS DE LAS CLASES DE NUESTRAS VISTAS
    public static VistaVentas viewVentas = new VistaVentas();
    public static VistaMenu viewMenu = new VistaMenu();
    public static VistaLogin viewLogin = new VistaLogin();
    public static VistaNuevoPassword viewPass = new VistaNuevoPassword();
    public static VistaMantUser viewMant = new VistaMantUser();
    // INSTANCIAMOS NUESTRO MODELO
    public static DataBase db = new DataBase();
    public static Usuario usuario = new Usuario();
    public static Usuario usuarioEdit = new Usuario();

    // METODOS PARA LA VENTA LOGIN
    public static void MostrarLogin() {
        viewLogin.setVisible(true);
        viewLogin.setLocationRelativeTo(null);
    }

    public static void MostrarMenu() {
        viewMenu.setVisible(true);
        viewMenu.setLocationRelativeTo(null);
        //mostramos los datos del usuario
        viewMenu.lblUser.setText(db.BuscarLogueo(usuario));
    }

    public static void MostrarVentas() {
        viewVentas.setVisible(true);
        viewVentas.setLocationRelativeTo(null);
    }

    public static void MostrarVentanaCambioPass() {
        viewPass.setVisible(true);
        viewPass.setLocationRelativeTo(null);
        LimpiarCamposCambiarPass();
    }

    public static void MostrarMantUser() {
        inhabilitarSeccion();
        viewMant.setVisible(true);
        viewMant.setLocationRelativeTo(null);
        MostrarTablaUsuario();
    }

    public static void OcultarLogin() {
        viewLogin.dispose();
    }

    public static void OcultarMenu() {
        viewMenu.dispose();
    }

    public static void OcultarVentas() {
        viewVentas.dispose();
    }

    public static void OcultarCambiarPass() {
        viewPass.dispose();
    }

    public static void OcultarMantUser() {
        viewMant.dispose();
    }

    public static void BtnIngresarLogin() {
        String user = viewLogin.txtUser.getText();
        String password = viewLogin.txtPass.getText();
        usuario.setLogUser(user);
        usuario.setPassword(password);

        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor digite un usuario y una Contraseña");
        } else if (db.ValidarUsuario(usuario)) {
            OcultarLogin();
            MostrarMenu();
        } else {
            viewLogin.txtUser.setText("");
            viewLogin.txtPass.setText("");
        }

    }

    public static void BtnSalirLogin() {
        int confirmar = JOptionPane.showConfirmDialog(null, "Esta usted seguro que desea salir? ");
        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // METODOS PARA LA VENTANA DE MENU
    public static void ItmVentas() {
        OcultarMenu();
        MostrarVentas();
    }

    public static void ItmCambiarPass() {
        OcultarMenu();
        MostrarVentanaCambioPass();
    }

    public static void ItmCerrarSesion() {
        viewLogin.txtUser.setText("");
        viewLogin.txtPass.setText("");
        OcultarMenu();
        MostrarLogin();
    }

    public static void ItmMantUser() {
        OcultarMenu();
        MostrarMantUser();
    }

    // METODOS PARA LA VENTANA DE VENTAS
    public static void BtnMenuVenta() {
        OcultarVentas();
        MostrarMenu();
    }

    public static void MostrarUserVenta() {
        LocalDate hoy = LocalDate.now();
        String fechaActual = String.valueOf(hoy);
        viewVentas.txtLabel.setText(db.BuscarLogueoSinTipPerson(usuario));
        viewVentas.labelFecha.setText(fechaActual);
    }

    // METODOS PARA LA VENTANA DE CAMBIAR PASSWORD
    public static void BtnCancelarCambio() {
        viewPass.txtPassAnterior.setText("");
        viewPass.txtPassNuevo.setText("");
        viewPass.txtPassConfirmar.setText("");
        viewPass.txtError.setText("");
    }

    public static void BtnRegresar() {
        OcultarCambiarPass();
        MostrarMenu();
    }

    public static void BtnAceptar() {
        String passAnterior = viewPass.txtPassAnterior.getText();
        String passNuevo = viewPass.txtPassNuevo.getText();
        String passConfirmar = viewPass.txtPassConfirmar.getText();
        //
        if (passAnterior.isEmpty() || passNuevo.isEmpty() || passConfirmar.isEmpty()) {
            viewPass.txtError.setText("por favor rellene todos los campos");
        } else if (db.CambiarPassword(usuario, passAnterior, passNuevo, passConfirmar)) {
            OcultarCambiarPass();
            MostrarMenu();
            usuario.setPassword(passConfirmar); // seteamos la nueva password a usuario, para que pueda cambiar otra vez estando en el sistema
            JOptionPane.showMessageDialog(null, "EL CAMBIO SE HIZO CON EXITO!!!");
        } else {
            LimpiarCamposCambiarPass();
            System.out.println("operacion sin ejecutar");
        }
    }

    public static void LimpiarCamposCambiarPass() {
        viewPass.txtPassAnterior.setText("");
        viewPass.txtPassNuevo.setText("");
        viewPass.txtPassConfirmar.setText("");
        viewPass.txtError.setText("");
    }

    public static void LimpiarCamposUsuarios() {
        viewMant.txtNewUser.setText("");
        viewMant.txtNewPass.setText("");
        viewMant.txtNewEdoCta.setText("");
        viewMant.newFecIni.setText("");
        viewMant.newFecFin.setText("");
    }

    // METODOS PARA LA VENTANA DE MANTENIMIENTO DE USUARIOS
    
    public static void MostrarTablaUsuario() {
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMant.tablaUser.getModel();
        // limpiamos las filas 
        listaUsuarios.setRowCount(0);
        for (Usuario usersTabla : db.ListarUsuarios()) {
            Object[] fila = new Object[7];
            fila[0] = usersTabla.getCvUser();
            fila[1] = usersTabla.getDatosPersonales();
            fila[2] = usersTabla.getLogUser();
            fila[3] = usersTabla.getPassword();
            fila[4] = usersTabla.getFecInicio();
            fila[5] = usersTabla.getFecFinal();
            fila[6] = usersTabla.getEdoCta();
            listaUsuarios.addRow(fila); // aniadimos las filas a la tabla
        }
    }
    
    public static void btnCancelarMant() {
        LimpiarCamposUsuarios();
        inhabilitarSeccion();
        viewMant.combo.setEnabled(false);
        viewMant.btnModify.setEnabled(false);
        viewMant.btnEditar.setVisible(true);
        viewMant.btnBorrar.setEnabled(true);
        //
        viewMant.combo.setSelectedIndex(0); // para que vuelva a aparecer: seleccione una persona en el combo
        //
        viewMant.btnAgregar.setVisible(false);
        viewMant.BtnNuevo.setVisible(true);
        viewMant.BtnNuevo.setEnabled(true);
        viewMant.btnEditar.setEnabled(true);
    }
    
    public static void btnSalirAMenu() {
        MostrarMenu();
        OcultarMantUser();
        btnCancelarMant();
    }

    public static void btnNuevo() {
        viewMant.BtnNuevo.setVisible(false);
        viewMant.btnAgregar.setVisible(true);
        //
        viewMant.btnEditar.setEnabled(false);
        viewMant.btnBorrar.setEnabled(false);
        habilitarSeccion();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaActual = LocalDate.now(); // Obtener la fecha actual
        LocalDate fechaFin = fechaActual.plusDays(10); // Sumar 10 días a la fecha actual para obtener la fecha de fin
        // Convertir las fechas a String usando el formateador
        String strFechaActual = fechaActual.format(formatter);
        String strFechaFin = fechaFin.format(formatter);
        //
        viewMant.newFecIni.setText(strFechaActual);
        viewMant.newFecFin.setText(strFechaFin);
        viewMant.txtNewEdoCta.setText("1");
        //
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMant.tablaUser.getModel();
        listaUsuarios.setRowCount(0); // limpiamos las filas 
        // MUESTRA TODAS LAS PERSONAS EN EL COMBOBOX
        for (Persona persona : db.MostrarPersonas()) {
            viewMant.combo.addItem(persona.NombreCompleto);
        }
        //
        MostrarTablaUsuario();
    }

    public static void btnAgregar() {
        Usuario usuarioNuevo = new Usuario();
        viewMant.btnEditar.setEnabled(false);
        //
        int cvPerson = viewMant.combo.getSelectedIndex();
        String newUser = viewMant.txtNewUser.getText();
        String newPass = viewMant.txtNewPass.getText();
        String newFecIni = viewMant.newFecIni.getText();
        String newFecFin = viewMant.newFecFin.getText();
        String newEdoCta = viewMant.txtNewEdoCta.getText();
        //
        if (cvPerson == 0) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UN USUARIO PARA PODER CONTINUAR");
        } else if (newUser.isEmpty() || newPass.isEmpty() || newFecIni.isEmpty() || newFecFin.isEmpty() || newEdoCta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "HAY CAMPOS VACIOS");
        } else {
            int edoCta = Integer.parseInt(newEdoCta); // convertimos el estado de cuenta a int
            usuarioNuevo.setCvPerson(cvPerson);
            usuarioNuevo.setLogUser(newUser);
            usuarioNuevo.setPassword(newPass);
            usuarioNuevo.setEdoCta(edoCta);
            if (db.AgregarUsuario(usuarioNuevo, newFecIni, newFecFin)) {
                viewMant.btnAgregar.setVisible(false);
                LimpiarCamposUsuarios();
                MostrarTablaUsuario();
                inhabilitarSeccion();
                //
                viewMant.btnEditar.setVisible(true);
                viewMant.btnEditar.setEnabled(true);
                viewMant.BtnNuevo.setVisible(true);
                viewMant.btnBorrar.setEnabled(true);
            }
        }
    }

    public static void inhabilitarSeccion() {
        for (Component inha : viewMant.seccionData.getComponents()) {
            inha.setEnabled(false);
        }
        viewMant.label0.setVisible(true);
        viewMant.label1.setVisible(false);
    }

    public static void habilitarSeccion() {
        for (Component hab : viewMant.seccionData.getComponents()) {
            hab.setEnabled(true);
        }
        viewMant.label0.setVisible(false);
        viewMant.label1.setVisible(true);
    }

    public static void btnEditar() {
        viewMant.btnModify.setEnabled(true);
        int fila = viewMant.tablaUser.getSelectedRow();
        if (fila >= 0) {
            habilitarSeccion();
            //
            viewMant.BtnNuevo.setEnabled(false);
            viewMant.btnBorrar.setEnabled(false);
            viewMant.btnEditar.setVisible(false);
            viewMant.btnModify.setVisible(true);
            viewMant.combo.setEnabled(false);
            //
            int cvUser = Integer.parseInt(viewMant.tablaUser.getValueAt(fila, 0).toString());
            String logUser = viewMant.tablaUser.getValueAt(fila, 2).toString();
            String passUser = viewMant.tablaUser.getValueAt(fila, 3).toString();
            String fechai = viewMant.tablaUser.getValueAt(fila, 4).toString();
            String fechaf = viewMant.tablaUser.getValueAt(fila, 5).toString();
            String edoCta = viewMant.tablaUser.getValueAt(fila, 6).toString();
            viewMant.txtNewUser.setText(logUser);
            viewMant.txtNewPass.setText(passUser);
            viewMant.newFecIni.setText(fechai);
            viewMant.newFecFin.setText(fechaf);
            viewMant.txtNewEdoCta.setText(edoCta);
            usuarioEdit.setCvUser(cvUser);
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA PARA EDITAR");
        }
    }

    public static void btnModify() {
        String updUser = viewMant.txtNewUser.getText();
        String updPass = viewMant.txtNewPass.getText();
        String updFecin = viewMant.newFecIni.getText();
        String updFecfn = viewMant.newFecFin.getText();
        String EdoCta = viewMant.txtNewEdoCta.getText();
        int updEdCta = Integer.parseInt(EdoCta);
        //
        usuarioEdit.setLogUser(updUser);
        usuarioEdit.setPassword(updPass);
        usuarioEdit.setEdoCta(updEdCta);
        if (db.ModificarUsuario(usuarioEdit, updFecin, updFecfn)) {
            viewMant.btnModify.setVisible(false);
            System.out.println("1 persona modificada");
            MostrarTablaUsuario();
            LimpiarCamposUsuarios();
            inhabilitarSeccion();
            //viewMant.btnModify.setEnabled(false);
            viewMant.combo.setEnabled(false);
            viewMant.btnEditar.setVisible(true);
            viewMant.BtnNuevo.setEnabled(true);
            viewMant.btnBorrar.setEnabled(true);
        }
    }

    public static void btnEliminar() {
        Usuario usuarioDelete = new Usuario();
        int fila = viewMant.tablaUser.getSelectedRow();
        if (fila >= 0) {
            int confirmar = JOptionPane.showConfirmDialog(null, "EN REALIDAD DESEA BORRAR EL REGISTRO? ");
            if (confirmar == JOptionPane.YES_OPTION) {
                int cvUser = Integer.parseInt(viewMant.tablaUser.getValueAt(fila, 0).toString());
                usuarioDelete.setCvUser(cvUser);
                db.BorrarUsuario(usuarioDelete);
                MostrarTablaUsuario();
                System.out.println("se borro un registro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "NO HAS SELECCIONADO UN REGISTRO PARA BORRAR");
        }
    }
}
