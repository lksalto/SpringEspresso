package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.EstrategiaDto;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize; // Import for security annotations

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EstrategiaService {

    @Autowired
    private EstrategiaRepository estrategiaRepository;

    private final Path uploadLocation = Paths.get("src/main/resources/static/uploads/estrategias");

    public EstrategiaService() {
        try {
            Files.createDirectories(uploadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload.", e);
        }
    }

    // Método principal para salvar (cria ou atualiza)
    // Only ADMINs can save (create or update) strategies
    @PreAuthorize("hasRole('ADMIN')")
    public EstrategiaModel save(EstrategiaDto dto, List<MultipartFile> imagensExemplo) {
        EstrategiaModel estrategia = convertDtoToModel(dto);

        // Processa as imagens de exemplo
        if (imagensExemplo != null && !imagensExemplo.isEmpty()) {
            int i = 0;
            for (ExemploModel exemplo : estrategia.getExemplos()) {
                // Ensure there's a corresponding image file for each example
                if (i < imagensExemplo.size()) {
                    MultipartFile imagem = imagensExemplo.get(i++);
                    if (!imagem.isEmpty()) {
                        if (exemplo.getUrlImagem() != null && !exemplo.getUrlImagem().isEmpty()) {
                            deleteFile(exemplo.getUrlImagem());
                        }
                        String newFilename = saveFile(imagem);
                        exemplo.setUrlImagem(newFilename);
                    }
                }
            }
        }
        return estrategiaRepository.save(estrategia);
    }

    @Transactional(readOnly = true)
    public EstrategiaModel findById(UUID id) {
        return estrategiaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<EstrategiaModel> findAll() {
        return estrategiaRepository.findAll();
    }

    // Only ADMINs can delete strategies
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        EstrategiaModel estrategia = findById(id);
        if (estrategia != null) {
            // Apaga os arquivos de imagem associados antes de deletar a entidade
            estrategia.getExemplos().forEach(ex -> {
                if (ex.getUrlImagem() != null && !ex.getUrlImagem().isEmpty()) {
                    deleteFile(ex.getUrlImagem());
                }
            });
            estrategiaRepository.deleteById(id);
        }
    }

    // Funções utilitárias (upload de arquivo e conversão DTO <-> Model)
    // No need for PreAuthorize on these private helper methods, as they are called
    // by the public methods which are already secured.
    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) return null;
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new RuntimeException("Arquivo sem extensão não é permitido.");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        try {
            Path destinationFile = this.uploadLocation.resolve(newFilename).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo.", e);
        }
    }

    private void deleteFile(String filename) {
        try {
            Path file = uploadLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("ERRO ao excluir arquivo: " + filename);
        }
    }

    // Converte DTO para Model para salvar no banco
    private EstrategiaModel convertDtoToModel(EstrategiaDto dto) {
        final EstrategiaModel model;
        if (dto.getId() != null) {
            EstrategiaModel existente = findById(dto.getId()); // Carrega a entidade existente para atualização
            if (existente == null) {
                model = new EstrategiaModel();
            } else {
                model = existente;
            }
        } else {
            model = new EstrategiaModel();
        }

        model.setNome(dto.getNome());
        model.setDescricao(dto.getDescricao());

        model.getDicas().clear();
        if (dto.getDicas() != null) {
            dto.getDicas().forEach(dicaDto -> {
                br.ufscar.dc.dsw.models.DicaModel dicaModel = new br.ufscar.dc.dsw.models.DicaModel();
                dicaModel.setId(dicaDto.getId());
                dicaModel.setTexto(dicaDto.getDica());
                dicaModel.setEstrategia(model);
                model.getDicas().add(dicaModel);
            });
        }

        model.getExemplos().clear();
        if (dto.getExemplos() != null) {
            dto.getExemplos().forEach(exemploDto -> {
                br.ufscar.dc.dsw.models.ExemploModel exemploModel = new br.ufscar.dc.dsw.models.ExemploModel();
                exemploModel.setId(exemploDto.getId());
                exemploModel.setTexto(exemploDto.getTexto());
                exemploModel.setUrlImagem(exemploDto.getUrlImagem());
                exemploModel.setEstrategia(model);
                model.getExemplos().add(exemploModel);
            });
        }

        return model;
    }

    // Converte Model para DTO para exibir no formulário
    public EstrategiaDto convertModelToDto(EstrategiaModel model) {
        EstrategiaDto dto = new EstrategiaDto();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setDescricao(model.getDescricao());

        java.util.Set<br.ufscar.dc.dsw.dtos.DicaDto> dicasDto = new java.util.HashSet<>();
        if (model.getDicas() != null) {
            model.getDicas().forEach(dicaModel -> {
                br.ufscar.dc.dsw.dtos.DicaDto dicaDto = new br.ufscar.dc.dsw.dtos.DicaDto();
                dicaDto.setId(dicaModel.getId());
                dicaDto.setDica(dicaModel.getTexto());
                dicasDto.add(dicaDto);
            });
        }
        dto.setDicas(dicasDto);

        java.util.Set<br.ufscar.dc.dsw.dtos.ExemploDto> exemplosDto = new java.util.HashSet<>();
        if (model.getExemplos() != null) {
            model.getExemplos().forEach(exemploModel -> {
                br.ufscar.dc.dsw.dtos.ExemploDto exemploDto = new br.ufscar.dc.dsw.dtos.ExemploDto();
                exemploDto.setId(exemploModel.getId());
                exemploDto.setTexto(exemploModel.getTexto());
                exemploDto.setUrlImagem(exemploModel.getUrlImagem());
                exemplosDto.add(exemploDto);
            });
        }
        dto.setExemplos(exemplosDto);

        return dto;
    }
}