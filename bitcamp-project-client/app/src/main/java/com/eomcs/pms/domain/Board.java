package com.eomcs.pms.domain;

import java.sql.Date;

public class Board {
  private int no;
  private String title;
  private String content;
  private Member writer;
  private Date registeredDate;
  private int viewCount;
  private int like;

  public Board() {}

  // 다음과 같이 인스턴스를 생성해주는 메서드를
  // "factory method"라 부른다.
  // 팩토리 메서드 패턴
  // - 인스턴스 생성 과정이 복잡할 때
  //    인스턴스 생성을 대신 해주는 메서드를 만들어
  //    그 메서드를 통해 객체를 생성하는 프로그래밍 방식이다.
  @Override
  public String toString() {
    return "Board [no=" + no + ", title=" + title + ", content=" + content + ", writer=" + writer
        + ", registeredDate=" + registeredDate + ", viewCount=" + viewCount + ", like=" + like
        + "]";
  }
  public int getNo() {
    return no;
  }
  public void setNo(int no) {
    this.no = no;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Member getWriter() {
    return writer;
  }
  public void setWriter(Member writer) {
    this.writer = writer;
  }
  public Date getRegisteredDate() {
    return registeredDate;
  }
  public void setRegisteredDate(Date registeredDate) {
    this.registeredDate = registeredDate;
  }
  public int getViewCount() {
    return viewCount;
  }
  public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }
  public int getLike() {
    return like;
  }
  public void setLike(int like) {
    this.like = like;
  }


}
