package com.argus.api.controller;

import com.argus.api.domain.model.Usuarios;
import com.argus.api.dto.UsuarioDTO;
import com.argus.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsers() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findUserById(@PathVariable Long id) {
        return usuarioService.findUserById(id)
                .map(userDTO -> ResponseEntity.status(HttpStatus.OK).body(userDTO))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> findUserByCpf(@PathVariable String cpf) {
        return usuarioService.findUserByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable Long id, @RequestBody Usuarios usuarios) throws Exception {
        Usuarios updatedUser = usuarioService.updateUser(id, usuarios);
        UsuarioDTO usuarioDTO = usuarioService.convertToDTO(updatedUser);
        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws Exception {
        UsuarioDTO deletedUser = usuarioService.deleteUser(id);
        String responseMessage = deletedUser.nome() + " Foi Deletado Com Sucesso!";
        return ResponseEntity.ok(responseMessage);
    }
}
