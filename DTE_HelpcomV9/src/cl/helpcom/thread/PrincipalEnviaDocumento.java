package cl.helpcom.thread;

import cl.helpcom.dte.method.CronometroEnviaDocumento;

public class PrincipalEnviaDocumento {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CronometroEnviaDocumento c = new CronometroEnviaDocumento();
		c.run();
	}
}	