package com.renjian.blog.web.admin;

import com.renjian.blog.module.User;
import com.renjian.blog.service.UserService;
import com.renjian.blog.util.MyExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String toRegister(){
        return "admin/register";
    }

    @PostMapping("/submitregister")
    public String submitRegister(User user, Model model){
        User u=userService.getByUsername(user.getUsername());
        if(u==null){
            CompletableFuture.runAsync(()->{
                userService.save(user);
            }, MyExecutor.getExecutor());
            return "redirect:/admin";
        }else {
            model.addAttribute("message","用户名重复，请更换用户名");
            return "admin/register";
        }
    }
}
