package modelo;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DataBase {

    // INICIALIZAMOS VARIABLES
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // CONEXION A MY SQL
    public DataBase() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/mybdcomp4b10?serverTimezone=UTC", "root", "");
            System.out.println("conexion exitosa");
        } catch (SQLException ex) {
            System.out.println("ERROR en DataBase: " + ex.getSQLState() + " - " + ex.getMessage());
        }
    }

    // METODOS DE CONSULTAS SQL
    public boolean ValidarUsuario(Usuario user) {
        String validar = "SELECT * FROM musuario WHERE logUser = ? and passUser = ?";
        try {
            ps = conexion.prepareStatement(validar);
            ps.setString(1, user.getLogUser());
            ps.setString(2, user.getPassword());
            rs = ps.executeQuery();
            while (rs.next()) {
                Date fechaIni = rs.getDate("fecIni");
                Date fechaFin = rs.getDate("fecFin");
                LocalDate hoy = LocalDate.now();
                LocalDate fecIni = fechaIni.toLocalDate();
                LocalDate fecFin = fechaFin.toLocalDate();
                if (rs.getInt("edoCta") == 0) {
                    JOptionPane.showMessageDialog(null, "CUENTA CADUCADA\nContacte al administrador");
                    return false;
                } else if (hoy.isBefore(fecIni) || hoy.equals(fecIni)) {
                    JOptionPane.showMessageDialog(null, "CUENTA POR ACTIVAR\nContacte al administrador");
                    return false;
                } else if (hoy.equals(fecFin)) {
                    JOptionPane.showMessageDialog(null, "TU CUENTA VENCE HOY\nContacte al administrador");
                    return true;
                } else if (hoy.isAfter(fecFin)) {
                    String sqlUpdate = "UPDATE musuario SET edoCta = 0 WHERE logUser = ?";
                    PreparedStatement psUpdate = conexion.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, user.getLogUser());
                    psUpdate.executeUpdate();
                    JOptionPane.showMessageDialog(null, "CUENTA VENCIDA\nContacte al administrador");
                    return false;
                }
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("ERROR EN: ValidarUsuario: " + ex.getMessage());
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
            System.out.println("ERROR EN: BuscarLogueo: " + ex.getMessage());
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
            System.out.println("ERROR EN: BuscarLogSinTipoPerson: " + ex.getMessage());
        }

        return nombre;
    }

    public boolean CambiarPassword(Usuario user, String passAnterior, String passNuevo, String passConfirmar) {

        String sentenciaRecorrerPass = "SELECT PassUser FROM musuario";
        String cambiarPass = "UPDATE musuario SET passUser = ? WHERE logUser = ? and PassUser = ? ";
        String passObtenido;
        try {
            ps = conexion.prepareStatement(sentenciaRecorrerPass);
            rs = ps.executeQuery();
            while (rs.next()) {
                passObtenido = rs.getString("PassUser");
                // muestra por consola las paswords existentes en la BD
                // para verificar si se esta haciendo bien el recorrido
                System.out.printf(" %s \n", passObtenido);
                if (passObtenido.equals(passConfirmar)) {
                    JOptionPane.showMessageDialog(null, "EL PASSWORD AL QUE INTENTAS CAMBIAR YA EXISTE, INTENTA CON OTRO");
                    return false;
                } else if (!user.getPassword().equals(passAnterior)) {
                    JOptionPane.showMessageDialog(null, "PASSWORD ANTERIOR INCORRECTO");
                    return false;
                } else if (!passConfirmar.equals(passNuevo)) {
                    JOptionPane.showMessageDialog(null, "VERIFIQUE LOS CAMPOS");
                    return false;
                }
            } // fin del while

        } catch (Exception e) {
            System.out.println("ERROR EN: CambiarPasswordTry1: " + e.getMessage());
        }
        try {
            ps = conexion.prepareStatement(cambiarPass);
            ps.setString(1, passConfirmar);
            ps.setString(2, user.getLogUser());
            ps.setString(3, passAnterior);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("ERROR EN: CambiarPasswordTry2: " + ex.getMessage());
        }
        // se hace el vambio si esque todo es valido
        return true;
    }

    public ArrayList<Persona> MostrarPersonas() {
        ArrayList<Persona> nombresCompletos = new ArrayList<>();
        String consulta = "SELECT \n"
                + "mDtPerson.CvPerson,"
                + "CONCAT(cNombre.DsNombre, ' ',Pater.DsApellido, ' ', \n"
                + "Mater.DsApellido,' (',ctpperson.DsTpPerson,')') AS NombreCompleto \n"
                + "FROM \n"
                + "mDtPerson, cNombre, \n"
                + "cApellido Pater,cApellido Mater,ctpperson\n"
                + "WHERE \n"
                + "mDtPerson.CvNombre = cNombre.CvNombre\n"
                + "AND mDtPerson.CvApePat = Pater.CvApellido\n"
                + "AND mDtPerson.CvApeMat = Mater.CvApellido \n"
                + "AND mDtperson.CvTpPerson = cTpPerson.CvTpPerson\n"
                + "ORDER BY CvPerson;";

        try {
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nombresCompletos.add(new Persona(rs.getInt("cvPerson"), rs.getString("NombreCompleto")));
                //Persona p = new Persona();
                //p.setCvPerson(rs.getInt("CvPerson"));
                //p.setNombreCompleto(rs.getString("NombreCompleto"));
                //nombresCompletos.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR EN: MostrarPersonas: " + ex.getMessage());
        }
        return nombresCompletos;

    }

    public ArrayList<Usuario> ListarUsuarios() {
        ArrayList<Usuario> usuariosList = new ArrayList<>();
        String consultaSql = "SELECT * FROM musuario;";
        try {
            PreparedStatement ps = conexion.prepareStatement(consultaSql);
            ResultSet rs = ps.executeQuery();
            System.out.println("CONSULTA EXITOSA!");
            while (rs.next()) {
                Usuario user2 = new Usuario();
                user2.setCvPerson(rs.getInt("cvPerson"));
                user2.setLogUser(rs.getString("logUser"));
                user2.setPassword(rs.getString("passUser"));
                user2.setFecInicio(rs.getDate("fecIni"));
                user2.setFecFinal(rs.getDate("fecFin"));
                user2.setEdoCta(rs.getInt("edoCta"));
                usuariosList.add(user2);
            }
        } catch (Exception e) {
            System.out.println("ERROR en ListarUsuarios: " + e.getMessage());
        }
        return usuariosList;
    }

    // clase para el insert
    public void agregarUsuario(Usuario newUser, String fecIni, String fecFin) {
        //Usuario u = new Usuario();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date fechaUtil = formato.parse(fecIni);
            java.sql.Date fechaIni = new java.sql.Date(fechaUtil.getTime());
            System.out.println("Fecha SQL: " + fechaIni);
            newUser.setFecInicio(fechaIni);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ERROR AL CONVERTIR FECHA");
        }
        try {
            java.util.Date fechaUtil = formato.parse(fecFin);
            java.sql.Date fechaFin = new java.sql.Date(fechaUtil.getTime());
            System.out.println("Fecha SQL: " + fechaFin);
            newUser.setFecFinal(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ERROR AL CONVERTIR FECHA");
        }
        String insertInto = "INSERT INTO musuario (cvPerson,logUser,passUser,fecIni,fecFin,edoCta) \n"
                + "VALUES (?,?,?,?,?,?)";
        try {
            ps = conexion.prepareStatement(insertInto);
            ps.setInt(1, newUser.getCvPerson());
            ps.setString(2, newUser.getLogUser());
            ps.setString(3, newUser.getPassword());
            ps.setDate(4, (Date) newUser.getFecInicio());
            ps.setDate(5, (Date) newUser.getFecFinal());
            ps.setInt(6, newUser.getEdoCta());
            ps.executeUpdate();
            System.out.println("USUARIO AGREGADO CON EXITO!");
        } catch (SQLException e) {
            System.out.println("ERROR en: agregarUsuario: " + e.getMessage());
        }
    }

}
