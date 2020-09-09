package MetodosSql;
 


import java.sql.*;

 
 
 
public class Conexion {
     
 
        private  Connection c;
        public Connection getC() {
			return c;
		}

		protected  Statement statemente;
        protected  ResultSet resulsete;
        public static String user=null;
        public static String pass=null;
        private static String cadena=null;
        private static String driver="com.mysql.jdbc.Driver";
        static Conexion conexion=null;
    
     
    
        
        
       
    
        
        public static String getUser() {
			return user;
		}



		public void setUser(String user) {
			Conexion.user = user;
		}



		public static String getPass() {
			return pass;
		}



		public void setPass(String pass) {
			Conexion.pass = pass;
		}



		public Conexion(){
             
        }
		public static Conexion getInstance() {
			if(conexion==null) {
				conexion=new Conexion();
			}
			return conexion;
		}
         
         
        
        public boolean conectar(String server,String database,String usuario,String password){
        	boolean conecto;
        	try{
        		
            	
            	// para cadena="jdbc:mysql://"+server+";user="+usuario+";password="+password+";database="+database+"";
            	cadena="jdbc:mysql://"+server+"/"+database;
                Class.forName(driver);
               // Para sql server c=DriverManager.getConnection(cadena);
               
                c=DriverManager.getConnection(cadena,usuario,password);
                System.out.println("Creando conexion");          
                	
                
                
                statemente=c.createStatement();       
                
                conecto=true;
                
                
            }catch(ClassNotFoundException e1){
            	
             System.out.println("Error en los drivers");
             conecto=false;
            }
            catch(SQLException e2){
             
                
               
                conecto=false;
                System.out.println("No me puedo conectar ");
 
            }catch(Exception e3){
            	 System.out.println("No me puedo conectar ");
            	
                 conecto=false;
            	
            }
 return conecto;
    }
        
        public  void desconectar(){
            //estado=new JTextField();
             
                try {
                    if (c != null){
                        c.close();
                       
                         
                         
                         
                    //  System.out.println("Conexión liberada OK");
                    }
                    else{
                        System.out.println("Ya estaba desconectado");
                         
                    }
                     
                    //statemente.close();
                     
                     
                     
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                	
                    System.out.println("Desconectado incorrecto");
                    e.printStackTrace();
                }
                 
             
        }
}