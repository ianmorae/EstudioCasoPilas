/**
 * Clase TablaVariables.
 * Guarda las variables numericas que el usuario define (nombre y valor)
 * para poder sustituirlas al evaluar una expresion.
 *
 * Usa dos arreglos paralelos: en la posicion i de "nombres" esta el nombre
 * de la variable y en la posicion i de "valores" esta su valor.
 * - Para encontrar una variable se usa busqueda lineal.
 * - Para mostrarlas en orden alfabetico se usa el ordenamiento burbuja.
 *
 * @author Ian Aaron Mora Espinoza
 */
public class TablaVariables {

    private static final int MAXIMO = 50;   // Cantidad maxima de variables

    private String[] nombres;   // Nombres de las variables
    private double[] valores;   // Valores de las variables
    private int cantidad;       // Cantidad de variables registradas

    /**
     * Constructor: crea la tabla vacia.
     */
    public TablaVariables() {
        nombres = new String[MAXIMO];
        valores = new double[MAXIMO];
        cantidad = 0;
    }

    /**
     * Busqueda lineal: recorre el arreglo hasta encontrar el nombre.
     * @param nombre nombre de la variable buscada
     * @return la posicion de la variable, o -1 si no existe
     */
    public int buscarVariable(String nombre) {
        for (int i = 0; i < cantidad; i++) {
            if (nombres[i].equals(nombre)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Define una variable. Si ya existe se actualiza su valor; si no
     * existe se agrega al final de la tabla.
     * @param nombre nombre de la variable
     * @param valor valor numerico
     * @return true si se pudo guardar, false si la tabla esta llena
     */
    public boolean definirVariable(String nombre, double valor) {
        int posicion = buscarVariable(nombre);
        if (posicion != -1) {
            valores[posicion] = valor;
            return true;
        }
        if (cantidad == MAXIMO) {
            return false;
        }
        nombres[cantidad] = nombre;
        valores[cantidad] = valor;
        cantidad++;
        return true;
    }

    /**
     * Indica si la variable ya fue definida.
     * @param nombre nombre de la variable
     * @return true si existe
     */
    public boolean existe(String nombre) {
        return buscarVariable(nombre) != -1;
    }

    /**
     * Devuelve el valor de una variable. Se debe llamar despues de
     * verificar con existe() que la variable esta definida.
     * @param nombre nombre de la variable
     * @return valor de la variable
     */
    public double obtenerValor(String nombre) {
        return valores[buscarVariable(nombre)];
    }

    /**
     * Ordenamiento burbuja: ordena las variables alfabeticamente por nombre.
     * Cada vez que se intercambia un nombre tambien se intercambia su valor,
     * para que los dos arreglos sigan correspondiendo.
     */
    public void ordenarPorNombre() {
        for (int i = 0; i < cantidad - 1; i++) {
            for (int j = 0; j < cantidad - 1 - i; j++) {
                if (nombres[j].compareTo(nombres[j + 1]) > 0) {
                    String nombreTemp = nombres[j];
                    nombres[j] = nombres[j + 1];
                    nombres[j + 1] = nombreTemp;

                    double valorTemp = valores[j];
                    valores[j] = valores[j + 1];
                    valores[j + 1] = valorTemp;
                }
            }
        }
    }

    /**
     * Muestra todas las variables en orden alfabetico.
     */
    public void mostrarVariables() {
        if (cantidad == 0) {
            System.out.println("No hay variables definidas.");
            return;
        }
        ordenarPorNombre();
        System.out.println("Variables definidas (" + cantidad + "):");
        for (int i = 0; i < cantidad; i++) {
            System.out.println("   " + nombres[i] + " = " + AnalizadorExpresiones.formatear(valores[i]));
        }
    }

    /**
     * Verifica que un nombre de variable sea valido: debe iniciar con una
     * letra y solo puede contener letras, digitos o guion bajo.
     * @param nombre nombre a verificar
     * @return true si el nombre es valido
     */
    public static boolean esNombreValido(String nombre) {
        if (nombre == null || nombre.length() == 0) {
            return false;
        }
        if (!Character.isLetter(nombre.charAt(0))) {
            return false;
        }
        for (int i = 1; i < nombre.length(); i++) {
            char c = nombre.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }
}
