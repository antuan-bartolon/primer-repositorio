package controlador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vistas.*;

public class Controlador {

    public static LocalDate hoy = LocalDate.now();
    public static String fechaActual = String.valueOf(hoy);

    // INSTANCIAS DE LAS CLASES DE NUESTRAS VISTAS
    public static VistaVentas viewVentas = new VistaVentas();
    public static VistaMenu viewMenu = new VistaMenu();
    public static VistaLogin viewLogin = new VistaLogin();
    public static VistaNuevoPassword viewPass = new VistaNuevoPassword();
    public static VistaMantUser viewMantUser = new VistaMantUser();
    // INSTANCIAMOS NUESTRO MODELO
    public static DataBase db = new DataBase();
    public static Usuario usuario = new Usuario();

    // METODOS PARA LA VENTANA DEL LOGIN
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
        viewMantUser.setVisible(true);
        viewMantUser.setLocationRelativeTo(null);
        viewMantUser.newFecIni.setText(fechaActual);
        viewMantUser.newFecFin.setText(fechaActual);
        viewMantUser.txtNewEdoCta.setText("0");
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMantUser.tablaUser.getModel();
        listaUsuarios.setRowCount(0); // limpiamos las filas 
        // MUESTRA TODAS LAS PERSONAS EN EL COMBOBOX
        for (Persona persona : db.MostrarPersonas()) {
            viewMantUser.cbxSelecPerson.addItem(persona.NombreCompleto);
        }
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
            // seteamos la nueva passqord a usuario, para que pueda cambiar otra vez estando en el sistema
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
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMantUser.tablaUser.getModel();
        listaUsuarios.setRowCount(0); // limpiamos las filas 
    }

    public static void BtnCancelarMant() {
        LimpiarCamposUsuarios();
    }

    public static void BtnNuevo() {
        Usuario u = new Usuario();
        //
        int cvPerson = viewMantUser.cbxSelecPerson.getSelectedIndex();
        String newUser = viewMantUser.txtNewUser.getText();
        String newPass = viewMantUser.txtNewPass.getText();
        String newFecIni = viewMantUser.newFecIni.getText();
        String newFecFin = viewMantUser.newFecFin.getText();
        String newEdoCta = viewMantUser.txtNewEdoCta.getText();

        if (cvPerson == 0) {
            JOptionPane.showMessageDialog(null, "NO HAS SELECCIONADO UN USUARIO");
        } else if (newUser.isEmpty() || newPass.isEmpty() || newFecIni.isEmpty() || newFecFin.isEmpty() || newEdoCta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "HAY CAMPOS VACIOS");
        } else {
            int edoCta = Integer.parseInt(newEdoCta); // convertimos el estado de cuenta a int
            u.setCvPerson(cvPerson);
            u.setLogUser(newUser);
            u.setPassword(newPass);
            u.setEdoCta(edoCta);
            db.agregarUsuario(u, newFecIni, newFecFin); 
            LimpiarCamposUsuarios();
        }
        //System.out.println(viewMantUser.cbxSelecPerson.getSelectedIndex());
    }

    public static void BtnConsultar() {
        DefaultTableModel listaUsuarios = (DefaultTableModel) viewMantUser.tablaUser.getModel();
        listaUsuarios.setRowCount(0); // limpiamos las filas 
        for (Usuario user2 : db.ListarUsuarios()) {
            Object[] fila = new Object[6];
            fila[0] = user2.getCvPerson();
            fila[1] = user2.getLogUser();
            //fila[] = user2.getPassword();
            fila[2] = user2.getFecInicio();
            fila[3] = user2.getFecFinal();
            fila[4] = user2.getEdoCta();
            listaUsuarios.addRow(fila); // aniadimos las filas la tabla
        }
    }

    public static void BtnSalirMenu() {
        MostrarMenu();
        OcultarMantUser();
    }
}
