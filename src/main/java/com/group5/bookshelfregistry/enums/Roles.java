package com.group5.bookshelfregistry.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN","ADMIN CAN DO EVERYTHING"),
    VIEWER("ROLE_VIEWER","This user can perform only get operations");


    private String roleName;
    private String roleDescription;

    Roles(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
}
