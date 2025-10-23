package vn.ute.utescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {
    @GetMapping("/")
    public String home() {
        return "employee"; 
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee";
    }
}
