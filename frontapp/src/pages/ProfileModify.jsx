import "../CSS/Profile.css"
import Nav from "../components/Nav.jsx"
import { FaHome, FaClipboardList, FaRegEdit, FaRegHeart, FaEnvelope, FaUserFriends, FaUserEdit } from "react-icons/fa"
import React, { useState } from "react"

function ProfileModify() {
  return (
    <div className="profile-home-page">
      {/* 상단 바 */}
      <div className="top-bar">
        <div className="logo">로고</div>
        <Nav />
        <div className="search-box">
          <input type="text" placeholder="검색어를 입력하세요" />
          <button>검색</button>
        </div>
      </div>

      {/* 프로필 홈 페이지 레이아웃 */}
      <div className="sidebar">
        <div className="sidebar-item">
          <FaHome className="sidebar-icon" />
          <span>프로필 홈</span>
        </div>
        <div className="sidebar-item">
          <FaClipboardList className="sidebar-icon" />
          <span>작품 관리</span>
        </div>
        <div className="sidebar-item">
          <FaRegEdit className="sidebar-icon" />
          <span>작품 등록</span>
        </div>
        <div className="sidebar-item">
          <FaRegHeart className="sidebar-icon" />
          <span>관심 작품</span>
        </div>
        <div className="sidebar-item">
          <FaEnvelope className="sidebar-icon" />
          <span>쪽지함</span>
        </div>
        <div className="sidebar-item">
          <FaUserFriends className="sidebar-icon" />
          <span>작가 제안</span>
        </div>
        <div className="sidebar-item">
          <FaUserEdit className="sidebar-icon" />
          <span>정보 수정</span>
        </div>
      </div>
    </div>
  )
}

export default ProfileModify
