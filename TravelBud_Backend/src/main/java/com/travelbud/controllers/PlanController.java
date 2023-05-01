package com.travelbud.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.travelbud.apis.PlanAPI;

@RestController("/plans")
public interface PlanController extends PlanAPI {

}
