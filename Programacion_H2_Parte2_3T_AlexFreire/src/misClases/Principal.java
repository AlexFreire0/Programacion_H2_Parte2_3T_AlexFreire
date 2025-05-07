package misClases;
import java.util.*;

public class Principal {
    public static void main(String[] args) {
    	try {
        Scanner sc = new Scanner(System.in);
        int opcion = 0; 
        do { //menu de opciones
            System.out.println("\n------ MENÚ ------");
            System.out.println("1 – Ver películas");
            System.out.println("2 – Añadir película");
            System.out.println("3 – Eliminar película");
            System.out.println("4 – Modificar película");
            System.out.println("5 – Salir");
            System.out.print("Seleccione una opción: ");
             
            opcion = sc.nextInt();
            sc.nextLine();
            //controlamos la opcion elegida
            switch (opcion) {
                case 1:
                    ConexionCine.mostrarPeliculas(sc);
                    break;
                case 2:
                	ConexionCine.anadirPelicula(sc);
                	break;
                case 3:
                	ConexionCine.eliminarPelicula(sc);
                	break;
                case 4:
                	ConexionCine.modificarPelicula(sc);
                	break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);
        	
        sc.close();  
        }catch(Exception e) { //excepcion por si no se elige un numero.
        	System.out.println("Solo se pueden elegir numeros.");
        	Principal.main(args); //llamamos de nuevo al metodo principal
        }
    }
}
