/**
 * Clase Nodo.
 * Representa un token de una expresion aritmetica. Cada nodo guarda el
 * texto del token, su tipo y una referencia al siguiente nodo, por lo que
 * es una clase autorreferenciada (igual que el nodo de una lista enlazada).
 *
 * Tipos de token posibles:
 *  - NUMERO     : un numero literal (ej. 5, 3.75)
 *  - VARIABLE   : el nombre de una variable numerica (ej. x, precio)
 *  - OPERADOR   : uno de los operadores + - * /
 *  - PAR_ABRE   : parentesis de apertura (
 *  - PAR_CIERRA : parentesis de cierre )
 *
 * @author Ian Aaron Mora Espinoza
 */
public class Nodo {

    // Constantes con los tipos de token, para no escribir los textos a mano
    public static final String NUMERO = "NUMERO";
    public static final String VARIABLE = "VARIABLE";
    public static final String OPERADOR = "OPERADOR";
    public static final String PAR_ABRE = "PAR_ABRE";
    public static final String PAR_CIERRA = "PAR_CIERRA";

    private String valor;     // Texto del token (ej. "12", "x", "+", "(")
    private String tipo;      // Tipo del token (una de las constantes anteriores)
    private Nodo siguiente;   // Referencia al nodo que esta debajo en la pila

    /**
     * Constructor del nodo. El siguiente inicia en null porque el nodo
     * todavia no esta enlazado con ningun otro.
     * @param valor texto del token
     * @param tipo tipo del token
     */
    public Nodo(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
        this.siguiente = null;
    }

    // ----------------------- Getters y setters -----------------------

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    // ----------------------- Metodos de apoyo -----------------------

    /**
     * Indica si el token es un operando (numero o variable).
     * @return true si es NUMERO o VARIABLE
     */
    public boolean esOperando() {
        return tipo.equals(NUMERO) || tipo.equals(VARIABLE);
    }

    /**
     * Devuelve la precedencia del operador: * y / tienen mayor prioridad
     * que + y -. Si el token no es un operador devuelve 0.
     * @return 2 para * y /, 1 para + y -, 0 en otro caso
     */
    public int getPrecedencia() {
        if (valor.equals("*") || valor.equals("/")) {
            return 2;
        } else if (valor.equals("+") || valor.equals("-")) {
            return 1;
        }
        return 0;
    }

    /**
     * Representacion del nodo como texto, util para mostrar la pila.
     * @return texto del token y su tipo
     */
    @Override
    public String toString() {
        return valor + " (" + tipo + ")";
    }
}
