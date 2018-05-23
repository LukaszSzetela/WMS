package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szetela.lukasz.WMS.models.Role;
import pl.szetela.lukasz.WMS.dao.RoleDao;

import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

    private RoleDao roleDao;

    @Autowired
    public RoleController(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> allRoles() {
        List<Role> allRoles = roleDao.findAll();
        return ResponseEntity.ok(allRoles);
    }
}