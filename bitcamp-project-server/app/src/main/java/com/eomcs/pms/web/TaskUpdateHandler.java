package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.domain.Task;
import com.eomcs.pms.service.ProjectService;
import com.eomcs.pms.service.TaskService;

@SuppressWarnings("serial")
@WebServlet("/task/update")
public class TaskUpdateHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    TaskService taskService = (TaskService) request.getServletContext().getAttribute("taskService");
    ProjectService projectService = (ProjectService) request.getServletContext().getAttribute("projectService");

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();

    out.println("[작업 변경]");

    try {
      int no = Integer.parseInt(request.getParameter("no"));

      Task oldTask = taskService.get(no);
      if (oldTask == null) {
        out.println("해당 번호의 작업이 없습니다.");
        return;
      }

      out.printf("현재 프로젝트: %s\n", oldTask.getProjectTitle());

      List<Project> projects = projectService.list();
      out.println("프로젝트들:");
      if (projects.size() == 0) {
        out.println("현재 등록된 프로젝트가 없습니다!");
        return;
      }
      for (Project p : projects) {
        out.printf("  %d, %s\n", p.getNo(), p.getTitle());
      }

      // 현재 작업이 소속된 프로젝트를 변경한다.
      int selectedProjectNo = 0;
      loop: while (true) {
        try {
          selectedProjectNo = Integer.parseInt(request.getParameter("projectNo"));
          if (selectedProjectNo == 0) {
            out.println("기존 프로젝트를 유지합니다.");
            break loop;
          }
          for (Project p : projects) {
            if (p.getNo() == selectedProjectNo) {
              break loop;
            }
          }
          out.println("유효하지 않은 프로젝트 번호 입니다.");

        } catch (Exception e) {
          out.println("숫자를 입력하세요!");
        }
      }

      Task task = new Task();
      task.setNo(no);

      if (selectedProjectNo != 0) {
        task.setProjectNo(selectedProjectNo);
      }

      // 사용자에게서 변경할 데이터를 입력 받는다.
      task.setContent(request.getParameter("content"));
      task.setDeadline(Date.valueOf(request.getParameter("deadline")));
      task.setStatus(Integer.parseInt(request.getParameter("status")));

      Member loginUser = (Member) request.getSession().getAttribute("loginUser");
      task.setOwner(loginUser);

      if(task.getOwner() == null) {
        out.println("작업 변경을 취소합니다.");
        return;
      }

      taskService.update(task);

      out.println("작업을 변경하였습니다.");

    } catch (Exception e) {
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);

      // StringWriter 에 들어있는 출력내용을 꺼내 클라이언트로 보낸다.
      out.println(strWriter.toString());
    }
  }
}
