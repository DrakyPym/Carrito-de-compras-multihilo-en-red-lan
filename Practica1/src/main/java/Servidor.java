import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {
    public static void main(String[] args) throws ClassNotFoundException {
        int puerto = 6030; 
        ServerSocket servidorSocket = null;
        int[] existencias = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Map<Integer, Socket> clientesConectados = new ConcurrentHashMap<>();

        try {
            servidorSocket = new ServerSocket(puerto);
            System.out.println("Servidor esperando conexiones en el puerto " + puerto);
            int id = 0;
            while (true) { // Espera constantemente una nueva conexion 
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress().getHostName());
                // Añadir el socket del cliente al mapa
                clientesConectados.put(id, clienteSocket);
                id++;

                //Hilo para manejar al cliente
                Thread hiloCliente = new HiloCliente(clienteSocket, existencias, clientesConectados);
                hiloCliente.start();
                

                //Cerrar los recursos
                //servidorSocket.close();
            }
        } catch (SocketException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HiloCliente extends Thread {
    private Socket clienteSocket;
    private int[] existencias;
    private Map<Integer, Socket> clientesConectados;

    public HiloCliente(Socket socket, int[] existencias, Map<Integer, Socket> clientesConectados) {
        this.clienteSocket = socket;
        this.existencias = existencias;
        this.clientesConectados = clientesConectados;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dosCl = new DataOutputStream(clienteSocket.getOutputStream()); // Creamos un flujo de salida de datos para enviar datos 
            DataInputStream dis = null; // Creamos un flujo de entrada de datos para leer el archivo 
            
            File folder = new File(".\\img");
            File[] files = folder.listFiles();

            //Cliclo para enviar las imagenes de los productos
            for (File f : files) { // Iteramos sobre cada archivo 
                String archivo = f.getAbsolutePath(); // Obtenemos la ruta absoluta del archivo
                String nombre = f.getName(); // Obtenemos el nombre del archivo
                long tam = f.length(); // Obtenemos el tamaño del archivo

                dosCl = new DataOutputStream(clienteSocket.getOutputStream()); // Creamos un flujo de salida de datos para enviar datos al cliente
                dis = new DataInputStream(new FileInputStream(archivo)); // Creamos un flujo de entrada de datos para leer el archivo 

                dosCl.writeUTF(nombre); // Enviamos el nombre del archivo al cliente
                dosCl.flush();
                dosCl.writeLong(tam); // Enviamos el tamaño del archivo al cliente
                dosCl.flush();

                byte[] b = new byte[1024]; // Creamos un buffer de bytes para almacenar los datos del archivo
                long enviados = 0;
                int n;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (enviados < tam) { // Bucle para leer y enviar el archivo en paquetes
                    n = dis.read(b); // Leemos datos del archivo y los almacenamos en el buffer 'b'
                    dosCl.write(b, 0, n); // Enviamos los datos al cliente a través del socket
                    dosCl.flush();
                    //try {
                    //    Thread.sleep(0.01);
                    //} catch (InterruptedException e) {
                    //    e.printStackTrace();
                    //}
                    enviados = enviados + n; // Actualizamos el contador de bytes enviados
                }
            }
            System.out.println("\nImagenes de los prodcutos enviadas");
     
            existencias = DB.consultarRegistros();   

            // Crear una instancia de la clase ContenidoDB 
            ContenidoDB contenidoDB = new ContenidoDB(existencias[0], existencias[1], existencias[2],existencias[3],
            existencias[4],existencias[5],existencias[6],existencias[7],existencias[8],existencias[9]);
            //contenidoDB.imprimirDB();
            
            //Serializamos el contenido de la base de datos
            ByteArrayOutputStream contenidoDBByteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream contenidoObjectOutputStream = new ObjectOutputStream(contenidoDBByteArrayOutputStream);
            contenidoObjectOutputStream.writeObject(contenidoDB);

            // Tamaño del objeto
            int contenidoSize = contenidoDBByteArrayOutputStream.size();

            //Enviamos el contenido de la base de datos
            dosCl.writeInt(contenidoSize);
            dosCl.write(contenidoDBByteArrayOutputStream.toByteArray());
            

            //Recibimos las actualizaciones de la BD enviadas por el cliente
            DataInputStream disCl = new DataInputStream(clienteSocket.getInputStream()); // Creación de un objeto DataInputStream para recibir datos
            
            while(disCl.readInt() == 1){
                // Recibir el tamaño del carrito serializado
                int contenidoDBSize = disCl.readInt();

                // Recibir el carrito serializado
                byte[] contenidoDBBytes = new byte[contenidoDBSize];
                disCl.readFully(contenidoDBBytes);

                // Deserializar el carrito
                ByteArrayInputStream carritoByteArrayInputStream = new ByteArrayInputStream(contenidoDBBytes);
                ObjectInputStream carritoObjectInputStream = new ObjectInputStream(carritoByteArrayInputStream);
                ContenidoDB cDB = (ContenidoDB) carritoObjectInputStream.readObject();

                //Logica para actualizar la DB
                DB.modificarRegistro("Agua", cDB.agua);
                DB.modificarRegistro("Arroz", cDB.arroz);
                DB.modificarRegistro("Cereal", cDB.cereal);
                DB.modificarRegistro("Huevos", cDB.huevos);
                DB.modificarRegistro("Jabon", cDB.jabon);
                DB.modificarRegistro("Leche", cDB.leche);
                DB.modificarRegistro("Manzanas", cDB.manzanas);
                DB.modificarRegistro("Pan", cDB.pan);
                DB.modificarRegistro("Papas", cDB.papas);
                DB.modificarRegistro("Pollo", cDB.pollo);
                System.out.println("\n\nBase de datos actualizada");

                //Logica para actualizar los clientes  
                existencias = DB.consultarRegistros();   

                // Crear una instancia de la clase ContenidoDB 
                contenidoDB = new ContenidoDB(existencias[0], existencias[1], existencias[2],existencias[3],
                existencias[4],existencias[5],existencias[6],existencias[7],existencias[8],existencias[9]);
                //contenidoDB.imprimirDB();
                
                //Serializamos el contenido de la base de datos
                contenidoDBByteArrayOutputStream = new ByteArrayOutputStream();
                contenidoObjectOutputStream = new ObjectOutputStream(contenidoDBByteArrayOutputStream);
                contenidoObjectOutputStream.writeObject(contenidoDB);

                // Tamaño del objeto
                contenidoSize = contenidoDBByteArrayOutputStream.size();
                
                //Actualizamos todos loc clientes 
                for(int i = 0; i < clientesConectados.size(); i++){
                    try {
                        Socket cliente = clientesConectados.get(i);
                        dosCl = new DataOutputStream(cliente.getOutputStream()); // Creamos un flujo de salida de datos para actualizar los datos
                        //Enviamos el contenido de la base de datos
                    
                        dosCl.writeInt(contenidoSize);
                        dosCl.flush();
                        dosCl.write(contenidoDBByteArrayOutputStream.toByteArray());
                        dosCl.flush();
                    } catch (IOException e) { //Si un cliente se ha desconectado
                        // El cliente se ha desconectado
                        System.out.println("Cliente desconectado: " + clienteSocket.getInetAddress().getHostName());
                        clientesConectados.remove(i);
                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

   

