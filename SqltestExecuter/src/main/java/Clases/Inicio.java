package Clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.fabric.xmlrpc.base.Array;

import MetodosSql.Credenciales;
import MetodosSql.MetodosSql;

public class Inicio {
	private static ArrayList<ArrayList<String>>Ips=null;
	private static HashMap<String,String>ipEstado=new HashMap<String,String>();
	
	private static ArrayList<ArrayList<String>>comandos=null;
	private static MetodosSql metodos=new MetodosSql(
			Credenciales.ip_CCCC,
			Credenciales.base_CCCC,
			Credenciales.usuario_CCCC,
			Credenciales.password_CCCC);

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		comandos=metodos.consultar("select prueba_id,prueba_comando from prueba");
		
		monitorear();	

	}
	private static void monitorear() throws UnknownHostException, IOException {
		
		String prueba_id;
		String comando;
		String accion;
		for(int i=0;i<comandos.size();) {			
			
			prueba_id=comandos.get(i).get(0);
			comando=comandos.get(i).get(1);
			
			accion=metodos.consultarUnaCelda("select accion from tiempos_ejecucion where prueba_id="+prueba_id);
			
			System.out.println(accion+"->"+prueba_id);
			
			if(!accion.contentEquals("esperar")) {
			if(ejecutarPrueba(comando)==1) {
				System.out.println ("OK");			
					
				actualizarEstado( 1, prueba_id);
							
			}else {
				System.out.println ("FALLA");	
				actualizarEstado( 0, prueba_id);
			}
			if(i==comandos.size()-1) {
				i=0;
			}else {
				i++;
			}
				
		}else {
			if(i==comandos.size()-1) {
				i=0;
			}else {
				i++;
				System.gc();
			}
		}
		}
	}
	
	private static int ejecutarPrueba(String comando) {
		
		int valorDevuelto=0;
		
		try {
			Process process = Runtime.getRuntime().exec(comando);
			process.getInputStream();
			process.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(process.getInputStream()));
			
				String s = null;
			
				s = stdInput.readLine();
				if(s!=null) {
					System.out.println("El valor devuelto fué ->"+s);
					}else if(s==null) {
						System.out.println("Valor nulo en la salida de la ejecución de su cmd");
					}
				
				if(s.contains("OK")) {
					valorDevuelto=1;
				}else {
					valorDevuelto=0;
					
				}
				
				escribirFichero(comando+" -> "+s,"C:/xampp/htdocs/Mapa/horaProcesoPrueba.txt");
				
					
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		return valorDevuelto;
		
	}
	
	private static void actualizarEstado(int estado,String prueba_id) {
		metodos.insertarOmodif("update prueba set prueba_resultado="+estado+" where "
				+ "prueba_id='"+prueba_id+"';");
	}
	
	private static void escribirFichero(String host,String url) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(url);
            pw = new PrintWriter(fichero);
            
                pw.println("Escaneando host "+host+" "+metodos.dameFechaDeHoyConFormatoX("yyyy'-'MM'-'dd' 'hh':'mm':'ss"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	

}
