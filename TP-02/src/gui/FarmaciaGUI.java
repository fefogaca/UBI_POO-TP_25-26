
package gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import modelo.Farmacia;
import persistencia.GestorDados;

/**
 * Janela principal da aplicação da Farmácia.
 * <p>
 * Utiliza Swing para criar a interface gráfica com separadores (JTabbedPane)
 * para gestão de clientes, produtos e encomendas. Gere o ciclo de vida da aplicação,
 * incluindo confirmação ao encerrar e opção de guardar dados.
 * </p>
 *
 * @author Afonso Esteves - 55047
 * @author Felipe Dourado - 53394
 * @author Tiago Pereira - 55019
 */
public class FarmaciaGUI extends JFrame {

    private static final int LARGURA_JANELA = 1000;
    private static final int ALTURA_JANELA = 700;
    private static final int LARGURA_MINIMA = 800;
    private static final int ALTURA_MINIMA = 600;

    private Farmacia farmacia;

    /**
     * Construtor que cria a janela principal da aplicação.
     * <p>
     * Configura a janela com tamanho otimizado, ícone (se disponível) e
     * gestão do ciclo de vida da aplicação.
     * </p>
     *
     * @param farmacia A instância da farmácia que contém os dados a gerir.
     */
    public FarmaciaGUI(Farmacia farmacia) {
        this.farmacia = farmacia;

        setTitle("Gestão de Farmácia");
        setSize(LARGURA_JANELA, ALTURA_JANELA);
        setMinimumSize(new Dimension(LARGURA_MINIMA, ALTURA_MINIMA));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tentar carregar ícone da aplicação (se existir)
        // Usa ImageIO.read() que é mais confiável que Toolkit.getDefaultToolkit().getImage()
        try {
            Image icon = null;
            File iconFile = null;
            String userDir = System.getProperty("user.dir");
            
            // Tentar descobrir o diretório do projeto baseado no classpath
            String projectDir = userDir;
            try {
                String classPath = System.getProperty("java.class.path");
                if (classPath != null && classPath.contains("out")) {
                    // Se o classpath contém "out", tentar encontrar o diretório do projeto
                    String[] paths = classPath.split(File.pathSeparator);
                    for (String path : paths) {
                        if (path.contains("out")) {
                            File outDir = new File(path);
                            if (outDir.exists() && outDir.isDirectory()) {
                                // Se o caminho é "out", o projeto está um nível acima
                                if (outDir.getName().equals("out")) {
                                    projectDir = outDir.getParent();
                                    break;
                                } else if (path.endsWith("out")) {
                                    // Se o caminho termina com "out", extrair o diretório pai
                                    projectDir = outDir.getParent();
                                    break;
                                }
                            }
                        }
                    }
                }
                
                // Verificação adicional: se user.dir é "out", subir um nível
                File currentDir = new File(userDir);
                if (currentDir.getName().equals("out")) {
                    projectDir = currentDir.getParent();
                }
            } catch (Exception e) {
                // Usar user.dir como fallback
            }
            
            // Lista de caminhos possíveis para tentar (em ordem de prioridade)
            String[] caminhosPossiveis = {
                projectDir + File.separator + "resources" + File.separator + "icon.png",  // Caminho absoluto do diretório do projeto
                userDir + File.separator + "resources" + File.separator + "icon.png",    // Caminho absoluto do diretório atual
                "resources" + File.separator + "icon.png",                                // Caminho relativo ao diretório atual
                projectDir + File.separator + ".." + File.separator + "resources" + File.separator + "icon.png",  // Um nível acima do projeto
                userDir + File.separator + ".." + File.separator + "resources" + File.separator + "icon.png"      // Um nível acima
            };
            
            for (String caminho : caminhosPossiveis) {
                try {
                    iconFile = new File(caminho);
                    boolean exists = iconFile.exists();
                    boolean isFile = iconFile.isFile();
                    boolean canRead = iconFile.canRead();
                    
                    if (exists && isFile && canRead) {
                        // Usar ImageIO.read() que é mais confiável
                        icon = ImageIO.read(iconFile);
                        if (icon != null) {
                            setIconImage(icon);
                            System.out.println("[DEBUG] Ícone carregado com sucesso de: " + iconFile.getAbsolutePath());
                            break; // Ícone encontrado e carregado
                        } else {
                            System.out.println("[DEBUG] ImageIO.read() retornou null para: " + iconFile.getAbsolutePath());
                        }
                    } else {
                        System.out.println("[DEBUG] Arquivo não encontrado ou não acessível: " + caminho + 
                            " (existe: " + exists + ", é arquivo: " + isFile + ", pode ler: " + canRead + ")");
                    }
                } catch (IOException ex) {
                    System.out.println("[DEBUG] IOException ao ler: " + caminho + " - " + ex.getMessage());
                    // Continuar tentando outros caminhos
                    continue;
                } catch (Exception ex) {
                    System.out.println("[DEBUG] Erro ao processar: " + caminho + " - " + ex.getMessage());
                    // Continuar tentando outros caminhos
                    continue;
                }
            }
            
            if (icon == null) {
                System.out.println("[DEBUG] Ícone não foi carregado. Diretório atual: " + userDir + ", Projeto detectado: " + projectDir);
            }
        } catch (Exception e) {
            // Ícone não encontrado ou erro ao carregar - continuar sem ícone
        }



        // Adiciona um listener para guardar os dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Deseja guardar os dados antes de sair?", 
                    "Sair", JOptionPane.YES_NO_CANCEL_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    GestorDados.guardarDados(farmacia.getClientes(), farmacia.getProdutos(), farmacia.getEncomendas());
                    dispose();
                    System.exit(0);
                } else if (confirm == JOptionPane.NO_OPTION) {
                    dispose();
                    System.exit(0);
                } 
                // Se for CANCEL, não faz nada
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();

        PainelGestaoClientes painelClientes = new PainelGestaoClientes(farmacia);
        PainelGestaoProdutos painelProdutos = new PainelGestaoProdutos(farmacia);
        PainelGestaoEncomendas painelEncomendas = new PainelGestaoEncomendas(farmacia, painelProdutos); // Passa painelProdutos
        PainelEstatisticas painelEstatisticas = new PainelEstatisticas(farmacia);

        // Adicionar os painéis de gestão
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Produtos", painelProdutos);
        tabbedPane.addTab("Encomendas", painelEncomendas);
        tabbedPane.addTab("Estatísticas", painelEstatisticas);

        add(tabbedPane);
    }
}
