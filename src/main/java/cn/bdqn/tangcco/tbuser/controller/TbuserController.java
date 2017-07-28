package cn.bdqn.tangcco.tbuser.controller;

import cn.bdqn.tangcco.entity.Menu;
import cn.bdqn.tangcco.entity.RoleMenu;
import cn.bdqn.tangcco.entity.Roleuser;
import cn.bdqn.tangcco.entity.Tbuser;
import cn.bdqn.tangcco.roleMenu.services.RoleMenuService;
import cn.bdqn.tangcco.tbuser.service.TbuserService;
import cn.bdqn.tangcco.tools.Message;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Clown on 2017/07/25.
 */
@Controller
@RequestMapping(value = "user")
public class TbuserController {

    @Inject
    private TbuserService tbuserService;
    @Inject
    private RoleMenuService roleMenuService;

    @RequestMapping(value = "login", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    public String login(Tbuser tbuser, HttpSession session, Model model) {

        String str = "";
        Roleuser ru = tbuserService.login(tbuser);
        if (ru != null) {
            session.setAttribute("user", ru);

            return "main";
        }

        model.addAttribute("msg", "用户名或密码错误！");
        str = "index";
        return str;

    }

    @RequestMapping(value = "initTree", method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String initTree(HttpSession session) {
        Roleuser ru = (Roleuser) session.getAttribute("user");
        Integer roleId = ru.getRole().getRoleId();
        List<Menu> list = roleMenuService.queryMenuByRoleId(roleId);
        return JSONArray.toJSONString(list);
    }


    @RequestMapping(value = "appendTree", method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String queryMenuByParentId(Integer parentId,HttpSession session) {
        System.out.println(parentId);
        List<Menu> list = roleMenuService.queryMenuByParentId(parentId,((Roleuser)session.getAttribute("user")).getRole().getRoleId());
        System.out.println(list);
        return JSONArray.toJSONString(list);
    }
}