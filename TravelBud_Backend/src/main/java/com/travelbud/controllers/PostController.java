package com.travelbud.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.travelbud.apis.PostAPI;

@RestController("/posts")
public interface PostController extends PostAPI{

}
