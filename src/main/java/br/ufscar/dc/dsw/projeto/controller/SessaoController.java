package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;
import br.ufscar.dc.dsw.projeto.service.SessaoService;
import br.ufscar.dc.dsw.projeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;

@Controller
public class SessaoController {

    private final SessaoService sessaoService;
    private final ProjetoService projetoService;
    private final EstrategiaService estrategiaService;
    private final UsuarioService usuarioService; // Adicionar serviço de usuário

    public SessaoController(SessaoService sessaoService, ProjetoService projetoService, EstrategiaService estrategiaService, UsuarioService usuarioService) {
        this.sessaoService = sessaoService;
        this.projetoService = projetoService;
        this.estrategiaService = estrategiaService;
        this.usuarioService = usuarioService; // Injetar no construtor
    }

    @GetMapping("/projetos/{projetoId}/estrategias/{estrategiaId}/sessoes")
    public String listarSessoes(@PathVariable Long projetoId, @PathVariable Long estrategiaId, Model model) {
        List<SessaoModel> sessoes = sessaoService.buscarPorProjetoEEstrategia(projetoId, estrategiaId);
        ProjetoModel projeto = projetoService.buscar(projetoId);
        EstrategiaModel estrategia = estrategiaService.buscarPorId(estrategiaId);

        model.addAttribute("sessoes", sessoes);
        model.addAttribute("projeto", projeto);
        model.addAttribute("estrategia", estrategia);

        return "sessoes/lista";
    }

    @GetMapping("/projetos/{projetoId}/estrategias/{estrategiaId}/sessoes/nova")
    public String novaSessaoForm(@PathVariable Long projetoId, @PathVariable Long estrategiaId, Model model) {
        ProjetoModel projeto = projetoService.buscar(projetoId);
        EstrategiaModel estrategia = estrategiaService.buscarPorId(estrategiaId);

        SessaoModel sessao = new SessaoModel();
        sessao.setProjeto(projeto);
        sessao.setEstrategia(estrategia);

        model.addAttribute("sessao", sessao);
        return "sessoes/formulario";
    }

    @GetMapping("/sessoes/detalhes/{id}")
    public String detalhesSessao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        SessaoModel sessao = sessaoService.buscarPorIdComBugs(id);

        if (sessao == null) {
            redirectAttributes.addFlashAttribute("fail", "Sessão não encontrada.");
            return "redirect:/home";
        }

        model.addAttribute("sessao", sessao);
        return "sessao/detalhes";
    }

    @PostMapping("/sessoes/salvar")
    public String salvarSessao(@RequestParam Long projetoId,
                               @RequestParam Long estrategiaId,
                               @RequestParam String descricao,
                               @RequestParam(required = false) Long testerId,
                               @RequestParam Long duracaoMinutos, // Receber como minutos
                               RedirectAttributes redirectAttributes) {

        ProjetoModel projeto = projetoService.buscar(projetoId);
        EstrategiaModel estrategia = estrategiaService.buscarPorId(estrategiaId);

        if (projeto == null || estrategia == null) {
            redirectAttributes.addFlashAttribute("mensagemFalha", "Erro ao salvar: Projeto ou Estratégia inválido.");
            return "redirect:/home";
        }

        SessaoModel sessao = new SessaoModel();
        sessao.setProjeto(projeto);
        sessao.setEstrategia(estrategia);
        sessao.setDescricao(descricao);
        sessao.setDuracao(Duration.ofMinutes(duracaoMinutos)); // Converter minutos para Duration

        if (testerId != null) {
            UsuarioModel tester = usuarioService.buscarPorId(testerId);
            sessao.setTester(tester);
        }

        sessaoService.salvar(sessao);
        return "redirect:/projetos/" + projetoId + "/estrategias/" + estrategiaId + "/sessoes";
    }

    @GetMapping("/sessoes/cadastro")
    public String formCadastro(@RequestParam Long projetoId, @RequestParam Long estrategiaId, Model model, RedirectAttributes redirectAttributes) {
        ProjetoModel projeto = projetoService.buscar(projetoId);
        EstrategiaModel estrategia = estrategiaService.buscarPorId(estrategiaId);

        if (projeto == null || estrategia == null) {
            redirectAttributes.addFlashAttribute("mensagemFalha", "Projeto ou Estratégia não encontrado.");
            return "redirect:/projetos/listar";
        }

        SessaoModel sessao = new SessaoModel();
        sessao.setProjeto(projeto);
        sessao.setEstrategia(estrategia);

        model.addAttribute("sessao", sessao);
        return "sessoes/formulario";
    }
}
