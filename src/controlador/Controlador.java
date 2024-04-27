package controlador;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import modelo.DataBase;
import vistas.*;

public class Controlador {

    public static VentasVista viewVentas = new VentasVista();
    public static MenuVista viewMenu = new MenuVista();
    public static VistaLogin viewLogin = new VistaLogin();
    public static VistaNuevoPassword viewPass = new VistaNuevoPassword();
   
    // METODOS PARA LA VENTANA DEL LOGIN
    public static void MostrarLogin() {
        viewLogin.setVisible(true);
        viewLogin.setLocationRelativeTo(null);
    }

    public static void OcultarLogin() {
        viewLogin.setVisible(false);
    }

    public static void BtnIngresar() {
        String user = viewLogin.txtUser.getText();
        String password = viewLogin.txtPass.getText();
        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "DIGITE UN USUARIO Y UNA CONTRASENIA");
        } else {
            DataBase bd = new DataBase();
            if (bd.validarUsuario(user, password)) {
                OcultarLogin();
                MostrarMenu();
            } else {
                viewLogin.txtUser.setText("");
                viewLogin.txtPass.setText("");
            }
        }
    }

    public static void CheckBoxPassword() {
        // check pass del login
        if (viewLogin.checBoxPass.isSelected()) {
            viewLogin.txtPass.setEchoChar((char) 0);
        } else {
            viewLogin.txtPass.setEchoChar('*');
        }
        // ceckpass de la pass actual
        if (viewPass.check1.isSelected()) {
            viewPass.txtPassActual.setEchoChar((char) 0);
        } else {
             viewPass.txtPassActual.setEchoChar('*');
        }
        
        //checkpass de la nueva pass 2
        if (viewPass.check2.isSelected()) {
            viewPass.txtPassNuevo.setEchoChar((char) 0);
        } else {
             viewPass.txtPassNuevo.setEchoChar('*');
        }
        
        //check pass de la confirmacion del pass nuevo
        if (viewPass.check3.isSelected()) {
            viewPass.txtPassConfirmar.setEchoChar((char) 0);
        } else {
             viewPass.txtPassConfirmar.setEchoChar('*');
        }
        
    }

    public static void BtnSalir() {
        int confirmar = JOptionPane.showConfirmDialog(null, "ESTA SEGURO QUE DESEA SALIR DEL SISTEMA? ");
        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // METODOS PARA LA VENTANA DE MENU
    public static void MostrarMenu() {
        DataBase db = new DataBase();
        viewMenu.setVisible(true);
        viewMenu.setLocationRelativeTo(null);
        viewMenu.lblUser.setText(db.buscarLog(viewLogin.txtUser.getText()));
    }

    public static void OcultarMenu() {
        viewMenu.setVisible(false);
    }

    public static void ItmCambiarPass() {
        OcultarMenu();
        MostrarCambiarPass();
    }

    public static void ItmCerrarSesion() {
        viewLogin.txtUser.setText("");
        viewLogin.txtPass.setText("");

        OcultarMenu();
        MostrarLogin();
    }

    public static void ItmVentas() {
        OcultarMenu();
        MostrarVentas();
    }

    // METODOS PARA LA VENTANA DE VENTAS
    public static void MostrarVentas() {
        viewVentas.setVisible(true);
        viewVentas.setLocationRelativeTo(null);
    }

    public static void OcultarVentas() {
        viewVentas.setVisible(false);
    }

    public static void BtnMenu() {
        OcultarVentas();
        MostrarMenu();
    }

    public static void MostrarLog() {
        DataBase db = new DataBase();
        String user = viewLogin.txtUser.getText();
        viewVentas.txtLabel.setText(db.buscarLogSinTipPerson(user));
        LocalDate hoy = LocalDate.now();
        String fecha = String.valueOf(hoy);
        viewVentas.labelFecha.setText(fecha);
        
    }

    // METODOS PARA LA VENTANA DE CAMBIAR PASSWORD
    public static void MostrarCambiarPass() {
        viewPass.setVisible(true);
        viewPass.setLocationRelativeTo(null);
    }

    public static void OcultarCambiarPass() {
        viewPass.setVisible(false);
    }

    public static void BtnCancelar() {
        viewPass.txtPassActual.setText("");
        viewPass.txtPassNuevo.setText("");
        viewPass.txtPassConfirmar.setText("");
    }

    public static void BtnRegresar() {
        OcultarCambiarPass();
        MostrarMenu();
    }

}
