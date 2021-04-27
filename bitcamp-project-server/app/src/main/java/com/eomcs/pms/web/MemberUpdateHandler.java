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
@WebServlet("/member/update")
public class MemberUpdateHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    MemberService memberService = (MemberService) request.getServletContext().getAttribute("memberService");

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();

    out.println("[회원 변경]");

    try {
      int no = Integer.parseInt(request.getParameter("no"));

      Member oldMember = memberService.get(no);

      if (oldMember == null) {
        out.println("해당 번호의 회원이 없습니다.");
        return;
      }

      Member member = new Member();
      member.setNo(oldMember.getNo());
      member.setName(request.getParameter("name"));
      member.setEmail(request.getParameter("email"));
      member.setPassword(request.getParameter("password"));
      member.setPhoto(request.getParameter("photo"));
      member.setTel(request.getParameter("tel"));

      memberService.update(member);

      out.println("회원을 변경하였습니다.");

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






