
package modelo;
import java.util.Date;

public class Usuario {
    
    private int cvUser;
    private int cvPerson;
    private String logUser;
    private String Password;
    private Date fecInicio;
    private Date fecFinal;
    private int edoCta; 
    
    private String DatosPersonales;

    public String getDatosPersonales() {
        return DatosPersonales;
    }

    public void setDatosPersonales(String DatosPersonales) {
        this.DatosPersonales = DatosPersonales;
    }
    
    public Usuario() {
    }

    public Usuario(int cvUser, int cvPerson, String logUser, String Password, Date fecInicio, Date fecFinal, int edoCta,String DatosPersonales) {
        this.cvUser = cvUser;
        this.cvPerson = cvPerson;
        this.logUser = logUser;
        this.Password = Password;
        this.fecInicio = fecInicio;
        this.fecFinal = fecFinal;
        this.edoCta = edoCta;
        this.DatosPersonales = DatosPersonales;
    }

    public int getCvUser() {
        return cvUser;
    }

    public void setCvUser(int cvUser) {
        this.cvUser = cvUser;
    }

    public int getCvPerson() {
        return cvPerson;
    }

    public void setCvPerson(int cvPerson) {
        this.cvPerson = cvPerson;
    }

    public String getLogUser() {
        return logUser;
    }

    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
    }

    public Date getFecFinal() {
        return fecFinal;
    }

    public void setFecFinal(Date fecFinal) {
        this.fecFinal = fecFinal;
    }

    public int getEdoCta() {
        return edoCta;
    }

    public void setEdoCta(int edoCta) {
        this.edoCta = edoCta;
    }
    
    
            
    
}
