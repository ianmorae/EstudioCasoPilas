import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase Main.
 * Punto de entrada del programa. Presenta un menu para trabajar con el
 * analizador de expresiones aritmeticas y con la pila de tokens.
 *
 * Curso: SOFT-10 Estructuras de Datos - Primer estudio de caso
 * @author Ian Aaron Mora Espinoza
 */
public class Main {

    // Lector de la consola, compartido por todos los metodos
    static BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));

    // Objetos principales del programa
    static AnalizadorExpresiones analizador = new AnalizadorExpresiones();
    static TablaVariables variables = new TablaVariables();
    static Pila pilaManual = new Pila(); // Pila para la prueba manual (opcion 7)

    /**
     * Rutina principal: muestra el menu hasta que el usuario elija salir.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("==================================================");
        System.out.println("   ANALIZADOR DE EXPRESIONES ARITMETICAS CON PILAS");
        System.out.println("==================================================");

        int opcion;
        do {
            menu();
            opcion = leerEntero("Seleccione una opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    ingresarExpresion();
                    break;
                case 2:
                    definirVariable();
                    break;
                case 3:
                    variables.mostrarVariables();
                    break;
                case 4:
                    validarParentesis();
                    break;
                case 5:
                    convertirAPostfija();
                    break;
                case 6:
                    evaluarExpresion();
                    break;
                case 7:
                    menuPilaManual();
                    break;
                case 0:
                    System.out.println("Gracias por usar el programa. Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
            if (opcion != 0) {
                pausar();
            }
        } while (opcion != 0);
    }

    /**
     * Muestra las opciones del menu principal y la expresion actual.
     */
    public static void menu() {
        System.out.println();
        System.out.println("------------------ MENU PRINCIPAL ------------------");
        if (analizador.tieneExpresion()) {
            System.out.println(" Expresion actual: " + analizador.getExpresion());
        } else {
            System.out.println(" Expresion actual: (ninguna)");
        }
        System.out.println("----------------------------------------------------");
        System.out.println(" 1. Ingresar expresion aritmetica");
        System.out.println(" 2. Definir o modificar una variable");
        System.out.println(" 3. Ver variables definidas");
        System.out.println(" 4. Validar parentesis (paso a paso)");
        System.out.println(" 5. Convertir a notacion postfija (paso a paso)");
        System.out.println(" 6. Evaluar expresion (paso a paso)");
        System.out.println(" 7. Probar operaciones de la pila manualmente");
        System.out.println(" 0. Salir");
        System.out.println("----------------------------------------------------");
    }

    // ===================== Opciones del menu ===========================

    /**
     * Opcion 1: lee una expresion, la separa en tokens y muestra el resultado.
     */
    public static void ingresarExpresion() throws IOException {
        System.out.println("Use numeros, variables, + - * / y parentesis.");
        System.out.println("Ejemplo: (precio + 5) * cantidad / 2");
        System.out.print("Expresion: ");
        String texto = lector.readLine();

        if (analizador.setExpresion(texto)) {
            System.out.println("Expresion registrada correctamente.");
            analizador.mostrarTokens();
        } else {
            System.out.println("Error: " + analizador.getMensajeError());
        }
    }

    /**
     * Opcion 2: define una variable nueva o cambia el valor de una existente.
     */
    public static void definirVariable() throws IOException {
        System.out.print("Nombre de la variable: ");
        String nombre = lector.readLine().trim();

        if (!TablaVariables.esNombreValido(nombre)) {
            System.out.println("Error: el nombre debe iniciar con una letra y solo usar letras, digitos o _.");
            return;
        }
        double valor = leerDecimal("Valor de " + nombre + ": ");

        if (variables.definirVariable(nombre, valor)) {
            System.out.println("Variable guardada: " + nombre + " = " + AnalizadorExpresiones.formatear(valor));
        } else {
            System.out.println("Error: ya no hay espacio para mas variables.");
        }
    }

    /**
     * Opcion 4: valida los parentesis mostrando el uso de la pila.
     */
    public static void validarParentesis() {
        if (!hayExpresion()) {
            return;
        }
        System.out.println("Validacion de parentesis de: " + analizador.getExpresion());
        System.out.println();
        if (analizador.validarParentesis(true)) {
            System.out.println();
            System.out.println("Resultado: la pila quedo vacia, los parentesis estan balanceados.");
        } else {
            System.out.println();
            System.out.println("Error: " + analizador.getMensajeError());
        }
    }

    /**
     * Opcion 5: convierte la expresion a postfija mostrando el paso a paso.
     */
    public static void convertirAPostfija() {
        if (!hayExpresion()) {
            return;
        }
        System.out.println("Conversion a postfija de: " + analizador.getExpresion());
        System.out.println();
        if (analizador.convertirAPostfija(true)) {
            System.out.println();
            System.out.println("Expresion postfija: " + analizador.textoPostfija());
        } else {
            System.out.println("Error: " + analizador.getMensajeError());
        }
    }

    /**
     * Opcion 6: evalua la expresion mostrando la pila de operandos.
     */
    public static void evaluarExpresion() {
        if (!hayExpresion()) {
            return;
        }
        System.out.println("Evaluacion de: " + analizador.getExpresion());
        if (analizador.evaluar(variables, true)) {
            System.out.println();
            System.out.println("Postfija utilizada: " + analizador.textoPostfija());
            System.out.println("RESULTADO: " + AnalizadorExpresiones.formatear(analizador.getResultado()));
        } else {
            System.out.println();
            System.out.println("Error: " + analizador.getMensajeError());
        }
    }

    /**
     * Opcion 7: submenu para probar push, pop, peek y demas operaciones
     * de la pila de forma manual, con tokens escritos por el usuario.
     */
    public static void menuPilaManual() throws IOException {
        int opcion;
        do {
            System.out.println();
            System.out.println("------------- PRUEBA MANUAL DE LA PILA -------------");
            pilaManual.mostrarPila();
            System.out.println("----------------------------------------------------");
            System.out.println(" 1. push (apilar un token)");
            System.out.println(" 2. pop (desapilar)");
            System.out.println(" 3. peek (ver la cima)");
            System.out.println(" 4. Ver tamanio");
            System.out.println(" 5. Vaciar la pila");
            System.out.println(" 0. Volver al menu principal");
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    System.out.print("Token a apilar (numero, variable, operador o parentesis): ");
                    String texto = lector.readLine().trim();
                    String tipo = AnalizadorExpresiones.detectarTipo(texto);
                    if (tipo == null) {
                        System.out.println("Error: '" + texto + "' no es un token valido.");
                    } else {
                        pilaManual.push(texto, tipo);
                        System.out.println("push -> " + texto + " (" + tipo + ")");
                    }
                    break;
                case 2:
                    Nodo removido = pilaManual.pop();
                    if (removido == null) {
                        System.out.println("La pila esta vacia, no hay nada que desapilar.");
                    } else {
                        System.out.println("pop -> " + removido.toString());
                    }
                    break;
                case 3:
                    Nodo cima = pilaManual.peek();
                    if (cima == null) {
                        System.out.println("La pila esta vacia.");
                    } else {
                        System.out.println("peek -> " + cima.toString());
                    }
                    break;
                case 4:
                    System.out.println("La pila tiene " + pilaManual.getTamanio() + " nodo(s).");
                    break;
                case 5:
                    pilaManual.vaciar();
                    System.out.println("La pila fue vaciada.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ===================== Metodos de apoyo ===========================

    /**
     * Verifica que ya se haya ingresado una expresion.
     * @return true si hay una expresion registrada
     */
    public static boolean hayExpresion() {
        if (!analizador.tieneExpresion()) {
            System.out.println("Primero debe ingresar una expresion (opcion 1).");
            return false;
        }
        return true;
    }

    /**
     * Lee un numero entero. Si el usuario escribe algo invalido, se lo
     * vuelve a pedir en lugar de detener el programa.
     * @param mensaje texto que se muestra al usuario
     * @return numero entero leido
     */
    public static int leerEntero(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String linea = lector.readLine();
            if (linea == null) {
                return 0; // Fin de la entrada: se sale del programa
            }
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe digitar un numero entero.");
            }
        }
    }

    /**
     * Lee un numero decimal, con la misma validacion que leerEntero().
     * @param mensaje texto que se muestra al usuario
     * @return numero decimal leido
     */
    public static double leerDecimal(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(lector.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe digitar un numero valido (use punto para los decimales).");
            }
        }
    }

    /**
     * Espera a que el usuario presione Enter antes de volver al menu.
     */
    public static void pausar() throws IOException {
        System.out.print("\nPresione Enter para continuar...");
        lector.readLine();
    }
}
