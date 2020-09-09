package Clases;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private static MetodosSql metodos=new MetodosSql(
			Credenciales.ip_CCCC,
			Credenciales.base_CCCC,
			Credenciales.usuario_CCCC,
			Credenciales.password_CCCC);

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Ips=metodos.consultar("select ip_address from chinchetas where es_monitoreable=1");
		
		monitorear();	

	}
	private static void monitorear() throws UnknownHostException, IOException {
		String host=null;
		for(int i=0;i<Ips.size();i++) {			
			
			host=Ips.get(i).get(0);
			escribirFichero(host,"C:/xampp/htdocs/Mapa/horaProcesoMapa.txt");
			if(ejecutarPrueba("C:\\xampp\\htdocs\\Mapa\\Pinger.jar", host+" 1000 4")>0) {//Inet4Address.getByName(host).isReachable(1000)
				System.out.println (host+" OK");
				
				if(ipEstado.get(host)==null) {
				ipEstado.put(host, "OK");	
				actualizarEstado( 1, host);
				}
				
				if(ipEstado.get(host).equals("NoOK")) {
					ipEstado.replace(host, "OK");
					actualizarEstado( 1, host);
				}
				
			}else {
				
				System.out.println(host+" NoOK");
				
				if(ipEstado.get(host)==null) {
					ipEstado.put(host, "NoOK");	
					actualizarEstado( 2, host);
				}
				if(ipEstado.get(host).equals("OK")) {
					ipEstado.replace(host, "NoOK");
					actualizarEstado( 2, host);
				}
				
			}		
			
		
			if(i==Ips.size()-1) {
			try {
				Thread.sleep(1000);
				escribirFichero(host,"C:/xampp/htdocs/Mapa/horaProcesoMapa.txt");
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			i=-1;
			
		}
	}
		
 
		//monitorear();
	}
	private static int ejecutarPrueba(String ejecutable,String parametros) {
		String prevCommand="";
		int valorDevuelto=0;
		if(ejecutable.contains("jar")) {
			prevCommand="java -jar ";
		}
		try {
			Process process = Runtime.getRuntime().exec(prevCommand+ejecutable+" "+parametros);
			process.waitFor();
			valorDevuelto=process.exitValue();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valorDevuelto;
		
	}
	
	private static void actualizarEstado(int estado,String host) {
		metodos.insertarOmodif("update chinchetas set estado_id="+estado+" where "
				+ "ip_address='"+host+"';");
	}
	
	private static void escribirFichero(String host,String url) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        String estado=ipEstado.get(host);
        if(estado==null)estado="Desconocido";
        try
        {
            fichero = new FileWriter(url);
            pw = new PrintWriter(fichero);
            
                pw.println("Escaneando host "+host+" "+"Estado->: "+estado+" "+metodos.dameFechaDeHoyConFormatoX("yyyy'-'MM'-'dd' 'hh':'mm':'ss"));

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
