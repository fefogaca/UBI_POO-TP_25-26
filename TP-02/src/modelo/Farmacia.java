package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe principal da lógica de negócios da Farmácia.
 * Gere as listas de clientes, produtos e encomendas.
 */
public class Farmacia {
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private List<Encomenda> encomendas;

    public Farmacia() {
        this.produtos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.encomendas = new ArrayList<>();
    }

    // Métodos para Clientes
    public void adicionarCliente(Cliente cliente) {
        if (acharClienteNif(cliente.getNif()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cliente com o NIF " + cliente.getNif());
        }
        this.clientes.add(cliente);
    }

    public Optional<Cliente> acharClienteNif(String nif) {
        return clientes.stream().filter(c -> c.getNif().equals(nif)).findFirst();
    }

    public void removerCliente(String nif) {
        Cliente cliente = acharClienteNif(nif)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com NIF " + nif + " não encontrado."));
        clientes.remove(cliente);
    }

    // Métodos para Produtos
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
    }

    public Optional<Produto> acharProdutoPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst();
    }

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

    public Optional<Encomenda> acharEncomendaPorId(int id) {
        return encomendas.stream().filter(e -> e.getId() == id).findFirst();
    }

    public void atualizarEstadoEncomenda(int id, Encomenda.EstadoEncomenda novoEstado) {
        Encomenda encomenda = acharEncomendaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Encomenda com ID " + id + " não encontrada."));
        encomenda.setEstado(novoEstado);
    }

    // Getters para as listas (necessário para a GUI e persistência)
    public List<Cliente> getClientes() { return new ArrayList<>(clientes); }
    public List<Produto> getProdutos() { return new ArrayList<>(produtos); }
    public List<Encomenda> getEncomendas() { return new ArrayList<>(encomendas); }

    // Setters para carregar os dados
    public void setClientes(List<Cliente> clientes) { this.clientes = new ArrayList<>(clientes); }
    public void setProdutos(List<Produto> produtos) { this.produtos = new ArrayList<>(produtos); }
    public void setEncomendas(List<Encomenda> encomendas) { this.encomendas = new ArrayList<>(encomendas); }
}