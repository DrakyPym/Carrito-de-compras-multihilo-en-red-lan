import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Socket socket = null;
        DataInputStream dis = null;
        DataOutputStream dosCl = null;
        Scanner ent = null;
        try {
            ent = new Scanner(System.in);
            //abrimos el socket con conexion
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Escriba la dirección del servidor: ");
            String host = br.readLine();
            System.out.printf("Escriba el puerto: ");
            int puerto = Integer.parseInt(br.readLine()); 
            
            // Peticion de conexion
            socket = new Socket(host, puerto);
            System.out.println("Conexion establecida");
            
            //Definimos un flujo de nivel de bits de entrada ligado al socket
            dis = new DataInputStream(socket.getInputStream()); // Creación de un objeto DataInputStream para recibir datos del servidor
            // Creamos un flujo de salida de datos para enviar al servidor
            dosCl = new DataOutputStream(socket.getOutputStream()); 
            
            DataOutputStream dos = null;

            for(int i = 0; i < 10; i++){ //Blucle infinito para recibir multiples archivos
                //Leemos los datos principales del archivo y creamos un flujo para escribir el archivo de salida
                byte[] b = new byte[1024]; // Crea un array de bytes para almacenar los datos del archivo
                
                String nombre = dis.readUTF(); // Lee el nombre del archivo enviado por el servidor
                long tam = dis.readLong(); // Lee el tamaño del archivo enviado por el servidor
                dos = new DataOutputStream(new FileOutputStream(nombre)); // Creación de un objeto DataOutputStream para escribir datos en un archivo

                //Preparamos los datos para recibir los paquetes de datos del archivo
                long recibidos = 0; // Inicializa el contador de bytes recibidos
                int n; // Variables para el número de bytes leídos y el porcentaje de recepción
                //Definimos el ciclo donde estaremos recibiendo los datos enviados por el servidor
                while (recibidos < tam) { // Bucle para recibir datos del servidor hasta que se reciba todo el archivo
                    n = dis.read(b); // Lee datos del servidor y los almacena en el array 'b'
                    dos.write(b, 0, n); // Escribe los datos en el archivo
                    dos.flush(); // Limpia el flujo de salida
                    recibidos = recibidos + n; // Actualiza el contador de bytes recibidos
                }
            }
            System.out.println("\nImagenes de los prodcutos recibidas");


            // Recibir el tamaño del carrito serializado
            int contenidoDBSize = dis.readInt();

            // Recibir el carrito serializado
            byte[] contenidoDBBytes = new byte[contenidoDBSize];
            dis.readFully(contenidoDBBytes);

            // Deserializar el carrito
            ByteArrayInputStream carritoByteArrayInputStream = new ByteArrayInputStream(contenidoDBBytes);
            ObjectInputStream carritoObjectInputStream = new ObjectInputStream(carritoByteArrayInputStream);
            ContenidoDB cDB = (ContenidoDB) carritoObjectInputStream.readObject();

            Carrito carrito = new Carrito(cDB);
            
            int opcion = 0;

            //Logica para recibir actualizaciones con un hilo que esta escuchando
            Thread hiloCliente = new HiloActualizarBD(dis, cDB, carrito);
            hiloCliente.start();

            do {
                opcion = carrito.menu(ent);
                if (opcion == 2){ //Enviar la actualizaciion de la BD al servidor //Comprar carrito
                    //Serializamos el contenido de la base de datos
                    ByteArrayOutputStream contenidoDBByteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream contenidoObjectOutputStream = new ObjectOutputStream(contenidoDBByteArrayOutputStream);
                    contenidoObjectOutputStream.writeObject(cDB);

                    // Tamaño del objeto
                    int contenidoSize = contenidoDBByteArrayOutputStream.size();
                    dosCl.writeInt(1); //Indicamos al servidor que se enviaran mas actualizaciones

                    //Enviamos el contenido de la base de datos
                    dosCl.writeInt(contenidoSize);
                    dosCl.write(contenidoDBByteArrayOutputStream.toByteArray());
                }
            } while (opcion == 1 || opcion == 2);
            dosCl.writeInt(0); // Indicamos al servidor que ya no se enviaran mas actualizaciones
            //Cerrar los recursos
            dosCl.close();
            dis.close();
            socket.close();
            ent.close();

        }catch (IOException | ClassNotFoundException e){ // Eliminar e depues de descomentar
            e.printStackTrace();
        } finally {
            try {
                if (dosCl != null) dosCl.close();
                if (dis != null) dis.close();
                if (socket != null) socket.close();
                if (ent != null) ent.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

class HiloActualizarBD extends Thread {
    private DataInputStream dis;
    private ContenidoDB cDB;
    private Carrito carrito;

    public HiloActualizarBD(DataInputStream dis, ContenidoDB cDB, Carrito carrito) {
        this.dis = dis;
        this.cDB = cDB;
        this.carrito = carrito;
    }

    @Override
    public void run() {
        try {
            while (true) { //Escucha continuamente si hay actualizaciones
                // Recibir el tamaño del carrito serializado
                int contenidoDBSize = dis.readInt();

                // Recibir el carrito serializado
                byte[] contenidoDBBytes = new byte[contenidoDBSize];
                dis.readFully(contenidoDBBytes);

                // Deserializar el carrito
                ByteArrayInputStream carritoByteArrayInputStream = new ByteArrayInputStream(contenidoDBBytes);
                ObjectInputStream carritoObjectInputStream = new ObjectInputStream(carritoByteArrayInputStream);
                ContenidoDB cDBTemp = (ContenidoDB) carritoObjectInputStream.readObject(); 
                cDB.agua = cDBTemp.agua - carrito.agua;
                if (cDB.agua < 0 ) {
                    cDB.agua = cDBTemp.agua;
                    carrito.agua = 0;
                    System.out.println("\nNo hay agua suficiente y se elimino del carrito");
                }
                cDB.arroz = cDBTemp.arroz - carrito.arroz;
                if (cDB.arroz < 0 ) {
                    cDB.arroz = cDBTemp.arroz;
                    carrito.arroz = 0;
                    System.out.println("\nNo hay arroz suficiente y se elimino del carrito");
                }
                cDB.cereal = cDBTemp.cereal - carrito.cereal;
                if (cDB.cereal < 0 ) {
                    cDB.cereal = cDBTemp.cereal;
                    carrito.cereal = 0;
                    System.out.println("\nNo hay cereal suficiente y se elimino del carrito");
                }
                cDB.huevos = cDBTemp.huevos - carrito.huevos;
                if (cDB.huevos < 0 ) {
                    cDB.huevos = cDBTemp.huevos;
                    carrito.huevos = 0;
                    System.out.println("\nNo hay huevos suficientes y se elimino del carrito");
                }
                cDB.jabon = cDBTemp.jabon - carrito.jabon;
                if (cDB.jabon < 0 ) {
                    cDB.jabon = cDBTemp.jabon;
                    carrito.jabon = 0;
                    System.out.println("\nNo hay jabon suficiente y se elimino del carrito");
                }
                cDB.leche = cDBTemp.leche - carrito.leche;
                if (cDB.leche < 0 ) {
                    cDB.leche = cDBTemp.leche;
                    carrito.leche = 0;
                    System.out.println("\nNo hay leche suficiente y se elimino del carrito");
                }
                cDB.manzanas = cDBTemp.manzanas - carrito.manzanas;
                if (cDB.manzanas < 0 ) {
                    cDB.manzanas = cDBTemp.manzanas;
                    carrito.manzanas = 0;
                    System.out.println("\nNo hay manzanas suficientes y se elimino del carrito");
                }
                cDB.pan = cDBTemp.pan - carrito.pan;
                if (cDB.pan < 0 ) {
                    cDB.pan = cDBTemp.pan;
                    carrito.pan = 0;
                    System.out.println("\nNo hay pan suficiente y se elimino del carrito");
                }
                cDB.papas = cDBTemp.papas - carrito.papas;
                if (cDB.papas < 0 ) {
                    cDB.papas = cDBTemp.papas;
                    carrito.papas = 0;
                    System.out.println("\nNo hay papas suficientes y se elimino del carrito");
                }
                cDB.pollo = cDBTemp.pollo - carrito.pollo;
                if (cDB.pollo < 0 ) {
                    cDB.pollo = cDBTemp.pollo;
                    carrito.pollo = 0;
                    System.out.println("\nNo hay pollo suficiente y se elimino del carrito");
                }
            }
        } catch (SocketException se) {
            //El socket se ha cerrado y no hacemos nada
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
