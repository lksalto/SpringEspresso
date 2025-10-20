package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.service.BugService;
import br.ufscar.dc.dsw.projeto.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    private BugService bugService;

    @Autowired
    private SessaoService sessaoService;

    // Diretório para salvar as imagens de upload
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(@RequestParam("idSessao") Long idSessao, ModelMap model) {
        SessaoModel sessao = sessaoService.buscarPorId(idSessao);
        if (sessao == null) {
            // Lidar com o caso de sessão não encontrada
            return "redirect:/home";
        }
        BugModel bug = new BugModel();
        bug.setSessao(sessao);
        model.addAttribute("bug", bug);
        // CORREÇÃO: Retornar o nome do seu arquivo de formulário
        return "bug/formulario";
    }

    @PostMapping("/salvar")
    public String salvarBug(@Valid @ModelAttribute("bug") BugModel bug,
                            BindingResult result,
                            @RequestParam("imagem") MultipartFile imagemFile,
                            RedirectAttributes attr, ModelMap model) {

        SessaoModel sessao = sessaoService.buscarPorId(bug.getSessao().getId());
        if (sessao == null) {
            attr.addFlashAttribute("mensagemFalha", "Sessão não encontrada.");
            return "redirect:/home";
        }
        bug.setSessao(sessao);

        if (result.hasErrors()) {
            model.addAttribute("bug", bug);
            return "bug/formulario";
        }

        // Define a data de registro e salva o bug uma primeira vez para obter o ID
        bug.setDataRegistro(LocalDateTime.now());
        BugModel bugSalvo = bugService.salvar(bug);

        // Lógica de upload de imagem, agora usando o ID do bug salvo
        if (!imagemFile.isEmpty()) {
            try {
                // Extrai a extensão do arquivo original
                String nomeArquivoOriginal = imagemFile.getOriginalFilename();
                String extensao = "";
                if (nomeArquivoOriginal != null && nomeArquivoOriginal.contains(".")) {
                    extensao = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
                }

                // Constrói o novo nome do arquivo com o formato solicitado
                Long bugId = bugSalvo.getId();
                Long estrategiaId = bugSalvo.getSessao().getEstrategia().getId();
                Long projetoId = bugSalvo.getSessao().getProjeto().getId();
                String nomeArquivoFormatado = String.format("bug_%d_E%d_P%d%s", bugId, estrategiaId, projetoId, extensao);

                // Garante que o diretório de upload exista
                Path diretorioUpload = Paths.get(UPLOAD_DIR);
                if (!Files.exists(diretorioUpload)) {
                    Files.createDirectories(diretorioUpload);
                }

                // Salva o arquivo no disco
                Path caminhoArquivo = diretorioUpload.resolve(nomeArquivoFormatado);
                Files.write(caminhoArquivo, imagemFile.getBytes());
                
                // Atualiza o objeto bug com o caminho da imagem e salva novamente
                bugSalvo.setCaminhoImagem(nomeArquivoFormatado);
                bugService.salvar(bugSalvo);

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("mensagemFalha", "Falha ao fazer upload da imagem.");
                // Se falhar, redireciona de volta para a edição do bug recém-criado
                return "redirect:/bugs/editar/" + bugSalvo.getId();
            }
        }

        attr.addFlashAttribute("mensagemSucesso", "Bug reportado com sucesso!");
        return "redirect:/sessoes/detalhes/" + bugSalvo.getSessao().getId();
    }

    @GetMapping("/detalhes/{id}")
    public String exibirDetalhes(@PathVariable("id") Long id, ModelMap model) {
        BugModel bug = bugService.buscarPorId(id);
        if (bug == null) {
            // Adicionar mensagem de erro e redirecionar
            return "redirect:/home"; 
        }
        model.addAttribute("bug", bug);
        return "bug/detalhes";
    }

 
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        // Primeiro, busca o bug para saber para qual sessão redirecionar
        BugModel bug = bugService.buscarPorId(id);
        if (bug == null) {
            attr.addFlashAttribute("mensagemFalha", "Bug não encontrado.");
            // Redireciona para a home se o bug não existir mais
            return "redirect:/";
        }

        // Guarda o ID da sessão para o redirecionamento
        Long sessaoId = bug.getSessao().getId();

        try {
            bugService.excluirBug(id);
            attr.addFlashAttribute("mensagemSucesso", "Bug removido com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("mensagemFalha", "Erro ao remover o bug.");
        }

        // Redireciona de volta para a página de detalhes da sessão original
        return "redirect:/sessoes/detalhes/" + sessaoId;
    }

}