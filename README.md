# TP-02: Sistema de Gestão de Farmácia - Versão Completa

Este documento descreve o **TP-02**, uma aplicação completa de gestão de farmácia desenvolvida no âmbito da disciplina de Programação Orientada a Objetos (POO) da Universidade da Beira Interior (UBI). Esta versão incorpora uma interface gráfica moderna, persistência de dados, funcionalidades de estatísticas avançadas, validações robustas e testes unitários.

## 📋 Índice

- [Descrição Geral](#descrição-geral)
- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Compilação e Execução](#compilação-e-execução)
- [Testes Unitários](#testes-unitários)
- [Documentação](#documentação)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)

---

## Descrição Geral

O **TP-02** é uma aplicação desktop completa para gestão de uma farmácia, desenvolvida em Java com interface gráfica Swing. A aplicação permite gerir clientes, produtos e encomendas de forma intuitiva, oferecendo funcionalidades de análise estatística e garantindo a persistência dos dados entre sessões.

### Características Principais

- ✅ **Interface Gráfica Moderna**: Interface Swing intuitiva com tabelas, formulários e diálogos modais
- ✅ **Persistência de Dados**: Dados guardados automaticamente em ficheiro binário (`farmacia.dat`)
- ✅ **Estatísticas Avançadas**: Painel dedicado com análises de vendas, clientes e faturação
- ✅ **Validações Robustas**: Validação de NIF português (algoritmo oficial), códigos postais e integridade referencial
- ✅ **Rastreamento Temporal**: Data/hora em todas as encomendas para análises temporais precisas
- ✅ **Testes Unitários**: Cobertura de testes com JUnit 5
- ✅ **Documentação Completa**: JavaDoc gerado automaticamente
- ✅ **Compatibilidade Multiplataforma**: Testado e funcional em Windows, Linux e macOS

---

## Funcionalidades

### 🧑‍💼 Gestão de Clientes

- **CRUD Completo**: Criar, visualizar, editar e remover clientes
- **Validação de NIF**: Validação rigorosa usando algoritmo oficial português (não apenas formato)
- **Validação de Código Postal**: Formato 0000-000 obrigatório
- **Integridade Referencial**: Não permite remover clientes com encomendas associadas
- **Interface Melhorada**: Tabela com cores alternadas e tooltips informativos

### 📦 Gestão de Produtos

- **CRUD Completo**: Criar, visualizar, editar e remover produtos
- **Gestão de Stock**: Atualização automática e sincronização entre painéis
- **Validações**: Preço e stock não podem ser negativos (exceções em vez de mensagens no console)
- **Integridade Referencial**: Não permite remover produtos presentes em encomendas
- **Sincronização**: Alterações de stock refletidas imediatamente em todos os painéis

### 📋 Gestão de Encomendas

- **Registo Completo**: Criar encomendas associando cliente e produtos com quantidades
- **Pesquisa Inteligente**: Pesquisa de clientes por NIF e produtos por nome/ID
- **Validação em Tempo Real**: Verificação de stock disponível antes de confirmar
- **Edição Completa**: 
  - Atualizar estado da encomenda (Em processamento, Pago, Enviado, Entregue)
  - **Editar data/hora** para análises temporais precisas
  - Gerir produtos da encomenda (adicionar, remover, ajustar quantidades)
- **Rastreamento Temporal**: Coluna de data/hora na tabela principal (formato dd/MM/yyyy HH:mm)
- **Reposição Automática**: Stock reposto automaticamente ao remover encomendas

### 📊 Estatísticas e Análises

Painel dedicado com 4 abas organizadas:

1. **Produtos Mais Vendidos**: Lista ordenada por quantidade vendida (maior → menor)
2. **Produtos Menos Vendidos**: Lista ordenada por quantidade vendida (menor → maior)
3. **Clientes Maior Volume**: Lista ordenada por valor total gasto (maior → menor)
4. **Faturação**: Calculadora para faturação por:
   - Dia específico
   - Semana (segunda a domingo)
   - Mês específico
   - Período customizado

**Características do Painel de Estatísticas**:
- Interface melhorada com separadores visuais
- Cores nos resultados para melhor legibilidade
- Tooltips informativos
- Mensagens quando não há dados disponíveis
- Botão para atualizar estatísticas manualmente

### 💾 Persistência e Validações

- **Persistência Automática**: Dados guardados em `farmacia.dat` via serialização
- **Carregamento Automático**: Dados carregados ao iniciar a aplicação
- **Compatibilidade**: Suporta dados antigos (define data/hora em encomendas sem esse campo)
- **Validações Centralizadas**: Classe `ValidadorUtil` para evitar duplicação de código
- **Diálogo de Confirmação**: Pergunta ao utilizador se deseja guardar ao encerrar

---

## Requisitos

### Software Necessário

- **Java Development Kit (JDK) 17 ou superior** (recomendado Java 21)
  - [Download JDK Oracle](https://www.oracle.com/java/technologies/downloads/)
  - [OpenJDK](https://openjdk.org/)

### Dependências

- **JUnit 5**: Incluído no projeto (`lib/junit-platform-console-standalone-1.10.2.jar`)
- **Java Swing**: Incluído no JDK (não requer instalação adicional)

### Sistemas Operativos Suportados

✅ **Windows** 10/11  
✅ **Linux** (Ubuntu, Debian, RHEL, etc.)  
✅ **macOS** 10.15+

---

## Estrutura do Projeto

```
TP-02/
├── src/
│   ├── app/                    # Ponto de entrada
│   │   ├── Main.java          # Classe principal que inicia a aplicação
│   │   └── package-info.java
│   │
│   ├── modelo/                 # Camada de domínio
│   │   ├── Pessoa.java         # Classe abstrata (herança)
│   │   ├── Cliente.java       # Extende Pessoa, implementa equals/hashCode
│   │   ├── Morada.java        # Value object (composição)
│   │   ├── Vendavel.java      # Interface
│   │   ├── Produto.java       # Implementa Vendavel
│   │   ├── Encomenda.java     # Inclui LocalDateTime para rastreamento
│   │   └── Farmacia.java      # Lógica de negócio e estatísticas
│   │
│   ├── gui/                    # Interface gráfica
│   │   ├── FarmaciaGUI.java           # Janela principal com JTabbedPane
│   │   ├── PainelGestaoClientes.java  # CRUD de clientes
│   │   ├── PainelGestaoProdutos.java  # CRUD de produtos
│   │   ├── PainelGestaoEncomendas.java # CRUD de encomendas
│   │   ├── PainelEstatisticas.java   # Painel de estatísticas (4 abas)
│   │   └── ValidadorUtil.java        # Validações centralizadas
│   │
│   ├── persistencia/           # Persistência de dados
│   │   └── GestorDados.java  # Serialização/desserialização
│   │
│   └── test/                   # Testes unitários
│       ├── FarmaciaTest.java  # Testes da lógica de negócio
│       └── package-info.java
│
├── lib/                        # Dependências externas
│   └── junit-platform-console-standalone-1.10.2.jar
│
├── out/                        # Classes compiladas (gerado automaticamente)
├── doc/                        # JavaDoc gerado (gerado automaticamente)
├── farmacia.dat                # Ficheiro de dados (gerado automaticamente)
├── CONTEXTO.md                 # Documentação técnica detalhada
├── diagrama.puml              # Diagrama UML (PlantUML)
├── diagrama.png               # Diagrama UML renderizado
└── README.md                   # Este ficheiro
```

### Classes Principais

#### Modelo de Domínio (`src/modelo/`)

- **`Pessoa`** (abstrata): Classe base com nome e NIF. Demonstra herança e obriga subclasses a implementar `toString()`.
- **`Cliente`**: Extende `Pessoa`, adiciona `Morada` (composição). Implementa `equals()` e `hashCode()` baseados no NIF.
- **`Produto`**: Implementa `Vendavel`, gere ID sequencial, preço e stock. Validações robustas com exceções.
- **`Encomenda`**: Associa cliente e produtos, controla estados via enum, inclui `LocalDateTime` para rastreamento temporal.
- **`Farmacia`**: Núcleo da lógica de negócio, implementa validações de integridade e métodos de estatísticas.

#### Interface Gráfica (`src/gui/`)

- **`FarmaciaGUI`**: Janela principal com `JTabbedPane` contendo 4 separadores.
- **`PainelGestaoClientes`**: Gestão de clientes com validação de NIF e código postal.
- **`PainelGestaoProdutos`**: Gestão de produtos com sincronização de stock.
- **`PainelGestaoEncomendas`**: Gestão completa de encomendas com edição de data/hora.
- **`PainelEstatisticas`**: Painel dedicado com 4 abas para análises.
- **`ValidadorUtil`**: Métodos estáticos para validação de NIF português e código postal.

---

## Compilação e Execução

### Pré-requisitos

Certifique-se de que o JDK está instalado e configurado:

```bash
java -version
javac -version
```

### Compilação

#### Windows (CMD ou PowerShell)

**Compilar código de produção:**
```cmd
cd TP-02
javac -cp "lib\junit-platform-console-standalone-1.10.2.jar;src" -d out src\app\*.java src\modelo\*.java src\gui\*.java src\persistencia\*.java
```

**Compilar testes:**
```cmd
javac -cp "lib\junit-platform-console-standalone-1.10.2.jar;out" -d out src\test\*.java
```

**Alternativa (PowerShell com find):**
```powershell
javac -cp "lib\junit-platform-console-standalone-1.10.2.jar;src" -d out (Get-ChildItem -Path src -Recurse -Filter "*.java" | Where-Object { $_.FullName -notmatch "\\test\\" }).FullName
javac -cp "lib\junit-platform-console-standalone-1.10.2.jar;out" -d out (Get-ChildItem -Path src\test -Recurse -Filter "*.java").FullName
```

#### Linux/macOS

**Compilar código de produção:**
```bash
cd TP-02
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:src" -d out $(find src -name "*.java" ! -path "*/test/*")
```

**Compilar testes:**
```bash
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:out" -d out $(find src/test -name "*.java")
```

**Alternativa (sem find):**
```bash
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:src" -d out src/app/*.java src/modelo/*.java src/gui/*.java src/persistencia/*.java
javac -cp "lib/junit-platform-console-standalone-1.10.2.jar:out" -d out src/test/*.java
```

### Execução da Aplicação

#### Windows
```cmd
cd TP-02
java -cp "out" app.Main
```

#### Linux/macOS
```bash
cd TP-02
java -cp "out" app.Main
```

A aplicação abrirá uma janela Swing com a interface gráfica.

---

## Testes Unitários

### Execução dos Testes

#### Windows
```cmd
cd TP-02
java -jar lib\junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

#### Linux/macOS
```bash
cd TP-02
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

### Cobertura de Testes

Os testes unitários cobrem:

- ✅ Adição e remoção de clientes
- ✅ Prevenção de clientes duplicados (mesmo NIF)
- ✅ Registo de encomendas e dedução de stock
- ✅ Remoção de encomendas e reposição de stock
- ✅ Atualização de produtos de encomendas
- ✅ Validação de stock insuficiente

---

## Documentação

### Geração de JavaDoc

#### Windows
```cmd
cd TP-02
javadoc -d doc -sourcepath src -subpackages app modelo gui persistencia
```

#### Linux/macOS
```bash
cd TP-02
javadoc -d doc -sourcepath src -subpackages app modelo gui persistencia
```

A documentação será gerada em `TP-02/doc/`. Abra `doc/index.html` no navegador para visualizar.

### Documentação Adicional

- **`CONTEXTO.md`**: Documentação técnica detalhada do projeto, incluindo arquitetura, decisões de design e considerações de implementação.
- **`diagrama.puml`**: Diagrama UML do projeto em formato PlantUML.
- **`diagrama.png`**: Diagrama UML renderizado para visualização.

---

## Tecnologias Utilizadas

### Linguagem e Bibliotecas

- **Java 21**: Linguagem de programação principal
- **Java Swing**: Biblioteca para interface gráfica
- **JUnit 5**: Framework de testes unitários
- **Java Time API**: Para rastreamento temporal (`LocalDateTime`, `LocalDate`)

### Conceitos de POO Demonstrados

- ✅ **Herança**: `Pessoa` (abstrata) → `Cliente`
- ✅ **Classes Abstratas**: `Pessoa` com método abstrato `toString()`
- ✅ **Interfaces**: `Vendavel` implementada por `Produto`
- ✅ **Overloading**: Construtores sobrecarregados em `Encomenda`
- ✅ **Overriding**: `toString()` em `Cliente`, métodos de `Vendavel` em `Produto`
- ✅ **Composição**: `Cliente` possui `Morada`, `Encomenda` possui `Cliente` e produtos
- ✅ **Polimorfismo**: Uso de `Vendavel` e `Pessoa` em contextos polimórficos
- ✅ **Encapsulamento**: Atributos privados com acesso controlado via métodos públicos

### Padrões e Práticas

- **Arquitetura em Camadas**: Separação clara entre domínio, vista e persistência
- **Programação Orientada a Eventos**: Listeners para interações na GUI
- **Validações Centralizadas**: Classe utilitária para evitar duplicação
- **Tratamento de Exceções**: Uso de exceções em vez de mensagens no console
- **Testes Automatizados**: Validação contínua da lógica de negócio

---

## Melhorias Implementadas

### Validações e Integridade

- ✅ Validação de NIF português usando algoritmo oficial (não apenas formato)
- ✅ Validações de integridade referencial (impede remoção de entidades com dependências)
- ✅ Tratamento de erros robusto (exceções em vez de `System.out.println`)

### Interface Visual

- ✅ Tamanho de janela otimizado (1000x700) com tamanho mínimo (800x600)
- ✅ Tabelas com cores alternadas para melhor legibilidade
- ✅ Tooltips informativos em botões e campos
- ✅ Botões "Cancelar" em diálogos de edição
- ✅ Layout melhorado no painel de estatísticas

### Funcionalidades de Estatísticas

- ✅ Painel dedicado com 4 abas organizadas
- ✅ Cálculos de faturação por dia, semana e mês
- ✅ Produtos mais/menos vendidos
- ✅ Clientes com maior volume de compras
- ✅ Mensagens informativas quando não há dados

### Rastreamento Temporal

- ✅ Campo `dataHora` em todas as encomendas
- ✅ Edição de data/hora nas encomendas
- ✅ Exibição de data/hora na tabela de encomendas
- ✅ Compatibilidade com dados antigos

---

## Autores

- **Afonso Esteves** (55047)
- **Felipe Dourado** (53394)
- **Tiago Pereira** (55019)

---

## Licença

Este projeto foi desenvolvido no âmbito académico da disciplina de Programação Orientada a Objetos da Universidade da Beira Interior.

---

## Conclusão

O **TP-02** representa uma aplicação completa e robusta de gestão de farmácia, demonstrando a aplicação prática de conceitos avançados de Programação Orientada a Objetos, desenvolvimento de interfaces gráficas, persistência de dados e testes unitários. A aplicação está totalmente funcional, testada e pronta para uso em ambiente de produção.

Para mais detalhes técnicos, consulte o ficheiro [`CONTEXTO.md`](CONTEXTO.md).
