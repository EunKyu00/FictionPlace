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
          <li>
            <Link to="/auth/login">로그인</Link>
          </li>
          <li>
            <Link to="/signup">회원가입</Link>
          </li>
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
