import React, { useState } from "react"
import Slider from "react-slick"
import "../CSS/Toonlist.css"
import Nav from "../components/Nav.jsx"
import "slick-carousel/slick/slick.css"
import "slick-carousel/slick/slick-theme.css"
import webtoonImage1 from "../assets/슬릭 가데이터.png"
import webtoonImage2 from "../assets/슬릭가데이터3.jpg"
import webtoonImage3 from "../assets/슬릭가데이터4.png"
import InfiniteScroll from "react-infinite-scroll-component"

function Toonlist() {
  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
  }

  const [items, setItems] = useState([...Array(30).keys()]) // 초기 30개의 아이템
  const [hasMore, setHasMore] = useState(true)

  const fetchMoreData = () => {
    if (items.length >= 78) {
      setHasMore(false)
      return
    }
    // 10개의 새로운 아이템을 추가
    setItems((prevItems) => [...prevItems, ...Array.from({ length: 10 }, (_, i) => prevItems.length + i)])
  }

  return (
    <div className="webtoon-list-page">
      {/* 상단 바 */}
      <div className="top-bar">
        <div className="logo">로고</div>
        <Nav />
        <div className="search-box">
          <input type="text" placeholder="검색어를 입력하세요" />
          <button>검색</button>
        </div>
      </div>

      <div className="slider-section">
        <Slider {...sliderSettings}>
          <div>
            <img src={webtoonImage1} alt="웹툰 이미지 1" />
          </div>
          <div>
            <img src={webtoonImage2} alt="웹툰 이미지 2" />
          </div>
          <div>
            <img src={webtoonImage3} alt="웹툰 이미지 3" />
          </div>
        </Slider>
      </div>

      {/* Featured Webtoons */}
      <div className="featured-section">
        <h2>Featured Webtoons</h2>
        <div className="button-group">
          <button className="active-button">연재</button>
          <button className="active-button">완결</button>
        </div>
      </div>

      {/* 장르 선택 바 */}
      <div className="genre-bar">
        {["성인", "드라마", "판타지", "액션", "스릴러", "개그"].map((genre) => (
          <div className="genre-button" key={genre}>
            {genre}
          </div>
        ))}
      </div>

      {/* 웹툰 카드 컨테이너 (Infinite Scroll 적용) */}
      <div className="card-container">
        {items.map((item, index) => (
          <div className="card" key={index}>
            <div className="card-image">이미지 {item + 1}</div>
            <div className="card-title">웹툰 제목 {item + 1}</div>
            <div className="card-description">웹툰 설명 {item + 1}</div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default Toonlist
