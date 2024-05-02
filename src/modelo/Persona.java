
package modelo;

public class Persona {

    public int cvPerson;
    public String NombreCompleto;

    public Persona(int cvPerson, String NombreCompleto) {
        this.cvPerson = cvPerson;
        this.NombreCompleto = NombreCompleto;
    }

    public Persona() {
    }

    public int getCvPerson() {
        return cvPerson;
    }

    public void setCvPerson(int cvPerson) {
        this.cvPerson = cvPerson;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String NombreCompleto) {
        this.NombreCompleto = NombreCompleto;
    }
    
    @Override
    public String toString() {
        return ""+cvPerson+" "+NombreCompleto;
    }

}
