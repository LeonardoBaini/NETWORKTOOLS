package MetodosSql;
 
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



 
public class MetodosSql extends Conexion {
	
	String server;
	String database;
	String usuario;
	String password;

     
    public MetodosSql(String server,String database,String usuario,String password) {
    	this.server=server;
    	this.database=database;
    	this.usuario=usuario;
    	this.password=password;
    }   
     
         
    public static String dameFechaDeHoy(){
         SimpleDateFormat formateador = new SimpleDateFormat("yyyy'-'MM'-'dd", new Locale("es_ES"));
         Date fechaDate = new Date();
          String fecha=formateador.format(fechaDate);
           
     
    return fecha;
    }
     
    public String dameFechaDeHoyConFormatoX(String formatoFechaseparadoXguionyGuionEntreComillas){//el MM va con mayuscula
         SimpleDateFormat formateador = new SimpleDateFormat(formatoFechaseparadoXguionyGuionEntreComillas, new Locale("es_ES"));
         Date fechaDate = new Date();
         String fecha=formateador.format(fechaDate);
          
     
    return fecha;
    }
    
 
    public int insertarOmodif2(String sentenciaSql) {
        System.out.println(sentenciaSql);
        //System.out.println("Luego borrar este syso, solo es para mostrar los datos enviados a la base, (metodosSql linea 34 y 35)");
        int status=0;
        Conexion con = new Conexion();
 
        try {
            con.conectar(this.server,this.database,this.usuario,this.password);
            con.statemente.execute(sentenciaSql);
 
            con.desconectar();
            status=1;
             
 
        } catch (SQLException e) {
            System.out.println("Error en insertarOmodificar");
            System.out.println(e.getMessage());
            con.desconectar();
            status=-1;
           
        }
        return status;
 
    }
    public int insertarOmodif(String sentenciaSql) {
        System.out.println(sentenciaSql);
        //System.out.println("Luego borrar este syso, solo es para mostrar los datos enviados a la base, (metodosSql linea 34 y 35)");
        int status=0;
        Conexion con = new Conexion();
       
 
        try {
            con.conectar(this.server,this.database,this.usuario,this.password) ;
            con.statemente.executeUpdate(sentenciaSql);
 
            con.desconectar();
            status=1;
            
 
        } catch (SQLException e) {
            System.out.println("Error en insertarOmodificar");
            System.out.println(e.getMessage());
            con.desconectar();
            status=-1;
           
        }
        return status;
 
    }
 
    public ArrayList<ArrayList<String>> consultar(String SentenciaSql) {
        ResultSet res =null;
        ArrayList<ArrayList<String>> matriz = new ArrayList<ArrayList<String>>();//creo una matriz
        String aux=null;
         
        Conexion con = Conexion.getInstance();
         
         
        try {
            con.conectar(this.server,this.database,this.usuario,this.password);
            con.resulsete=con.statemente.executeQuery(SentenciaSql);
            res = con.resulsete;
            ResultSetMetaData rmd = res.getMetaData(); //guardo los datos referentes al resultset
             
              
                while ( res.next()){
                        ArrayList<String> columnas = new ArrayList<String>();
                         for (int i=1; i<=rmd.getColumnCount(); i++) {
                             aux=res.getString(i);            
                                  
                             columnas.add(aux);
                         }
                         matriz.add(columnas);
                }
            con.desconectar();
 
             
 
        } catch (Exception e) {
            System.out.println("Error en metodosSql.consultar"+e.getMessage());
            System.out.println(e.getLocalizedMessage());
          
             
        }
 
        return matriz;
         
 
    }
     
         
         
     
    public String ejecutarBackup(String SentenciaSql) {
       
         
        Conexion con = new Conexion();
       // System.out.println("Mostrando Query --->"+SentenciaSql+"<---");
         
        try {
            con.conectar(this.server,this.database,this.usuario,this.password);
            if(con!= null) {
           // con.resulsete=
            		con.statemente.executeQuery(SentenciaSql);
          /*  res = con.resulsete;
             
             
              
                while ( res.next()){
                     
                    arreglo.add(res.getString(1));
                }*/
            con.desconectar();
 
            }
 
        } catch (Exception e) {
            return ("Error en metodosSql.consultarUnaColumna"+e.getMessage());
          //  BuscadorErrores.errores.add("Error en metodosSql.consultarUnaColumna l155"+e.getMessage());
          
          
             
        }
 
        return "ok";
         
 
    }
   
    public String getDatabase() {
		return database;
	}




	public void setDatabase(String database) {
		this.database = database;
	}




	public String consultarUnaCelda(String SentenciaSql) {
        ResultSet res =null;
        ArrayList<String> arreglo = new ArrayList<String>();//creo una matriz
         
         
        Conexion con = new Conexion();
    //    System.out.println("Mostrando Query --->"+SentenciaSql+"<---");
         
        try {
            con.conectar(this.server,this.database,this.usuario,this.password);
            if(con!= null) {
            con.resulsete=con.statemente.executeQuery(SentenciaSql);
            res = con.resulsete;
             
             
              
                while ( res.next()){
                     
                    arreglo.add(res.getString(1));
                }
            con.desconectar();
 
            }
 
        } catch (Exception e) {
        	//BuscadorErrores.errores.add("Error en metodosSql.consultarUnaCelda l191"+e.getMessage());
           
          
          
             
        }
        if(arreglo.isEmpty()) {
        	return "";
        	
        }else {
        	  return arreglo.get(0);
        }
      
         
 
    }
   
         
      
    public JTable llenarJtable(String sentencia, JLabel labelInfo, String hora, String minutos, String segundo ) throws InterruptedException{
        Conexion con = new Conexion();
        DefaultTableModel modelo=new DefaultTableModel();//voy a modelar mi jtable
        JTable tablaDatos=new JTable(modelo);
        if(con.conectar(this.server,this.database,this.usuario,this.password)==true){
        //TableColumnModel modeloColumnas = null;
        java.sql.ResultSetMetaData metadatos;
         
       
         
        
        
         
        try {
        labelInfo.setText("Ejecutando Consulta a la Base");
        con.resulsete = con.statemente.executeQuery(sentencia);
        Thread.sleep(1500);
        
        metadatos=con.resulsete.getMetaData();//extraigo datos sobre el resulset
        labelInfo.setText("Obteniendo datos");
         
        int cantColumnas=metadatos.getColumnCount();// pido cant columnas
        
        //modeloColumnas.setSelectionModel((ListSelectionModel) tablaDatos);
         
         
         
        for(int i=1;i<=cantColumnas;i++){
        modelo.addColumn(metadatos.getColumnName(i).toUpperCase());
         
         
        }
        //avanzo por el resulset para mostrar resultado de consultas
        labelInfo.setText("Cargando datos...");
        Thread.sleep(1500);
          while(con.resulsete.next()){
            // Bucle para cada resultado en la consulta
              
                 // Se crea un array que será una de las filas de la tabla. 
                 Object [] fila = new Object[cantColumnas]; // Hay columnas en la tabla
 
                 // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                 for (int i=0;i<cantColumnas;i++)
                    fila[i] = con.resulsete.getObject(i+1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
 
                 // Se añade al modelo la fila completa.
                 modelo.addRow(fila);
                
                 //cell.setBackground(Color.RED);
              }
          labelInfo.setText("Entregando Datos.");
          Thread.sleep(1500);
          labelInfo.setText("Última conexión "+hora + ":" + minutos + ":" + segundo);
          Thread.sleep(1500);
         
        } 
        catch (SQLException e) {
        
          
        }catch (Exception e1){
        	
        }
        con.desconectar();
        return tablaDatos;
        }else{
        return tablaDatos;	
        }
        
         
 
    }
   
   
    public String LeeArchivoParametros(String archivo)  {
    	
    	  String resultado=null;    
    	  String strLinea=null;
    	  InputStream fstream = this.getClass().getResourceAsStream(archivo);
          // Creamos el objeto de entrada
          DataInputStream entrada = new DataInputStream(fstream);
          // Creamos el Buffer de Lectura
          BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada)); 
          
          // Leer el archivo linea por linea
          try {
			while ((strLinea = buffer.readLine()) != null)   {
			      // Imprimimos la línea por pantalla
				  if(resultado==null){
			    	  resultado=strLinea;
			         }else{
			        	 resultado=resultado+" "+strLinea;
			         }
			  
			   
			     // System.out.println(strLinea);
				 
			  }
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		//	e.printStackTrace();
			
		}
          // Cerramos el archivo
          try {
			entrada.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			
		}    			
          return resultado;
   	}   	
    
    public ArrayList<String> LeeArchivoParametrosArray(String archivo)  {
    	ArrayList<String>listaParam=new ArrayList<String>();
  	 
  	  String resultado=null;    
  	  String strLinea=null;
  	  InputStream fstream = this.getClass().getResourceAsStream(archivo);
        // Creamos el objeto de entrada
        DataInputStream entrada = new DataInputStream(fstream);
        // Creamos el Buffer de Lectura
        BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada)); 
        
        // Leer el archivo linea por linea
        try {
			while ((strLinea = buffer.readLine()) != null)   {
			      // Imprimimos la línea por pantalla
				  if(resultado==null){
			    	  resultado=strLinea;
			         }else{
			        	 resultado=resultado+" "+strLinea;
			         }
			  
			   
				  listaParam.add(strLinea);
				 
			  }
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		//	e.printStackTrace();
			
		}
        // Cerramos el archivo
        try {
			entrada.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			
			
		}    			
        return listaParam;
 	}   	
	       
	      
		
	
    public void abrirarchivo(String archivo){

        try {
        	
        	 
               File objetofile = new File (archivo);
               Desktop.getDesktop().open(objetofile);
             
               

        }catch (IOException ex) {
        //System.out.println(ex.getMessage());
      
        }
        
    }
    
    


 
    public boolean existeArchivo(String rutaAlArchivo){
    	File af = new File(rutaAlArchivo);
    	if(af.exists()) {
    		return true;
    	}else {
    		return false;
    	}
		
 	}
 
     
   
     
 
}