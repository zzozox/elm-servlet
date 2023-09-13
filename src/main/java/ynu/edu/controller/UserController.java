package ynu.edu.controller;

import jakarta.servlet.http.HttpServletRequest;

public class UserController {
    public Object sayHi(HttpServletRequest res){
        return "Hello world!";
    }
}
