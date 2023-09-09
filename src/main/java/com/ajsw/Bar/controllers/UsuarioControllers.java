package com.ajsw.Bar.controllers;

import com.ajsw.Bar.models.Usuario;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioControllers {

    @RequestMapping(value = "usuario")
    public Usuario prueba()
    {
        return new Usuario(1L,"nombre", "apellido", "email", "telefono", "password");
    }
}
