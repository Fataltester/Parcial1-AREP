/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.eci.arep.parcial1;
import edu.eci.arep.parcial1.HTTPBackend;
import java.io.IOException;
import java.net.URISyntaxException;
/**
 *
 * @author juan.mmendez
 */
public class Parcial1 {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello World!");
        HTTPBackend back = new HTTPBackend();
        back.main(args);
    }
}
