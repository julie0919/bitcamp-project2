package com.eomcs.pms.dao.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;

public class MemberDaoImpl implements MemberDao {

  Connection con;

  // Connection 객체를 자체적으로 생성하지 않고 외부에서 주입받는다.
  // - Connection 객체를 여러 DAO가 공유할 수 있다.
  // - 교체하기도 쉽다.
  public MemberDaoImpl(Connection con) throws Exception {
    this.con = con;
  }

  @Override
  public int insert(Member member) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_member(name, email, password, photo, tel) values(?,?,password(?),?,?)");) {

      stmt.setString(1, member.getName());
      stmt.setString(2, member.getEmail());
      stmt.setString(3, member.getPassword());
      stmt.setString(4, member.getPhoto());
      stmt.setString(5, member.getTel());

      return stmt.executeUpdate();
    }
  }

  @Override
  public List<Member> findAll() throws Exception {
    ArrayList<Member> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select no,name,email,photo,tel,cdt from pms_member order by no desc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Member member = new Member();
        member.setNo(rs.getInt("no"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhoto(rs.getString("photo"));
        member.setTel(rs.getString("tel"));
        member.setRegisteredDate(rs.getDate("cdt"));

        list.add(member);
      }
      return list;
    }
  }

  @Override
  public Member findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select * from pms_member where no = ?")) {

      stmt.setInt(1, no);
      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 멤버가 없습니다.");
          return null;
        }
        Member member = new Member();
        member.setNo(rs.getInt("no"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        member.setPhoto(rs.getString("photo"));
        member.setTel(rs.getString("tel"));
        member.setRegisteredDate(rs.getDate("cdt"));
        return member;
      } 
    }
  }

  @Override
  public int update(Member member) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_member set name=?, email=?, photo=?, tel=? where no=?")) {

      stmt.setString(1, member.getName());
      stmt.setString(2, member.getEmail());
      stmt.setString(3, member.getPhoto());
      stmt.setString(4, member.getTel());
      stmt.setInt(5, member.getNo());
      return stmt.executeUpdate();

    }
  }

  @Override
  public int delete(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_member where no=?")) {
      stmt.setInt(1, no);
      return stmt.executeUpdate();
    }
  }

  @Override
  public Member findByName(String name) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select * from pms_member where name=?")) {

      stmt.setString(1, name);

      ResultSet rs = stmt.executeQuery();

      if(!rs.next()) {
        return null;
      }

      Member m = new Member();
      m.setNo(rs.getInt("no"));
      m.setName(rs.getString("name"));
      m.setEmail(rs.getString("email"));

      return m;
    }
  }
}  