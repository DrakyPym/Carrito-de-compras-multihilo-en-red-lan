import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 

// Esta clase no se va a serializar

public class DB{

    private static Connection conectar() {
        Connection conn = null;
        try {
            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión con la base de datos
            String url = "jdbc:mysql://localhost:3306/carrito";
            String user = "root";
            String password = "123456";
            conn = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos MySQL");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver JDBC");
            e.printStackTrace();
        }
        return conn;
    }

    // Método para modificar un registro en una tabla
    public static void modificarRegistro(String producto, int nuevaCantidad) {
        try {
            // Obtener la conexion
            Connection conn = conectar();

            if((nuevaCantidad) >= 0){
                // Consulta SQL para actualizar el registro con el ID especificado
                String queryU = "UPDATE items SET existencia = ? WHERE producto = ?";

                // Crear una declaracion preparada
                PreparedStatement statementU = conn.prepareStatement(queryU);

                // Establecer los parametros de la consulta
                statementU.setInt(1, nuevaCantidad);
                statementU.setString(2, producto);

                // Ejecutar la consulta
                int rowsAffected = statementU.executeUpdate();

                // Verificar si se modifico el registro correctamente
                if (rowsAffected <= 0) 
                    System.out.println("No se encontró ningún registro con el ID especificado");
    
                // Cerrar recursos
                statementU.close();
            }else{
                System.out.println("No pueden existir cantidades negativas en la base de datos");
            }
            

            // Cerrar recursos
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error al modificar el registro en la tabla");
            e.printStackTrace();
        }
    }

    // Metodo para consultar todo el inventario
    public static int[] consultarRegistros() {
        int[] existencias = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        try {
            // Obtener la conexión
            Connection conn = conectar();

            // Consulta SQL para seleccionar todos los registros de la tabla
            String query = "SELECT * FROM items ORDER BY producto ASC";

            // Crear una declaración preparada
            PreparedStatement statement = conn.prepareStatement(query);

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();
            
            // Recorrer los resultados e imprimirlos
            int i = 1;
            while (resultSet.next()) {
                // Obtener los valores de cada columna en el registro
                //String producto = resultSet.getString("producto");
                //String precio = resultSet.getString("precio");
                String existencia = resultSet.getString("existencia");
                existencias[i-1] = Integer.parseInt(existencia);

                // Imprimir los valores de las columnas
                //System.out.println("Estado actual de la base de datos:\n");
                //System.out.println(i + ". Producto: " + producto + ", Precio: " + precio + ", Existencia: " + existencia);
                i++;
                
            }

            // Cerrar recursos
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error al consultar los registros de la tabla");
            e.printStackTrace();
        }
        return existencias;
    }
}