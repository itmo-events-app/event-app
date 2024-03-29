package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exception.NotFoundException;
import org.itmo.eventapp.main.model.dto.RoleDto;
import org.itmo.eventapp.main.model.entity.RoleType;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper mapper;


    public void deleteRole(Integer id) throws NotFoundException {
        if (!roleRepository.existsById(id)) throw new NotFoundException(String.format("Role with id %d doesn't exist", id));
        // добавить проверки
        roleRepository.deleteById(id);
    }

    public List<RoleDto> getAll() {

        return roleRepository.findAll().stream().map(role -> mapper.map(role, RoleDto.class)).toList();
    }


    public List<RoleDto> getOrganizational() {
        return roleRepository.findAllByType(RoleType.EVENT).stream().map(role -> mapper.map(role, RoleDto.class)).toList();
    }
}
