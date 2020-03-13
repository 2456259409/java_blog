package com.renjian.blog.web;

import com.renjian.blog.module.Comment;
import com.renjian.blog.module.User;
import com.renjian.blog.service.BlogService;
import com.renjian.blog.service.CommentService;
import com.renjian.blog.util.MyExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CompletableFuture;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){

        CompletableFuture.runAsync(()->{
            Long blogId=comment.getBlog().getId();
            comment.setBlog(blogService.getBlog(blogId));
            User user= (User) session.getAttribute("user");
            if(user!=null){
                comment.setAvatar(user.getAvatar());
                comment.setAdminComment(true);
            }else {
                comment.setAvatar(avatar);
            }
            commentService.saveComment(comment);
        }, MyExecutor.getExecutor());
        return "redirect:/comments/"+comment.getBlog().getId();
    }
}
