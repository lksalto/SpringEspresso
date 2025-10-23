package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.service.BugService;
import br.ufscar.dc.dsw.projeto.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/bugs")
public class BugController {
    
    @Autowired
    private BugService bugService;
    
    @Autowired
    private SessaoRepository sessaoRepository;
    
    @GetMapping("/detalhes/{id}")
    public String detalhesBug(@PathVariable Long id, Model model) {
        BugModel bug = bugService.buscarPorId(id);
        if (bug != null) {
            model.addAttribute("bug", bug);
            return "bug/detalhes";
        }
        return "redirect:/";
    }
    
    @GetMapping("/cadastro")
    public String formularioCadastro(@RequestParam Long idSessao, Model model) {
        SessaoModel sessao = sessaoRepository.findById(idSessao).orElse(null);
        if (sessao != null) {
            model.addAttribute("sessao", sessao);
            model.addAttribute("bug", new BugModel());
            return "bug/cadastro";
        }
        return "redirect:/";
    }
    
    @PostMapping("/cadastro")
    @Transactional
    public String salvarBug(@ModelAttribute BugModel bug,
                           @RequestParam Long idSessao,
                           @RequestParam(required = false) MultipartFile arquivo,
                           RedirectAttributes redirectAttributes) {
        try {
            SessaoModel sessao = sessaoRepository.findById(idSessao).orElse(null);
            if (sessao != null) {
                bug.setSessao(sessao);
                bug.setDataRegistro(LocalDateTime.now());
                bug.setResolvido(false);
                
                bugService.salvar(bug, arquivo);
                
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Bug cadastrado com sucesso!");
                return "redirect:/sessoes/detalhes/" + idSessao;
            } else {
                redirectAttributes.addFlashAttribute("mensagemFalha", "Sessão não encontrada!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemFalha", "Erro ao cadastrar bug: " + e.getMessage());
        }
        
        return "redirect:/bugs/cadastro?idSessao=" + idSessao;
    }
    
    @PostMapping("/resolver")
    @Transactional
    public String resolverBug(@RequestParam Long bugId, 
                            RedirectAttributes redirectAttributes) {
        try {
            boolean sucesso = bugService.resolverBug(bugId);
            if (sucesso) {
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Bug marcado como resolvido!");
            } else {
                redirectAttributes.addFlashAttribute("mensagemFalha", "Bug não encontrado!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemFalha", "Erro: " + e.getMessage());
        }
        return "redirect:/bugs/detalhes/" + bugId;
    }
    
    @PostMapping("/reabrir")
    @Transactional
    public String reabrirBug(@RequestParam Long bugId, 
                            RedirectAttributes redirectAttributes) {
        try {
            bugService.reabrirBug(bugId);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Bug reaberto!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemFalha", "Erro: " + e.getMessage());
        }
        return "redirect:/bugs/detalhes/" + bugId;
    }
}