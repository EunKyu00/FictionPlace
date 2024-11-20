import "../CSS/Profile.css"
import Nav from "../components/Nav.jsx"
import { FaHome, FaClipboardList, FaRegEdit, FaRegHeart, FaEnvelope, FaUserFriends, FaUserEdit } from "react-icons/fa"
import webtoonImage4 from "../assets/프로필이미지가데이터.jpg"
import React, { useState } from "react"
import { Link } from "react-router-dom"

function ProfileHome() {
  //예제데이터 내작품 api구현때 삭제
  const works = [
    {
      id: 1,
      imageUrl: "https://via.placeholder.com/150",
      name: "작품 제목 1",
      mainHome: "https://example.com/work1",
      totalViews: 1234,
      likes: 100,
    },
    {
      id: 2,
      imageUrl: "https://via.placeholder.com/150",
      name: "작품 제목 2",
      mainHome: "https://example.com/work2",
      totalViews: 5678,
      likes: 200,
    },
    {
      id: 3,
      imageUrl: "https://via.placeholder.com/150",
      name: "작품 제목 3",
      mainHome: "https://example.com/work3",
      totalViews: 91011,
      likes: 300,
    },
  ]

  //가데이터 끝
  const [image, setImage] = useState(() => {
    // 컴포넌트가 처음 렌더링될 때 로컬 스토리지에서 이미지를 불러옵니다.
    return localStorage.getItem("profileImage") || null
  })

  const handleImageChange = (event) => {
    const file = event.target.files[0]
    if (file) {
      const reader = new FileReader()
      reader.onloadend = () => {
        setImage(reader.result)
        localStorage.setItem("profileImage", reader.result) // 이미지 데이터를 로컬 스토리지에 저장
      }
      reader.readAsDataURL(file)
    }
  }
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
          <Link to="/profile/modify" className="sidebar-item">
            <FaHome className="sidebar-icon" />
            <span>프로필 홈</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaClipboardList className="sidebar-icon" />
            <span>작품 관리</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaRegEdit className="sidebar-icon" />
            <span>작품 등록</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaRegHeart className="sidebar-icon" />
            <span>관심 작품</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaEnvelope className="sidebar-icon" />
            <span>쪽지함</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaUserFriends className="sidebar-icon" />
            <span>작가 제안</span>
          </Link>
        </div>
        <div className="sidebar-item">
          <Link to="/profile/modify" className="sidebar-item">
            <FaUserEdit className="sidebar-icon" />
            <span>정보 수정</span>
          </Link>
        </div>
      </div>
      <div class="profile_home">
        <div
          className="profile-upload-container"
          style={{ backgroundImage: image ? `url(${image})` : "url(../assets/프로필이미지가데이터.jpg)" }}
          onClick={() => document.getElementById("file-upload").click()}
        >
          <input type="file" accept="image/*" id="file-upload" className="upload-input" onChange={handleImageChange} />
        </div>
        <div class="profile_nickname">닉네임 : </div>
        <div id="profile-nickname">nickname</div>
        <div class="email">Email :</div>
        <div id="profile-email">test@email</div>
        <div class="discription-card">
          <div class="discription">자기소개</div>
        </div>
      </div>
    </div>
  )
}

export default ProfileHome
