/**
 * 
 */
package cl.helpcom.dte;

/**
 * Esta clase se encarga de representar el estado de un DTE en el SII, luego de
 * realizar la consulta en el webservice
 * 
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 * 
 */
public class EstadoDTESII {
	private String estado;
	private String glosa;
	private String errCode;
	private String errGlosa;
	private String numeroAtencion;

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrGlosa() {
		return errGlosa;
	}

	public void setErrGlosa(String errGlosa) {
		this.errGlosa = errGlosa;
	}

	public String getNumeroAtencion() {
		return numeroAtencion;
	}

	public void setNumeroAtencion(String numeroAtencion) {
		this.numeroAtencion = numeroAtencion;
	}

}
