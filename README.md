# Projeto de Programação Orientada a Objetos - Gestão de Farmácia

Este repositório contém duas implementações de um sistema de gestão de farmácia, desenvolvidas no âmbito da disciplina de Programação Orientada a Objetos (POO) da Universidade da Beira Interior (UBI). As implementações, designadas como `TP-01` e `TP-02`, representam fases distintas do desenvolvimento do projeto, com a `TP-02` sendo uma evolução da `TP-01`, incorporando conceitos mais avançados de POO e engenharia de software.

## Como Obter o Projeto

Para clonar este repositório e ter acesso aos ficheiros do projeto, utilize o seguinte comando:

```bash
git clone https://github.com/fefogaca/TP-POO-25-26
cd TP-POO-25-26
```
*Substitua `<URL_DO_REPOSITÓRIO>` pelo URL real do repositório Git.*

---

## Estrutura do Repositório

*   `Enunciado/`: Contém o enunciado original do projeto (Enunciado.pdf).
*   `TP-01/`: Implementação inicial do sistema de gestão de farmácia (versão de consola).
*   `TP-02/`: Implementação avançada do sistema de gestão de farmácia (versão com interface gráfica e persistência de dados).

---

## Requisitos

Para compilar e executar os projetos `TP-01` e `TP-02`, é necessário ter instalado o seguinte software:

*   **Java Development Kit (JDK) 8 ou superior:** Essencial para compilar e executar aplicações Java. Pode ser descarregado a partir do site oficial da Oracle ou através de gestores de pacotes como o `sdkman` (para Linux/macOS) ou `Chocolatey` (para Windows).
    *   [Download JDK](https://www.oracle.com/java/technologies/downloads/)

Para a execução dos testes unitários da `TP-02`, o projeto já inclui o JAR necessário do JUnit 5 (`junit-platform-console-standalone-1.10.2.jar`) no diretório `TP-02/lib/`.

---

---

## TP-01: Sistema de Gestão de Farmácia (Versão de Consola)

### Descrição Geral

A `TP-01` é uma aplicação de consola que simula um sistema básico de gestão para uma farmácia. Permite a gestão de clientes, produtos e encomendas através de um menu interativo na linha de comandos. O foco principal desta fase foi a implementação dos requisitos funcionais básicos e a compreensão dos conceitos fundamentais de Programação Orientada a Objetos, como classes, objetos, atributos, métodos e encapsulamento.

### Funcionalidades Implementadas

*   **Gestão de Clientes:**
    *   Adicionar novos clientes (Nome, NIF, Morada).
    *   Listar todos os clientes registados.
    *   Eliminar clientes existentes pelo NIF.
    *   Editar informações de clientes (Nome, Morada).
*   **Gestão de Produtos:**
    *   Adicionar novos produtos (Nome, Preço, Stock Inicial).
    *   Listar todos os produtos disponíveis.
    *   Eliminar produtos existentes pelo ID.
    *   Editar informações de produtos (Nome, Preço, Stock).
*   **Gestão de Encomendas:**
    *   Registar novas encomendas, associando-as a um cliente e a uma lista de produtos com quantidades.
    *   Listar todas as encomendas registadas.
    *   Atualizar o estado de uma encomenda (e.g., Pendente, Em Processamento, Entregue).

### Estrutura de Classes

As principais classes que compõem a `TP-01` são:

*   `Main.java`: Contém o método `main` e a lógica para a interação com o utilizador via consola, apresentando os menus e chamando os métodos da classe `Farmacia`.
*   `Farmacia.java`: A classe central que gere as listas de `Cliente`, `Produto` e `Encomenda`. Contém a lógica de negócio para adicionar, remover, listar e editar estes elementos.
*   `Cliente.java`: Representa um cliente da farmácia, com atributos como nome, NIF e morada.
*   `Produto.java`: Representa um produto disponível na farmácia, com atributos como ID, nome, preço e stock.
*   `Morada.java`: Representa a morada de um cliente.
*   `Encomenda.java`: Representa uma encomenda, associando um cliente, uma lista de produtos e o seu estado.

### Compilação e Execução

Para compilar e executar a `TP-01` a partir da linha de comandos:

1.  **Navegar até ao diretório:**
    ```bash
    cd TP-01/src
    ```
2.  **Compilar os ficheiros Java:**
    ```bash
    javac *.java
    ```
3.  **Executar a aplicação:**
    ```bash
    java Main
    ```

---

## TP-02: Sistema de Gestão de Farmácia (Versão com GUI e Persistência)

### Descrição Geral

A `TP-02` é uma evolução significativa da `TP-01`, transformando a aplicação de consola numa aplicação com Interface Gráfica de Utilizador (GUI) utilizando a biblioteca Swing do Java. Além disso, introduz mecanismos de persistência de dados, garantindo que as informações da farmácia sejam guardadas entre as sessões da aplicação. Esta fase demonstra a aplicação de padrões de design, modularidade, tratamento de exceções e testes unitários.

### Funcionalidades e Melhorias

*   **Interface Gráfica de Utilizador (GUI):**
    *   Substituição da interação por consola por uma interface gráfica intuitiva baseada em Swing.
    *   Menus e formulários dedicados para a gestão de clientes, produtos e encomendas.
*   **Persistência de Dados:**
    *   As informações (clientes, produtos, encomendas) são automaticamente guardadas num ficheiro (`farmacia.dat`) ao fechar a aplicação e carregadas ao iniciar.
    *   Utiliza `ObjectOutputStream` e `ObjectInputStream` para serialização de objetos.
*   **Modularidade e Organização:**
    *   O código foi refatorado e organizado em pacotes (`modelo`, `gui`, `persistencia`) para uma melhor separação de responsabilidades.
*   **Tratamento de Exceções:**
    *   A lógica de negócio na classe `Farmacia` utiliza exceções (`IllegalArgumentException`, `IllegalStateException`) para lidar com cenários inválidos (e.g., cliente duplicado, stock insuficiente), em vez de apenas imprimir mensagens na consola.
*   **Novas Classes e Interfaces:**
    *   `Pessoa.java`: Classe abstrata introduzida no pacote `modelo`, servindo como superclasse para `Cliente`, promovendo a reutilização de código.
    *   `Vendavel.java`: Interface que pode ser implementada por classes que representam itens vendáveis.
    *   `GestorDados.java`: Classe dedicada à gestão da persistência dos dados.
*   **Testes Unitários (JUnit 5):**
    *   Inclusão de testes unitários para a lógica de negócio da classe `Farmacia`, garantindo a correção e robustez das operações.

### Estrutura de Classes e Pacotes

*   **`src/`:**
    *   `Main.java`: Ponto de entrada da aplicação. Responsável por carregar os dados persistidos e iniciar a `FarmaciaGUI`.
*   **`src/modelo/`:** Contém as classes que representam o domínio da aplicação.
    *   `Farmacia.java`: Lógica de negócio central, agora mais robusta e utilizando `Optional` para pesquisas.
    *   `Cliente.java`, `Produto.java`, `Morada.java`, `Encomenda.java`: Classes de domínio, possivelmente com pequenas adaptações para serialização e melhorias internas.
    *   `Pessoa.java`: Classe abstrata base para entidades com nome e NIF.
    *   `Vendavel.java`: Interface para itens que podem ser vendidos.
*   **`src/gui/`:** Contém as classes relacionadas com a interface gráfica.
    *   `FarmaciaGUI.java`: A janela principal da aplicação Swing, que organiza os diferentes painéis de gestão.
    *   `PainelGestaoClientes.java`, `PainelGestaoProdutos.java`, `PainelGestaoEncomendas.java`: Painéis específicos para gerir cada tipo de entidade, contendo os componentes visuais e a lógica de interação com a `Farmacia`.
*   **`src/persistencia/`:** Contém a lógica para guardar e carregar os dados.
    *   `GestorDados.java`: Métodos estáticos para serializar e desserializar as listas de clientes, produtos e encomendas.
*   **`src/test/`:**
    *   `FarmaciaTest.java`: Classe de testes unitários para a classe `Farmacia` utilizando JUnit 5.

### Compilação e Execução

1.  **Navegar até ao diretório raiz do projeto (onde este `README.md` se encontra).**
2.  **Compilar os ficheiros Java:**
    ```bash
    javac -d TP-02/out/production/TP-02 -cp TP-02/lib/junit-platform-console-standalone-1.10.2.jar $(find TP-02/src -name "*.java")
    ```
    *Nota: Este comando compila todos os ficheiros `.java` do projeto, incluindo os testes, e adiciona o JAR do JUnit 5 ao classpath.*

3.  **Executar a aplicação GUI:**
    ```bash
    java -cp TP-02/out/production/TP-02 Main
    ```
    A aplicação abrirá uma janela Swing.

### Execução de Testes Unitários (JUnit 5)

1.  **Navegar até ao diretório raiz do projeto (onde este `README.md` se encontra).**
2.  **Certifique-se de que os ficheiros Java foram compilados (ver secção "Compilação e Execução" acima).**
3.  **Executar os testes JUnit:**
    ```bash
    java -jar TP-02/lib/junit-platform-console-standalone-1.10.2.jar -cp TP-02/out/production/TP-02 --scan-classpath
    ```

### Geração de Documentação Javadoc

Para gerar a documentação Javadoc para a `TP-02`:

1.  **Navegar até ao diretório raiz do projeto (onde este `README.md` se encontra).**
2.  **Gerar a documentação Javadoc:**
    ```bash
    javadoc -d TP-02/doc TP-02/src/**/*.java TP-02/src/*.java
    ```
    A documentação será gerada no diretório `TP-02/doc/`. Abra `TP-02/doc/index.html` no seu navegador para visualizá-la.

---

## Diferenças Chave entre TP-01 e TP-02

| Característica          | TP-01 (Versão de Consola)                               | TP-02 (Versão com GUI e Persistência)                                  |
| :---------------------- | :------------------------------------------------------ | :--------------------------------------------------------------------- |
| **Interface de Utilizador** | Consola (CLI)                                           | Gráfica (Swing GUI)                                                    |
| **Persistência de Dados** | Nenhuma (dados perdidos ao fechar a aplicação)          | Sim (serialização para `farmacia.dat` via `GestorDados`)               |
| **Estrutura do Código** | Ficheiros `.java` no diretório `src`                    | Organizado em pacotes (`modelo`, `gui`, `persistencia`)                |
| **Novas Classes/Interfaces** | N/A                                                     | `Pessoa` (abstrata), `Vendavel` (interface), `GestorDados`             |
| **Tratamento de Erros** | Mensagens `System.out.println`                          | Lançamento de exceções (`IllegalArgumentException`, `IllegalStateException`) |
| **Testes**              | Nenhuns                                                 | Testes unitários com JUnit 5 (`FarmaciaTest.java`)                     |
| **Robustez da Lógica**  | Básica, com validações simples                          | Mais robusta, utilizando `Optional` e tratamento de exceções           |
| **Gestão de Encomendas** | Adicionar, listar, atualizar estado                     | Adicionar, listar, atualizar estado, remover, atualizar produtos da encomenda (com reposição de stock) |

---

## Conclusão

A transição da `TP-01` para a `TP-02` ilustra um processo de desenvolvimento iterativo, onde os requisitos iniciais são expandidos e aprimorados com a introdução de uma interface gráfica, persistência de dados e uma arquitetura mais modular e testável. Este projeto serve como um excelente exemplo prático da aplicação de diversos princípios e padrões de Programação Orientada a Objetos.
