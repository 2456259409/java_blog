package com.renjian.blog.web;


import com.renjian.blog.module.Type;
import com.renjian.blog.module.User;
import com.renjian.blog.service.TypeServiceImpl;
import com.renjian.blog.util.MyExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;


@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeServiceImpl typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model,HttpSession session){
        User user= (User) session.getAttribute("user");
        if(user.getPower()==0){
            model.addAttribute("page",typeService.listMyType(user.getId(),pageable));
        }else {
            model.addAttribute("page",typeService.listType(pageable));
        }
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types/{id}")
    public String updateType(@Valid Type type, BindingResult result,@PathVariable Long id,RedirectAttributes attributes,HttpSession session){
        if(result.hasErrors()){
            return "admin/types-input";
        }

        Type ty = typeService.getByName(type.getName());
        if(ty!=null){
            result.rejectValue("name","nameError","不能重复添加相同的分类");
            return "admin/types-input";
        }

        User user= (User) session.getAttribute("user");
        if(id==0){
            type.setUser(user);
            Type t = typeService.saveType(type);
            if(t==null){
                attributes.addFlashAttribute("message","新增失败");
            }else{
                attributes.addFlashAttribute("message","新增成功");
            }
            return "redirect:/admin/types";
        }
        type.setUser(user);
        Type t = typeService.updateType(id,type);
        if(t==null){
            attributes.addFlashAttribute("message","修改失败");
        }else{
            attributes.addFlashAttribute("message","修改成功");
        }

        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id,RedirectAttributes attributes){
        CompletableFuture.runAsync(()->{
            typeService.deleteType(id);
        }, MyExecutor.getExecutor());
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
