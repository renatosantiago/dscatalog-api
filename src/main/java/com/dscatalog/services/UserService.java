package com.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.dscatalog.dto.RoleDTO;
import com.dscatalog.dto.UserDTO;
import com.dscatalog.dto.UserInsertDTO;
import com.dscatalog.dto.UserUpdateDTO;
import com.dscatalog.entities.Role;
import com.dscatalog.entities.User;
import com.dscatalog.repositories.RoleRepository;
import com.dscatalog.repositories.UserRepository;
import com.dscatalog.services.exceptions.DataBaseException;
import com.dscatalog.services.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

  private static Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
    Page<User> list = repository.findAll(pageRequest);
    return list.map(c -> new UserDTO(c));
  }

  @Transactional
  public UserDTO findById(Long id) {
    Optional<User> obj = repository.findById(id);
    User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO insert(UserInsertDTO dto) {
    User entity = new User();
    copyDtoToEntity(dto, entity);
    entity.setPassword(passwordEncoder.encode(dto.getPassword())); // senha encriptada
    entity = repository.save(entity);
    return new UserDTO(entity);
  }

  @Transactional
  public UserDTO update(Long id, UserUpdateDTO dto) {
    try {
      User entity = repository.getOne(id);
      copyDtoToEntity(dto, entity);
      entity = repository.save(entity);
      return new UserDTO(entity);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not Found " + id);
    }
  }

  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    } catch (DataIntegrityViolationException e) {
      throw new DataBaseException("Integrity violation");
    }
  }

  private void copyDtoToEntity(UserDTO dto, User entity) {
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setEmail(dto.getEmail());

    entity.getRoles().clear();
    ;
    for (RoleDTO roleDTO : dto.getRoles()) {
      Role r = roleRepository.getOne(roleDTO.getId());
      entity.getRoles().add(r);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repository.findByEmail(email);
    if(user == null) {
      logger.error("User not found: " + email);
      throw new UsernameNotFoundException("Email not found");
    }
    logger.info("User found: " + email);
    return user;
  }
}
