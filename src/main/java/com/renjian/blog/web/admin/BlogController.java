package com.renjian.blog.web.admin;

import com.renjian.blog.module.*;
import com.renjian.blog.service.BlogServiceImpl;
import com.renjian.blog.service.TagServiceImpl;
import com.renjian.blog.service.TypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    BlogServiceImpl blogService;

    @Autowired
    TypeServiceImpl typeService;

    @Autowired
    TagServiceImpl tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model,HttpSession session){
        User user= (User) session.getAttribute("user");
        if(user.getPower()==0){
            model.addAttribute("types",typeService.listType());
            model.addAttribute("page",blogService.getMyBlogs(user.getId(),pageable));
        }else {
            model.addAttribute("types",typeService.listType(user.getId()));
            model.addAttribute("page",blogService.listBlog(pageable,blog));
        }
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String searchBlog(@PageableDefault(size = 5,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog,Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String blogInput(Model model){
        Blog blog = new Blog();
        blog.setType(new Type());
        blog.setTags(new ArrayList<Tag>());
        model.addAttribute("blog",blog);
        setTagAndType(model);
        return "admin/blogs-input";
    }

    private void setTagAndType(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(Model model,@PathVariable Long id){
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        setTagAndType(model);
        return "admin/blogs-input";
    }

    @PostMapping("/save_blog")
    public String saveBlog(Blog blog, HttpSession session, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);
        if(b==null){
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }

}
