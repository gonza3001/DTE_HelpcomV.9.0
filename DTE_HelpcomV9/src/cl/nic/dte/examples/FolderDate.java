/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.nic.dte.examples;

import java.io.File;
import java.util.Date;

/**
 *
 * @author Mauro
 */
public class FolderDate {

    public FolderDate() {
    }

    public String folderYear() {

        String sMes = "";
        java.util.Date dateFolder = new Date();

        switch (dateFolder.getMonth()+1) {

            case 1:
                sMes = "Enero";
                break;
            case 2:
                sMes = "Febrero";
                break;
            case 3:
                sMes = "Marzo";
                break;
            case 4:
                sMes = "Abril";
                break;
            case 5:
                sMes = "Mayo";
                break;
            case 6:
                sMes = "Junio";
                break;
            case 7:
                sMes = "Julio";
                break;
            case 8:
                sMes = "Agosto";
                break;
            case 9:
                sMes = "Septiembre";
                break;
            case 10:
                sMes = "Octubre";
                break;
            case 11:
                sMes = "Noviembre";
                break;
            case 12:
                sMes = "Diciembre";
                break;
        }

        String ruta = "facturas/respaldo/" + dateFolder.getYear() + "/" + sMes;
        File folder = new File(ruta);
        folder.mkdirs();

        return ruta;
    }

}
