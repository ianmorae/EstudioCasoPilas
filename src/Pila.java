/**
 * Clase Pila.
 * Implementacion dinamica de una pila mediante nodos enlazados.
 * Funciona bajo el principio LIFO (Last In, First Out): el ultimo nodo
 * que entra es el primero que sale, y solo el nodo de la cima es accesible.
 *
 * Al ser dinamica no tiene un tamanio maximo fijo: cada push() crea el
 * espacio que necesita, por lo que no existe el caso de "pila llena".
 *
 * @author Ian Aaron Mora Espinoza
 */
public class Pila {

    private Nodo cima;     // Referencia al nodo que esta en la cima de la pila
    private int tamanio;   // Cantidad de nodos que tiene la pila

    /**
     * Constructor: crea una pila vacia.
     */
    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    /**
     * push(): inserta un nodo nuevo en la cima de la pila.
     * El nodo nuevo apunta a la cima anterior y luego pasa a ser la cima,
     * asi no se pierde ninguno de los nodos que ya estaban.
     * @param nuevo nodo que se va a insertar
     */
    public void push(Nodo nuevo) {
        nuevo.setSiguiente(cima);
        cima = nuevo;
        tamanio++;
    }

    /**
     * push(): version que crea el nodo a partir del valor y el tipo.
     * @param valor texto del token
     * @param tipo tipo del token
     */
    public void push(String valor, String tipo) {
        push(new Nodo(valor, tipo));
    }

    /**
     * pop(): remueve el nodo de la cima y lo retorna para que el programa
     * lo pueda usar. Si la pila esta vacia retorna null.
     * @return el nodo removido, o null si la pila esta vacia
     */
    public Nodo pop() {
        if (estaVacia()) {
            return null;
        }
        Nodo removido = cima;
        cima = cima.getSiguiente();
        removido.setSiguiente(null); // Se desconecta el nodo de la pila
        tamanio--;
        return removido;
    }

    /**
     * peek(): consulta el nodo de la cima sin removerlo.
     * @return el nodo de la cima, o null si la pila esta vacia
     */
    public Nodo peek() {
        return cima;
    }

    /**
     * Indica si la pila no tiene nodos.
     * @return true si la pila esta vacia
     */
    public boolean estaVacia() {
        return cima == null;
    }

    /**
     * Devuelve la cantidad de nodos de la pila.
     * @return tamanio actual
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Elimina todos los nodos de la pila.
     */
    public void vaciar() {
        cima = null;
        tamanio = 0;
    }

    /**
     * Muestra la pila de forma vertical, de la cima hacia el fondo.
     * Se recorre con un nodo auxiliar (nodoTemp), igual que en una lista.
     */
    public void mostrarPila() {
        if (estaVacia()) {
            System.out.println("   | (pila vacia) |");
            return;
        }
        Nodo nodoTemp = cima;
        System.out.println("   Cima");
        while (nodoTemp != null) {
            System.out.println("   | " + nodoTemp.toString());
            nodoTemp = nodoTemp.getSiguiente();
        }
        System.out.println("   Fondo   (tamanio: " + tamanio + ")");
    }

    /**
     * Devuelve el contenido de la pila en una sola linea, del fondo a la
     * cima, por ejemplo [ ( + * ]. Se usa para mostrar el paso a paso.
     * @return contenido de la pila como texto
     */
    public String contenidoEnLinea() {
        String texto = "";
        Nodo nodoTemp = cima;
        // Como se recorre desde la cima, cada valor se agrega al inicio
        while (nodoTemp != null) {
            texto = nodoTemp.getValor() + " " + texto;
            nodoTemp = nodoTemp.getSiguiente();
        }
        return "[ " + texto + "]";
    }
}
