package com.eomcs.pms.handler;

import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

public class TaskDetailHandler implements Command {

  TaskDao taskDao;

  public TaskDetailHandler(TaskDao taskDao) {
    this.taskDao = taskDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Task t = taskDao.findByNo(no);

    if (t == null) {
      System.out.println("해당 번호의 작업이 없습니다.");
      return;
    }

    System.out.printf("프로젝트: %s\n", t.getProjectTitle());
    System.out.printf("내용: %s\n", t.getContent());
    System.out.printf("마감일: %s\n", t.getDeadline());
    System.out.printf("상태: %s\n", Task.getStatusLabel(t.getStatus()));
    System.out.printf("담당자: %s\n", t.getOwner());
  } 
}
