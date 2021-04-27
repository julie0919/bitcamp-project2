package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.service.ProjectService;

@SuppressWarnings("serial")
@WebServlet("/project/update")
public class ProjectUpdateHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    ProjectService projectService = (ProjectService) request.getServletContext().getAttribute("projectService");

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();

    out.println("[프로젝트 변경]");

    try {
      int no = Integer.parseInt(request.getParameter("no"));

      Project oldProject = projectService.get(no);

      if (oldProject == null) {
        out.println("해당 번호의 프로젝트가 없습니다.");
        return;
      }

      Member loginUser = (Member) request.getSession().getAttribute("loginUser");
      if (oldProject.getOwner().getNo() != loginUser.getNo()) {
        out.println("변경 권한이 없습니다!");
        return;
      }

      // 사용자에게서 변경할 데이터를 입력 받는다.
      Project project = new Project();
      project.setNo(no);
      project.setTitle(request.getParameter("title"));
      project.setContent(request.getParameter("content"));
      project.setStartDate(Date.valueOf(request.getParameter("startDate")));
      project.setEndDate(Date.valueOf(request.getParameter("endDate")));
      project.setOwner(loginUser);

      // 프로젝트 팀원 정보를 입력 받는다.
      // ...&member=1&member=1&member=23
      String[] members = request.getParameterValues("member");
      ArrayList<Member> memberList = new ArrayList<>();
      for (String value : members) {
        Member member = new Member();
        member.setNo(Integer.parseInt(value));
        memberList.add(member);
      }

      project.setMembers(memberList);

      projectService.update(project);
      out.println("프로젝트을 변경하였습니다.");

    } catch (Exception e) {
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);

      // StringWriter 에 들어있는 출력내용을 꺼내 클라이언트로 보낸다.
      out.println(strWriter.toString());
    }
  }
}






