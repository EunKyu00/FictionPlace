import React from "react"
import { Link } from "react-router-dom"
import "../CSS/NavBar.css" // 네비게이션 스타일 CSS import
import logo from "../assets/logoimage2.gif" //로고이미지

function Nav() {
  return (
    <div className="top-bar">
      <div></div>
      <div className="project-brand">
        <a href="/">
          <img src={logo} alt="Logo" className="logo-image" />
        </a>
        <div className="project-name">
          <a href="/">Fiction Place</a>
        </div>
      </div>

      {/* 네비게이션 메뉴 */}
      <nav className="navigation">
        <ul className="nav-links">
          <li>
            <Link to="/">웹툰</Link>
          </li>
          <li>
            <Link to="/bordlist">게시판</Link>
          </li>
          <li>
            <Link to="/myprofile">프로필</Link>
          </li>
          <div className="login-type">
            로그인
            <ul>
              <li>
                <a href="/login/user">일반 로그인</a>
              </li>
              <li>
                <a href="/login/company">기업 로그인</a>
              </li>
            </ul>
          </div>
          <div className="signup-type">
            회원가입
            <ul>
              <li>
                <a href="/signup/user">일반 회원가입</a>
              </li>
              <li>
                <a href="/signup/company">기업 회원가입</a>
              </li>
            </ul>
          </div>
        </ul>
      </nav>

      {/* 검색창 */}
      <div className="search-box">
        <input type="text" className="search-input" placeholder="검색..." />
        <div className="search-icon"></div>
      </div>
    </div>
  )
}

export default Nav
