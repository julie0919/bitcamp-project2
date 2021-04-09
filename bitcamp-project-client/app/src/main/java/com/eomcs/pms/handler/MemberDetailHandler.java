package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;
import com.eomcs.util.Prompt;

public class MemberDetailHandler implements Command {

  MemberService memberService;

  public MemberDetailHandler(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[회원 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Member m = memberService.get(no);

    if (m == null) {
      System.out.println("해당 번호의 멤버가 없습니다.");
      return;
    }

    System.out.printf("이름: %s\n", m.getName());
    System.out.printf("이메일: %s\n", m.getEmail());
    System.out.printf("비밀번호: %s\n", m.getPassword());
    System.out.printf("사진: %s\n", m.getPhoto());
    System.out.printf("연락처: %s\n", m.getTel());
    System.out.printf("등록일: %s\n", m.getRegisteredDate());
  } 
}






