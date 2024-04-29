
package modelo;

public class PasswordsCampos {
    
    String passAnterior;
    String passNuevo;
    String confirmarPass;

    public PasswordsCampos() {
    }

    public PasswordsCampos(String passAnterior, String passNuevo, String confirmarPass) {
        this.passAnterior = passAnterior;
        this.passNuevo = passNuevo;
        this.confirmarPass = confirmarPass;
    }

    public String getPassAnterior() {
        return passAnterior;
    }

    public void setPassAnterior(String passAnterior) {
        this.passAnterior = passAnterior;
    }

    public String getPassNuevo() {
        return passNuevo;
    }

    public void setPassNuevo(String passNuevo) {
        this.passNuevo = passNuevo;
    }

    public String getConfirmarPass() {
        return confirmarPass;
    }

    public void setConfirmarPass(String confirmarPass) {
        this.confirmarPass = confirmarPass;
    }
    
    
}
