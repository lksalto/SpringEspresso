package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.BugCadastroDTO;
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.BugEdicaoDTO;
import br.ufscar.dc.dsw.dtos.ProjetoDTO;
import br.ufscar.dc.dsw.services.BugService;
import br.ufscar.dc.dsw.services.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.BugStatus;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/projetos/{projetoId}/bugs")
public class BugController {

    @Autowired
    private BugService bugService;

    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public String listarBugsPorProjeto(@PathVariable UUID projetoId, ModelMap model) {
        ProjetoDTO projeto = projetoService.buscarPorId(projetoId);
        List<BugDTO> bugs = bugService.buscarPorProjeto(projetoId);
        
        model.addAttribute("projeto", projeto);
        model.addAttribute("listaBugs", bugs);

        return "bug/lista"; // Você precisará criar ou adaptar a view 'bug/lista.html'
    }

    @GetMapping("/cadastro")
    public String preRenderCadastroBug(@PathVariable UUID projetoId, ModelMap model) {
        ProjetoDTO projeto = projetoService.buscarPorId(projetoId);
        
        // Assumindo que BugCadastroDTO agora recebe (UUID projetoId, String titulo, String descricao)
        model.addAttribute("bugCadastroDTO", new BugCadastroDTO(projetoId, "", ""));
        model.addAttribute("projeto", projeto);
        model.addAttribute("isEditModeBug", false);
        return "bug/formulario"; // Você precisará criar ou adaptar a view 'bug/formulario.html'
    }

    @PostMapping("/salvar")
    public String salvarBug(@PathVariable UUID projetoId,
                            @Valid @ModelAttribute("bugCadastroDTO") BugCadastroDTO bugCadastroDTO,
                            BindingResult result,
                            RedirectAttributes attr,
                            @AuthenticationPrincipal UsuarioModel reporter) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bugCadastroDTO", result);
            attr.addFlashAttribute("bugCadastroDTO", bugCadastroDTO);
            return "redirect:/projetos/" + projetoId + "/bugs/cadastro";
        }

        bugService.salvarNovoBug(bugCadastroDTO, reporter);
        attr.addFlashAttribute("success", "Bug registrado com sucesso!");
        return "redirect:/projetos/" + projetoId + "/bugs";
    }

    @GetMapping("/editar/{bugId}")
    public String preRenderEdicaoBug(@PathVariable UUID projetoId, @PathVariable UUID bugId, ModelMap model) {
        ProjetoDTO projeto = projetoService.buscarPorId(projetoId);
        BugDTO bug = bugService.buscarBugPorId(bugId);
        
        // Assumindo que BugEdicaoDTO agora recebe (UUID id, String titulo, String descricao)
        BugEdicaoDTO bugEdicaoDTO = new BugEdicaoDTO(bug.id(), bug.titulo(), bug.descricao());
        
        model.addAttribute("bugEdicaoDTO", bugEdicaoDTO);
        model.addAttribute("projeto", projeto);
        model.addAttribute("isEditModeBug", true);
        return "bug/formulario";
    }

    @PostMapping("/atualizar")
    public String atualizarBug(@PathVariable UUID projetoId, @Valid @ModelAttribute("bugEdicaoDTO") BugEdicaoDTO bugEdicaoDTO,
                               BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bugEdicaoDTO", result);
            attr.addFlashAttribute("bugEdicaoDTO", bugEdicaoDTO);
            return "redirect:/projetos/" + projetoId + "/bugs/editar/" + bugEdicaoDTO.id();
        }
        bugService.atualizarBug(bugEdicaoDTO);
        attr.addFlashAttribute("success", "Bug atualizado com sucesso!");
        return "redirect:/projetos/" + projetoId + "/bugs";
    }

   @GetMapping("/remover/{bugId}")
    public String removerBug(@PathVariable UUID projetoId, @PathVariable UUID bugId, RedirectAttributes attr) {
        bugService.excluirBug(bugId);
        attr.addFlashAttribute("success", "Bug removido com sucesso!");
        return "redirect:/projetos/" + projetoId + "/bugs";
   }
}