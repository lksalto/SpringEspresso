package br.ufscar.dc.dsw.projeto.service;

import org.springframework.stereotype.Service;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.repository.EstrategiaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EstrategiaService {

    private final EstrategiaRepository estrategiaRepository;

    public EstrategiaService(EstrategiaRepository estrategiaRepository) {
        this.estrategiaRepository = estrategiaRepository;
    }

    public List<EstrategiaModel> buscarTodas() {
        return estrategiaRepository.findAll();
    }

    public EstrategiaModel buscarPorId(Long id) {
        Optional<EstrategiaModel> op = estrategiaRepository.findById(id);
        return op.orElse(null);
    }

    public void salvar(EstrategiaModel estrategia) {
        estrategiaRepository.save(estrategia);
    }

    public void remover(Long id) {
        estrategiaRepository.deleteById(id);
    }


}
