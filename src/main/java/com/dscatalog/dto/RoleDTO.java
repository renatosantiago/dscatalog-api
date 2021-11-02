package com.dscatalog.dto;

import java.io.Serializable;

import com.dscatalog.entities.Role;

public class RoleDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private String authority;

  public RoleDTO() {
  }

  public RoleDTO(Role entity) {
    id = entity.getId();
    authority = entity.getAuthority();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthority() {
    return this.authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

}
