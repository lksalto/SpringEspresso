package br.ufscar.dc.dsw.config.seeders;

import br.ufscar.dc.dsw.models.DicaModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.repositories.DicaRepository;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.ExemploRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaSeeder {
    private final EstrategiaRepository estrategiaRepository;
    private final DicaRepository dicaRepository;
    private final ExemploRepository exemploRepository;

    public EstrategiaSeeder(EstrategiaRepository estrategiaRepository, DicaRepository dicaRepository, ExemploRepository exemploRepository) {
        this.estrategiaRepository = estrategiaRepository;
        this.dicaRepository = dicaRepository;
        this.exemploRepository = exemploRepository;
    }

    public void seedEstrategias() {
        if (estrategiaRepository.count() > 0) {
            System.out.println("✓ Estratégias já existem no banco de dados.");
            return;
        }

        System.out.println("Criando estratégias iniciais...");

        // Estratégia 1: Teste de Interface
        //EstrategiaModel estrategia1 = criarEstrategiaInterface();
        
        // Estratégia 2: Teste de Performance
        //EstrategiaModel estrategia2 = criarEstrategiaPerformance();
        
        // Estratégia 3: Teste de Segurança
        EstrategiaModel estrategia3 = criarEstrategiaSeguranca();

        // Estratégias do XPLOIT
        EstrategiaModel estrategia4 = criarEstrategiaSingleSession();
        EstrategiaModel estrategia5 = criarEstrategiaGoldenPath();
        EstrategiaModel estrategia6 = criarEstrategiaNoobJourney();
        EstrategiaModel estrategia7 = criarEstrategiaCompletionist();
        EstrategiaModel estrategia8 = criarEstrategiaStressTest();
        EstrategiaModel estrategia9 = criarEstrategiaSpeedRun();
        EstrategiaModel estrategia10 = criarEstrategiaUserInterface();
        EstrategiaModel estrategia11 = criarEstrategiaNeighboring();
        EstrategiaModel estrategia12 = criarEstrategiaOvertime();

        System.out.println("✓ Estratégias iniciais criadas com sucesso!");

    }

    private EstrategiaModel criarEstrategiaInterface() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Interface de Usuário");
        estrategia.setDescricao("Estratégia para testar a interface do usuário, incluindo elementos visuais, navegação e responsividade.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Sempre teste em diferentes resoluções de tela para garantir responsividade.");
        criarDica(estrategia, "Verifique se todos os elementos clicáveis têm estados visuais distintos (hover, focus, active).");
        criarDica(estrategia, "Teste a navegação por teclado para garantir acessibilidade.");
        criarDica(estrategia, "Valide se as cores têm contraste adequado para leitura.");
        criarDica(estrategia, "Teste a funcionalidade em diferentes navegadores.");

        criarExemplo(estrategia, "Botão de login que muda de cor quando o mouse passa por cima", "/uploads/estrategias/exemplo-botao-hover.jpg",0);
        criarExemplo(estrategia, "Menu responsivo que se adapta a telas menores", "/uploads/estrategias/exemplo-menu-responsivo.jpg",1);
        criarExemplo(estrategia, "Formulário com validação visual em tempo real", "/uploads/estrategias/exemplo-validacao-formulario.jpg",2);

        return estrategia;
    }

    private EstrategiaModel criarEstrategiaPerformance() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Performance");
        estrategia.setDescricao("Estratégia para avaliar o desempenho da aplicação, incluindo tempo de resposta e uso de recursos.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Monitore o tempo de carregamento das páginas em diferentes conexões de internet.");
        criarDica(estrategia, "Teste com diferentes volumes de dados para identificar gargalos.");
        criarDica(estrategia, "Verifique o uso de memória e CPU durante operações intensivas.");
        criarDica(estrategia, "Teste a performance de consultas ao banco de dados.");
        criarDica(estrategia, "Monitore o tempo de resposta de APIs externas.");

        criarExemplo(estrategia, "Gráfico de tempo de resposta de uma API", "/uploads/estrategias/exemplo-grafico-performance.jpg",0);
        criarExemplo(estrategia, "Monitoramento de uso de memória durante testes de carga", "/uploads/estrategias/exemplo-monitoramento-memoria.jpg",1);
        criarExemplo(estrategia, "Análise de consultas lentas no banco de dados", "/uploads/estrategias/exemplo-consultas-lentas.jpg",2);

        return estrategia;
    }

    private EstrategiaModel criarEstrategiaSeguranca() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Segurança");
        estrategia.setDescricao("Estratégia para identificar vulnerabilidades de segurança na aplicação.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Teste injeção de SQL em todos os campos de entrada.");
        criarDica(estrategia, "Verifique se dados sensíveis não são expostos em logs ou respostas de erro.");
        criarDica(estrategia, "Teste autenticação e autorização em todas as rotas protegidas.");
        criarDica(estrategia, "Valide proteção contra ataques XSS (Cross-Site Scripting).");
        criarDica(estrategia, "Teste proteção contra CSRF (Cross-Site Request Forgery).");
        criarDica(estrategia, "Verifique se senhas são armazenadas de forma segura (hash).");



        return estrategia;
    }

    private EstrategiaModel criarEstrategiaSingleSession() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Single Session Strategy");
        estrategia.setDescricao("Estratégia para deixar o tester ter um primeiro contato com o jogo.");
        estrategia = estrategiaRepository.save(estrategia);

        criarExemplo(estrategia, "Teste Single Session", "single-session.jpg",0);

        criarDica(estrategia, "O intuito é jogar normalmente, sem se preocupar com detecção de bugs ou jogar de forma otimizada.");
        criarDica(estrategia, "Seja livre, sem  pressão.");

        return estrategia;
    }

    private EstrategiaModel criarEstrategiaGoldenPath() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Golden Path Strategy");
        estrategia.setDescricao("Estratégia de testes para jogar de maneira otimizada.");
        estrategia = estrategiaRepository.save(estrategia);

        criarExemplo(estrategia, "Teste Golden Path", "golden-path.jpg",0);

        criarDica(estrategia, "Agora o intuito é jogar de maneira otimizada, conforme elaborado pelos desenvolvedores.");
        criarDica(estrategia, "Entre em contato com os desenvolvedores, para saber quais as formas ótimas de se alcançar os objetivos.");
        
       
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaNoobJourney() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Noob Journey");
        estrategia.setDescricao("Estratégia utilizada para se jogar o jogo de maneira 'noob', não seguindo recomendações e errando de propósito.");
        estrategia = estrategiaRepository.save(estrategia);

        criarExemplo(estrategia, "Teste Noob Journey", "noob-journey.jpg",0);
        criarDica(estrategia, "Tente fazer o oposto do que é sugerido.");

        criarDica(estrategia, "Jogue igual aquele seu amigo que você sempre precisa carregar.");
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaCompletionist() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Completionist");
        estrategia.setDescricao("Estratégia focada em completar o jogo em 100% dos objetivos");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Busque até a última moeda de todos os mapas.");
        criarDica(estrategia, "Mate todos os inimigos possíveis.");
        
        criarExemplo(estrategia, "Teste Completionist", "completionist.jpg",0);
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaStressTest() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Stress Test");
        estrategia.setDescricao("Estratégia focada em testar inputs e ações não esperadas pelos desenvolvedores do jogo");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Tente utilizar comandos inválidos, ou de maneira inesperada.");
        criarDica(estrategia, "Ataque um personagem enquanto o diálogo está aberto.");
        
        criarExemplo(estrategia, "Teste Stress Test", "stress-test.jpg",0);
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaSpeedRun() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Speed Run");
        estrategia.setDescricao("Estratégia focada em finalizar o jogo da maneira mais rápida possível");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Não importa a sua pontuação, o importante é acabar com o jogo logo.");
                
        criarExemplo(estrategia, "Teste Speed Run", "speed-run.jpg",0);
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaUserInterface() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("User Interface");
        estrategia.setDescricao("Interaja com a UI, e teste os seus limites");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Abra menus, tente navegar por eles e veja se estão todos funcionando.");
                
        criarExemplo(estrategia, "Teste UI", "user-interface.jpg",0);
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaNeighboring() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Neighboring");
        estrategia.setDescricao("Uma vez encontrado um bug, o usuário tenta encontrar mais deles realizando ações parecidas e em locais próximos.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "A possibilidade de encontrar mais bugs mostra-se maior em áreas onde já foram encontrados outros anteriormente.");
                
        criarExemplo(estrategia, "Teste Neighboring", "neighboring.jpg",0);
        return estrategia;
    }

    private EstrategiaModel criarEstrategiaOvertime() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Overtime");
        estrategia.setDescricao("Testar novamente algum bug conhecido, em um outro momento (aplicar o neighboring após melhorias/evoluções).");
        
        estrategia = estrategiaRepository.save(estrategia);
        criarExemplo(estrategia, "Teste Overtime", "overtime.jpg",0);
        return estrategia;
    }

    private void criarDica(EstrategiaModel estrategia, String texto) {
        DicaModel dica = new DicaModel();
        dica.setTexto(texto);
        dica.setEstrategia(estrategia);
        dicaRepository.save(dica);
    }

    private void criarExemplo(EstrategiaModel estrategia, String texto, String urlImagem, int ordem) {
        ExemploModel exemplo = new ExemploModel();
        exemplo.setTexto(texto);
        exemplo.setUrlImagem(urlImagem);
        exemplo.setEstrategia(estrategia);
        exemplo.setOrdem(ordem);
        exemploRepository.save(exemplo);
    }
} 