package com.renjian.blog.web.admin;

import com.renjian.blog.module.User;
import com.renjian.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TypeService typeService;


    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @GetMapping("/index")
    public String toIndex(){
        return "admin/index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,@PageableDefault(size = 5,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable, Model model){
        User user = userService.checkUser(username, password);
        if(user==null){
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }
        user.setPassword(null);
        session.setAttribute("user",user);
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogs(8));
        return "index";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
