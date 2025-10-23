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
import br.ufscar.dc.dsw.projeto.repository.SessaoRepository;

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
    private SessaoRepository sessaoRepository;

    @GetMapping("/listar")
    public String listar(Model model, Authentication authentication) { // Mudança aqui: Principal -> Authentication
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

            model.addAttribute("projetos", projetos);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("titulo", titulo);

            return "projetos/listar"; // Certifique-se que está usando o caminho correto

        } catch (Exception e) {
            model.addAttribute("fail", "Erro ao carregar projetos: " + e.getMessage());
            model.addAttribute("projetos", new ArrayList<>());
            model.addAttribute("isAdmin", false);
            model.addAttribute("titulo", "Projetos");
            return "projetos/listar";
        }
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            //redirectAttributes.addFlashAttribute("fail", "Projeto não encontrado.");
            return "redirect:/projetos/listar";
        }
        
        // Contar sessões finalizadas por estratégia
        Map<Long, Long> sessoesFinalizadasPorEstrategia = new HashMap<>();
        
        System.out.println("=== DEBUG SESSÕES ===");
        System.out.println("Projeto ID: " + id);
        System.out.println("Total de sessões no sistema: " + sessaoRepository.findAll().size());
        
        for (EstrategiaModel estrategia : projeto.getEstrategias()) {
            long sessoesFinalizadas = contarSessoesFinalizadas(id, estrategia.getId());
            sessoesFinalizadasPorEstrategia.put(estrategia.getId(), sessoesFinalizadas);
            
            System.out.println("Estratégia: " + estrategia.getNome() + " (ID: " + estrategia.getId() + ")");
            System.out.println("Sessões finalizadas: " + sessoesFinalizadas);
        }
        
        model.addAttribute("projeto", projeto);
        model.addAttribute("sessoesFinalizadasPorEstrategia", sessoesFinalizadasPorEstrategia);
        return "projetos/detalhes"; // Era "projeto/detalhes"
    }
    private long contarSessoesFinalizadas(Long projetoId, Long estrategiaId) {
        // Método alternativo mais direto
        try {
            long count = sessaoRepository.findAll()
                    .stream()
                    .peek(s -> System.out.println("Sessão ID: " + s.getId() + ", Projeto: " + 
                            (s.getProjeto() != null ? s.getProjeto().getId() : "null") + 
                            ", Estratégia: " + (s.getEstrategia() != null ? s.getEstrategia().getId() : "null") + 
                            ", Status: " + s.getStatus()))
                    .filter(s -> s.getProjeto() != null && 
                                s.getProjeto().getId() != null && 
                                s.getProjeto().getId().equals(projetoId) &&
                                s.getEstrategia() != null && 
                                s.getEstrategia().getId() != null && 
                                s.getEstrategia().getId().equals(estrategiaId) &&
                                s.getStatus() != null &&
                                s.getStatus() == StatusSessao.FINALIZADO)
                    .count();
            
            System.out.println("Contagem final para Projeto " + projetoId + ", Estratégia " + estrategiaId + ": " + count);
            return count;
        } catch (Exception e) {
            System.err.println("Erro ao contar sessões: " + e.getMessage());
            return 0;
        }
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/novo")
    public String novo(Model model) {
        ProjetoCadastroDTO dto = new ProjetoCadastroDTO();
        model.addAttribute("projetoCadastroDTO", dto);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projetos/formulario"; // Era "projeto/cadastro" ou "projeto/formulario"
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
            //redirectAttributes.addFlashAttribute("fail", "Projeto não encontrado.");
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
        return "projetos/formulario"; // Era "projeto/formulario"
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
}
