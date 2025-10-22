package br.ufscar.dc.dsw.projeto.util;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.repository.EstrategiaRepository;
import br.ufscar.dc.dsw.projeto.repository.ProjetoRepository;
import br.ufscar.dc.dsw.projeto.repository.SessaoRepository;
import br.ufscar.dc.dsw.projeto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- ADICIONE ESTE IMPORT

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;
    private final SessaoRepository sessaoRepository;
    private final PasswordEncoder passwordEncoder; // <-- ADICIONE ESTA LINHA

    public DatabaseSeeder(
        UsuarioRepository usuarioRepository,
        ProjetoRepository projetoRepository,
        EstrategiaRepository estrategiaRepository,
        SessaoRepository sessaoRepository,
        PasswordEncoder passwordEncoder // <-- ADICIONE ESTA LINHA
    ) {
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
        this.sessaoRepository = sessaoRepository;
        this.passwordEncoder = passwordEncoder; // <-- ADICIONE ESTA LINHA
    }

    @Override
    public void run(String... args) {

        // Limpa o banco na ordem certa
        sessaoRepository.deleteAll();
        projetoRepository.deleteAll();    
        estrategiaRepository.deleteAll(); 
        usuarioRepository.deleteAll();    
        
        // Cria usuários com senha criptografada
        UsuarioModel admin = new UsuarioModel("Admin", "admin@admin.com", passwordEncoder.encode("admin")); // <-- MODIFIQUE AQUI
        admin.setRole("ROLE_ADMIN"); 

        UsuarioModel user = new UsuarioModel("Maria Silva", "user@user.com", passwordEncoder.encode("user")); // <-- MODIFIQUE AQUI
        user.setRole("ROLE_USER");

        usuarioRepository.saveAll(List.of(admin, user));
        
        // Cria estratégias padrão
        List<EstrategiaModel> estrategiasPadrao = new ArrayList<>();
        estrategiasPadrao.add(new EstrategiaModel("Single Session Strategy", "Consiste em deixar o jogador ter um primeiro contato com o jogo, e jogar da maneira que achar melhor, sem nenhuma estratégia em mente, apenas para se acostumar com o jogo"));
        estrategiasPadrao.add(new EstrategiaModel("Golden Path Strategy", "Consiste em apresentar ao jogador a maneira ótima de se jogar, ou seja, seguir o caminho recomendado pelos desenvolvedores, de maneira otimizada e direta, evitando desvios desnecessários, e rotas não desejadas."));
        estrategiasPadrao.add(new EstrategiaModel("Noob Journey", "Tentar jogar o jogo de maneira errada, não seguindo recomendações, e tentando achar maneiras alternativas de realizar as ações (basicamente tentar fazer o contrário do que é recomendado no “Golden Path Strategy”), a fim de descobrir maneiras alternativas de prosseguir no jogo."));
        estrategiasPadrao.add(new EstrategiaModel("Completionist", "Consiste em jogar o jogo de maneira a realizar todas as ações possíveis dentro dele (pegar todas as moedas, matar todos os inimigos, abrir todas as portas, etc), de maneira que não reste mais nenhum objetivo a ser completo."));
        estrategiasPadrao.add(new EstrategiaModel("Stress Test", "Consiste em realizar comandos e inválidos, ou não esperados pelos desenvolvedores, como por exemplo apertar botões que não realizam ações, ou apertá-los de maneira muito rápida, com o intuito de “quebrar” o jogo"));
        estrategiasPadrao.add(new EstrategiaModel("Speedrun", "Consiste em tentar finalizar a sessão da maneira mais rápida possível, fazendo o mínimo de ações necessárias para concluir o objetivo."));
        estrategiasPadrao.add(new EstrategiaModel("User Interface", "Testar menus, interfaces e gráficos do jogo, de maneira a ver se estão funcionando da maneira adequada."));
        estrategiasPadrao.add(new EstrategiaModel("Neighboring", "Uma vez encontrado um bug, o usuário tenta encontrar mais deles realizando ações parecidas e em locais próximos, uma vez que a possibilidade de encontrar mais bugs mostra-se maior em áreas onde já foram encontrados outros anteriormente"));
        estrategiasPadrao.add(new EstrategiaModel("Overtime", "Testar novamente algum bug conhecido, em um outro momento (aplicar o neighboring após melhorias/evoluções)"));
        
        
        ProjetoModel projeto1 = new ProjetoModel("Projeto Alpha", "Teste Projeto Alpha");
        projeto1.getEstrategias().addAll(estrategiasPadrao); // Adiciona as estratégias novas ao projeto
        
        ProjetoModel projeto2 = new ProjetoModel("Projeto Beta", "Teste Projeto Beta");
        projeto2.getEstrategias().addAll(estrategiasPadrao);

        EstrategiaModel estrategia2 = new EstrategiaModel("Automated Testing", "Descrição da Automated Testing");
        projeto2.getEstrategias().add(estrategia2);

        
        projetoRepository.saveAll(List.of(projeto1, projeto2));

        System.out.println("Banco populado com Usuarios, Projetos, Estratégias!");
    }
}


