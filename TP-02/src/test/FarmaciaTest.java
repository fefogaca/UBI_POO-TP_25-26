import modelo.Cliente;
import modelo.Encomenda;
import modelo.Farmacia;
import modelo.Morada;
import modelo.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FarmaciaTest {

    private Farmacia farmacia;
    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        farmacia = new Farmacia();
        Morada morada = new Morada("Rua da UBI", "Covilhã", "6200-001");
        cliente = new Cliente("João Neves", "290192837", morada);
        farmacia.adicionarCliente(cliente);

        produto1 = new Produto("Paracetamol", 2.50, 100);
        produto2 = new Produto("Ibuprofeno", 3.00, 50);
        farmacia.adicionarProduto(produto1);
        farmacia.adicionarProduto(produto2);
    }

    @Test
    void testAdicionarEObterCliente() {
        assertEquals(1, farmacia.getClientes().size());
        assertEquals("João Neves", farmacia.getClientes().get(0).getNome());
    }

    @Test
    void testAdicionarClienteDuplicadoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            farmacia.adicionarCliente(cliente);
        });
    }

    @Test
    void testRemoverCliente() {
        farmacia.removerCliente("290192837");
        assertEquals(0, farmacia.getClientes().size());
    }

    @Test
    void testRemoverClienteInexistenteLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> {
            farmacia.removerCliente("123456789");
        });
    }

    @Test
    void testRegistarEncomendaEDeduzirStock() {
        Map<Produto, Integer> produtosEncomenda = new HashMap<>();
        produtosEncomenda.put(produto1, 10);
        produtosEncomenda.put(produto2, 5);

        farmacia.registarEncomenda(cliente, produtosEncomenda);

        assertEquals(1, farmacia.getEncomendas().size());
        assertEquals(90, produto1.getStock()); // 100 - 10
        assertEquals(45, produto2.getStock()); // 50 - 5
    }

    @Test
    void testRemoverEncomendaEReporStock() {
        Map<Produto, Integer> produtosEncomenda = new HashMap<>();
        produtosEncomenda.put(produto1, 10);
        farmacia.registarEncomenda(cliente, produtosEncomenda);

        int idEncomenda = farmacia.getEncomendas().get(0).getId();
        farmacia.removerEncomenda(idEncomenda);

        assertEquals(0, farmacia.getEncomendas().size());
        assertEquals(100, produto1.getStock()); // Stock deve ser reposto
    }

    @Test
    void testAtualizarProdutosEncomenda() {
        Map<Produto, Integer> produtosEncomendaOriginal = new HashMap<>();
        produtosEncomendaOriginal.put(produto1, 10);
        farmacia.registarEncomenda(cliente, produtosEncomendaOriginal);

        int idEncomenda = farmacia.getEncomendas().get(0).getId();
        assertEquals(90, produto1.getStock());

        // Novos produtos para a encomenda
        Map<Produto, Integer> produtosEncomendaNova = new HashMap<>();
        produtosEncomendaNova.put(produto2, 15);

        farmacia.atualizarProdutosEncomenda(idEncomenda, produtosEncomendaNova);

        Encomenda encomendaAtualizada = farmacia.acharEncomendaPorId(idEncomenda).get();
        assertEquals(1, encomendaAtualizada.getProdutos().size());
        assertTrue(encomendaAtualizada.getProdutos().containsKey(produto2));
        assertEquals(15, encomendaAtualizada.getProdutos().get(produto2));
        assertEquals(100, produto1.getStock()); // Stock do produto1 deve ser reposto
        assertEquals(35, produto2.getStock()); // Stock do produto2 deve ser deduzido (50 - 15)
        assertEquals(3.00 * 15, encomendaAtualizada.getPrecoTotal()); // Preço total atualizado
    }

    @Test
    void testAtualizarProdutosEncomendaComStockInsuficiente() {
        Map<Produto, Integer> produtosEncomendaOriginal = new HashMap<>();
        produtosEncomendaOriginal.put(produto1, 10);
        farmacia.registarEncomenda(cliente, produtosEncomendaOriginal);

        int idEncomenda = farmacia.getEncomendas().get(0).getId();

        // Tentar adicionar mais stock do que o disponível para produto2
        Map<Produto, Integer> produtosEncomendaNova = new HashMap<>();
        produtosEncomendaNova.put(produto2, 60); // Apenas 50 em stock

        assertThrows(IllegalStateException.class, () -> {
            farmacia.atualizarProdutosEncomenda(idEncomenda, produtosEncomendaNova);
        });

        // Verificar se o stock original foi restaurado após a falha
        assertEquals(100, produto1.getStock()); // Stock original do produto1 deve ser 100 (reposto)
        assertEquals(50, produto2.getStock()); // Stock do produto2 não deve ter sido alterado
    }
}