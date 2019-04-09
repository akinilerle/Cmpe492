package com.bogazici.akinilerle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStory {

    public enum Type{
        TYPE_RRB, // RoleRequestBenefit:  <ROLE> olarak, <REQUEST> istiyorum. Böylece <BENEFIT>.
        TYPE_RBR, // RoleBenefitRequest:  <ROLE> olarak, <BENEFIT> için, <REQUEST> istiyorum.
        TYPE_RR //   RoleRequest:   <ROLE> olarak, <REQUEST> istiyorum.
    }

    private String role;
    private String request;
    private String benefit;
    private Type type;
}
