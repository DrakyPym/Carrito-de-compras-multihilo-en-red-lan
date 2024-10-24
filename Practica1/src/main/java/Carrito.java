import java.util.Scanner;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Carrito implements Serializable {

    int agua = 0;
    int arroz = 0;
    int cereal = 0;
    int huevos = 0;
    int jabon = 0;
    int leche = 0;
    int manzanas = 0;
    int pan = 0;
    int papas = 0;
    int pollo = 0;
    
    float precioAgua = 1.00f;
    float precioArroz = 3.00f;
    float precioCereal = 4.50f;
    float precioHuevos = 2.00f;
    float precioJabon = 2.20f;
    float precioLeche = 2.50f;
    float precioManzanas = 1.50f;
    float precioPan = 1.20f;
    float precioPapas = 1.80f;
    float precioPollo = 5.00f; 

    ContenidoDB estadoDB = null;
    
    public Carrito (ContenidoDB estadoDB) {
        this.estadoDB = estadoDB;
    }

    int menu (Scanner ent) {

        System.out.println("¡Bienvenido al Mart Virtual! ¿Qué desea hacer?");
        System.out.println("1. Agregar al carrito");
        System.out.println("2. Quitar del carrito");
        System.out.println("3. Ver carrito");
        System.out.println("4. Salir");
        System.out.print("Opcion: ");

        int sel = ent.nextInt();
       /// int sel2 = 0;

        switch (sel) {
            case 1: // Agregar al carrito
                System.out.println("\n\n¡Bienvenido!\n Aquí está nuestro catálogo: \n");
                estadoDB.imprimirDB();
                System.out.println("0. Regresar");
                System.out.println("\n");
                System.out.println("Seleccione un producto para agregar al carrito");
                System.out.print("Opcion: ");
                sel = ent.nextInt();
                

                if (sel == 0) { //Regresar
                    System.out.println("\n");
                    break;
                }
                System.out.println("¿Cuantos? Ingresa un numero: ");
                int sel2 = ent.nextInt();

                if (sel2 >= 0) {
                    
                    switch (sel) {
                        case 1:
                            if (sel2 <= estadoDB.agua){
                                estadoDB.agua -= sel2;
                                agua += sel2;
                            }else
                                System.out.println("No hay suficiente agua");
                            break;
                        case 2:
                            if (sel2 <= estadoDB.arroz){
                                estadoDB.arroz -= sel2;
                                arroz += sel2;
                            }else
                                System.out.println("No hay suficiente arroz");
                            break;
                        case 3:
                            if (sel2 <= estadoDB.cereal){
                                estadoDB.cereal -= sel2;
                                cereal += sel2;
                            }else
                                System.out.println("No hay suficiente cereal");                            
                            break;
                        case 4:
                            if (sel2 <= estadoDB.huevos){
                                estadoDB.huevos -= sel2;
                                huevos += sel2;
                            }else
                                System.out.println("No hay suficiente huevos");  
                            break;
                        case 5:
                            if (sel2 <= estadoDB.jabon){
                                estadoDB.jabon -= sel2;
                                jabon += sel2;
                            }else
                                System.out.println("No hay suficiente jabon");
                            break;
                        case 6:
                            if (sel2 <= estadoDB.leche){
                                estadoDB.leche -= sel2;
                                leche += sel2;
                            }else
                                System.out.println("No hay suficiente leche");
                            break;
                        case 7:
                            if (sel2 <= estadoDB.manzanas) {
                                estadoDB.manzanas -= sel2;
                                manzanas += sel2;
                            }else
                                System.out.println("No hay suficiente manzanas");
                            break;
                        case 8:
                            if (sel2 <= estadoDB.pan){
                                estadoDB.pan -= sel2;
                                pan += sel2;
                            }else
                                System.out.println("No hay suficiente pan");                            
                            break;
                        case 9:
                            if (sel2 <= estadoDB.papas) {
                                estadoDB.papas -= sel2;
                                papas += sel2;
                            }else
                                System.out.println("No hay suficientes papas");
                            break;
                        case 10:
                            if (sel2 <= estadoDB.pollo) {
                                estadoDB.pollo -= sel2;
                                pollo += sel2;
                            }else
                                System.out.println("No hay suficiente pollo");
                            break;
                        default:
                            System.out.println("Selección no válida.");
                    }
                } else
                    System.out.println("No puedes agregar cantidades negativas ni 0 unidades");

                System.out.println("\n");
                break;
            case 2: // Quitar del carrito
                // Mostrar carrito
                int i = 1;
                System.out.println("Bienvenido a tu carrito!!");
                System.out.println("\n");
                if ((agua + arroz + cereal + huevos + jabon + leche + manzanas + pan + papas + pollo) != 0) {
                    if (agua > 0){
                        System.out.println(i + ". (Opcion 1)Agua: " + agua);
                        i++;
                    }
                    if (arroz > 0){
                        System.out.println(i + ". (Opcion 2)Arroz: " + arroz);
                        i++;
                    }
                    if (cereal > 0){
                        System.out.println(i + ". (Opcion 3)Cereal: " + cereal);
                        i++;
                    }
                    if (huevos > 0){
                        System.out.println(i + ". (Opcion 4)Huevos: " + huevos);
                        i++;
                    }
                    if (jabon > 0){
                        System.out.println(i + ". (Opcion 5)Jabon: " + jabon);
                        i++;
                    }
                    if (leche > 0){
                        System.out.println(i + ". (Opcion 6)Leche: " + leche);
                        i++;
                    }
                    if (manzanas > 0){
                        System.out.println(i + ". (Opcion 7)Manzanas: " + manzanas);
                        i++;
                    }
                    if (pan > 0){
                        System.out.println(i + ". (Opcion 8)Pan: " + pan);
                        i++;
                    }
                    if (papas > 0){
                        System.out.println(i + ". (Opcion 9)Papas: " + papas);
                        i++;
                    }
                    if (pollo > 0){
                        System.out.println(i + ". (Opcion 10)Pollo: " + pollo);
                        i++;
                    }
                } else
                    System.out.println("Tu carrito esta vacio\n");
                // Termina mostrar carrito
                

                System.out.println("0. (Opcion 0)Regresar");
                System.out.println("\n");
                System.out.println("Seleccione un producto para quitar del carrito");
                System.out.print("Opcion: ");
                sel = ent.nextInt();

                if (sel == 0) { //Regresar
                    System.out.println("\n");
                    break;
                }
                System.out.println("¿Cuantos? Ingresa un numero: ");
                sel2 = ent.nextInt();

                if (sel2 > 0) {
                    switch (sel) {
                        case 1:
                            if (sel2 <= agua){
                                estadoDB.agua += sel2;
                                agua -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 2:
                            if (sel2 <= arroz){
                                estadoDB.arroz += sel2;
                                arroz -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 3:
                            if (sel2 <= cereal){
                                estadoDB.cereal += sel2;
                                cereal -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 4:
                            if (sel2 <= huevos){
                                estadoDB.huevos += sel2;
                                huevos -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 5:
                            if (sel2 <= jabon){
                                estadoDB.jabon += sel2;
                                jabon -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 6:
                            if (sel2 <= leche){
                                estadoDB.leche += sel2;
                                leche -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 7:
                            if (sel2 <= manzanas){
                                estadoDB.manzanas += sel2;
                                manzanas -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 8:
                            if (sel2 <= pan){
                                estadoDB.pan += sel2;
                                pan -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 9:
                            if (sel2 <= papas){
                                estadoDB.papas += sel2;
                                papas -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        case 10:
                            if (sel2 <= pollo) {
                                estadoDB.pollo += sel2;
                                pollo -= sel2;
                            }else
                                System.out.println("No puedes quitar mas productos de los que hay en el carrito");
                            break;
                        default:
                            System.out.println("Selección no válida.");
                            break;
                    }
                } else
                    System.out.println("No puedes agregar cantidades negativas ni 0 unidades");

                System.out.println("\n");
                
                break;
            case 3: // Ver el carrito
                int j = 1;
                System.out.println("\n\nBienvenido a tu carrito!!");
                System.out.println("\n");
                if ((agua + arroz + cereal + huevos + jabon + leche + manzanas + pan + papas + pollo) != 0) {
                    if (agua > 0){
                        System.out.println(j + ". Agua: " + agua);
                        j++;                  
                    }
                    if (arroz > 0){
                        System.out.println(j + ". Arroz: " + arroz);
                        j++;
                    }
                    if (cereal > 0){
                        System.out.println(j + ". Cereal: " + cereal);
                        j++;
                    }
                    if (huevos > 0){
                        System.out.println(j + ". Huevos: " + huevos);
                        j++;
                    }
                    if (jabon > 0){
                        System.out.println(j + ". Jabon: " + jabon);
                        j++;
                    }
                    if (leche > 0){
                        System.out.println(j + ". Leche: " + leche);
                        j++;
                    }
                    if (manzanas > 0){
                        System.out.println(j + ". Manzanas: " + manzanas);
                        j++;
                    }
                    if (pan > 0){
                        System.out.println(j + ". Pan: " + pan);
                        j++;
                    }
                    if (papas > 0){
                        System.out.println(j + ". Papas: " + papas);
                        j++;
                    }
                    if (pollo > 0){
                        System.out.println(j + ". Pollo: " + pollo);
                        j++;
                    }
                    System.out.println("\n¿Que desea hacer?");
                    System.out.println("1. Comprar carrito");
                    System.out.println("2. Regresar");
                    System.out.println("Opcion: ");
                    int sel3 = ent.nextInt(); 
                    
                    switch (sel3) {
                        case 1: //Comprar carrito
                            //Sumamos lo del carrito()
                            float total = 0;                        
                            total += precioAgua * agua;
                            total += precioArroz * arroz;
                            total += precioCereal * cereal;
                            total += precioHuevos * huevos;
                            total += precioJabon * jabon;
                            total += precioLeche * leche;
                            total += precioManzanas * manzanas;
                            total += precioPan * pan;
                            total += precioPapas * papas;
                            total += precioPollo * pollo;

                            // Imprimir el total
                            System.out.println("\nTotal a pagar: $\n" + total);

                            //Gaudar ticket en PDF
                            ticket(); 
                            return 2; // Indicar al cliente que actualice la BD y que siga iterando (mostrando el menu)

                        case 2: //Regresar
                            break;
                        default:
                            break;
                    }
                }else{
                    System.out.println("Carrito vacio\n\n");
                }

                break;
            case 4:
                return 0; //Salir

            default:
                System.out.println("Selección no válida. Por favor, seleccione una opción válida.");
                break;
        }
        return 1; //Todo normal, seguir iterando
    }
    
    public void ticket(){
        try {
            float total = 0;
            total += precioAgua * agua;
            total += precioArroz * arroz;
            total += precioCereal * cereal;
            total += precioHuevos * huevos;
            total += precioJabon * jabon;
            total += precioLeche * leche;
            total += precioManzanas * manzanas;
            total += precioPan * pan;
            total += precioPapas * papas;
            total += precioPollo * pollo;

            PdfWriter escritor = new PdfWriter("ticket.pdf");
            // Crear un documento PDF
            PdfDocument pdf = new PdfDocument(escritor);
            Document documento = new Document(pdf);

            // Agregar el contenido al documento
            documento.add(new Paragraph("Ticket"));
            documento.add(new Paragraph("\n"));

            // Obtener la fecha y hora actual
            LocalDateTime fechaHoraActual = LocalDateTime.now();

            // Formatear la fecha y hora
            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHoraFormateada = fechaHoraActual.format(formateador);

            // Imprimir la fecha y hora
            documento.add(new Paragraph("Fecha y hora de compra: " + fechaHoraFormateada));
            documento.add(new Paragraph("\n\n"));

            documento.add(new Paragraph("Productos comprados:\n"));
            documento.add(new Paragraph("\n"));
            
            int j = 1;
            while (agua > 0){
                documento.add(new Paragraph(j + ". Agua: " + "           Precio: $" + precioAgua));
                agua--;
                j++;                  
            }
            while (arroz > 0){
                documento.add(new Paragraph(j + ". Arroz: " + "          Precio: $" + precioArroz));
                arroz--;
                j++;
            }
            while (cereal > 0){
                documento.add(new Paragraph(j + ". Cereal: " + "         Precio: $" + precioCereal));
                cereal--;
                j++;
            }
            while (huevos > 0){
                documento.add(new Paragraph(j + ". Huevos: " + "         Precio: $" + precioHuevos));
                huevos--;
                j++;
            }
            while (jabon > 0){
                documento.add(new Paragraph(j + ". Jabon: " + "         Precio: $" + precioJabon));
                jabon--;
                j++;
            }
            while (leche > 0){
                documento.add(new Paragraph(j + ". Leche: " + "          Precio: $" + precioLeche));
                leche--;
                j++;
            }
            while (manzanas > 0){
                documento.add(new Paragraph(j + ". Manzanas: " + "       Precio: $" + precioManzanas));
                manzanas--;
                j++;
            }
            while (pan > 0){
                documento.add(new Paragraph(j + ". Pan: " + "            Precio: $" + precioPan));
                pan--;
                j++;
            }
            while (papas > 0){
                documento.add(new Paragraph(j + ". Papas: " + "          Precio: $" + precioPapas));
                papas--;
                j++;
            }
            while (pollo > 0){
                documento.add(new Paragraph(j + ". Pollo: " + "          Precio: $" + precioPollo));
                pollo--;
                j++;
            }

            // Agregar el total al documento
            documento.add(new Paragraph("\n"));
            documento.add(new Paragraph("\nTotal a pagar:      $" + total));

            // Cerrar el documento
            documento.close();

            System.out.println("\nRecibo guardado en recibo.pdf\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}