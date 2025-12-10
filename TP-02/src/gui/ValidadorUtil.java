package gui;

/**
 * Classe utilitária para validações comuns na interface gráfica.
 * <p>
 * Centraliza métodos de validação para evitar duplicação de código.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class ValidadorUtil {

    /**
     * Valida se um NIF português é válido usando o algoritmo oficial de validação.
     * <p>
     * O algoritmo verifica se o NIF tem 9 dígitos e se o dígito de controlo (último dígito)
     * corresponde ao cálculo baseado nos 8 primeiros dígitos.
     * </p>
     *
     * @param nif Número de identificação fiscal a validar.
     * @return {@code true} se o NIF for válido segundo o algoritmo oficial, {@code false} caso contrário.
     */
    public static boolean nifValido(String nif) {
        if (nif == null) {
            return false;
        }
        
        final int max = 9;
        
        // Verificar se é numérico e tem 9 números
        if (!nif.matches("[0-9]+") || nif.length() != max) {
            return false;
        }
        
        int checkSum = 0;
        
        // Calcular checkSum
        for (int i = 0; i < max - 1; i++) {
            checkSum += (nif.charAt(i) - '0') * (max - i);
        }
        
        int checkDigit = 11 - (checkSum % 11);
        
        // Se checkDigit é maior que 9, definir como zero
        if (checkDigit > 9) {
            checkDigit = 0;
        }
        
        // Comparar checkDigit com o último número do NIF
        return checkDigit == nif.charAt(max - 1) - '0';
    }

    /**
     * Valida se um código postal está no formato 0000-000.
     *
     * @param codigoPostal Código postal a validar.
     * @return {@code true} se respeitar o formato definido, {@code false} caso contrário.
     */
    public static boolean codigoPostalValido(String codigoPostal) {
        return codigoPostal != null && codigoPostal.matches("\\d{4}-\\d{3}");
    }
}

