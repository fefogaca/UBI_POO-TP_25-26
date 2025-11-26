package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe principal da lógica de negócios da Farmácia.
 * <p>
 * Gere as listas de clientes, produtos e encomendas, implementando as regras de negócio
 * como validação de stock, dedução/reposição automática de stock e gestão de estados de encomendas.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class Farmacia {
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private List<Encomenda> encomendas;

    /**
     * Construtor padrão que inicializa as listas vazias de produtos, clientes e encomendas.
     */
    public Farmacia() {
        this.produtos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.encomendas = new ArrayList<>();
    }

    /**
     * Adiciona um novo cliente à farmácia.
     * Valida que não existe já um cliente com o mesmo NIF.
     *
     * @param cliente O cliente a adicionar.
     * @throws IllegalArgumentException Se já existir um cliente com o mesmo NIF.
     */
    public void adicionarCliente(Cliente cliente) {
        if (acharClienteNif(cliente.getNif()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente com o NIF " + cliente.getNif());
        }
        this.clientes.add(cliente);
    }

    /**
     * Procura um cliente pelo seu NIF.
     *
     * @param nif O NIF do cliente a procurar.
     * @return Um Optional contendo o cliente se encontrado, ou vazio caso contrário.
     */
    public Optional<Cliente> acharClienteNif(String nif) {
        return clientes.stream().filter(c -> c.getNif().equals(nif)).findFirst();
    }

    /**
     * Remove um cliente da farmácia pelo seu NIF.
     *
     * @param nif O NIF do cliente a remover.
     * @throws IllegalArgumentException Se o cliente não for encontrado.
     */
    public void removerCliente(String nif) {
        Cliente cliente = acharClienteNif(nif)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com NIF " + nif + " não encontrado."));
        clientes.remove(cliente);
    }

    /**
     * Adiciona um novo produto à farmácia.
     *
     * @param produto O produto a adicionar.
     */
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
    }

    /**
     * Procura um produto pelo seu ID.
     *
     * @param id O ID do produto a procurar.
     * @return Um Optional contendo o produto se encontrado, ou vazio caso contrário.
     */
    public Optional<Produto> acharProdutoPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst();
    }

    /**
     * Remove um produto da farmácia pelo seu ID.
     *
     * @param id O ID do produto a remover.
     * @throws IllegalArgumentException Se o produto não for encontrado.
     */
    public void removerProduto(int id) {
        Produto produto = acharProdutoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto com ID " + id + " não encontrado."));
        produtos.remove(produto);
    }

    // Métodos para Encomendas
    /**
     * Regista uma nova encomenda na farmácia.
     * Deduz o stock dos produtos envolvidos.
     * @param cliente O cliente que fez a encomenda.
     * @param produtos O mapa de produtos e quantidades da encomenda.
     */
    public void registarEncomenda(Cliente cliente, Map<Produto, Integer> produtos) {
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto p = entry.getKey();
            int quantidadePedida = entry.getValue();
            if (p.getStock() < quantidadePedida) {
                throw new IllegalStateException("Stock insuficiente para o produto: " + p.getNome());
            }
            p.removerStock(quantidadePedida);
        }
        Encomenda novaEncomenda = new Encomenda(cliente, produtos);
        this.encomendas.add(novaEncomenda);
    }

    /**
     * Remove uma encomenda existente e repõe o stock dos produtos.
     * @param id O ID da encomenda a remover.
     */
    public void removerEncomenda(int id) {
        Encomenda encomenda = acharEncomendaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda com ID " + id + " não encontrada."));
        encomendas.remove(encomenda);
        // Devolver stock dos produtos da encomenda
        for (Map.Entry<Produto, Integer> entry : encomenda.getProdutos().entrySet()) {
            entry.getKey().adicionarStock(entry.getValue());
        }
    }

    /**
     * Atualiza os produtos de uma encomenda existente, ajustando o stock e o preço total.
     * @param idEncomenda O ID da encomenda a atualizar.
     * @param novosProdutos O novo mapa de produtos e quantidades para a encomenda.
     */
    public void atualizarProdutosEncomenda(int idEncomenda, Map<Produto, Integer> novosProdutos) {
        Encomenda encomenda = acharEncomendaPorId(idEncomenda)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda com ID " + idEncomenda + " não encontrada."));

        // 1. Devolver stock dos produtos antigos
        for (Map.Entry<Produto, Integer> entry : encomenda.getProdutos().entrySet()) {
            entry.getKey().adicionarStock(entry.getValue());
        }

        // 2. Verificar e deduzir stock dos novos produtos
        for (Map.Entry<Produto, Integer> entry : novosProdutos.entrySet()) {
            Produto p = entry.getKey();
            int quantidadePedida = entry.getValue();
            if (p.getStock() < quantidadePedida) {
                throw new IllegalStateException("Stock insuficiente para o produto: " + p.getNome());
            }
            p.removerStock(quantidadePedida);
        }

        // 3. Atualizar produtos e preço total da encomenda
        encomenda.getProdutos().clear(); // Limpa os produtos antigos
        encomenda.getProdutos().putAll(novosProdutos); // Adiciona os novos produtos

        double novoPrecoTotal = 0;
        for (Map.Entry<Produto, Integer> entry : novosProdutos.entrySet()) {
            novoPrecoTotal += entry.getKey().getPreco() * entry.getValue();
        }
        encomenda.setPrecoTotal(novoPrecoTotal);
    }

    /**
     * Procura uma encomenda pelo seu ID.
     *
     * @param id O ID da encomenda a procurar.
     * @return Um Optional contendo a encomenda se encontrada, ou vazio caso contrário.
     */
    public Optional<Encomenda> acharEncomendaPorId(int id) {
        return encomendas.stream().filter(e -> e.getId() == id).findFirst();
    }

    /**
     * Atualiza o estado de uma encomenda existente.
     *
     * @param id O ID da encomenda a atualizar.
     * @param novoEstado O novo estado da encomenda.
     * @throws IllegalArgumentException Se a encomenda não for encontrada.
     */
    public void atualizarEstadoEncomenda(int id, Encomenda.EstadoEncomenda novoEstado) {
        Encomenda encomenda = acharEncomendaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda com ID " + id + " não encontrada."));
        encomenda.setEstado(novoEstado);
    }

    /**
     * Obtém uma cópia da lista de clientes.
     *
     * @return Uma nova lista contendo todos os clientes.
     */
    public List<Cliente> getClientes() { return new ArrayList<>(clientes); }

    /**
     * Obtém uma cópia da lista de produtos.
     *
     * @return Uma nova lista contendo todos os produtos.
     */
    public List<Produto> getProdutos() { return new ArrayList<>(produtos); }

    /**
     * Obtém uma cópia da lista de encomendas.
     *
     * @return Uma nova lista contendo todas as encomendas.
     */
    public List<Encomenda> getEncomendas() { return new ArrayList<>(encomendas); }

    /**
     * Define a lista de clientes (usado ao carregar dados persistidos).
     *
     * @param clientes A nova lista de clientes.
     */
    public void setClientes(List<Cliente> clientes) { this.clientes = new ArrayList<>(clientes); }

    /**
     * Define a lista de produtos (usado ao carregar dados persistidos).
     *
     * @param produtos A nova lista de produtos.
     */
    public void setProdutos(List<Produto> produtos) { this.produtos = new ArrayList<>(produtos); }

    /**
     * Define a lista de encomendas (usado ao carregar dados persistidos).
     *
     * @param encomendas A nova lista de encomendas.
     */
    public void setEncomendas(List<Encomenda> encomendas) { this.encomendas = new ArrayList<>(encomendas); }
}