package com.sytoss.domain.bom.exceptions.business.notfound;

public class GroupNotFoundException extends NotFoundException {

    public GroupNotFoundException(Long id) {
        super("Group", id);
    }
}
