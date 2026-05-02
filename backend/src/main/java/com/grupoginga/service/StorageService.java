package com.grupoginga.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    // Recebe o arquivo e retorna o nome/caminho gerado para salvar no banco
    String salvar(MultipartFile arquivo);

    // Método futuro caso queira implementar exclusão da foto antiga ao atualizar
    void remover(String nomeArquivo);
}