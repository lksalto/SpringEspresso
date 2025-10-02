package br.ufscar.dc.dsw.projeto.util;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.repository.EstrategiaRepository;
import br.ufscar.dc.dsw.projeto.repository.ProjetoRepository;
import br.ufscar.dc.dsw.projeto.repository.UsuarioRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;
    //private final SessaoRepository sessaoRepository;

    public DatabaseSeeder(
        UsuarioRepository usuarioRepository,
        ProjetoRepository projetoRepository,
        EstrategiaRepository estrategiaRepository
        //SessaoRepository sessaoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
        //this.sessaoRepository = sessaoRepository;
    }

    @Override
    public void run(String... args) {

        // Limpa o banco na ordem certa
        estrategiaRepository.deleteAll(); // filhos
        //sessaoRepository.deleteAll(); // se usar SessaoModel
        projetoRepository.deleteAll();    // pais
        usuarioRepository.deleteAll();    // últimos

        // Cria usuários
        UsuarioModel tester1 = new UsuarioModel("123", "123", "{noop}123");
        UsuarioModel tester2 = new UsuarioModel("Maria Silva", "maria", "{noop}senha");
        usuarioRepository.save(tester1);
        usuarioRepository.save(tester2);

        // Cria projetos
        ProjetoModel projeto1 = new ProjetoModel("Projeto Alpha", "Teste Projeto Alpha");
        ProjetoModel projeto2 = new ProjetoModel("Projeto Beta", "Teste Projeto Beta");
        projetoRepository.save(projeto1);
        projetoRepository.save(projeto2);

        // Cria estratégias
        EstrategiaModel estrategia1 = new EstrategiaModel();
        estrategia1.setNome("Exploratory Testing");
        estrategia1.setDescricao("Descrição da Exploratory Testing");
        estrategia1.getProjetos().add(projeto1);
        projeto1.getEstrategias().add(estrategia1);

        EstrategiaModel estrategia2 = new EstrategiaModel();
        estrategia2.setNome("Automated Testing");
        estrategia2.setDescricao("Descrição da Automated Testing");
        estrategia2.getProjetos().add(projeto2);
        projeto2.getEstrategias().add(estrategia2);

        // Salva estratégias
        estrategiaRepository.save(estrategia1);
        estrategiaRepository.save(estrategia2);

        // Atualiza projetos para refletir o lado inverso
        projetoRepository.save(projeto1);
        projetoRepository.save(projeto2);


        System.out.println("Banco populado com Usuarios, Projetos, Estratégias!");
    }
}
