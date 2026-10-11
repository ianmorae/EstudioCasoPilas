# Analizador de expresiones aritméticas con pilas

**Primer estudio de caso** — SOFT-10 Estructuras de Datos (C3-2026)<br>
**Universidad CENFOTEC**<br>
**Docente:** Romario Salas Cerdas<br>
**Estudiante:** Ian Aarón Mora Espinoza

## Descripción

Programa de consola en Java que usa una **pila dinámica** (nodos enlazados) para analizar
expresiones aritméticas con números literales, variables numéricas, operadores (`+ - * /`) y
paréntesis. El programa:

1. Separa la expresión en tokens (cada token es un `Nodo`).
2. Valida que los paréntesis estén balanceados usando una pila.
3. Convierte la expresión de notación infija a postfija (algoritmo Shunting-yard).
4. Evalúa la expresión postfija con una pila de operandos.

Cada etapa muestra el paso a paso de la pila en pantalla.

## Estructura

```
src/
├── Nodo.java                  Token de la expresión (clase autorreferenciada)
├── Pila.java                  Pila dinámica: push, pop, peek, estaVacia, vaciar, mostrarPila
├── TablaVariables.java        Variables (búsqueda lineal y ordenamiento burbuja)
├── AnalizadorExpresiones.java Tokenización, validación, conversión y evaluación
└── Main.java                  Rutina main() y menú
```

## Cómo ejecutar

Desde la carpeta del proyecto:

```bash
javac -d out src/*.java
java -cp out Main
```

También se puede abrir la carpeta en Visual Studio Code (con el Extension Pack for Java) y
ejecutar `Main.java`.

## Ejemplo

```
Expresión: (precio + 5) * cantidad / 2
Variables: precio = 15, cantidad = 4
Postfija:  precio 5 + cantidad * 2 /
Resultado: 40
```
