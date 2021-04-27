package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;

@SuppressWarnings("serial")
@WebServlet("/member/detail")
public class MemberDetailHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    MemberService memberService = (MemberService) request.getServletContext().getAttribute("memberService");

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    int no = Integer.parseInt(request.getParameter("no"));

    out.println("[회원 상세보기]");

    try {
      Member m = memberService.get(no);

      if (m == null) {
        out.println("해당 번호의 회원이 없습니다.");
        return;
      }

      out.printf("이름: %s\n", m.getName());
      out.printf("이메일: %s\n", m.getEmail());
      out.printf("사진: %s\n", m.getPhoto());
      out.printf("전화: %s\n", m.getTel());
      out.printf("가입일: %s\n", m.getRegisteredDate());

    } catch (Exception e) {
      // 상세 오류 내용을 StringWriter로 출력한다.
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);

      // StringWriter 에 들어있는 출력내용을 꺼내 클라이언트로 보낸다.
      out.println(strWriter.toString());
    }
  }
}






