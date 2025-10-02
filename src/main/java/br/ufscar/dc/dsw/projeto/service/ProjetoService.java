package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public List<ProjetoModel> listar() { return projetoRepository.findAll(); }
    public ProjetoModel salvar(ProjetoModel projeto) { return projetoRepository.save(projeto); }
    public ProjetoModel buscar(UUID id) { return projetoRepository.findById(id).orElse(null); }
    public void remover(UUID id) { projetoRepository.deleteById(id); }
}
