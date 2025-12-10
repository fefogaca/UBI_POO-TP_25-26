import gui.ValidadorUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes unitários para a classe ValidadorUtil.
 * <p>
 * Testa a validação de NIF português usando o algoritmo oficial de validação.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
class ValidadorUtilTest {

    @Test
    void testNifValido() {
        // NIFs válidos (exemplos reais com dígito de controlo correto)
        assertTrue(ValidadorUtil.nifValido("123456789")); // NIF válido exemplo
        assertTrue(ValidadorUtil.nifValido("500000000")); // NIF válido exemplo
    }

    @Test
    void testNifInvalido() {
        // NIFs inválidos
        assertFalse(ValidadorUtil.nifValido("123456788")); // Dígito de controlo incorreto
        assertFalse(ValidadorUtil.nifValido("12345678")); // Menos de 9 dígitos
        assertFalse(ValidadorUtil.nifValido("1234567890")); // Mais de 9 dígitos
        assertFalse(ValidadorUtil.nifValido("12345678a")); // Contém letras
        assertFalse(ValidadorUtil.nifValido("")); // String vazia
        assertFalse(ValidadorUtil.nifValido(null)); // Null
    }

    @Test
    void testCodigoPostalValido() {
        assertTrue(ValidadorUtil.codigoPostalValido("6200-001"));
        assertTrue(ValidadorUtil.codigoPostalValido("1000-100"));
        assertTrue(ValidadorUtil.codigoPostalValido("0000-000"));
    }

    @Test
    void testCodigoPostalInvalido() {
        assertFalse(ValidadorUtil.codigoPostalValido("6200-00")); // Formato incorreto
        assertFalse(ValidadorUtil.codigoPostalValido("6200001")); // Sem hífen
        assertFalse(ValidadorUtil.codigoPostalValido("620-001")); // Formato incorreto
        assertFalse(ValidadorUtil.codigoPostalValido("")); // String vazia
        assertFalse(ValidadorUtil.codigoPostalValido(null)); // Null
    }
}

