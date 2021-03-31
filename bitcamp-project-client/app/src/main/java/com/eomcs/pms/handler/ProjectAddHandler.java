package com.eomcs.pms.handler;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectAddHandler implements Command {

  ProjectDao projectDao;
  MemberValidator memberValidatorHandler;

  public ProjectAddHandler(ProjectDao projectDao, MemberValidator memberValidatorHandler) {
    this.projectDao = projectDao;
    this.memberValidatorHandler = memberValidatorHandler;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 등록]");

    Project p = new Project();

    p.setTitle(Prompt.inputString("프로젝트명? "));
    p.setContent(Prompt.inputString("내용? "));
    p.setStartDate(Prompt.inputDate("시작일? "));
    p.setEndDate(Prompt.inputDate("종료일? "));

    p.setOwner(memberValidatorHandler.inputMember("만든이?(취소: 빈 문자열) "));
    if (p.getOwner() == null) {
      System.out.println("프로젝트 입력을 취소합니다.");
      return;
    }

    p.setMembers(memberValidatorHandler.inputMembers("팀원?(완료: 빈 문자열) "));

    projectDao.insert(p);

    System.out.println("프로젝트를 등록하였습니다.");
  }
}







