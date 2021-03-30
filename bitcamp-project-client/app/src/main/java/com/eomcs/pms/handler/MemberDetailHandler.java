package com.eomcs.pms.handler;

import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class MemberDetailHandler implements Command {

  //핸들러가 사용할 DAO: 의존 객체 (dependency)
  MemberDao memberDao;

  // DAO 객체는 이 클래스가 작업하는데 필수 객체이기 때문에
  // 생성자를 통해 반드시 주입 받도록 한다.
  public MemberDetailHandler(MemberDao memberDao) {
    this.memberDao = memberDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[회원 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Member m = memberDao.findByNo(no);

    if (m == null) {
      System.out.println("해당 번호의 멤버가 없습니다.");
      return;
    }

    System.out.printf("이름: %s\n", m.getName());
    System.out.printf("이메일: %s\n", m.getEmail());
    System.out.printf("비밀번호: %s\n", m.getPassword());
    System.out.printf("사진: %s\n", m.getPhoto());
    System.out.printf("연락처: %s\n", m.getTel());
    System.out.printf("등록일: %s %s\n", m.getRegisteredDate());
  } 
}






