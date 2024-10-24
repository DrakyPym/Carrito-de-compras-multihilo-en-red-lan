import java.io.Serializable;
public class ContenidoDB implements Serializable {
    public int agua = 0;
    public int arroz = 0;
    public int cereal = 0;
    public int huevos = 0;
    public int jabon = 0;
    public int leche = 0;
    public int manzanas = 0;
    public int pan = 0;
    public int papas = 0;
    public int pollo = 0;
    
   // private DB db;
    
    public ContenidoDB(int agua, int arroz, int cereal, int huevos, int jabon, int leche, int manzanas, int pan, int papas, int pollo) {
        this.agua = agua;
        this.arroz = arroz;
        this.cereal = cereal;
        this.huevos = huevos;
        this.jabon = jabon;
        this.leche = leche;
        this.manzanas = manzanas;
        this.pan = pan;
        this.papas = papas;
        this.pollo = pollo;
    }
    
    public void imprimirDB(){
        System.out.println("1. Producto: Agua, precio: $1.00, existencia: " + agua);
        System.out.println("2. Producto: Arroz, precio: $3.00, existencia: " + arroz);
        System.out.println("3. Producto: Cereal, precio: $4.50, existencia: " + cereal);
        System.out.println("4. Producto: Huevos, precio: $2.00, existencia: " + huevos);
        System.out.println("5. Producto: Jabon, precio: $2.20, existencia: " + jabon);
        System.out.println("6. Producto: Leche, precio: $2.50, existencia: " + leche);
        System.out.println("7. Producto: Manzanas, precio: $1.50, existencia: " + manzanas);
        System.out.println("8. Producto: Pan, precio: $1.20, existencia: " + pan);
        System.out.println("9. Producto: Papas, precio: $1.80, existencia: " + papas);
        System.out.println("10. Producto: Pollo, precio: $5.00, existencia: " + pollo);
    }
}
