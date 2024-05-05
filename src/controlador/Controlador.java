package controlador;

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
    public static VistaMantUser viewMantUser = new VistaMantUser();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Sumar 10 días a la fecha actual para obtener la fecha de fin
        LocalDate fechaFin = fechaActual.plusDays(10);
        // Convertir las fechas a String usando el formateador
        String strFechaActual = fechaActual.format(formatter);
        String strFechaFin = fechaFin.format(formatter);
        //
        viewMantUser.setVisible(true);
        viewMantUser.setLocationRelativeTo(null);
        //
        viewMantUser.newFecIni.setText(strFechaActual);
        viewMantUser.newFecFin.setText(strFechaFin);
        viewMantUser.txtNewEdoCta.setText("0");
        viewMantUser.btnModify.setEnabled(false);
        //
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMantUser.tablaUser.getModel();
        listaUsuarios.setRowCount(0); // limpiamos las filas 

        // MUESTRA TODAS LAS PERSONAS EN EL COMBOBOX
        for (Persona persona : db.MostrarPersonas()) {
            viewMantUser.cbxSelecPerson.addItem(persona.NombreCompleto);
        }
        //
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
        viewMantUser.dispose();
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

        if (passAnterior.isEmpty() || passNuevo.isEmpty() || passConfirmar.isEmpty()) {
            viewPass.txtError.setText("por favor rellene todos los campos");
        } else if (db.CambiarPassword(usuario, passAnterior, passNuevo, passConfirmar)) {
            OcultarCambiarPass();
            MostrarMenu();
            // seteamos la nueva password a usuario, para que pueda cambiar otra vez estando en el sistema
            usuario.setPassword(passConfirmar);
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
        viewMantUser.txtNewUser.setText("");
        viewMantUser.txtNewPass.setText("");
        viewMantUser.txtNewEdoCta.setText("");
        viewMantUser.newFecIni.setText("");
        viewMantUser.newFecFin.setText("");
    }

    public static void BtnCancelarMant() {
        LimpiarCamposUsuarios();
        viewMantUser.btnModify.setEnabled(false);
        viewMantUser.BtnNuevo.setEnabled(true);
        viewMantUser.cbxSelecPerson.setEnabled(true);
    }

    public static void BtnNuevo() {
        Usuario usuarioNuevo = new Usuario();
        //
        int cvPerson = viewMantUser.cbxSelecPerson.getSelectedIndex();
        String newUser = viewMantUser.txtNewUser.getText();
        String newPass = viewMantUser.txtNewPass.getText();
        String newFecIni = viewMantUser.newFecIni.getText();
        String newFecFin = viewMantUser.newFecFin.getText();
        String newEdoCta = viewMantUser.txtNewEdoCta.getText();

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
                LimpiarCamposUsuarios();
                MostrarTablaUsuario();
            }
        }
        //System.out.println(viewMantUser.cbxSelecPerson.getSelectedIndex());
    }

    public static void MostrarTablaUsuario() {
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMantUser.tablaUser.getModel();

        listaUsuarios.setRowCount(0); // limpiamos las filas 
        for (Usuario usersTabla : db.ListarUsuarios()) {
            Object[] fila = new Object[7];
            fila[0] = usersTabla.getCvPerson();
            fila[1] = usersTabla.getLogUser();
            fila[2] = usersTabla.getPassword();
            fila[3] = usersTabla.getFecInicio();
            fila[4] = usersTabla.getFecFinal();
            fila[5] = usersTabla.getEdoCta();
            fila[6] = usersTabla.getCvUser();
            listaUsuarios.addRow(fila); // aniadimos las filas a la tabla
        }
    }

    public static void BtnEditar() {
        int fila = viewMantUser.tablaUser.getSelectedRow();

        if (fila >= 0) {
            viewMantUser.BtnNuevo.setEnabled(false);
            viewMantUser.btnModify.setEnabled(true);
            viewMantUser.cbxSelecPerson.setEnabled(false);
            String logUser = viewMantUser.tablaUser.getValueAt(fila, 1).toString();
            String passUser = viewMantUser.tablaUser.getValueAt(fila, 2).toString();
            String fechai = viewMantUser.tablaUser.getValueAt(fila, 3).toString();
            String fechaf = viewMantUser.tablaUser.getValueAt(fila, 4).toString();
            String edoCta = viewMantUser.tablaUser.getValueAt(fila, 5).toString();
            int cvUser = Integer.parseInt(viewMantUser.tablaUser.getValueAt(fila, 6).toString());
            viewMantUser.txtNewUser.setText(logUser);
            viewMantUser.txtNewPass.setText(passUser);
            viewMantUser.newFecIni.setText(fechai);
            viewMantUser.newFecFin.setText(fechaf);
            viewMantUser.txtNewEdoCta.setText(edoCta);
            usuarioEdit.setCvUser(cvUser);

        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA PARA EDITAR");
        }
    }

    public static void BtnModify() {
        String updUser = viewMantUser.txtNewUser.getText();
        String updPass = viewMantUser.txtNewPass.getText();
        String updFecin = viewMantUser.newFecIni.getText();
        String updFecfn = viewMantUser.newFecFin.getText();
        String EdoCta = viewMantUser.txtNewEdoCta.getText();
        int updEdCta = Integer.parseInt(EdoCta);
        usuarioEdit.setLogUser(updUser);
        usuarioEdit.setPassword(updPass);
        usuarioEdit.setEdoCta(updEdCta);
        if (db.ModificarUsuario(usuarioEdit, updFecin, updFecfn)) {
            JOptionPane.showMessageDialog(null, "UNA PERSONA FUE MODIFICADA");
            System.out.println("1 persona modificada");
            MostrarTablaUsuario();
            LimpiarCamposUsuarios();
            viewMantUser.btnModify.setEnabled(false);
            viewMantUser.cbxSelecPerson.setEnabled(true);
        }
    }

    public static void BtnEliminarUsuario() {
        int fila = viewMantUser.tablaUser.getSelectedRow();
        Usuario usuarioDelete = new Usuario();
        if (fila >= 0) {
            int confirmar = JOptionPane.showConfirmDialog(null, "EN REALIDAD DESEA BORRAR EL REGISTRO? ");
            if (confirmar == JOptionPane.YES_OPTION) {
                int cvUser = Integer.parseInt(viewMantUser.tablaUser.getValueAt(fila, 6).toString());
                usuarioDelete.setCvUser(cvUser);
                db.BorrarUsuario(usuarioDelete);
                MostrarTablaUsuario();
                System.out.println("se borro un registro");
                JOptionPane.showMessageDialog(null, "UN USUARIO FUE ELIMINADO");
            }
        } else {
            JOptionPane.showMessageDialog(null, "NO HAS SELECCIONADO UN REGISTRO PARA BORRAR");
        }
    }

    public static void BtnSalirAMenu() {
        MostrarMenu();
        OcultarMantUser();
    }
}
