package c3_infracomp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MinerThread extends Thread {
	private int id;
	private String algorithm;
    private String cadenaC;
    private int nBitsCero;
    private char startChar, endChar;
    
    public MinerThread(int id, String algorithm, String cadenaC, int nBitsCero, char startChar, char endChar) {
    	this.id = id;
        this.algorithm = algorithm;
        this.cadenaC = cadenaC;
        this.nBitsCero = nBitsCero;
        this.startChar = startChar;
        this.endChar = endChar;
    }
    
    private String calcularHash(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private boolean verificarHash(String hash, int nBitsCero) {
        int nBits = 0;

        for (char c : hash.toCharArray()) {
            if (c == '0') {
                nBits += 4;
            } else {
                int value = Character.digit(c, 16);
                for (int i = 3; i >= 0; i--) {
                    if ((value & (1 << i)) == 0) {
                        nBits++;
                    } else {
                        return false; // Retorna falso tan pronto como encuentres un bit que no es cero
                    }
                    if (nBits >= nBitsCero) { // Si hemos encontrado suficientes bits cero, retorna verdadero
                        return true;
                    }
                }
            }
        }

        return nBits >= nBitsCero; // Retorna verdadero si hemos encontrado suficientes bits cero
    }
 
    private String incrementarV(String v) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int base = alphabet.length();

        // Incrementar el último carácter de la cadena y manejar el desbordamiento
        for (int i = v.length() - 1; i >= 0; i--) {
            int index = alphabet.indexOf(v.charAt(i));

            // Si el carácter actual no es el último del alfabeto, o es el último pero es el primer carácter y está dentro del rango permitido
            if (index < base - 1 && (i != 0 || (v.charAt(i) >= startChar && v.charAt(i) < endChar))) {
                v = v.substring(0, i) + alphabet.charAt(index + 1) + v.substring(i+1);
                return v;
            } else if (i == 0) { // Si estamos en el primer carácter y necesitamos desbordar
                v = v.substring(0, i) + startChar + v.substring(i+1);
                if (v.length() < 7) { // Si la longitud es menos que 7, agregar un nuevo carácter al final
                    v = v + 'a';
                }
            } else { // Si necesitamos desbordar y no estamos en el primer carácter
                v = v.substring(0, i) + 'a' + v.substring(i+1);
            }
        }

        return v;
    }

    public void run() {
        String v = String.valueOf(startChar);
        long startTime = System.currentTimeMillis();
        boolean continuarBuscando = true; // Variable para controlar el bucle

        while (!Main.found && continuarBuscando) {
            String hash = "";
            try {
                hash = calcularHash(cadenaC + v, algorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            if (verificarHash(hash, nBitsCero)) {
                long endTime = System.currentTimeMillis();
                Main.found = true;
                System.out.println("Valor v: " + v);
                System.out.println("Tiempo de búsqueda: " + (endTime - startTime) + " ms");
                continuarBuscando = false; // Detiene el bucle
            } else {
            	if(Main.nThreads == 1) {
            		v = incrementarV(v);
                    if (v.equals("zzzzzzz"))
                	{
                        continuarBuscando = false;
                        long endTime = System.currentTimeMillis();
                        System.out.println("Thread"+id+" no encontró una solución | Tiempo de búsqueda: "+ (endTime - startTime) + " ms");
                	}
            	}
            	else {
            		v = incrementarV(v);
                    if (v.equals("mzzzzzz") || v.equals("zzzzzzz"))
                	{
                        continuarBuscando = false;
                        long endTime = System.currentTimeMillis();
                        System.out.println("Thread"+id+" no encontró una solución | Tiempo de búsqueda: "+ (endTime - startTime) + " ms");
                	}	
            	}                
            }
        }
    }


}
