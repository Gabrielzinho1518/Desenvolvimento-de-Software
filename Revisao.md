# Guia de Revisão para a Prova — Desenvolvimento de Sistemas

## A IDEIA PRINCIPAL

**Banco de Dados → Conexão → Beans → DAO → Formulários → Menu**

Essa é a ordem que você deve lembrar quando receber um exercício de cadastro, consulta, atualização, exclusão e relatórios.

---

# 1. Entenda o que o professor está pedindo

Antes de começar a programar, identifique os requisitos do enunciado.

| Se o professor pedir... | O que você precisa criar |
|------------------------|--------------------------|
| Cadastrar uma entidade | Tabela + Bean + DAO + formulário |
| Consultar pelo ID | Método `get...()` no DAO |
| Atualizar dados | Método `editar()` no DAO |
| Excluir registros | Método `excluir()` no DAO |
| Mostrar todos os registros | `JTable` + método `listar()` |
| Pesquisar pelo nome | SQL com `LIKE` |
| Relacionar duas tabelas | Chave estrangeira + objeto no Bean |
| Escolher um registro relacionado | `JComboBox` carregado do banco |
| Abrir várias telas | `JMenuBar` no `MenuPrincipal` |

### Macete

Para cada entidade principal, pense em três partes:

- `Bean`
- `DAO`
- `Formulário`

---

# 2. Organização do Projeto no NetBeans

Estrutura utilizada no exercício:

```text
SistemaProdutos
│
├── beans
│   ├── Categoria.java
│   └── Produto.java
│
├── DAO
│   ├── CategoriaDAO.java
│   └── ProdutoDAO.java
│
├── conexao_db
│   └── Conexao.java
│
├── view
│   ├── FormCategorias.java
│   ├── FormProduto.java
│   ├── RelatorioProdutos.java
│   └── MenuPrincipal.java
│
└── principal
    └── Principal.java
```

## Para que serve cada pacote?

1. **beans**: representa os dados. Contém atributos, construtores (quando necessários), getters e setters.
2. **DAO**: contém os comandos SQL e a comunicação com o MySQL.
3. **conexao_db**: estabelece a conexão com o banco.
4. **view**: contém as telas feitas no Design do NetBeans.
5. **principal**: pode conter o início da aplicação, embora você também possa configurar o `MenuPrincipal` como Main Class.

---

# 3. Comece pelo Banco de Dados

Primeiro, transforme o enunciado em tabelas.

## Exemplo

```sql
CREATE DATABASE LojaOnline;

USE ecommerce_db;

CREATE TABLE Categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE Produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES Categorias(id)
);
```

## Comandos SQL Essenciais

| Operação | SQL |
|-----------|------|
| Cadastrar | `INSERT INTO ... VALUES (...)` |
| Consultar | `SELECT * FROM ... WHERE id = ?` |
| Atualizar | `UPDATE ... SET ... WHERE id = ?` |
| Excluir | `DELETE FROM ... WHERE id = ?` |
| Listar | `SELECT * FROM ...` |
| Pesquisar | `SELECT * FROM ... WHERE nome LIKE ?` |

### Atenção ao `WHERE`

Nos comandos `UPDATE` e `DELETE`, ele impede que todos os registros da tabela sejam alterados ou removidos.

## Relacionamentos

Uma categoria pode ter vários produtos.

Cada produto pertence a uma categoria.

O campo `categoria_id` fica na tabela `Produtos`, pois é a chave estrangeira.

---

# 4. Criar a Conexão com o MySQL

No pacote `conexao_db`, crie o arquivo `Conexao.java`.

A ideia é centralizar a conexão para que todos os DAOs a utilizem.

```java
public Connection getConexao() {
    try {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/LojaOnline",
            "root",
            "SUA_SENHA"
        );
    } catch (SQLException ex) {
        System.out.println("Erro: " + ex.getMessage());
        return null;
    }
}
```

## Imports

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
```

### Lembre-se

Adicione o **MySQL Connector/J** nas bibliotecas do projeto.

Se o banco mudar na prova, altere apenas o nome do banco na URL.

---

# 5. Criar os Beans

O Bean representa um registro da tabela, mas não executa SQL.

## Exemplo: `Produto.java`

```java
private int id;
private String nome;
private BigDecimal preco;
private int quantidade;
private Categoria categoria;
```

Depois, gere os getters e setters pelo NetBeans.

## Mapeamento de Tipos

| MySQL | Java |
|---------|---------|
| `INT` | `int` |
| `VARCHAR` | `String` |
| `DECIMAL(10,2)` | `BigDecimal` |
| `DATE` | `LocalDate` ou equivalente |
| Chave estrangeira | Objeto relacionado |

Exemplo:

```java
private Categoria categoria;
```

Isso permite:

```java
produto.getCategoria().getNome();
produto.getCategoria().getId();
```

## Método `toString()`

Em `Categoria.java`:

```java
@Override
public String toString() {
    return id + " - " + nome;
}
```

Isso define como o objeto aparecerá no `JComboBox`.

---

# 6. Criar os DAOs (Parte Mais Importante)

O DAO recebe dados da tela, executa SQL e devolve resultados.

## Estrutura Básica

```java
private Conexao conexao;
private Connection conn;

public ProdutoDAO() {
    this.conexao = new Conexao();
    this.conn = this.conexao.getConexao();
}
```

## Métodos JDBC que Você Deve Memorizar

| Método | Função |
|----------|---------|
| `prepareStatement(sql)` | Prepara SQL com parâmetros |
| `setString()`, `setInt()`, `setBigDecimal()` | Preenchem os `?` |
| `executeQuery()` | Executa SELECT |
| `executeUpdate()` | Executa INSERT, UPDATE e DELETE |

Seu professor também utiliza:

```java
stmt.execute();
```

É válido para INSERT, UPDATE e DELETE.

---

## Exemplo de INSERT

```java
String sql = "INSERT INTO Produtos "
           + "(nome, preco, quantidade, categoria_id) "
           + "VALUES (?, ?, ?, ?)";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setString(1, produto.getNome());
stmt.setBigDecimal(2, produto.getPreco());
stmt.setInt(3, produto.getQuantidade());
stmt.setInt(4, produto.getCategoria().getId());

stmt.execute();
stmt.close();
```

### Regra dos `?`

Cada parâmetro corresponde à posição de um valor no SQL, começando em **1**.

---

## Exemplo de SELECT por ID

```java
String sql = "SELECT * FROM Produtos WHERE id = ?";

PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setInt(1, id);

ResultSet rs = stmt.executeQuery();

if (rs.next()) {
    // Criar o Bean e preencher seus atributos
}
```

### Lembre-se

- `if(rs.next())` → quando espera apenas um registro.
- `while(rs.next())` → quando vai percorrer vários registros.

---

## Exemplo de UPDATE

```java
String sql = "UPDATE Produtos SET nome = ? WHERE id = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setString(1, produto.getNome());
stmt.setInt(2, produto.getId());

stmt.execute();
stmt.close();
```

---

## Exemplo de DELETE

```java
String sql = "DELETE FROM Produtos WHERE id = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setInt(1, id);

stmt.execute();
stmt.close();
```

⚠️ Não basta preparar o SQL.

É obrigatório executá-lo com:

```java
stmt.execute();
```

---

# 7. Trabalhando com Duas Tabelas Sem INNER JOIN

Primeiro buscamos o produto:

```java
Produto produto = new Produto();

produto.setId(rs.getInt("id"));
produto.setNome(rs.getString("nome"));
produto.setPreco(rs.getBigDecimal("preco"));
produto.setQuantidade(rs.getInt("quantidade"));
```

Depois buscamos a categoria:

```java
CategoriaDAO cDAO = new CategoriaDAO();

Categoria categoria =
    cDAO.getCategoria(rs.getInt("categoria_id"));

produto.setCategoria(categoria);
```

### Ideia

Cada DAO cuida apenas da própria tabela.

O `ProdutoDAO` utiliza o `CategoriaDAO` quando precisa recuperar a categoria.

---

# 8. Criar as Telas no Design do NetBeans

## Componentes Principais

| Componente | Uso |
|------------|------|
| `JFrame Form` | Janela do sistema |
| `JLabel` | Texto |
| `JTextField` | Digitação |
| `JButton` | Executar ação |
| `JComboBox` | Seleção |
| `JTable` | Mostrar registros |
| `JMenuBar` | Menu principal |
| `JOptionPane` | Mensagens |

## Padrão de Nomes

| Elemento | Exemplo |
|-----------|----------|
| Campo | `txtNome` |
| Botão | `btnCadastrar` |
| Tabela | `tblProdutos` |
| ComboBox | `cmbCategorias` |
| Label | `lblNome` |

---

## Botão Cadastrar

### Passos

1. Criar o Bean.
2. Ler os campos.
3. Converter os tipos.
4. Validar.
5. Chamar o DAO.
6. Informar resultado.
7. Limpar campos.

### Exemplo

```java
Produto produto = new Produto();

produto.setNome(txtNome.getText());

produto.setPreco(
    new BigDecimal(
        txtPreco.getText().replace(",", ".")
    )
);

produto.setQuantidade(
    Integer.parseInt(txtQuantidade.getText())
);

produto.setCategoria(
    (Categoria) cmbCategorias.getSelectedItem()
);

ProdutoDAO dao = new ProdutoDAO();

dao.inserir(produto);
```

---

## Botão Consultar

1. Ler ID.
2. Chamar `dao.getProduto(id)`.
3. Verificar se existe.
4. Preencher campos.
5. Selecionar categoria no ComboBox.

---

## Botão Atualizar

1. Informar ID.
2. Alterar campos.
3. Montar Bean.
4. Chamar `dao.editar(produto)`.

---

## Botão Excluir

1. Pegar ID.
2. Verificar existência.
3. Confirmar com `showConfirmDialog()`.
4. Chamar `dao.excluir(id)`.

---

# 9. Como Usar o JComboBox com Dados do Banco

O ComboBox deve ser do tipo:

```java
JComboBox<Categoria>
```

Não utilize Strings fixas como:

```java
"Item 1"
"Selecione"
```

## Preenchimento

```java
private void preencherComboCategorias() {

    CategoriaDAO dao = new CategoriaDAO();

    cmbCategorias.removeAllItems();

    for (Categoria categoria : dao.listar("")) {
        cmbCategorias.addItem(categoria);
    }

    cmbCategorias.setSelectedIndex(-1);
}
```

No construtor:

```java
public FormProduto() {
    initComponents();
    preencherComboCategorias();
}
```

### Recuperar Item Selecionado

```java
Categoria categoria =
    (Categoria) cmbCategorias.getSelectedItem();
```

### Recuperar ID

```java
categoria.getId();
```

### Macete

- Dados do banco → use objetos (`Bean`).
- Opções fixas → use `String`.

---

# 10. JTable e Relatórios

Quando o professor pedir relatórios ou listagens, pense em:

```java
DefaultTableModel
```

## Exemplo

```java
DefaultTableModel modelo =
    (DefaultTableModel) tblProdutos.getModel();

modelo.setRowCount(0);

ProdutoDAO dao = new ProdutoDAO();

for (Produto produto : dao.listar()) {
    modelo.addRow(new Object[] {
        produto.getId(),
        produto.getNome(),
        produto.getPreco(),
        produto.getQuantidade(),
        produto.getCategoria().getNome()
    });
}
```

## Três Regras Importantes

1. `setRowCount(0)` limpa registros antigos.
2. `addRow()` adiciona uma nova linha.
3. Quantidade e ordem dos dados devem ser iguais às colunas da tabela.

---

# 11. Pesquisa por Nome ou Categoria

## Pesquisa por Nome

```sql
SELECT * FROM Produtos WHERE nome LIKE ?
```

```java
stmt.setString(1, "%" + nome + "%");
```

O `%` permite localizar textos parciais.

---

## Pesquisa por Categoria

```sql
SELECT * FROM Produtos
WHERE categoria_id IN (
    SELECT id
    FROM Categorias
    WHERE nome LIKE ?
)
```

---

## Na Tela

```java
if (tipo.equals("Nome")) {
    produtos = dao.buscarPorNome(busca);
}
else if (tipo.equals("Categoria")) {
    produtos = dao.buscarPorCategoria(busca);
}
```

### Pesquisa Automática

Utilize o evento:

```java
caretUpdate
```

do `JTextField`.

---

# 12. Criar o MenuPrincipal

Estrutura sugerida:

```text
Cadastros
├── Categorias
└── Produtos

Relatórios
└── Relatório de Produtos

Arquivo
└── Sair
```

Abrir formulário:

```java
new FormProduto().setVisible(true);
```

Sair:

```java
System.exit(0);
```

Nas outras telas use:

```java
DISPOSE_ON_CLOSE
```

e não:

```java
EXIT_ON_CLOSE
```

Para iniciar pelo menu:

```text
Projeto
→ Properties
→ Run
→ Main Class
→ view.MenuPrincipal
```

---

# 13. Erros que Você Deve Reconhecer Rapidamente

| Erro | O que conferir |
|--------|----------------|
| `NullPointerException` | Objeto está `null` |
| `NumberFormatException` | Conversão inválida de número |
| `SQLException` | SQL, conexão, colunas, FK ou parâmetros |
| `cannot infer type arguments` | Mistura de `JComboBox<Categoria>` com `String` |
| Erro no `initComponents()` | Conferir chaves `{}` próximas |
| Cadastro duplicado | INSERT executado duas vezes |
| Exclusão não funciona | Verificar `stmt.execute()` |
| JTable errada | Quantidade ou ordem das colunas |
| JComboBox vazio | Método de preenchimento ou dados inexistentes |
| Pesquisa não retorna | Tipo, texto e dados do banco |
| Tela fecha sistema | Verificar `EXIT_ON_CLOSE` e `DISPOSE_ON_CLOSE` |

---

# 14. Leitura de Arquivos TXT em Java

A leitura de arquivos é muito utilizada para importar dados externos para o sistema.

---

# 1. Exemplo Simples: Ler um Arquivo TXT

Crie uma classe chamada `LeitorArquivo.java` no pacote `principal`.

```java
package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivo {

    public static void main(String[] args) {

        try {

            BufferedReader leitor =
                new BufferedReader(
                    new FileReader("dados.txt")
                );

            String linha;

            while ((linha = leitor.readLine()) != null) {
                System.out.println(linha);
            }

            leitor.close();

        } catch (IOException ex) {

            System.out.println(
                "Erro ao ler arquivo: "
                + ex.getMessage()
            );
        }
    }
}
```

## O Que Lembrar

1. `FileReader` abre o arquivo.
2. `BufferedReader` permite ler linha por linha.
3. `readLine()` retorna cada linha do arquivo.
4. O `while` continua até o final do arquivo.
5. `close()` fecha o arquivo.

### Observação

O arquivo `dados.txt` deve estar no diretório esperado pelo programa, geralmente a pasta principal do projeto quando executado pelo NetBeans.

---

# 2. Exemplo com JFileChooser (Ideal para Swing)

Esse é o modelo mais indicado para a prova, pois permite que o usuário selecione qualquer arquivo `.txt` do computador.

Crie um `JFrame Form` chamado `FormArquivo` no pacote `view`.

## Componentes da Tela

| Componente | Variable Name |
|------------|--------------|
| JButton - Abrir TXT | `btnAbrir` |
| JTextArea | `txtConteudo` |

---

## Código do Botão Abrir TXT

No evento `actionPerformed` do botão:

```java
JFileChooser chooser = new JFileChooser();

FileNameExtensionFilter filtro =
    new FileNameExtensionFilter(
        "Arquivos TXT",
        "txt"
    );

chooser.setFileFilter(filtro);

int resultado =
    chooser.showOpenDialog(this);

if (resultado ==
    JFileChooser.APPROVE_OPTION) {

    File arquivo =
        chooser.getSelectedFile();

    try (BufferedReader leitor =
            new BufferedReader(
                new FileReader(arquivo))) {

        txtConteudo.setText("");

        String linha;

        while ((linha =
                leitor.readLine()) != null) {

            txtConteudo.append(
                linha + "\n"
            );
        }

        JOptionPane.showMessageDialog(
            this,
            "Arquivo aberto com sucesso!"
        );

    } catch (IOException ex) {

        JOptionPane.showMessageDialog(
            this,
            "Erro ao abrir arquivo: "
            + ex.getMessage()
        );
    }
}
```

---

## Imports Necessários

```java
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.swing.filechooser.FileNameExtensionFilter;
```

---

# 3. Como Funciona?

## Arquivo TXT de Exemplo

### `dados.txt`

```text
João;20;Informática
Maria;22;Direito
Pedro;19;Administração
```

---

## Fluxo da Operação

```text
Usuário
    ↓
JFileChooser
    ↓
Seleciona arquivo TXT
    ↓
BufferedReader
    ↓
Lê linha por linha
    ↓
JTextArea
    ↓
Exibe o conteúdo na tela
```

---

## Resultado no JTextArea

```text
João;20;Informática
Maria;22;Direito
Pedro;19;Administração
```

---

# Resumo Para a Prova

✅ `FileReader` → abre o arquivo

✅ `BufferedReader` → lê o arquivo

✅ `readLine()` → retorna uma linha

✅ `while ((linha = leitor.readLine()) != null)` → percorre todo o arquivo

✅ `close()` → fecha o arquivo

✅ `JFileChooser` → permite escolher o arquivo

✅ `FileNameExtensionFilter` → limita aos arquivos `.txt`

✅ `JTextArea` → exibe o conteúdo

✅ `IOException` → trata erros de leitura

### Macete

Sempre lembre da sequência:

```text
JFileChooser
    ↓
File
    ↓
FileReader
    ↓
BufferedReader
    ↓
readLine()
    ↓
JTextArea
```

Se aparecer uma questão sobre leitura de TXT em Java Swing, esse é o fluxo mais importante para memorizar.






# Resumo Final Para Decorar

✅ Banco de Dados → Conexão → Bean → DAO → Tela → Menu

✅ Bean = dados

✅ DAO = SQL

✅ Form = interface

✅ `INSERT`, `SELECT`, `UPDATE`, `DELETE`

✅ `PreparedStatement`

✅ `executeQuery()` → SELECT

✅ `executeUpdate()` ou `execute()` → INSERT, UPDATE e DELETE

✅ `ResultSet`

✅ `JComboBox` para relacionamentos

✅ `JTable` para relatórios

✅ `LIKE` para pesquisas

✅ `MenuPrincipal` para navegação

✅ Sempre fechar `PreparedStatement`

✅ Conferir quantidade de colunas da JTable

✅ Verificar se o SQL foi executado

✅ Nunca esquecer o `WHERE` em UPDATE e DELETE
