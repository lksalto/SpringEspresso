package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.dto.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.model.StatusSessao;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;
import br.ufscar.dc.dsw.projeto.service.UsuarioService;
import br.ufscar.dc.dsw.projeto.service.SessaoService; // NOVA IMPORTAÇÃO

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.security.Principal;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private EstrategiaService estrategiaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessaoService sessaoService; // NOVA INJEÇÃO - SUBSTITUI O SessaoRepository

    @GetMapping("/listar")
    public String listar(Model model, Authentication authentication) {
        try {
            List<ProjetoModel> projetos;
            boolean isAdmin = false;
            String titulo = "Projetos";

            if (authentication != null && authentication.getAuthorities() != null) {
                // Verificar se é ADMIN
                isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                if (isAdmin) {
                    projetos = projetoService.listarTodos();
                    titulo = "Todos os Projetos (Administrador)";
                } else {
                    String email = authentication.getName();
                    projetos = projetoService.buscarProjetosPorMembro(email);
                    titulo = "Meus Projetos";
                }
            } else {
                projetos = new ArrayList<>();
            }

            // ===== CALCULAR MEDALHAS PARA CADA PROJETO =====
            Map<Long, Map<String, Integer>> medalhasPorProjeto = new HashMap<>();
            
            for (ProjetoModel projeto : projetos) {
                Map<Long, Long> sessoesFinalizadas = sessaoService.contarSessoesPorEstrategia(
                    projeto.getId(), 
                    StatusSessao.FINALIZADO
                );
                
                Map<String, Integer> medalhas = calcularMedalhas(sessoesFinalizadas);
                medalhasPorProjeto.put(projeto.getId(), medalhas);
            }

            model.addAttribute("projetos", projetos);
            model.addAttribute("medalhasPorProjeto", medalhasPorProjeto);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("titulo", titulo);

            return "projetos/listar";

        } catch (Exception e) {
            model.addAttribute("fail", "Erro ao carregar projetos: " + e.getMessage());
            model.addAttribute("projetos", new ArrayList<>());
            model.addAttribute("medalhasPorProjeto", new HashMap<>());
            model.addAttribute("isAdmin", false);
            model.addAttribute("titulo", "Projetos");
            return "projetos/listar";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            return "redirect:/projetos/listar";
        }
        
        // ===== VERSÃO OTIMIZADA - UMA SÓ QUERY =====
        Map<Long, Long> sessoesFinalizadasPorEstrategia = sessaoService.contarSessoesPorEstrategia(
            id, 
            StatusSessao.FINALIZADO
        );
        
        model.addAttribute("projeto", projeto);
        model.addAttribute("sessoesFinalizadasPorEstrategia", sessoesFinalizadasPorEstrategia);
        return "projetos/detalhes";
    }
    
    // ===== REMOVER COMPLETAMENTE ESTE MÉTODO =====
    // private long contarSessoesFinalizadas(Long projetoId, Long estrategiaId) { ... }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/novo")
    public String novo(Model model) {
        try {
            ProjetoCadastroDTO projetoCadastroDTO = new ProjetoCadastroDTO();
            
            List<EstrategiaModel> estrategias = estrategiaService.buscarTodas();
            List<UsuarioModel> usuarios = usuarioService.listarTodos();

            // Pré-selecionar todas as estratégias
            List<Long> todasEstrategiasIds = estrategias.stream()
                .map(EstrategiaModel::getId)
                .collect(Collectors.toList());
            projetoCadastroDTO.setEstrategiasIds(todasEstrategiasIds);

            model.addAttribute("projetoCadastroDTO", projetoCadastroDTO);
            model.addAttribute("estrategias", estrategias);
            model.addAttribute("usuarios", usuarios);

            return "projetos/formulario";

        } catch (Exception e) {
            model.addAttribute("fail", "Erro ao carregar formulário: " + e.getMessage());
            return "projetos/listar";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cadastro")
    public String formCadastro(Model model) {
        return novo(model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ProjetoCadastroDTO projetoCadastroDTO, 
                    RedirectAttributes redirectAttributes) {
        try {
            projetoService.salvar(projetoCadastroDTO);
            redirectAttributes.addFlashAttribute("success", "Projeto cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao cadastrar projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            return "redirect:/projetos/listar";
        }

        ProjetoEdicaoDTO dto = new ProjetoEdicaoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        
        if (projeto.getEstrategias() != null) {
            dto.setEstrategiasIds(projeto.getEstrategias().stream()
                    .map(EstrategiaModel::getId)
                    .collect(Collectors.toList()));
        }
        
        if (projeto.getMembros() != null) {
            dto.setMembrosIds(projeto.getMembros().stream()
                    .map(UsuarioModel::getId)
                    .collect(Collectors.toList()));
        }

        model.addAttribute("projetoEdicaoDTO", dto);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projetos/formulario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar")
    public String editar(@ModelAttribute ProjetoEdicaoDTO projetoEdicaoDTO, 
                    RedirectAttributes redirectAttributes) {
        try {
            projetoService.atualizar(projetoEdicaoDTO);
            redirectAttributes.addFlashAttribute("success", "Projeto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao atualizar projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/remover/{id}")
    public String removerProjeto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projetoService.remover(id);
            redirectAttributes.addFlashAttribute("success", "Projeto removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao remover projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    // ===== NOVO MÉTODO PARA CALCULAR MEDALHAS =====
    private Map<String, Integer> calcularMedalhas(Map<Long, Long> sessoesFinalizadas) {
        Map<String, Integer> medalhas = new HashMap<>();
        medalhas.put("ouro", 0);
        medalhas.put("prata", 0);
        medalhas.put("bronze", 0);
        
        if (sessoesFinalizadas == null || sessoesFinalizadas.isEmpty()) {
            return medalhas;
        }
        
        // Contar medalhas baseado no número de sessões finalizadas
        for (Long count : sessoesFinalizadas.values()) {
            if (count >= 10) {
                medalhas.put("ouro", medalhas.get("ouro") + 1);
            } else if (count >= 5) {
                medalhas.put("prata", medalhas.get("prata") + 1);
            } else if (count >= 1) {
                medalhas.put("bronze", medalhas.get("bronze") + 1);
            }
        }
        
        return medalhas;
    }
}
