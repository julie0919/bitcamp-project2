package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.Prompt;

public class BoardAddHandler implements Command {

  MemberValidatorHandler memberValidatorHandler;

  public BoardAddHandler(MemberValidatorHandler memberValidatorHandler) {
    this.memberValidatorHandler = memberValidatorHandler;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 등록]");

    Board b = new Board();

    b.setTitle(Prompt.inputString("제목? "));
    b.setContent(Prompt.inputString("내용? "));
    b.setWriter(memberValidatorHandler.inputMember("작성자? "));
    if (b.getWriter() == null) {
      System.out.println("프로젝트 입력을 취소합니다.");
      return;
    }

    try (Connection con = DriverManager.getConnection( //
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt =
            con.prepareStatement("insert into pms_board(title, content, writer) values(?,?,?)");) {


      stmt.setString(1, b.getTitle());
      stmt.setString(2, b.getContent());
      stmt.setInt(3, b.getWriter().getNo());

      stmt.executeUpdate();
      System.out.println("게시글을 등록하였습니다.");
    }
  }
}






