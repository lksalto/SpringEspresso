package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.services.SessaoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sessoes")
public class SessaoController {
    private final SessaoService sessaoService;
    // private final ProjetoService projetoService;
    // private final EstrategiaService estrategiaService;

    public SessaoController(
            SessaoService sessaoService
            // ProjetoService projetoService,
            // EstrategiaService estrategiaService
    ) {
        this.sessaoService = sessaoService;
        // this.projetoService = projetoService;
        // this.estrategiaService = estrategiaService;
    }

    // @GetMapping("/cadastro")
    // public String exibirFormularioCadastro(@RequestParam("projetoId") UUID projetoId, Model model) {
    //     ProjetoModel projeto = projetoService.buscarPorId(projetoId);
    //     model.addAttribute("sessaoDTO", new SessaoDTO(projetoId, null, null, ""));
    //     model.addAttribute("projeto", projeto);
    //     model.addAttribute("estrategias", estrategiaService.listarTodos());
    //     return "sessao/formulario";
    // }

    @PostMapping("/criar")
    public String criarSessao(
            @ModelAttribute @Valid SessaoDTO sessaoDto,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        SessaoModel novaSessao = sessaoService.criarSessao(sessaoDto, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Sessão criada com sucesso!");
        return "redirect:/sessoes/" + novaSessao.getId();
    }

    @GetMapping("/{id}")
    public String detalhesSessao(@PathVariable UUID id, Model model) {
        SessaoModel sessao = sessaoService.buscarPorId(id);
        model.addAttribute("sessao", sessao);
        model.addAttribute("todosStatus", StatusSessao.values());
        return "sessao/detalhes";
    }

    // @GetMapping("/projeto/{projetoId}")
    // public String listarPorProjeto(@PathVariable UUID projetoId, Model model) {
    //     List<SessaoModel> sessoes = sessaoService.listarPorProjeto(projetoId);
    //     ProjetoModel projeto = projetoService.buscarPorId(projetoId);
    //     model.addAttribute("sessoes", sessoes);
    //     model.addAttribute("projeto", projeto);
    //     return "sessao/lista";
    // }

    @PostMapping("/atualizarStatus")
    public String atualizarStatus(
            @RequestParam UUID sessaoId,
            @RequestParam StatusSessao novoStatus,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        sessaoService.atualizarStatus(sessaoId, novoStatus, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Status da sessão foi atualizado com sucesso!");
        return "redirect:/sessoes/" + sessaoId;
    }

    @PostMapping("/deletar")
    public String deletarSessao(
            @RequestParam UUID sessaoId,
            @RequestParam UUID projetoId,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        sessaoService.deletarSessao(sessaoId, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Sessão foi removida");
        return "redirect:/";
    }

    @GetMapping("/minhas-sessoes")
    public String listarMinhasSessoes(Model model, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        List<SessaoModel> minhasSessoes = sessaoService.listarPorTester(usuarioLogado.getId());
        model.addAttribute("sessoes", minhasSessoes);
        model.addAttribute("tituloPagina", "Minhas Sessões de Teste");
        return "sessao/lista";
    }
}