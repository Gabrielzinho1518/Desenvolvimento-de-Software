/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Cadastro;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CadastroForm extends JFrame {

    // Componentes da Interface
    private JTextField txtNome;
    private JTextField txtDataNasc;
    private JComboBox<String> cbSexo;
    private JTextField txtMatricula;
    private JTextField txtCurso;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JComboBox<String> cbEstado;
    private JTextField txtTelefone;
    private JTextArea txtAreaResultado;
    private JButton btnCadastrar;

    public CadastroForm() {
        // Configurações básicas da janela
        setTitle("Sistema de Cadastro de Alunos");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        
        // Inicializa os componentes e o layout
        initComponents();
    }

    private void initComponents() {
        // Painel principal utilizando BorderLayout para organizar o formulário e a área de texto
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel do Formulário (com GridBagLayout para alinhar os campos perfeitamente)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;

        // 1. Nome Completo
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; 
        txtNome = new JTextField(25);
        painelFormulario.add(txtNome, gbc);

        // 2. Data de Nascimento
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Data de Nascimento (DD/MM/AAAA):"), gbc);
        gbc.gridx = 1; 
        txtDataNasc = new JTextField(25);
        painelFormulario.add(txtDataNasc, gbc);

        // 3. Sexo (ComboBox)
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1; 
        cbSexo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Outro"});
        painelFormulario.add(cbSexo, gbc);

        // 4. Matrícula
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; 
        txtMatricula = new JTextField(25);
        painelFormulario.add(txtMatricula, gbc);

        // 5. Curso
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1; 
        txtCurso = new JTextField(25);
        painelFormulario.add(txtCurso, gbc);

        // 6. CPF
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; 
        txtCpf = new JTextField(25);
        painelFormulario.add(txtCpf, gbc);

        // 7. Endereço Completo
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Endereço Completo (Rua, Nº, Bairro, Cidade, CEP):"), gbc);
        gbc.gridx = 1; 
        txtEndereco = new JTextField(25);
        painelFormulario.add(txtEndereco, gbc);

        // 8. Estado (ComboBox com as siglas do Brasil)
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; 
        String[] estados = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", 
                            "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", 
                            "SP", "SE", "TO"};
        cbEstado = new JComboBox<>(estados);
        painelFormulario.add(cbEstado, gbc);

        // 9. Telefone de Contato
        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Telefone de Contato:"), gbc);
        gbc.gridx = 1; 
        txtTelefone = new JTextField(25);
        painelFormulario.add(txtTelefone, gbc);

        // 10. Botão Cadastrar
        linha++;
        gbc.gridx = 0; gbc.gridy = linha; gbc.gridwidth = 2;
        btnCadastrar = new JButton("Cadastrar Aluno");
        btnCadastrar.setBackground(new Color(70, 130, 180));
        btnCadastrar.setForeground(Color.WHITE);
        painelFormulario.add(btnCadastrar, gbc);

        // Adiciona o formulário ao centro do painel principal
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);

        // Configuração do JTextArea para exibir os resultados com barra de rolagem
        txtAreaResultado = new JTextArea(8, 30);
        txtAreaResultado.setEditable(false);
        txtAreaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtAreaResultado);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Alunos Cadastrados (Formato Separado por ;) "));
        
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Adiciona o painel principal na janela
        add(painelPrincipal);

        // Evento de clique do botão
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarCadastro();
            }
        });
    }

    private void executarCadastro() {
        // Validação básica para impedir campos vazios essenciais
        if (txtNome.getText().trim().isEmpty() || txtMatricula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha pelo menos o Nome e a Matrícula!", 
                    "Campos Obrigatórios", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recolhe os dados dos campos
        String nome = txtNome.getText();
        String dataNasc = txtDataNasc.getText();
        String sexo = cbSexo.getSelectedItem().toString();
        String matricula = txtMatricula.getText();
        String curso = txtCurso.getText();
        String cpf = txtCpf.getText();
        String endereco = txtEndereco.getText();
        String estado = cbEstado.getSelectedItem().toString();
        String telefone = txtTelefone.getText();

        // Cria o objeto Aluno
        Aluno aluno = new Aluno(nome, dataNasc, sexo, matricula, curso, cpf, endereco, estado, telefone);

        // Adiciona o resultado formatado no JTextArea
        txtAreaResultado.append(aluno.toString() + "\n");

        // Limpa os campos para o próximo cadastro
        limparCampos();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDataNasc.setText("");
        cbSexo.setSelectedIndex(0);
        txtMatricula.setText("");
        txtCurso.setText("");
        txtCpf.setText("");
        txtEndereco.setText("");
        cbEstado.setSelectedIndex(0);
        txtTelefone.setText("");
        txtNome.requestFocus(); // Retorna o foco para o primeiro campo
    }

    // Método principal para rodar a aplicação no NetBeans
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastroForm().setVisible(true);
            }
        });
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

     *
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
