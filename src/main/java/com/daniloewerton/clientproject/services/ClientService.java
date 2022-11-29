package com.daniloewerton.clientproject.services;

import com.daniloewerton.clientproject.dto.ClientDTO;
import com.daniloewerton.clientproject.entities.Client;
import com.daniloewerton.clientproject.repositories.ClientRepository;
import com.daniloewerton.clientproject.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client entity = convertDtoToEntity(dto);
        return new ClientDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(PageRequest pageRequest) {
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> optional = repository.findById(id);
        Client entity = optional.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        Client entity = repository.getOne(id);
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException e) {
            throw new EntityNotFoundException("Cliente não encontrado.");
        }
    }

    private Client convertDtoToEntity(ClientDTO dto) {
        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
        return entity;
    }
}
