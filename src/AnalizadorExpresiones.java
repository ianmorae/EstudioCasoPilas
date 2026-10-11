/**
 * Clase AnalizadorExpresiones.
 * Analiza y evalua expresiones aritmeticas en notacion infija usando pilas.
 *
 * El proceso se divide en cuatro etapas:
 *  1. tokenizar()           : separa la expresion en tokens (nodos).
 *  2. validarParentesis()   : usa una pila para revisar que cada "(" tenga su ")".
 *  3. convertirAPostfija()  : usa una pila de operadores para pasar de infija
 *                             a postfija (algoritmo Shunting-yard de Dijkstra).
 *  4. evaluar()             : usa una pila de operandos para calcular el
 *                             resultado de la expresion postfija.
 *
 * Operadores soportados: + - * /  (con * y / de mayor precedencia).
 *
 * @author Ian Aaron Mora Espinoza
 */
public class AnalizadorExpresiones {

    private String expresion;        // Expresion original escrita por el usuario
    private Nodo[] tokens;           // Tokens de la expresion en notacion infija
    private int cantidadTokens;      // Cantidad de tokens en el arreglo tokens
    private Nodo[] postfija;         // Tokens de la expresion en notacion postfija
    private int cantidadPostfija;    // Cantidad de tokens en el arreglo postfija
    private double resultado;        // Ultimo resultado calculado
    private String mensajeError;     // Descripcion del ultimo error encontrado

    /**
     * Constructor: el analizador inicia sin expresion.
     */
    public AnalizadorExpresiones() {
        expresion = "";
        tokens = new Nodo[0];
        cantidadTokens = 0;
        postfija = new Nodo[0];
        cantidadPostfija = 0;
        resultado = 0;
        mensajeError = "";
    }

    // ----------------------- Getters -----------------------

    public String getExpresion() {
        return expresion;
    }

    public boolean tieneExpresion() {
        return cantidadTokens > 0;
    }

    public double getResultado() {
        return resultado;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    // ======================= ETAPA 1: TOKENIZAR =======================

    /**
     * Recibe una expresion nueva, la separa en tokens y revisa el orden
     * de los tokens. Si algo falla, la expresion no se acepta.
     * @param nuevaExpresion texto escrito por el usuario
     * @return true si la expresion se pudo separar en tokens validos
     */
    public boolean setExpresion(String nuevaExpresion) {
        cantidadPostfija = 0; // La postfija anterior ya no sirve
        if (!tokenizar(nuevaExpresion) || !validarOrden()) {
            cantidadTokens = 0;
            expresion = "";
            return false;
        }
        expresion = nuevaExpresion.trim();
        return true;
    }

    /**
     * Recorre la expresion caracter por caracter y crea un nodo por cada
     * token: numeros (con o sin decimales), variables, operadores y parentesis.
     * @param texto expresion a separar
     * @return true si todos los caracteres son validos
     */
    private boolean tokenizar(String texto) {
        tokens = new Nodo[texto.length()]; // Nunca hay mas tokens que caracteres
        cantidadTokens = 0;
        int i = 0;

        while (i < texto.length()) {
            char c = texto.charAt(i);

            if (c == ' ') {
                i++; // Los espacios solo separan, no son tokens

            } else if (Character.isDigit(c) || c == '.') {
                // Numero literal: se leen todos los digitos seguidos
                int inicio = i;
                int puntos = 0;
                while (i < texto.length() && (Character.isDigit(texto.charAt(i)) || texto.charAt(i) == '.')) {
                    if (texto.charAt(i) == '.') {
                        puntos++;
                    }
                    i++;
                }
                String numero = texto.substring(inicio, i);
                if (puntos > 1 || numero.equals(".")) {
                    mensajeError = "El numero '" + numero + "' no es valido.";
                    return false;
                }
                tokens[cantidadTokens] = new Nodo(numero, Nodo.NUMERO);
                cantidadTokens++;

            } else if (Character.isLetter(c)) {
                // Variable: inicia con letra y sigue con letras, digitos o _
                int inicio = i;
                while (i < texto.length() && (Character.isLetterOrDigit(texto.charAt(i)) || texto.charAt(i) == '_')) {
                    i++;
                }
                tokens[cantidadTokens] = new Nodo(texto.substring(inicio, i), Nodo.VARIABLE);
                cantidadTokens++;

            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens[cantidadTokens] = new Nodo(String.valueOf(c), Nodo.OPERADOR);
                cantidadTokens++;
                i++;

            } else if (c == '(') {
                tokens[cantidadTokens] = new Nodo("(", Nodo.PAR_ABRE);
                cantidadTokens++;
                i++;

            } else if (c == ')') {
                tokens[cantidadTokens] = new Nodo(")", Nodo.PAR_CIERRA);
                cantidadTokens++;
                i++;

            } else {
                mensajeError = "Caracter no valido '" + c + "' en la posicion " + (i + 1) + ".";
                return false;
            }
        }

        if (cantidadTokens == 0) {
            mensajeError = "La expresion esta vacia.";
            return false;
        }
        return true;
    }

    /**
     * Revisa que los tokens esten en un orden logico:
     * - No puede haber dos operandos seguidos (ej. "3 4").
     * - No puede haber dos operadores seguidos (ej. "3 + * 4").
     * - La expresion no puede iniciar ni terminar con un operador.
     * - Despues de "(" no puede venir un operador ni ")".
     * - Antes de "(" no puede haber un operando ni ")".
     * @return true si el orden de los tokens es correcto
     */
    private boolean validarOrden() {
        for (int i = 0; i < cantidadTokens; i++) {
            Nodo actual = tokens[i];
            Nodo anterior = null;
            if (i > 0) {
                anterior = tokens[i - 1];
            }

            if (actual.esOperando() || actual.getTipo().equals(Nodo.PAR_ABRE)) {
                // Un operando o "(" no puede ir despues de otro operando o de ")"
                if (anterior != null && (anterior.esOperando() || anterior.getTipo().equals(Nodo.PAR_CIERRA))) {
                    mensajeError = "Falta un operador antes de '" + actual.getValor() + "'.";
                    return false;
                }
            } else {
                // Un operador o ")" debe ir despues de un operando o de ")"
                if (anterior == null || anterior.getTipo().equals(Nodo.OPERADOR) || anterior.getTipo().equals(Nodo.PAR_ABRE)) {
                    mensajeError = "Falta un operando antes de '" + actual.getValor() + "'.";
                    return false;
                }
            }
        }

        // La expresion no puede terminar con un operador
        if (tokens[cantidadTokens - 1].getTipo().equals(Nodo.OPERADOR)) {
            mensajeError = "La expresion no puede terminar con el operador '" + tokens[cantidadTokens - 1].getValor() + "'.";
            return false;
        }
        return true;
    }

    /**
     * Muestra la lista de tokens con su tipo.
     */
    public void mostrarTokens() {
        System.out.println("Tokens encontrados (" + cantidadTokens + "):");
        for (int i = 0; i < cantidadTokens; i++) {
            System.out.println("   " + (i + 1) + ". " + tokens[i].toString());
        }
    }

    // ================== ETAPA 2: VALIDAR PARENTESIS ===================

    /**
     * Usa una pila para verificar que los parentesis esten balanceados:
     * cada "(" se apila y cada ")" desapila su pareja. Si al final la pila
     * queda vacia, todos los parentesis tienen pareja.
     * @param mostrarPasos true para imprimir el paso a paso
     * @return true si los parentesis estan balanceados
     */
    public boolean validarParentesis(boolean mostrarPasos) {
        Pila pila = new Pila();

        if (mostrarPasos) {
            System.out.println(String.format("%-6s %-10s %-26s %s", "Paso", "Token", "Accion", "Pila"));
        }

        int paso = 1;
        for (int i = 0; i < cantidadTokens; i++) {
            Nodo token = tokens[i];
            String accion;

            if (token.getTipo().equals(Nodo.PAR_ABRE)) {
                pila.push(new Nodo("(", Nodo.PAR_ABRE));
                accion = "push (";
            } else if (token.getTipo().equals(Nodo.PAR_CIERRA)) {
                if (pila.estaVacia()) {
                    if (mostrarPasos) {
                        imprimirFila(paso, token.getValor(), "ERROR: pila vacia", pila.contenidoEnLinea());
                    }
                    mensajeError = "Hay un ')' en la posicion " + (i + 1) + " que no tiene un '(' que lo abra.";
                    return false;
                }
                pila.pop();
                accion = "pop ( (pareja encontrada)";
            } else {
                continue; // Los demas tokens no afectan esta validacion
            }

            if (mostrarPasos) {
                imprimirFila(paso, token.getValor(), accion, pila.contenidoEnLinea());
            }
            paso++;
        }

        if (!pila.estaVacia()) {
            if (pila.getTamanio() == 1) {
                mensajeError = "Falta 1 parentesis ')' por cerrar.";
            } else {
                mensajeError = "Faltan " + pila.getTamanio() + " parentesis ')' por cerrar.";
            }
            return false;
        }
        return true;
    }

    // ================= ETAPA 3: CONVERTIR A POSTFIJA ==================

    /**
     * Convierte la expresion de infija a postfija con el algoritmo
     * Shunting-yard. Usa una pila para guardar operadores y parentesis:
     * - Operando: pasa directo a la salida.
     * - "(": se apila.
     * - ")": se desapilan operadores a la salida hasta encontrar "(".
     * - Operador: se desapilan los operadores de precedencia mayor o igual
     *   y luego se apila el operador actual.
     * - Al final se desapila todo lo que quede.
     * @param mostrarPasos true para imprimir el paso a paso
     * @return true si la conversion fue exitosa
     */
    public boolean convertirAPostfija(boolean mostrarPasos) {
        if (!validarParentesis(false)) {
            return false;
        }

        Pila pilaOperadores = new Pila();
        postfija = new Nodo[cantidadTokens];
        cantidadPostfija = 0;

        if (mostrarPasos) {
            System.out.println(String.format("%-6s %-10s %-26s %-16s %s", "Paso", "Token", "Accion", "Pila", "Salida"));
        }

        for (int i = 0; i < cantidadTokens; i++) {
            Nodo token = tokens[i];
            String accion = "";

            if (token.esOperando()) {
                agregarASalida(token);
                accion = "a la salida";

            } else if (token.getTipo().equals(Nodo.PAR_ABRE)) {
                pilaOperadores.push(new Nodo("(", Nodo.PAR_ABRE));
                accion = "push (";

            } else if (token.getTipo().equals(Nodo.PAR_CIERRA)) {
                // Se sacan operadores hasta llegar al "(" correspondiente
                while (!pilaOperadores.peek().getTipo().equals(Nodo.PAR_ABRE)) {
                    Nodo operador = pilaOperadores.pop();
                    agregarASalida(operador);
                    accion = accion + "pop " + operador.getValor() + ", ";
                }
                pilaOperadores.pop(); // Se descarta el "("
                accion = accion + "descartar (";

            } else {
                // Operador: salen los de precedencia mayor o igual
                while (!pilaOperadores.estaVacia()
                        && pilaOperadores.peek().getTipo().equals(Nodo.OPERADOR)
                        && pilaOperadores.peek().getPrecedencia() >= token.getPrecedencia()) {
                    Nodo operador = pilaOperadores.pop();
                    agregarASalida(operador);
                    accion = accion + "pop " + operador.getValor() + ", ";
                }
                pilaOperadores.push(new Nodo(token.getValor(), Nodo.OPERADOR));
                accion = accion + "push " + token.getValor();
            }

            if (mostrarPasos) {
                imprimirFila(i + 1, token.getValor(), accion, pilaOperadores.contenidoEnLinea(), textoPostfija());
            }
        }

        // Al terminar la expresion se vacia la pila en la salida
        String accionFinal = "";
        while (!pilaOperadores.estaVacia()) {
            Nodo operador = pilaOperadores.pop();
            agregarASalida(operador);
            accionFinal = accionFinal + "pop " + operador.getValor() + ", ";
        }
        if (mostrarPasos && accionFinal.length() > 0) {
            accionFinal = accionFinal.substring(0, accionFinal.length() - 2); // Quita la ultima coma
            imprimirFila(cantidadTokens + 1, "fin", accionFinal, pilaOperadores.contenidoEnLinea(), textoPostfija());
        }
        return true;
    }

    /**
     * Agrega una copia del token al final de la expresion postfija.
     * @param token token que pasa a la salida
     */
    private void agregarASalida(Nodo token) {
        postfija[cantidadPostfija] = new Nodo(token.getValor(), token.getTipo());
        cantidadPostfija++;
    }

    /**
     * Devuelve la expresion postfija como texto, separada por espacios.
     * @return expresion postfija
     */
    public String textoPostfija() {
        String texto = "";
        for (int i = 0; i < cantidadPostfija; i++) {
            texto = texto + postfija[i].getValor() + " ";
        }
        return texto.trim();
    }

    // ===================== ETAPA 4: EVALUAR ===========================

    /**
     * Evalua la expresion postfija usando una pila de operandos:
     * - Numero: se apila.
     * - Variable: se busca su valor y se apila.
     * - Operador: se desapilan dos operandos (primero sale el derecho),
     *   se opera y el resultado se vuelve a apilar.
     * Al final, el unico nodo que queda en la pila es el resultado.
     * @param variables tabla con los valores de las variables
     * @param mostrarPasos true para imprimir el paso a paso
     * @return true si la evaluacion fue exitosa
     */
    public boolean evaluar(TablaVariables variables, boolean mostrarPasos) {
        if (!convertirAPostfija(false)) {
            return false;
        }

        // Antes de evaluar se revisa que todas las variables esten definidas
        for (int i = 0; i < cantidadPostfija; i++) {
            if (postfija[i].getTipo().equals(Nodo.VARIABLE) && !variables.existe(postfija[i].getValor())) {
                mensajeError = "La variable '" + postfija[i].getValor() + "' no esta definida. Definala con la opcion 2.";
                return false;
            }
        }

        Pila pilaOperandos = new Pila();

        if (mostrarPasos) {
            System.out.println(String.format("%-6s %-10s %-26s %s", "Paso", "Token", "Accion", "Pila"));
        }

        for (int i = 0; i < cantidadPostfija; i++) {
            Nodo token = postfija[i];
            String accion;

            if (token.getTipo().equals(Nodo.NUMERO)) {
                pilaOperandos.push(new Nodo(token.getValor(), Nodo.NUMERO));
                accion = "push " + token.getValor();

            } else if (token.getTipo().equals(Nodo.VARIABLE)) {
                // Se sustituye la variable por su valor
                String valor = aTexto(variables.obtenerValor(token.getValor()));
                pilaOperandos.push(new Nodo(valor, Nodo.NUMERO));
                accion = token.getValor() + " = " + valor + ", push " + valor;

            } else {
                // Operador: el primer pop es el operando derecho
                Nodo derecho = pilaOperandos.pop();
                Nodo izquierdo = pilaOperandos.pop();
                if (derecho == null || izquierdo == null) {
                    mensajeError = "La expresion esta mal formada: faltan operandos.";
                    return false;
                }
                double b = Double.parseDouble(derecho.getValor());
                double a = Double.parseDouble(izquierdo.getValor());

                if (token.getValor().equals("/") && b == 0) {
                    mensajeError = "Division entre cero: " + formatear(a) + " / 0.";
                    return false;
                }
                // El resultado parcial se guarda completo (sin redondear) para
                // no perder precision en las siguientes operaciones
                double parcial = operar(a, b, token.getValor());
                pilaOperandos.push(new Nodo(aTexto(parcial), Nodo.NUMERO));
                accion = formatear(a) + " " + token.getValor() + " " + formatear(b) + " = " + formatear(parcial);
            }

            if (mostrarPasos) {
                imprimirFila(i + 1, token.getValor(), accion, pilaOperandos.contenidoEnLinea());
            }
        }

        if (pilaOperandos.getTamanio() != 1) {
            mensajeError = "La expresion esta mal formada: sobran operandos.";
            return false;
        }
        resultado = Double.parseDouble(pilaOperandos.pop().getValor());
        return true;
    }

    /**
     * Aplica un operador a dos numeros.
     * @param a operando izquierdo
     * @param b operando derecho
     * @param operador + - * o /
     * @return resultado de la operacion
     */
    private double operar(double a, double b, String operador) {
        if (operador.equals("+")) {
            return a + b;
        } else if (operador.equals("-")) {
            return a - b;
        } else if (operador.equals("*")) {
            return a * b;
        } else {
            return a / b;
        }
    }

    // ===================== Metodos de apoyo ===========================

    /**
     * Determina el tipo de un token escrito a mano (se usa en la prueba
     * manual de la pila).
     * @param texto token escrito por el usuario
     * @return el tipo del token, o null si no es un token valido
     */
    public static String detectarTipo(String texto) {
        if (texto.equals("(")) {
            return Nodo.PAR_ABRE;
        } else if (texto.equals(")")) {
            return Nodo.PAR_CIERRA;
        } else if (texto.equals("+") || texto.equals("-") || texto.equals("*") || texto.equals("/")) {
            return Nodo.OPERADOR;
        } else if (TablaVariables.esNombreValido(texto)) {
            return Nodo.VARIABLE;
        }
        try {
            Double.parseDouble(texto);
            return Nodo.NUMERO;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convierte un numero a texto sin perder precision. Si es entero se
     * escribe sin ".0" (ej. 7 en lugar de 7.0).
     * @param numero numero a convertir
     * @return numero como texto
     */
    private static String aTexto(double numero) {
        if (numero == (long) numero) {
            return String.valueOf((long) numero);
        }
        return String.valueOf(numero);
    }

    /**
     * Da formato a un numero para mostrarlo: los enteros se muestran sin ".0" y los
     * decimales se redondean a 4 cifras.
     * @param numero numero a mostrar
     * @return numero como texto
     */
    public static String formatear(double numero) {
        double redondeado = Math.round(numero * 10000.0) / 10000.0;
        if (redondeado == (long) redondeado) {
            return String.valueOf((long) redondeado);
        }
        return String.valueOf(redondeado);
    }

    /**
     * Imprime una fila de la tabla del paso a paso (4 columnas).
     */
    private void imprimirFila(int paso, String token, String accion, String pila) {
        System.out.println(String.format("%-6d %-10s %-26s %s", paso, token, accion, pila));
    }

    /**
     * Imprime una fila de la tabla del paso a paso (5 columnas).
     */
    private void imprimirFila(int paso, String token, String accion, String pila, String salida) {
        System.out.println(String.format("%-6d %-10s %-26s %-16s %s", paso, token, accion, pila, salida));
    }
}
