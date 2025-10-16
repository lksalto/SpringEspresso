package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.service.BugService;
import br.ufscar.dc.dsw.projeto.service.SessaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/bugs")
public class BugController {

    private final BugService bugService;
    private final SessaoService sessaoService;

    // Diretório para salvar as imagens. Crie esta pasta na raiz do seu projeto.
    private static final String UPLOAD_DIR = "uploads/";

    public BugController(BugService bugService, SessaoService sessaoService) {
        this.bugService = bugService;
        this.sessaoService = sessaoService;
    }

    @GetMapping("/cadastro")
    public String formCadastro(@RequestParam Long idSessao, Model model) {
        SessaoModel sessao = sessaoService.buscarPorId(idSessao);
        if (sessao == null) {
            // Lidar com erro de sessão não encontrada
            return "redirect:/home";
        }
        model.addAttribute("bug", new BugModel());
        model.addAttribute("sessao", sessao);
        return "bug/formulario"; // Usaremos um formulário genérico
    }

    @PostMapping("/salvar")
    public String salvarBug(@ModelAttribute BugModel bug, @RequestParam Long idSessao,
                            @RequestParam("imagem") MultipartFile imagem, RedirectAttributes redirectAttributes) {
        SessaoModel sessao = sessaoService.buscarPorId(idSessao);
        if (sessao == null) {
            redirectAttributes.addFlashAttribute("fail", "Sessão não encontrada.");
            return "redirect:/home";
        }

        bug.setSessao(sessao);
        bug.setDataRegistro(LocalDateTime.now());

        if (!imagem.isEmpty()) {
            try {
                // Garante que o diretório de upload existe
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Gera um nome de arquivo único para evitar conflitos
                String nomeArquivo = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();
                Path caminhoArquivo = Paths.get(UPLOAD_DIR + nomeArquivo);
                Files.write(caminhoArquivo, imagem.getBytes());
                bug.setCaminhoImagem(nomeArquivo); // Salva apenas o nome do arquivo no banco

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("fail", "Falha ao salvar a imagem.");
                return "redirect:/sessoes/detalhes/" + idSessao;
            }
        }

        bugService.salvar(bug);
        redirectAttributes.addFlashAttribute("success", "Bug registrado com sucesso!");
        return "redirect:/sessoes/detalhes/" + idSessao;
    }
    
    // Adicione aqui métodos para editar, visualizar e deletar bugs conforme necessário.
}