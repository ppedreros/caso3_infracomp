package c3_infracomp;

import java.util.Scanner;

public class Main {
    public static volatile boolean found = false;
    public static int nThreads;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el algoritmo de generación de código criptográfico de hash (SHA-256 o SHA-512):");
        String algorithm = scanner.nextLine();
        
        System.out.println("Ingrese la cadena C:");
        String cadenaC = scanner.nextLine();
        
        System.out.println("Ingrese el numero entero buscado de bits en cero:");
        int nBitsCero = scanner.nextInt();
        
        System.out.println("Ingrese si quiere correr 1 o 2 threads:");
        nThreads = scanner.nextInt();
        
        scanner.close();

        if (nThreads == 1) {
            MinerThread minerThread = new MinerThread(1, algorithm, cadenaC, nBitsCero, 'a', 'z');
            minerThread.start();
        } else if (nThreads == 2) {
            MinerThread minerThread1 = new MinerThread(1, algorithm, cadenaC, nBitsCero, 'a', 'm');
            MinerThread minerThread2 = new MinerThread(2, algorithm, cadenaC, nBitsCero, 'n', 'z');
            System.out.println("Cadena C: " + cadenaC);
            minerThread1.start();
            minerThread2.start();
        } else {
            System.out.println("Número de threads no válido.");
        }
    }
}

