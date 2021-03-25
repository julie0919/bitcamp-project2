package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.eomcs.driver.Statement;

public class TaskListHandler implements Command {

  Statement stmt;

  public TaskListHandler(Statement stmt) {
    this.stmt = stmt;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 목록]");

    try (Connection con = DriverManager.getConnection( //
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement( //
            "select no, deadline, owner, status from pms_task order by no desc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        System.out.printf("%d, %s, %s, %s, %s, %s\n", 
            rs.getInt("no"), 
            rs.getDate("deadline"),
            rs.getString("owner"),
            rs.getInt("status"));
      }
    }
  }
}
