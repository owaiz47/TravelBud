package com.travelbud.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.travelbud.apis.UserAPI;

@RestController("/users")
public interface UserController extends UserAPI{

	
}
