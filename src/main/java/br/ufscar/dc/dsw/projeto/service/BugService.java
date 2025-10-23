package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import br.ufscar.dc.dsw.projeto.model.TipoArquivo;
import br.ufscar.dc.dsw.projeto.repository.BugRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class BugService {

    private static final Logger logger = LoggerFactory.getLogger(BugService.class);

    @Autowired
    private BugRepository bugRepository;

    private final String uploadDir = "uploads/";

    @Transactional
    public BugModel salvar(BugModel bug, MultipartFile arquivo) throws IOException {
        
        if (arquivo != null && !arquivo.isEmpty()) {
            // Verificar tipo do arquivo
            String contentType = arquivo.getContentType();
            TipoArquivo tipoArquivo = null;
            
            if (contentType != null) {
                if (contentType.startsWith("image/")) {
                    tipoArquivo = TipoArquivo.IMAGEM;
                } else if (contentType.startsWith("video/")) {
                    tipoArquivo = TipoArquivo.VIDEO;
                } else {
                    throw new IOException("Tipo de arquivo não suportado: " + contentType);
                }
            }
            
            // Criar diretório se não existir
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Gerar nome único para o arquivo
            String nomeArquivoOriginal = arquivo.getOriginalFilename();
            String extensao = "";
            if (nomeArquivoOriginal != null && nomeArquivoOriginal.contains(".")) {
                extensao = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
            }
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path caminhoArquivo = uploadPath.resolve(nomeArquivo);
            
            // Salvar arquivo
            Files.copy(arquivo.getInputStream(), caminhoArquivo);
            
            // Definir no objeto bug
            bug.setCaminhoArquivo(nomeArquivo);
            bug.setTipoArquivo(tipoArquivo);
        }
        
        return bugRepository.save(bug);
    }

    @Transactional(readOnly = true)
    public BugModel buscarPorId(Long id) {
        return bugRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<BugModel> buscarPorSessaoId(Long sessaoId) {
        return bugRepository.findBySessaoId(sessaoId);
    }

    @Transactional
    public void deletar(Long id) {
        bugRepository.deleteById(id);
    }

    @Transactional
    public void excluirBug(Long id) {
        bugRepository.deleteById(id);
    }

    @Transactional
    public void marcarComoResolvido(Long bugId) {
        BugModel bug = bugRepository.findById(bugId).orElse(null);
        if (bug != null) {
            bug.setResolvido(true);
            bugRepository.save(bug);
        }
    }
    
    @Transactional
    public void reabrirBug(Long bugId) {
        BugModel bug = bugRepository.findById(bugId).orElse(null);
        if (bug != null) {
            bug.setResolvido(false);
            bugRepository.save(bug);
        }
    }
    
    @Transactional
    public boolean resolverBug(Long bugId) {
        try {
            BugModel bug = bugRepository.findById(bugId).orElse(null);
            if (bug != null) {
                bug.setResolvido(true);
                bugRepository.save(bug);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro ao resolver bug {}: {}", bugId, e.getMessage());
            return false;
        }
    }
}