package com.renjian.blog.web;


import com.renjian.blog.module.Tag;
import com.renjian.blog.module.Type;
import com.renjian.blog.module.User;
import com.renjian.blog.service.TagServiceImpl;
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
public class TagController {

    @Autowired
    TagServiceImpl tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model,HttpSession session){
        User user= (User) session.getAttribute("user");
        if(user.getPower()==0){
            model.addAttribute("page",tagService.getMyTags(user.getId(),pageable));
        }else {
            model.addAttribute("page",tagService.listTag(pageable));
        }
        return "admin/tags";
    }

    @GetMapping("tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags/{id}")
    public String updateTag(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes, HttpSession session){
        if(result.hasErrors()){
            return "admin/tags-input";
        }

        Tag tg = tagService.getByName(tag.getName());
        if(tg!=null){
            result.rejectValue("name","nameError","不能重复添加相同的标签");
            return "admin/tags-input";
        }
        User user= (User) session.getAttribute("user");
        if(id==0){
            CompletableFuture.runAsync(()->{
                tag.setUser(user);
                tagService.saveTag(tag);
            },MyExecutor.getExecutor());
            attributes.addFlashAttribute("message","新增成功");
            return "redirect:/admin/tags";
        }
        CompletableFuture.runAsync(()->{
            tag.setUser(user);
            tagService.updateTag(id,tag);
        },MyExecutor.getExecutor());
        attributes.addFlashAttribute("message","修改成功");

        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id,RedirectAttributes attributes){
        CompletableFuture.runAsync(()->{
            tagService.deleteTag(id);
        }, MyExecutor.getExecutor());
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
