import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SOFT-10 Estructura de Datos
 * Algoritmos de ordenamiento y búsqueda binaria sobre un arreglo estático de Strings.
 *
 * Nota: todas las comparaciones usan compareToIgnoreCase para que el ordenamiento
 * y la búsqueda binaria sean consistentes entre sí (no distinguen mayúsculas/minúsculas).
 */
public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        // ---------- 1. Llenado del arreglo ----------
        int n = leerEnteroPositivo("Ingrese el tamaño del arreglo: ");
        String[] palabras = new String[n]; // arreglo estático de tamaño definido por el usuario

        for (int i = 0; i < n; i++) {
            System.out.print("Palabra " + (i + 1) + ": ");
            palabras[i] = br.readLine().trim();
        }

        System.out.println("\nArreglo original:");
        imprimir(palabras);

        // ---------- 2. Menú de ordenamiento ----------
        int opcion = 0;
        while (opcion < 1 || opcion > 5) {
            System.out.println("\n=== MENÚ DE ORDENAMIENTO ===");
            System.out.println("1. Selección");
            System.out.println("2. Inserción");
            System.out.println("3. Burbuja");
            System.out.println("4. Mezcla (Merge Sort)");
            System.out.println("5. Rápido (Quick Sort)");
            opcion = leerEnteroPositivo("Elija una opción (1-5): ");
        }

        switch (opcion) {
            case 1: ordenarSeleccion(palabras); break;
            case 2: ordenarInsercion(palabras); break;
            case 3: ordenarBurbuja(palabras); break;
            case 4: ordenarMezcla(palabras, 0, palabras.length - 1); break;
            case 5: ordenarRapido(palabras, 0, palabras.length - 1); break;
        }

        System.out.println("\nArreglo ordenado:");
        imprimir(palabras);

        // ---------- 3. Búsqueda binaria ----------
        String respuesta;
        do {
            System.out.print("\nIngrese la palabra a buscar: ");
            String objetivo = br.readLine().trim();

            int pos = busquedaBinaria(palabras, objetivo);
            if (pos >= 0) {
                System.out.println("\"" + objetivo + "\" encontrada en la posición " + pos
                        + " (índice base 0).");
            } else {
                System.out.println("\"" + objetivo + "\" no se encuentra en el arreglo.");
            }

            System.out.print("¿Desea buscar otra palabra? (s/n): ");
            respuesta = br.readLine().trim();
        } while (respuesta.equalsIgnoreCase("s"));

        System.out.println("Programa finalizado.");
    }

    // =====================================================================
    // ALGORITMOS DE ORDENAMIENTO
    // =====================================================================

    /**
     * ORDENAMIENTO POR SELECCIÓN
     * Complejidad de tiempo: O(n^2) en mejor, promedio y peor caso.
     */
    static void ordenarSeleccion(String[] a) {
        // Se recorre el arreglo; en cada vuelta se fija la posición i con el menor restante
        for (int i = 0; i < a.length - 1; i++) {
            int indiceMenor = i; // Se asume que el menor elemento está en la posición i

            // Se busca el menor elemento en la parte aún no ordenada (i+1 .. n-1)
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareToIgnoreCase(a[indiceMenor]) < 0) {
                    indiceMenor = j; // Se encontró uno menor: se actualiza el índice
                }
            }

            // Se intercambia el menor encontrado con el elemento de la posición i
            String temp = a[i];
            a[i] = a[indiceMenor];
            a[indiceMenor] = temp;
        }
    }

    /**
     * ORDENAMIENTO POR INSERCIÓN
     * Complejidad de tiempo: O(n^2) en peor y caso promedio; O(n) en el mejor caso
     * (arreglo ya ordenado).
     */
    static void ordenarInsercion(String[] a) {
        // Se considera que a[0] ya es una sublista ordenada; se empieza desde la posición 1
        for (int i = 1; i < a.length; i++) {
            String clave = a[i]; // Elemento que se va a insertar en la parte ordenada
            int j = i - 1;

            // Se desplazan a la derecha los elementos mayores que la clave
            while (j >= 0 && a[j].compareToIgnoreCase(clave) > 0) {
                a[j + 1] = a[j];
                j--;
            }

            // Se coloca la clave en su posición correcta
            a[j + 1] = clave;
        }
    }

    /**
     * ORDENAMIENTO BURBUJA (con bandera de optimización)
     * Complejidad de tiempo: O(n^2) en peor y caso promedio; O(n) en el mejor caso
     * gracias a la bandera "huboIntercambio".
     */
    static void ordenarBurbuja(String[] a) {
        boolean huboIntercambio;

        // Cada pasada "burbujea" el mayor elemento restante hacia el final
        for (int i = 0; i < a.length - 1; i++) {
            huboIntercambio = false;

            // Se comparan pares adyacentes; el límite se reduce porque los últimos i ya están ordenados
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j].compareToIgnoreCase(a[j + 1]) > 0) {
                    // Si están en desorden, se intercambian
                    String temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    huboIntercambio = true;
                }
            }

            // Si en una pasada completa no hubo intercambios, el arreglo ya está ordenado
            if (!huboIntercambio) {
                break;
            }
        }
    }

    /**
     * ORDENAMIENTO POR MEZCLA (MERGE SORT) - "divide y vencerás"
     * Consiste en dividir el arreglo por la mitad, ordenar cada mitad de forma recursiva
     * y luego mezclar (fusionar) las dos mitades ordenadas.
     * Complejidad de tiempo: O(n log n) en mejor, promedio y peor caso.
     * Usa memoria adicional O(n) para el arreglo auxiliar.
     */
    static void ordenarMezcla(String[] a, int izq, int der) {
        // Caso base: una sublista de 0 o 1 elemento ya está ordenada
        if (izq >= der) {
            return;
        }

        int medio = (izq + der) / 2; // Se calcula el punto medio

        ordenarMezcla(a, izq, medio);    // Se ordena la mitad izquierda
        ordenarMezcla(a, medio + 1, der); // Se ordena la mitad derecha
        mezclar(a, izq, medio, der);      // Se fusionan ambas mitades ordenadas
    }

    /** Fusiona las sublistas ordenadas a[izq..medio] y a[medio+1..der]. */
    static void mezclar(String[] a, int izq, int medio, int der) {
        // Se copia el rango a un arreglo auxiliar
        String[] aux = new String[der - izq + 1];
        int i = izq;        // Puntero de la mitad izquierda
        int j = medio + 1;  // Puntero de la mitad derecha
        int k = 0;          // Puntero del arreglo auxiliar

        // Se toma siempre el menor de los dos elementos al frente de cada mitad
        while (i <= medio && j <= der) {
            if (a[i].compareToIgnoreCase(a[j]) <= 0) {
                aux[k++] = a[i++];
            } else {
                aux[k++] = a[j++];
            }
        }

        // Se copian los elementos que sobraron de la mitad izquierda (si hay)
        while (i <= medio) {
            aux[k++] = a[i++];
        }
        // Se copian los elementos que sobraron de la mitad derecha (si hay)
        while (j <= der) {
            aux[k++] = a[j++];
        }

        // Se devuelve el resultado al arreglo original
        for (k = 0; k < aux.length; k++) {
            a[izq + k] = aux[k];
        }
    }

    /**
     * ORDENAMIENTO RÁPIDO (QUICK SORT) - "divide y vencerás"
     * Consiste en elegir un pivote, repartir los elementos menores a su izquierda y los
     * mayores a su derecha (partición), y repetir recursivamente en cada lado.
     * Complejidad de tiempo: O(n log n) en caso promedio y mejor caso;
     * O(n^2) en el peor caso (por ejemplo, arreglo ya ordenado con pivote en el extremo).
     */
    static void ordenarRapido(String[] a, int bajo, int alto) {
        // Caso base: sublista de 0 o 1 elemento
        if (bajo < alto) {
            int indicePivote = particionar(a, bajo, alto); // El pivote queda en su posición final
            ordenarRapido(a, bajo, indicePivote - 1);      // Se ordenan los menores al pivote
            ordenarRapido(a, indicePivote + 1, alto);      // Se ordenan los mayores al pivote
        }
    }

    /** Partición de Lomuto: usa el último elemento como pivote. */
    static int particionar(String[] a, int bajo, int alto) {
        String pivote = a[alto]; // Se elige el último elemento como pivote
        int i = bajo - 1;        // Frontera de la zona de elementos menores o iguales al pivote

        for (int j = bajo; j < alto; j++) {
            // Si el elemento es menor o igual al pivote, se pasa a la zona izquierda
            if (a[j].compareToIgnoreCase(pivote) <= 0) {
                i++;
                String temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        // Se coloca el pivote justo después de la zona de menores
        String temp = a[i + 1];
        a[i + 1] = a[alto];
        a[alto] = temp;

        return i + 1; // Posición final del pivote
    }

    // =====================================================================
    // BÚSQUEDA BINARIA
    // =====================================================================

    /**
     * BÚSQUEDA BINARIA (requiere arreglo ordenado)
     * Complejidad de tiempo: O(log n). En cada iteración se descarta la mitad de los
     * elementos, por lo que para n elementos se necesitan como máximo log2(n) comparaciones.
     * En el mejor caso (elemento en el centro) es O(1).
     *
     * @return índice de la palabra, o -1 si no existe.
     */
    static int busquedaBinaria(String[] a, String objetivo) {
        int izq = 0;              // Límite inferior del rango de búsqueda
        int der = a.length - 1;   // Límite superior del rango de búsqueda

        while (izq <= der) {
            int medio = izq + (der - izq) / 2; // Posición central (evita desbordamiento)
            int cmp = objetivo.compareToIgnoreCase(a[medio]);

            if (cmp == 0) {
                return medio;       // Se encontró la palabra
            } else if (cmp < 0) {
                der = medio - 1;    // La palabra está en la mitad izquierda: se descarta la derecha
            } else {
                izq = medio + 1;    // La palabra está en la mitad derecha: se descarta la izquierda
            }
        }
        return -1; // El rango quedó vacío: la palabra no está en el arreglo
    }

    // =====================================================================
    // UTILIDADES
    // =====================================================================

    static void imprimir(String[] a) {
        System.out.print("[");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + (i < a.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }

    /** Lee un entero positivo validando la entrada. */
    static int leerEnteroPositivo(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(br.readLine().trim());
                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Se ignora y se vuelve a pedir el dato
            }
            System.out.println("Entrada inválida. Ingrese un número entero positivo.");
        }
    }
}