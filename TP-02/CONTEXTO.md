# Contexto do Projeto de Gestão de Farmácia

Este documento reúne o enquadramento completo do trabalho prático realizado na disciplina de Programação Orientada a Objetos da Universidade da Beira Interior. O objetivo é documentar a arquitetura, o funcionamento e os principais aspetos técnicos da aplicação desenvolvida, servindo como referência para a equipa e para o relatório final.

---

## Resumo do Problema

O projeto consiste no desenvolvimento de uma aplicação para gerir os serviços de uma farmácia (aplicável a qualquer retalhista semelhante). Devem ser suportadas as operações de:

- Gestão de produtos (marketing, stock, preços);
- Gestão de clientes e moradas;
- Registo e acompanhamento de encomendas;
- Planeamento de entregas e estatísticas básicas;
- Persistência dos dados sem recorrer a bases de dados relacionais ou NoSQL.

O enunciado exige ainda:

- Interface em texto ou gráfica (foi adotado Swing);
- Documentação com JavaDoc;
- Testes unitários com JUnit;
- Relatório descritivo com menos de 10 páginas;
- Utilização de princípios de herança, interfaces, sobrecarga, sobreposição e composição.

---

## Tecnologias Utilizadas

- **Linguagem:** Java 21 (qualquer versão compatível com Swing e JUnit 5).
- **Interface Gráfica:** Swing (javax.swing e java.awt).
- **Persistência:** Serialização com `ObjectOutputStream` e `ObjectInputStream`.
- **Testes:** JUnit 5 (biblioteca `junit-platform-console-standalone-1.10.2.jar`).
- **Ferramentas de build:** Compilação manual via `javac` (não foi usado Maven/Gradle).
- **Documentação:** JavaDoc (geração automática via `javadoc`, resultados em `doc/`).
- **Ambiente:** macOS (darwin 25.0.0), shell `/bin/zsh`.

---

## Estrutura de Diretórios

- `src/`: Código-fonte Java.
  - `app/`: Ponto de entrada da aplicação (`Main`) no pacote `app`.
  - `modelo/`: Camada de domínio (entidades e lógica de negócio básica).
  - `gui/`: Interface gráfica (JFrame e painéis).
  - `persistencia/`: Gestão de leitura/escrita de dados.
  - `test/`: Testes unitários com JUnit.
- `doc/`: JavaDoc gerado.
- `lib/`: Dependências externas (JUnit standalone).
- `farmacia.dat`: Ficheiro binário de persistência (pode ser regenerado).
- `CONTEXTO.md`: Este documento.

---

## Arquitetura e Domínio

### Classes Principais (`modelo/`)

- `Pessoa` (abstrata): Nome e NIF, demonstra herança.
- `Cliente` (extends `Pessoa`): Acrescenta morada (composição com `Morada`), sobrescreve `toString`.
- `Morada`: Rua, cidade e código postal.
- `Vendavel` (interface): Define contrato com `getNome()` e `getPreco()`.
- `Produto` (implements `Vendavel`): ID sequencial, nome, preço, stock; inclui validações e atualização de stock.
- `Encomenda`: Associação entre cliente e mapa `Produto -> quantidade`, estados (`EM_PROCESSAMENTO`, `PAGO`, `ENVIADO`, `ENTREGUE`), sobrecarga de construtores e cálculo de preço total.
- `Farmacia`: Serviço central que gere listas de clientes, produtos e encomendas, com regras de negócio (verificações de stock, reposição, atualização de estado e produtos da encomenda).

### Persistência (`persistencia/GestorDados.java`)

- Serializa (`guardarDados`) e desserializa (`carregarDados`) listas de clientes, produtos e encomendas para o ficheiro `farmacia.dat`.
- Atualiza contadores estáticos de `Produto` e `Encomenda` após carregamento, evitando colisões de IDs.

### Interface Gráfica (`gui/`)

- `FarmaciaGUI`: `JFrame` principal com `JTabbedPane` para clientes, produtos e encomendas; ao fechar, questiona se o utilizador pretende guardar dados.
- `PainelGestaoClientes`: Lista clientes numa `JTable`, permite adicionar, editar e remover (diálogos modais com `GridBagLayout`).
- `PainelGestaoProdutos`: Gestão de produtos com validações (preço/stock não negativos) e atualização dinâmica do stock.
- `PainelGestaoEncomendas`: Gestão completa de encomendas, incluindo:
  - seleção de cliente com pesquisa por NIF;
  - seleção de produtos com pesquisa por nome/ID;
  - validação de stock em tempo real e no registo;
  - atualização de estado;
  - reposição automática de stock quando uma encomenda é removida;
  - sincronização com a tabela de produtos sempre que uma encomenda altera stock.

### Testes (`src/test/FarmaciaTest.java`)

- Testes unitários para:
  - adicionar/remover clientes;
  - impedir clientes duplicados;
  - registar encomendas e deduzir stock;
  - remover encomendas e repor stock;
  - atualizar produtos de uma encomenda (incluindo casos de stock insuficiente).
- Utiliza `@BeforeEach` para preparar dados consistentes.

---

## Detalhamento dos Ficheiros e Diretórios

- `src/app/Main.java`  
  Ponto de entrada. Garante carregamento de dados via `GestorDados`, inicializa `Farmacia` e lança a GUI na Event Dispatch Thread. Necessário para arrancar a aplicação.

- `src/modelo/Pessoa.java`  
  Classe abstrata base para entidades com nome e NIF. Obriga subclasses a definirem `toString()`, demonstrando polimorfismo. Serve como fundação para domínio de pessoas.

- `src/modelo/Cliente.java`  
  Extende `Pessoa`, adicionando morada e serialização. Overscreve `toString()` para exibir dados completos. Necessário para representar clientes da farmácia.

- `src/modelo/Morada.java`  
  Value object serializável com rua, cidade e código postal. Usado por `Cliente` (composição) para armazenar dados de endereço.

- `src/modelo/Vendavel.java`  
  Interface que define `getNome()` e `getPreco()`. Garante que qualquer item vendável implementa esse contrato. Essencial para abstração de itens de encomenda.

- `src/modelo/Produto.java`  
  Implementa `Vendavel`, possui ID sequencial, preço e stock. Expõe operações para gerir stock com validações. Fundamenta o catálogo de produtos.

- `src/modelo/Encomenda.java`  
  Associação entre `Cliente` e um mapa de `Produto` para quantidades. Calcula preço total, controla estado (enum interno) e fornece construtores sobrecarregados. Necessária para registar pedidos.

- `src/modelo/Farmacia.java`  
  Classe de serviço que gere listas de clientes, produtos e encomendas. Implementa regras de negócio (validações, dedução/reposição de stock, atualização de estado). É o núcleo da lógica.

- `src/persistencia/GestorDados.java`  
  Responsável por serializar e desserializar o estado completo (clientes, produtos, encomendas). Atualiza contadores estáticos após carregar. Necessário para persistência sem base de dados.

- `src/gui/FarmaciaGUI.java`  
  `JFrame` principal com `JTabbedPane`. Gere ciclo de vida da aplicação (confirmação ao sair, escolha de guardar dados). Necessário para agrupar os painéis.

- `src/gui/PainelGestaoClientes.java`  
  `JPanel` com tabela, botões e diálogos para CRUD de clientes. Implementa validação de NIF (9 dígitos) e código postal (0000-000), permitindo corrigir o NIF apenas neste contexto. Necessário para gerir dados de clientes.

- `src/gui/PainelGestaoProdutos.java`  
  Painel para gerir produtos, com validações de stock/preço e atualização visual. Comunica com `Farmacia` para refletir alterações. Essencial para controlar inventário.

- `src/gui/PainelGestaoEncomendas.java`  
  Painel complexo para criar, editar e remover encomendas. Inclui pesquisa, controlo de stock, sincronização com painel de produtos, gestão de estados, apresentação apenas informativa do cliente durante a edição, coluna de quantidade total por encomenda e um diálogo de gestão com ações de adicionar, remover e guardar alterações. Necessário para operações de venda.

- `src/test/FarmaciaTest.java`  
  Conjunto de testes JUnit 5. Valida regras críticas de negócio (clientes, encomendas, stock). Necessário para garantir regressões mínimas.

- `lib/junit-platform-console-standalone-1.10.2.jar`  
  Dependência externa única que fornece motor JUnit 5 sem precisar de Maven/Gradle. Necessária para executar os testes via linha de comando.

- `doc/`  
  Saída do JavaDoc. Contém documentação HTML gerada automaticamente para a aplicação. Pode ser regenerada com o comando indicado.

- `farmacia.dat`  
  Ficheiro binário gerado em tempo de execução com o estado atual (clientes, produtos, encomendas). Não deve ser versionado; serve de persistência local.

- `CONTEXTO.md`  
  Documento de arquitetura e funcionamento (este ficheiro). Deve acompanhar o projeto para referência e atualização contínua.

---

## Fluxo de Execução

1. `app.Main.main()` cria instância de `Farmacia`.
2. `GestorDados.carregarDados()` tenta carregar dados persistidos e repõe estado interno.
3. Interface gráfica é inicializada na Event Dispatch Thread (`SwingUtilities.invokeLater`).
4. Utilizador interage com os separadores:
   - Clientes: CRUD simples.
   - Produtos: CRUD com validação de stock e preços.
   - Encomendas: criação (seleciona cliente e produtos com quantidades válidas), edição (ajuste de quantidades/estado), remoção (reposiciona stock).
5. Ao fechar a janela principal, o utilizador pode guardar os dados atuais em `farmacia.dat`.

---

## Considerações de Implementação

- **Validações essenciais:**
  - Preço e stock não podem ser negativos.
  - NIF dos clientes deve conter exatamente 9 dígitos e códigos postais seguem o formato 0000-000.
  - Quantidades nas encomendas devem respeitar o stock disponível.
  - NIF é único na lista de clientes.
  - Atualização de produtos de uma encomenda devolve stock anterior antes de aplicar o novo mapa, garantindo consistência mesmo em exceções.
- **Sincronização de stock:**
  - Ao registar ou editar encomendas, deduz stock dos produtos envolvidos.
  - Remoções devolvem stock automaticamente.
  - Painéis de produtos e encomendas coordenam-se para refletir stock em tempo real.
- **Persistência robusta:**
  - Em caso de ficheiro inexistente, a app inicia vazia.
  - Erros de IO apresentam mensagem no console e a app continua com dados limpos.
- **Interface amigável:**
  - Uso consistente de diálogos modais (`JDialog`).
  - Campos não editáveis onde apropriado (ex.: NIF nos fluxos de encomenda).
  - Mensagens de erro e avisos com `JOptionPane`.

---

## Procedimentos de Execução

Compilação (a partir do diretório do projeto):

```bash
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:src" -d out $(find src -name "*.java" ! -path "*/test/*")
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:out" -d out $(find src/test -name "*.java")
```

Execução da aplicação (GUI):

```bash
java -cp "out" app.Main
```

Execução dos testes unitários:

```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

Gerar JavaDoc (exemplo):

```bash
javadoc -d doc -sourcepath src app modelo gui persistencia
```

---

## Próximos Passos / Melhorias Futuras

- Adicionar dashboard com estatísticas (produtos mais vendidos, clientes com maior volume de compras, faturação por período).
- Implementar filtros e exportação de relatórios (CSV/PDF).
- Introduzir autenticação básica para operadores.
- Migrar para uma solução de build como Maven/Gradle para simplificar dependências.
- Adicionar logs estruturados e testes de integração simulando fluxos completos.
- Evoluir GUI para JavaFX ou outra tecnologia mais moderna, mantendo compatibilidade com Swing enquanto necessário.

---

## Conclusão

A aplicação cumpre os requisitos da segunda entrega: interface gráfica, documentação JavaDoc, testes com JUnit e uso dos conceitos fundamentais de POO (herança, sobrecarga, overriding, composição e interfaces). Os dados persistem entre execuções via serialização, permitindo continuidade de trabalho. Este documento sintetiza toda a informação relevante, servindo como referência de arquitetura, operação e manutenção.

