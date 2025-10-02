package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.ProjetoResumoDTO;
import br.ufscar.dc.dsw.dtos.SessaoDetalhesDTO;
import br.ufscar.dc.dsw.dtos.BugCadastroDTO;
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.BugEdicaoDTO;
import br.ufscar.dc.dsw.models.BugModel;
import br.ufscar.dc.dsw.models.ProjetoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.BugStatus;
import br.ufscar.dc.dsw.repositories.BugRepository;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BugService {

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    /*@Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarSessao(#idSessao)")
    public List<BugDTO> buscarTodosBugsPorSessao(UUID idSessao, UsuarioModel usuarioLogado) {
        List<BugModel> bugs = bugRepository.findBySessaoIdOrderByDataRegistroDesc(idSessao);
        return bugs.stream().map(this::toBugDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarBug(#id)")
    public BugDTO buscarBugPorId(UUID id) {
        BugModel bug = bugRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bug não encontrado com ID: " + id));
        return toBugDTO(bug);
    } 
*/
    @Transactional
    @PreAuthorize("@securityService.podeAcessarSessao(#dto.idSessao())")
    public void salvarNovoBug(BugCadastroDTO dto, UsuarioModel reporter) {
        BugModel bug = new BugModel();
        bug.setTitulo(dto.titulo());
        bug.setDescricao(dto.descricao());
        bug.setProjeto(projetoRepository.findById(dto.projetoId()).orElseThrow());
        bug.setReporter(reporter);

        bug.setStatus(BugStatus.ABERTO); // sempre começa como ABERTO

        bugRepository.save(bug);
    }

    @Transactional
    @PreAuthorize("@securityService.podeAcessarBug(#dto.id())")
    public void atualizarBug(BugEdicaoDTO dto) {
        BugModel bug = bugRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Bug não encontrado."));
        
        bug.setTitulo(dto.titulo());
        bug.setDescricao(dto.descricao());

        bugRepository.save(bug);
    }

    @Transactional
    @PreAuthorize("@securityService.podeAcessarBug(#id)")
    public void excluirBug(UUID id) {
        bugRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BugDTO> buscarPorProjeto(UUID projetoId) {
        return bugRepository.findByProjetoId(projetoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BugDTO buscarBugPorId(UUID id) {
        BugModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bug não encontrado."));
        return convertToDto(bug);
    }

private BugDTO convertToDto(BugModel bug) {
    return new BugDTO(
            bug.getId(),
            bug.getTitulo(),
            bug.getDescricao(),
            bug.getReporter().getNome(),
            bug.getDataReporte(),
            bug.getProjeto().getId(), 
            bug.getStatus()
        );
    }
    /*@Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarSessao(#idSessao)")
    public SessaoDetalhesDTO buscarDetalhesDaSessao(UUID idSessao) {
        SessaoModel sessao = sessaoRepository.findById(idSessao)
            .orElseThrow(() -> new IllegalArgumentException("Sessão de teste não encontrada com ID: " + idSessao));
        
        ProjetoResumoDTO projetoDTO = new ProjetoResumoDTO(
            //sessao.getProjeto().getId(),
            sessao.getProjeto().getNome()
        );
        
        return new SessaoDetalhesDTO(
            sessao.getId(),
            sessao.getDescricao(),
            projetoDTO
        );
    } 
    private BugDTO toBugDTO(BugModel bug) {
        return new BugDTO(
            bug.getId(),
            //bug.getSessao().getId(),
            bug.getDescricao()
            //bug.getDataRegistro(),
            //bug.isResolvido()
        );
    }
*/
}