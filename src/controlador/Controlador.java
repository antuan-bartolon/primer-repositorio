package controlador;

import javax.swing.JOptionPane;
import modelo.DataBase;
import vistas.*;

public class Controlador {
    
    public static VentasVista vv = new VentasVista();
    public static LoginVista lv = new LoginVista();
    public static MenuVista mv = new MenuVista();
    public static VistaLogin vl = new VistaLogin();
    
    public static void mostrarLogin() {
        vl.setVisible(true);
        vl.setLocationRelativeTo(null);
        
        
    }
    
    public static void ocultarLogin() {
        vl.setVisible(false);
    }
    
    public static void btnIngresar() {
        String user = vl.txtUser.getText();
        String password = vl.txtPass.getText();
        if (user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "DIGITE UN USUARIO Y UNA CONTRASENIA");
        } else {
            DataBase bd = new DataBase();
            if (bd.validarUsuario(user, password)) {
                ocultarLogin();
                mostrarMenu();
                
            } else {
                JOptionPane.showMessageDialog(null, "ACCESO DENEGADO");
                vl.txtUser.setText("");
               vl.txtPass.setText("");
            }
        }
    }
    
    public static void checkBoxPassword() {
        if (vl.checBoxPass.isSelected()) {
            vl.txtPass.setEchoChar((char) 0);
        } else {
            vl.txtPass.setEchoChar('*');
        }
    }
    
    public static void btnSalir() {
        int confirmar = JOptionPane.showConfirmDialog(null, "ESTA SEGURO QUE DESEA SALIR DEL SISTEMA? ");
        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void mostrarMenu() {
        mv.setVisible(true);
        mv.setLocationRelativeTo(null);
        mv.lblUser.setText(vl.txtUser.getText());
    }
    
    public static void ocultarMenu() {
        mv.setVisible(false);
        mv.setLocationRelativeTo(null);
    }
    
    public static void itmCerrarSesion() {
        vl.txtUser.setText("");
        vl.txtPass.setText("");
        ocultarMenu();
        mostrarLogin();
    }
    
    public static void itmVentas() {
        ocultarMenu();
        mostrarVentas();
    }
    
    public static void mostrarVentas() {
        vv.setVisible(true);
        vv.setLocationRelativeTo(null);
    }
    
    public static void ocultarVentas() {
        vv.setVisible(false);
        vv.setLocationRelativeTo(null);
        
    }
    
    public static void btnMenu() {
        ocultarVentas();
        mostrarMenu();
    }
    
    public static void mostrarLog() {
        DataBase db = new DataBase();
        String user = vl.txtUser.getText();
        vv.txtLabel.setText(db.buscarLog(user));
        
    }
    
}
