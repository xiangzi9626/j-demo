package cn.example.demo.servlet;

import cn.hutool.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/vue")
public class VueServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // System.out.print("11111");
        resp.setContentType("text/html;charset=utf-8");
        String action = req.getParameter("action");
        switch (action) {
            case "list":
                List<JSONObject> ls = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    JSONObject jb = new JSONObject();
                    jb.put("id", i + 1);
                    jb.put("name", "王华" + i);
                    jb.put("imgurl", "http://" + req.getServerName());
                    ls.add(jb);
                }
                resp.getWriter().print(ls);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
