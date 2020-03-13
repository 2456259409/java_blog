package com.renjian.blog.web;

import com.renjian.blog.module.BlogQuery;
import com.renjian.blog.module.Tag;
import com.renjian.blog.module.Type;
import com.renjian.blog.service.BlogService;
import com.renjian.blog.service.TagService;
import com.renjian.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,@PathVariable Long id, Model model){
        List<Tag> tags=tagService.listTagTop(10);
        if(id==-1){
            id=tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(pageable,id));
        model.addAttribute("activeTagId",id);
        return "tags";
    }
}
