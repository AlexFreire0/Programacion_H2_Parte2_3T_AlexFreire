package misClases;
import java.sql.*;
import java.util.*;

public class ConexionCine {

    // Método para mostrar todas las películas con su información
    public static void mostrarPeliculas(Scanner sc) {
        try {
            // Establece la conexión a la base de datos
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/cine_alexfreire", "root", "");

            // Crea un objeto Statement para ejecutar la consulta
            Statement miStatement = miConexion.createStatement();

            // Consulta que une las tablas películas y categorías
            ResultSet miResultSet = miStatement.executeQuery(
                "SELECT p.id, p.titulo, p.director, p.anyo, c.nombre AS categoria, p.duracion FROM peliculas p JOIN Categoria c ON p.categoria = c.id"
            );

            // Itera sobre el resultado e imprime los datos
            while (miResultSet.next()) {
                System.out.println("ID: " + miResultSet.getString("id")); // Este debería ser "ID", no "Título"
                System.out.println("Título: " + miResultSet.getString("titulo"));
                System.out.println("Director: " + miResultSet.getString("director"));
                System.out.println("Año: " + miResultSet.getString("anyo"));
                System.out.println("Categoría: " + miResultSet.getString("categoria"));
                System.out.println("Duración: " + miResultSet.getInt("duracion") + " minutos");
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error al conectar");
            e.printStackTrace();
        }
    }

    // Método para añadir una nueva película
    public static void anadirPelicula(Scanner sc) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/cine_alexfreire", "root", "");

            // Solicita ID y verifica si ya existe
            System.out.println("Id de la película:");
            String idpeli = sc.nextLine();

            String comprobarSQL = "SELECT * FROM peliculas WHERE id = ?";
            PreparedStatement comprobarStmt = miConexion.prepareStatement(comprobarSQL);
            comprobarStmt.setString(1, idpeli);
            ResultSet rs = comprobarStmt.executeQuery();

            if (rs.next()) {
                // Si existe, muestra mensaje y vuelve al menú principal
                System.out.println("Ya existe una película con ese ID.");
                Principal.main(null);
            } else {
                // Si no existe, solicita los datos de la nueva película
                System.out.println("Nombre de la película:");
                String nombrepeli = sc.nextLine();

                System.out.println("Director de la película:");
                String directorpeli = sc.nextLine();

                System.out.println("Año de la película:");
                int anyopeli = Integer.parseInt(sc.nextLine());

                // Lista de categorías disponibles
                System.out.println("-- Categorías --");
                System.out.println("1. Acción");
                System.out.println("2. Comedia");
                System.out.println("3. Drama");
                System.out.println("4. Ciencia Ficción");
                System.out.println("5. Terror");
                System.out.println("Categoría de la película (Número):");
                int categoriapeli = Integer.parseInt(sc.nextLine());

                System.out.println("Duración de la película (Minutos):");
                int duracionpeli = Integer.parseInt(sc.nextLine());

                // Inserta la nueva película en la base de datos
                String insertSQL = "INSERT INTO peliculas (id, titulo, director, anyo, categoria, duracion) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = miConexion.prepareStatement(insertSQL);
                insertStmt.setString(1, idpeli);
                insertStmt.setString(2, nombrepeli);
                insertStmt.setString(3, directorpeli);
                insertStmt.setInt(4, anyopeli);
                insertStmt.setInt(5, categoriapeli);
                insertStmt.setInt(6, duracionpeli);

                int i = insertStmt.executeUpdate();

                // Verifica si la inserción fue exitosa
                if (i > 0) {
                    System.out.println("Película introducida correctamente.");
                } else {
                    System.out.println("No se pudo introducir la película.");
                }

                insertStmt.close();
            }

            // Cierra recursos
            rs.close();
            comprobarStmt.close();
            miConexion.close();

        } catch (Exception e) {
            System.out.println("Error al conectar o insertar datos.");
            e.printStackTrace();
        }
    }

    // Método para eliminar una película por su ID
    public static void eliminarPelicula(Scanner sc) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/cine_alexfreire", "root", "");

            System.out.println("Introduce el ID de la película que deseas eliminar:");
            String idpeli = sc.nextLine();

            // Verifica si la película existe
            PreparedStatement comprobarStmt = miConexion.prepareStatement("SELECT * FROM peliculas WHERE id = ?");
            comprobarStmt.setString(1, idpeli);
            ResultSet resultado = comprobarStmt.executeQuery();

            if (resultado.next()) {
                // Si existe, procede a eliminarla
                String eliminarSQL = "DELETE FROM peliculas WHERE id = ?";
                PreparedStatement eliminarStmt = miConexion.prepareStatement(eliminarSQL);
                eliminarStmt.setString(1, idpeli);
                int filas = eliminarStmt.executeUpdate();

                if (filas > 0) {
                    System.out.println("Película eliminada correctamente.");
                } else {
                    System.out.println("No se pudo eliminar la película.");
                }

                eliminarStmt.close();
            } else {
                System.out.println("No existe ninguna película con ese ID.");
            }

            resultado.close();
            comprobarStmt.close();
            miConexion.close();

        } catch (Exception e) {
            System.out.println("Error al conectar o eliminar la película.");
            e.printStackTrace();
        }
    }

    // Método para modificar los datos de una película
    public static void modificarPelicula(Scanner sc) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/cine_alexfreire", "root", "");

            System.out.println("Introduce el ID de la película que deseas modificar:");
            String idpeli = sc.nextLine();

            // Verifica si la película existe
            String comprobarSQL = "SELECT * FROM peliculas WHERE id = ?";
            PreparedStatement comprobarStmt = miConexion.prepareStatement(comprobarSQL);
            comprobarStmt.setString(1, idpeli);
            ResultSet rs = comprobarStmt.executeQuery();

            if (rs.next()) {
                // Muestra los datos actuales de la película
                System.out.println("Película encontrada. Datos actuales:");
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("Director: " + rs.getString("director"));
                System.out.println("Año: " + rs.getInt("anyo"));
                System.out.println("Categoría: " + rs.getInt("categoria"));
                System.out.println("Duración: " + rs.getInt("duracion") + " minutos");

                // Solicita los nuevos datos
                System.out.println("Nuevo título:");
                String nuevoNombrepeli = sc.nextLine();

                System.out.println("Nuevo director:");
                String nuevoDirector = sc.nextLine();

                System.out.println("Nuevo año:");
                int nuevoAnyo = Integer.parseInt(sc.nextLine());

                System.out.println("-- Categorías --");
                System.out.println("1. Acción");
                System.out.println("2. Comedia");
                System.out.println("3. Drama");
                System.out.println("4. Ciencia Ficción");
                System.out.println("5. Terror");
                System.out.println("Nueva categoría (número):");
                int nuevaCategoria = Integer.parseInt(sc.nextLine());

                System.out.println("Nueva duración (minutos):");
                int nuevaDuracion = Integer.parseInt(sc.nextLine());

                // Actualiza los datos en la base de datos
                String updateSQL = "UPDATE peliculas SET titulo = ?, director = ?, anyo = ?, categoria = ?, duracion = ? WHERE id = ?";
                PreparedStatement updateStmt = miConexion.prepareStatement(updateSQL);
                updateStmt.setString(1, nuevoNombrepeli);
                updateStmt.setString(2, nuevoDirector);
                updateStmt.setInt(3, nuevoAnyo);
                updateStmt.setInt(4, nuevaCategoria);
                updateStmt.setInt(5, nuevaDuracion);
                updateStmt.setString(6, idpeli);

                int filas = updateStmt.executeUpdate();
                if (filas > 0) {
                    System.out.println("Película modificada correctamente.");
                } else {
                    System.out.println("No se pudo modificar la película.");
                }

                updateStmt.close();
            } else {
                System.out.println("No existe ninguna película con ese ID.");
            }
            //cerramos 
            rs.close();
            comprobarStmt.close();
            miConexion.close();

        } catch (Exception e) {
            System.out.println("Error al modificar la película.");
            e.printStackTrace();
        }
    }
}
