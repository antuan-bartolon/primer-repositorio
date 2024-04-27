package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DataBase {

    // INICIALIZAMOS VARIABLES
    Connection conexion;
    PreparedStatement ps;
    ResultSet rs;
    
    public DataBase() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/mybdcomp4b10?serverTimezone=UTC", "root", "");
            System.out.println("conexion exitosa");
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getSQLState() + " - " + ex.getMessage());
        }
    } // CONEXION A MY SQL

    // METODOS
    public boolean ValidarUsuario(Usuario user) {
        String validar = "SELECT * FROM musuario WHERE logUser = ? and passUser = ?";
        try {
            ps = conexion.prepareStatement(validar);
            ps.setString(1, user.getLogUser());
            ps.setString(2, user.getPassword());
            rs = ps.executeQuery();
            if (rs.next()) {
                Date fechaIni = rs.getDate("fecIni");
                Date fechaFin = rs.getDate("fecFin");
                LocalDate hoy = LocalDate.now();
                LocalDate fecIni = fechaIni.toLocalDate();
                LocalDate fecFin = fechaFin.toLocalDate();
                if (rs.getInt("edoCta") == 0) {
                    JOptionPane.showMessageDialog(null, "CUENTA CADUCADA\nContacte al adiministrador");
                    return false;
                } else if (hoy.isBefore(fecIni) || hoy.equals(fecIni)) {
                    JOptionPane.showMessageDialog(null, "CUENTA POR ACTIVAR\nContacte al adiministrador");
                    return false;
                } else if (hoy.equals(fecFin)) {
                    JOptionPane.showMessageDialog(null, "TU CUENTA VENCE HOY\nContacte al adiministrador");
                    return true;
                } else if (hoy.isAfter(fecFin)) {
                    String sqlUpdate = "UPDATE musuario SET edoCta = 0 WHERE logUser = ?";
                    PreparedStatement psUpdate = conexion.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, user.getLogUser());
                    psUpdate.executeUpdate();
                    JOptionPane.showMessageDialog(null, "CUENTA VENCIDA\nContacte al adiministrador");
                    return false;
                }
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "USUARIO O CONTRASEÑA INCORRECTOS");
        return false;
    }

    public String BuscarLogueo(Usuario user) {
        String buscar = "SELECT concat(DsNombre,' ',Pater.DsApellido ,' ', Mater.DsApellido,' ','(',DsTpPerson,')') As Nombre \n"
                + "FROM musuario,mdtperson, cNombre, \n"
                + "capellido Pater, capellido Mater, ctpperson \n"
                + "WHERE musuario.logUser= ? \n"
                + "AND musuario.CvPerson = mdtperson.CvPerson \n"
                + "AND mdtperson.CvNombre = cNombre.CvNombre\n"
                + "AND mdtperson.CvApePat = Pater.CvApellido \n"
                + "AND mdtperson.CvApeMat = Mater.CvApellido\n"
                + "AND ctpperson.CvTpPerson = mdtperson.CvTpPerson;";
        String nombre = " ";
        try {
            ps = conexion.prepareStatement(buscar);
            ps.setString(1, user.getLogUser());
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("Nombre");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nombre;
    }

    public String BuscarLogueoSinTipPerson(Usuario user) {
        String buscar = "SELECT concat(DsNombre,' ',Pater.DsApellido ,' ', Mater.DsApellido) As Nombre \n"
                + "FROM musuario,mdtperson, cNombre, \n"
                + "capellido Pater, capellido Mater, ctpperson \n"
                + "WHERE musuario.logUser= ? \n"
                + "AND musuario.CvPerson = mdtperson.CvPerson \n"
                + "AND mdtperson.CvNombre = cNombre.CvNombre\n"
                + "AND mdtperson.CvApePat = Pater.CvApellido \n"
                + "AND mdtperson.CvApeMat = Mater.CvApellido";
        String nombre = " ";
        try {
            ps = conexion.prepareStatement(buscar);
            ps.setString(1, user.getLogUser());
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("Nombre");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nombre;
    }
}
