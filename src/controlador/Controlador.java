package controlador;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import modelo.DataBase;
import modelo.Usuario;
import vistas.*;

public class Controlador {

    // INSTANCIAS DE LAS CLASES DE NUESTRAS VISTAS
    public static VentasVista viewVentas = new VentasVista();
    public static MenuVista viewMenu = new MenuVista();
    public static VistaLogin viewLogin = new VistaLogin();
    public static VistaNuevoPassword viewPass = new VistaNuevoPassword();
    // INSTANCIAMOS NUESTRO MODELO
    public static DataBase db = new DataBase();
    public static Usuario usuario = new Usuario();

    // METODOS PARA LA VENTANA DEL LOGIN
    public static void MostrarLogin() {
        viewLogin.setVisible(true);
        viewLogin.setLocationRelativeTo(null);
    }
    
    public static void OcultarLogin() {
        viewLogin.setVisible(false);
    }
    
    public static void BtnIngresarLogin() {
        String user = viewLogin.txtUser.getText();
        String password = viewLogin.txtPass.getText();
        usuario.setLogUser(user);
        usuario.setPassword(password);
        
        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor digite un usuario y una Contraseña");
        } else {
            if (db.ValidarUsuario(usuario)) {
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
    
    public static void BtnSalirLogin() {
        int confirmar = JOptionPane.showConfirmDialog(null, "Esta usted seguro que desea salir del sistma? ");
        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // METODOS PARA LA VENTANA DE MENU
    public static void MostrarMenu() {
        viewMenu.setVisible(true);
        viewMenu.setLocationRelativeTo(null);
        //mostramos los datos del usuario
        viewMenu.lblUser.setText(db.BuscarLogueo(usuario));
    }
    
    public static void OcultarMenu() {
        viewMenu.setVisible(false);
    }
    
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

    // METODOS PARA LA VENTANA DE VENTAS
    public static void MostrarVentas() {
        viewVentas.setVisible(true);
        viewVentas.setLocationRelativeTo(null);
    }
    
    public static void OcultarVentas() {
        viewVentas.setVisible(false);
    }
    
    public static void BtnMenuVenta() {
        OcultarVentas();
        MostrarMenu();
    }
    
    public static void MostrarUserVenta() {
        viewVentas.txtLabel.setText(db.BuscarLogueoSinTipPerson(usuario));
        LocalDate hoy = LocalDate.now();
        String fecha = String.valueOf(hoy);
        viewVentas.labelFecha.setText(fecha);
    }

    // METODOS PARA LA VENTANA DE CAMBIAR PASSWORD
    public static void MostrarVentanaCambioPass() {
        viewPass.setVisible(true);
        viewPass.setLocationRelativeTo(null);
        LimpiarCamposCambiarPass();
    }
    
    public static void OcultarCambiarPass() {
        viewPass.setVisible(false);
    }
    
    public static void BtnCancelarCambio() {
        viewPass.txtPassActual.setText("");
        viewPass.txtPassNuevo.setText("");
        viewPass.txtPassConfirmar.setText("");
    }
    
    public static void BtnRegresar() {
        OcultarCambiarPass();
        MostrarMenu();
    }
    
    public static void BtnAceptar() {
        String passActual = viewPass.txtPassActual.getText();
        String passNuevo = viewPass.txtPassNuevo.getText();
        String passConfirmar = viewPass.txtPassConfirmar.getText();
        if (db.CambiarPassword(usuario, passActual, passNuevo, passConfirmar)) {
            OcultarCambiarPass();
            MostrarMenu();
            JOptionPane.showMessageDialog(null, "EL CAMBIO SE HIZO CON EXITO!!!");
            usuario.setPassword(passConfirmar);
        } else {
            LimpiarCamposCambiarPass();
            System.out.println("algo ocurrio mal");
        }
        
    }
    
    public static void LimpiarCamposCambiarPass() {
        viewPass.txtPassActual.setText("");
        viewPass.txtPassNuevo.setText("");
        viewPass.txtPassConfirmar.setText("");
    }
}
