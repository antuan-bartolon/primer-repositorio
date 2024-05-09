package modelo;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
    public boolean ValidarUsuario(Usuario usuario) {
        String validar = "SELECT * FROM musuario WHERE logUser = ? and passUser = ?";
        try {
            ps = conexion.prepareStatement(validar);
            ps.setString(1, usuario.getLogUser());
            ps.setString(2, usuario.getPassword());
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
                } else if (hoy.equals(fecIni)) {
                    JOptionPane.showMessageDialog(null, "TU CUENTA SE ACTIVO HOY");
                    return true;
                } else if (hoy.isBefore(fecIni)) {
                    JOptionPane.showMessageDialog(null, "CUENTA POR ACTIVAR\nContacte al administrador");
                    return false;
                } else if (hoy.equals(fecFin)) {
                    JOptionPane.showMessageDialog(null, "TU CUENTA VENCE HOY\nContacte al administrador");
                    return true;
                } else if (hoy.isAfter(fecFin)) {
                    String sqlUpdate = "UPDATE musuario SET edoCta = 0 WHERE logUser = ?";
                    PreparedStatement psUpdate = conexion.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, usuario.getLogUser());
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

    public String BuscarLogueo(Usuario user2) {
        String buscar = "SELECT concat(DsNombre,' ',Pater.DsApellido ,' ', Mater.DsApellido,' ','(',DsTpPerson,')') As Nombre \n"
                + "FROM musuario,mdtperson, cNombre, \n"
                + "capellido Pater, capellido Mater, ctpperson \n"
                + "WHERE musuario.logUser= ? \n"
                + "AND musuario.CvPerson = mdtperson.CvPerson \n"
                + "AND mdtperson.CvNombre = cNombre.CvNombre\n"
                + "AND mdtperson.CvApePat = Pater.CvApellido \n"
                + "AND mdtperson.CvApeMat = Mater.CvApellido\n"
                + "AND ctpperson.CvTpPerson = mdtperson.CvTpPerson;";
        String nombreCompleto = " ";
        try {
            ps = conexion.prepareStatement(buscar);
            ps.setString(1, user2.getLogUser());
            rs = ps.executeQuery();
            if (rs.next()) {
                nombreCompleto = rs.getString("Nombre");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR EN: BuscarLogueo: " + ex.getMessage());
        }
        return nombreCompleto;
    }

    public String BuscarLogueoSinTipPerson(Usuario usuario) {
        String buscar = "SELECT concat(DsNombre,' ',Pater.DsApellido ,' ', Mater.DsApellido) As Nombre \n"
                + "FROM musuario,mdtperson, cNombre, \n"
                + "capellido Pater, capellido Mater, ctpperson \n"
                + "WHERE musuario.logUser= ? \n"
                + "AND musuario.CvPerson = mdtperson.CvPerson \n"
                + "AND mdtperson.CvNombre = cNombre.CvNombre\n"
                + "AND mdtperson.CvApePat = Pater.CvApellido \n"
                + "AND mdtperson.CvApeMat = Mater.CvApellido";
        String nombreSinPuesto = " ";
        try {
            ps = conexion.prepareStatement(buscar);
            ps.setString(1, usuario.getLogUser());
            rs = ps.executeQuery();
            if (rs.next()) {
                nombreSinPuesto = rs.getString("Nombre");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR EN: BuscarLogSinTipoPerson: " + ex.getMessage());
        }
        return nombreSinPuesto;
    }

    public boolean CambiarPassword(Usuario usuario, String passAnterior, String passNuevo, String passConfirmar) {
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
                } else if (!usuario.getPassword().equals(passAnterior)) {
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
        //
        try {
            ps = conexion.prepareStatement(cambiarPass);
            ps.setString(1, passConfirmar);
            ps.setString(2, usuario.getLogUser());
            ps.setString(3, passAnterior);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("ERROR EN: CambiarPasswordTry2: " + ex.getMessage());
        }
        // se hace el cambio si esque todo es valido
        return true;
    }

    public ArrayList<Persona> MostrarPersonas() {
        ArrayList<Persona> nombresCompletos = new ArrayList<>();
        String consulta = "call mostrarPersonas()";
        try {
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nombresCompletos.add(new Persona(rs.getInt("cvPerson"), rs.getString("NombreCompleto")));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR EN: MostrarPersonas: " + ex.getMessage());
        }
        return nombresCompletos;
    }

    public ArrayList<Usuario> ListarUsuarios() {
        ArrayList<Usuario> usuariosList = new ArrayList<>();
        String consultaSql = "call listarUsuarios();";
        try {
            PreparedStatement ps = conexion.prepareStatement(consultaSql);
            ResultSet rs = ps.executeQuery();
            System.out.println("MOSTRANDO USUARIOS EXISTENTES!");
            while (rs.next()) {
                Usuario user2 = new Usuario();
                user2.setCvUser(rs.getInt("cvUser"));
                user2.setCvPerson(rs.getInt("cvPerson"));
                user2.setLogUser(rs.getString("logUser"));
                user2.setPassword(rs.getString("passUser"));
                user2.setFecInicio(rs.getDate("fecIni"));
                user2.setFecFinal(rs.getDate("fecFin"));
                user2.setEdoCta(rs.getInt("edoCta"));
                user2.setDatosPersonales(rs.getString("DatosCompletos"));
                usuariosList.add(user2);
            }
        } catch (Exception e) {
            System.out.println("ERROR en ListarUsuarios: " + e.getMessage());
        }
        return usuariosList;
    }

    // clase para insert
    public boolean AgregarUsuario(Usuario usuario2, String fecIni, String fecFin) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate hoy = LocalDate.now(); // Fecha actual
        java.sql.Date fechaIniSql = null;
        java.sql.Date fechaFinSql = null;

        // Validar la longitud de la contraseña
        if (usuario2.getPassword().length() < 4) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres.");
            return false; // Retornar false si la contraseña es demasiado corta
        }
        //
        String buscarRepetidos = "SELECT logUser, passUser FROM musuario";
        try {
            ps = conexion.prepareStatement(buscarRepetidos);
            rs = ps.executeQuery();
            while (rs.next()) {                
                String logObtenido = rs.getString("logUser");
                String passObtenido = rs.getString("passUser");
                if (usuario2.getLogUser().equals(logObtenido)) {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. intente con otro.");
                    return false;
                } else if (usuario2.getPassword().equals(passObtenido)) {
                    JOptionPane.showMessageDialog(null, "El Password ya existe, intente con otro.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR en veririficar logs y pass: "+e.getMessage());
            return false;
        }

        try {
            // Convertir y verificar la fecha de inicio
            java.util.Date fechaIniUtil = formato.parse(fecIni);
            fechaIniSql = new java.sql.Date(fechaIniUtil.getTime());
            LocalDate fechaIniLocal = fechaIniUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaIniLocal.isBefore(hoy)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio no puede ser anterior a la fecha actual.");
                return false;
            }
            usuario2.setFecInicio(fechaIniSql);

            // Convertir y verificar la fecha de fin
            java.util.Date fechaFinUtil = formato.parse(fecFin);
            fechaFinSql = new java.sql.Date(fechaFinUtil.getTime());
            LocalDate fechaFinLocal = fechaFinUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaFinLocal.isBefore(fechaIniLocal)) {
                JOptionPane.showMessageDialog(null, "La fecha final no puede ser anterior a la fecha de inicio.");
                return false;
            }
            usuario2.setFecFinal(fechaFinSql);

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ERROR AL CONVERTIR FECHA");
            return false;
        }
        // Insertar en la base de datos
        String insertInto = "INSERT INTO musuario (cvPerson, logUser, passUser, fecIni, fecFin, edoCta) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = conexion.prepareStatement(insertInto);
            ps.setInt(1, usuario2.getCvPerson());
            ps.setString(2, usuario2.getLogUser());
            ps.setString(3, usuario2.getPassword());
            ps.setDate(4, fechaIniSql);
            ps.setDate(5, fechaFinSql);
            ps.setInt(6, usuario2.getEdoCta());
            ps.executeUpdate();
            System.out.println("USUARIO AGREGADO CON EXITO!");
        } catch (SQLException e) {
            System.out.println("ERROR en agregarUsuario: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ModificarUsuario(Usuario usuarioEdit, String fecini, String fecfin) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate hoy = LocalDate.now(); // Fecha actual
        java.sql.Date fechaIniSql = null;
        java.sql.Date fechaFinSql = null;

        // Validar la longitud de la contraseña
        if (usuarioEdit.getPassword().length() < 4) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres.");
            //System.out.println("La contraseña debe tener al menos 4 caracteres.");
            return false; // Retornar false si la contraseña es demasiado corta
        }
        
         String buscarRepetidos = "SELECT logUser, passUser FROM musuario";
        try {
            ps = conexion.prepareStatement(buscarRepetidos);
            rs = ps.executeQuery();
            while (rs.next()) {                
                //String logObtenido = rs.getString("logUser");
                String passObtenido = rs.getString("passUser");
                 if (usuarioEdit.getPassword().equals(passObtenido)) {
                    JOptionPane.showMessageDialog(null, "El Password ya existe, intente con otro.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR en veririficar logs y pass de ModificarUsuario: "+e.getMessage());
            return false;
        }

        try {
            // Convertir y verificar la fecha de inicio
            java.util.Date fechaIniUtil = formato.parse(fecini);
            fechaIniSql = new java.sql.Date(fechaIniUtil.getTime());
            LocalDate fechaIniLocal = fechaIniUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaIniLocal.isBefore(hoy)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio no puede ser anterior a la fecha actual.");
                return false;
            }
            usuarioEdit.setFecInicio(fechaIniSql);

            // Convertir y verificar la fecha de fin
            java.util.Date fechaFinUtil = formato.parse(fecfin);
            fechaFinSql = new java.sql.Date(fechaFinUtil.getTime());
            LocalDate fechaFinLocal = fechaFinUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaFinLocal.isBefore(fechaIniLocal)) {
                JOptionPane.showMessageDialog(null, "La fecha final no puede ser anterior a la fecha de inicio.");
                return false;
            }
            usuarioEdit.setFecFinal(fechaFinSql);

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ERROR AL CONVERTIR FECHA");
            return false;
        }

        String modificar = "UPDATE musuario set logUser = ?, passUser = ?, FecIni = ?, Fecfin = ?, EdoCta = ? WHERE cvUser = ?";
        try {
            ps = conexion.prepareStatement(modificar);
            ps.setString(1, usuarioEdit.getLogUser());
            ps.setString(2, usuarioEdit.getPassword());
            ps.setDate(3, (Date) usuarioEdit.getFecInicio());
            ps.setDate(4, (Date) usuarioEdit.getFecFinal());
            ps.setInt(5, usuarioEdit.getEdoCta());
            ps.setInt(6, usuarioEdit.getCvUser());
            ps.execute();
        } catch (SQLException ex) {
            System.out.println("ERROR en: ModificarUsuario: " + ex.getMessage());
        }
        return true;
    }

    public void BorrarUsuario(Usuario usuarioDelete) {
        String deleteFrom = "DELETE FROM musuario WHERE CvUser = ?";
        try {
            ps = conexion.prepareStatement(deleteFrom);
            ps.setInt(1, usuarioDelete.getCvUser());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR en: borrarUsuario: " + e.getMessage());
        }
    }

}
