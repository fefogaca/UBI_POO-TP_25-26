import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Morada;
import modelo.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes unitários para os métodos de estatísticas da classe Farmacia.
 * <p>
 * Testa operações de cálculo de estatísticas como produtos mais/menos vendidos,
 * clientes com maior volume e faturação por diferentes períodos.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
class FarmaciaEstatisticasTest {

    private Farmacia farmacia;
    private Cliente cliente1;
    private Cliente cliente2;
    private Produto produto1;
    private Produto produto2;
    private Produto produto3;

    @BeforeEach
    void setUp() {
        farmacia = new Farmacia();
        
        Morada morada1 = new Morada("Rua da UBI", "Covilhã", "6200-001");
        cliente1 = new Cliente("João Neves", "290192837", morada1);
        farmacia.adicionarCliente(cliente1);
        
        Morada morada2 = new Morada("Rua Principal", "Lisboa", "1000-100");
        cliente2 = new Cliente("Maria Silva", "123456789", morada2);
        farmacia.adicionarCliente(cliente2);

        produto1 = new Produto("Paracetamol", 2.50, 100);
        produto2 = new Produto("Ibuprofeno", 3.00, 50);
        produto3 = new Produto("Aspirina", 1.50, 200);
        farmacia.adicionarProduto(produto1);
        farmacia.adicionarProduto(produto2);
        farmacia.adicionarProduto(produto3);
    }

    @Test
    void testGetProdutosMaisVendidos() {
        // Criar encomendas
        Map<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 20); // Mais vendido
        produtos1.put(produto2, 10);
        farmacia.registarEncomenda(cliente1, produtos1);

        Map<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto1, 15); // Total: 35
        produtos2.put(produto3, 5);
        farmacia.registarEncomenda(cliente2, produtos2);

        List<Map.Entry<Produto, Integer>> maisVendidos = farmacia.getProdutosMaisVendidos(10);
        
        assertEquals(3, maisVendidos.size());
        assertEquals(produto1, maisVendidos.get(0).getKey()); // Deve ser o primeiro (35 unidades)
        assertEquals(35, maisVendidos.get(0).getValue());
    }

    @Test
    void testGetProdutosMenosVendidos() {
        // Criar encomendas
        Map<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 20);
        produtos1.put(produto2, 10);
        farmacia.registarEncomenda(cliente1, produtos1);

        Map<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto3, 5); // Menos vendido
        farmacia.registarEncomenda(cliente2, produtos2);

        List<Map.Entry<Produto, Integer>> menosVendidos = farmacia.getProdutosMenosVendidos(10);
        
        assertEquals(3, menosVendidos.size());
        assertEquals(produto3, menosVendidos.get(0).getKey()); // Deve ser o primeiro (5 unidades)
        assertEquals(5, menosVendidos.get(0).getValue());
    }

    @Test
    void testGetClientesMaiorVolume() {
        // Criar encomendas com valores diferentes
        Map<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 10); // 10 * 2.50 = 25.00
        farmacia.registarEncomenda(cliente1, produtos1);

        Map<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto2, 20); // 20 * 3.00 = 60.00 (maior volume)
        farmacia.registarEncomenda(cliente2, produtos2);

        List<Map.Entry<Cliente, Double>> clientesVolume = farmacia.getClientesMaiorVolume(10);
        
        assertEquals(2, clientesVolume.size());
        assertEquals(cliente2, clientesVolume.get(0).getKey()); // Deve ser o primeiro (60.00€)
        assertEquals(60.00, clientesVolume.get(0).getValue(), 0.01);
    }

    @Test
    void testGetFaturacaoPorDia() {
        // Criar encomenda normalmente (será criada com data atual)
        Map<Produto, Integer> produtos = new HashMap<>();
        produtos.put(produto1, 10);
        farmacia.registarEncomenda(cliente1, produtos);
        
        // Obter a encomenda criada e ajustar a data
        Encomenda encomenda = farmacia.getEncomendas().get(0);
        encomenda.setDataHora(LocalDateTime.of(2024, 12, 25, 10, 0));

        LocalDate data = LocalDate.of(2024, 12, 25);
        double faturacao = farmacia.getFaturacaoPorDia(data);
        
        assertEquals(25.00, faturacao, 0.01); // 10 * 2.50
    }

    @Test
    void testGetFaturacaoPorSemana() {
        // Criar encomendas normalmente
        Map<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 10);
        farmacia.registarEncomenda(cliente1, produtos1);
        
        Map<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto2, 5);
        farmacia.registarEncomenda(cliente2, produtos2);
        
        // Ajustar datas das encomendas criadas
        Encomenda encomenda1 = farmacia.getEncomendas().get(0);
        encomenda1.setDataHora(LocalDateTime.of(2024, 12, 23, 10, 0)); // Segunda-feira
        
        Encomenda encomenda2 = farmacia.getEncomendas().get(1);
        encomenda2.setDataHora(LocalDateTime.of(2024, 12, 25, 10, 0)); // Quarta-feira

        LocalDate data = LocalDate.of(2024, 12, 25); // Qualquer dia da semana
        double faturacao = farmacia.getFaturacaoPorSemana(data);
        
        assertEquals(40.00, faturacao, 0.01); // 25.00 + 15.00
    }

    @Test
    void testGetFaturacaoPorMes() {
        // Criar encomendas normalmente
        Map<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 10);
        farmacia.registarEncomenda(cliente1, produtos1);
        
        Map<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto2, 5);
        farmacia.registarEncomenda(cliente2, produtos2);
        
        // Ajustar datas das encomendas criadas
        Encomenda encomenda1 = farmacia.getEncomendas().get(0);
        encomenda1.setDataHora(LocalDateTime.of(2024, 12, 1, 10, 0));
        
        Encomenda encomenda2 = farmacia.getEncomendas().get(1);
        encomenda2.setDataHora(LocalDateTime.of(2024, 12, 15, 10, 0));

        double faturacao = farmacia.getFaturacaoPorMes(2024, 12);
        
        assertEquals(40.00, faturacao, 0.01); // 25.00 + 15.00
    }

    @Test
    void testEstatisticasSemDados() {
        // Testar quando não há encomendas
        List<Map.Entry<Produto, Integer>> maisVendidos = farmacia.getProdutosMaisVendidos(10);
        assertTrue(maisVendidos.isEmpty());

        List<Map.Entry<Cliente, Double>> clientesVolume = farmacia.getClientesMaiorVolume(10);
        assertTrue(clientesVolume.isEmpty());

        double faturacao = farmacia.getFaturacaoPorDia(LocalDate.now());
        assertEquals(0.0, faturacao, 0.01);
    }
}

