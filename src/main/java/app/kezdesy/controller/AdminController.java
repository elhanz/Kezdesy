package app.kezdesy.controller;

import app.kezdesy.entity.User;
import app.kezdesy.service.implementation.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }


}
